package com.Kebcaqa1.Veghar.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Kebcaqa1.Veghar.core.config.GameConfig
import com.Kebcaqa1.Veghar.data.sample.SampleData
import com.Kebcaqa1.Veghar.domain.model.Gate
import com.Kebcaqa1.Veghar.domain.model.GateControl
import com.Kebcaqa1.Veghar.domain.model.GateFacing
import com.Kebcaqa1.Veghar.domain.model.ModuleKind
import com.Kebcaqa1.Veghar.domain.model.MotionLevel
import com.Kebcaqa1.Veghar.domain.model.ShiftResult
import com.Kebcaqa1.Veghar.domain.model.Stage
import com.Kebcaqa1.Veghar.domain.repository.ProgressRepository
import com.Kebcaqa1.Veghar.domain.repository.SettingsRepository
import com.Kebcaqa1.Veghar.domain.usecase.AdvanceMissionsUseCase
import com.Kebcaqa1.Veghar.domain.usecase.BuildCalibrationUseCase
import com.Kebcaqa1.Veghar.domain.usecase.ResolveDropUseCase
import com.Kebcaqa1.Veghar.domain.usecase.ScoreDropUseCase
import com.Kebcaqa1.Veghar.domain.usecase.UnlockStageUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.max

sealed interface GameEvent {
    data class ShiftFinished(val result: ShiftResult) : GameEvent
}

class GameViewModel(
    private val progress: ProgressRepository,
    private val settings: SettingsRepository,
    private val resolveDrop: ResolveDropUseCase,
    private val scoreDrop: ScoreDropUseCase,
    private val buildCalibration: BuildCalibrationUseCase,
    private val advanceMissions: AdvanceMissionsUseCase,
    private val unlockStage: UnlockStageUseCase
) : ViewModel() {

    private val mutableState = MutableStateFlow(GameUiState())
    val state: StateFlow<GameUiState> = mutableState.asStateFlow()

    private val eventChannel = Channel<GameEvent>(Channel.BUFFERED)
    val events: Flow<GameEvent> = eventChannel.receiveAsFlow()

    private var loop: Job? = null
    private var session = -1L
    private var sessionSeed = 0L
    private var stage: Stage = SampleData.stages.first()
    private var coreHits = 0
    private var solvedCalibrations = 0
    private var overchargeHits = 0
    private var calibrationStreak = 0
    private var bestStreak = 0
    private var bestChain = 1.0
    private var stableRun = true
    private var bonusReady = false

    fun beginShift(newSession: Long) {
        if (session == newSession) return
        session = newSession
        loop?.cancel()
        loop = null
        val saved = progress.progress.value
        val options = settings.settings.value
        stage = SampleData.stageOf(saved.stageId)
        sessionSeed = newSession * 104729L + stage.id * 7919L
        coreHits = 0
        solvedCalibrations = 0
        overchargeHits = 0
        calibrationStreak = 0
        bestStreak = 0
        bestChain = 1.0
        stableRun = true
        bonusReady = false
        val gates = GameConfig.GATE_ROWS.mapIndexed { index, row ->
            Gate(
                id = index,
                row = row,
                lane = parkedLane(index),
                facing = if (index % 2 == 0) GateFacing.RIGHT else GateFacing.LEFT
            )
        }
        mutableState.value = GameUiState(
            phase = GamePhase.AIM,
            stageId = stage.id,
            stageNameKey = stage.nameKey,
            payouts = stage.payouts,
            dropIndex = 1,
            stability = GameConfig.STABILITY_CELLS,
            shiftCharge = 0,
            chain = 1.0,
            torqueTotal = stage.torque,
            torqueLeft = stage.torque,
            gates = gates,
            overchargeLane = buildCalibration.overchargeLane(sessionSeed, stage.payouts),
            highContrast = options.highContrastSlots,
            reducedMotion = options.motion == MotionLevel.REDUCED,
            tapOnly = options.gateControl == GateControl.TAP_ONLY,
            gateTint = tintOf(saved.equippedModules[ModuleKind.GATE_SKIN], 0xFFFF006E),
            pegTint = tintOf(saved.equippedModules[ModuleKind.PEG_FILAMENT], 0xFF00F5FF),
            tokenTint = tintOf(saved.equippedModules[ModuleKind.TOKEN_CORE], 0xFFFF4D8D),
            plateTint = tintOf(saved.equippedModules[ModuleKind.SLOT_PLATE], 0xFF9AA3C7)
        )
    }

    private fun parkedLane(index: Int): Int = when (index) {
        0 -> 2
        1 -> 4
        else -> 6
    }

    private fun tintOf(moduleId: String?, fallback: Long): Long {
        if (moduleId == null) return fallback
        return SampleData.modules.firstOrNull { it.id == moduleId }?.tint ?: fallback
    }

    fun onGateStep(gateId: Int, direction: Int) {
        val current = mutableState.value
        if (current.gatesLocked || current.torqueLeft <= 0) return
        val gate = current.gates.firstOrNull { it.id == gateId } ?: return
        val target = (gate.lane + direction)
            .coerceIn(GameConfig.GATE_MIN_LANE, GameConfig.GATE_MAX_LANE)
        if (target == gate.lane) return
        val cost = abs(target - gate.lane)
        if (cost > current.torqueLeft) return
        mutableState.update { previous ->
            previous.copy(
                torqueLeft = previous.torqueLeft - cost,
                gates = previous.gates.map { if (it.id == gateId) it.copy(lane = target) else it }
            )
        }
    }

    fun onGateFlip(gateId: Int) {
        val current = mutableState.value
        if (current.gatesLocked || current.torqueLeft <= 0) return
        mutableState.update { previous ->
            previous.copy(
                torqueLeft = previous.torqueLeft - 1,
                gates = previous.gates.map {
                    if (it.id == gateId) it.copy(facing = it.facing.flipped()) else it
                }
            )
        }
    }

    fun onResetGates() {
        val current = mutableState.value
        if (current.gatesLocked) return
        mutableState.update { previous ->
            previous.copy(
                torqueLeft = previous.torqueTotal,
                gates = previous.gates.map { gate ->
                    gate.copy(
                        lane = parkedLane(gate.id),
                        facing = if (gate.id % 2 == 0) GateFacing.RIGHT else GateFacing.LEFT
                    )
                }
            )
        }
    }

    fun onPrimaryAction() {
        val current = mutableState.value
        if (current.paused) return
        when (current.phase) {
            GamePhase.AIM -> startDrop()
            GamePhase.CALIBRATING -> finishCalibration(false)
            else -> Unit
        }
    }

    fun onPadTap(pad: Int) {
        val current = mutableState.value
        if (current.paused) return
        if (current.phase != GamePhase.CALIBRATING || !current.calibrationAcceptsInput) return
        val expected = current.calibrationPattern.getOrNull(current.calibrationEntered) ?: return
        if (pad != expected) {
            finishCalibration(false)
            return
        }
        val entered = current.calibrationEntered + 1
        mutableState.update { it.copy(calibrationEntered = entered, calibrationShown = pad) }
        if (entered >= current.calibrationPattern.size) finishCalibration(true)
    }

    fun onPauseRequested() {
        loop?.cancel()
        loop = null
        mutableState.update {
            it.copy(
                paused = true,
                phase = GamePhase.AIM,
                tokenVisible = false,
                ghosts = emptyList(),
                calibrationShown = -1,
                calibrationAcceptsInput = false,
                banner = BannerKind.NONE
            )
        }
    }

    fun onResume() {
        mutableState.update { it.copy(paused = false) }
    }

    fun pause() {
        loop?.cancel()
        loop = null
        mutableState.update {
            it.copy(
                phase = GamePhase.AIM,
                tokenVisible = false,
                ghosts = emptyList(),
                calibrationShown = -1,
                calibrationAcceptsInput = false,
                banner = BannerKind.NONE
            )
        }
    }

    private fun startDrop() {
        val current = mutableState.value
        val seed = sessionSeed xor (current.dropIndex * 7919L)
        val path = resolveDrop(stage, current.gates, seed)
        val duration = if (current.reducedMotion) {
            GameConfig.DROP_ANIM_REDUCED_MS
        } else {
            GameConfig.DROP_ANIM_MS
        }
        loop?.cancel()
        loop = viewModelScope.launch {
            mutableState.update {
                it.copy(
                    phase = GamePhase.FALLING,
                    tokenVisible = true,
                    tokenLane = GameConfig.CENTER_LANE.toFloat(),
                    tokenRow = -0.3f,
                    ghosts = emptyList(),
                    landedLane = -1,
                    flashRow = -1,
                    flashLane = -1
                )
            }
            val hops = GameConfig.PEG_ROWS + 1
            val hopMillis = max(48L, duration / hops)
            var fromLane = GameConfig.CENTER_LANE.toFloat()
            var fromRow = -0.3f
            for (hop in 0..GameConfig.PEG_ROWS) {
                val toLane = if (hop < GameConfig.PEG_ROWS) {
                    path.lanes[hop].toFloat()
                } else {
                    path.landing.toFloat()
                }
                val toRow = if (hop < GameConfig.PEG_ROWS) {
                    hop.toFloat()
                } else {
                    GameConfig.PEG_ROWS + 0.25f
                }
                var elapsed = 0L
                while (elapsed < hopMillis) {
                    delay(GameConfig.FRAME_MS)
                    elapsed += GameConfig.FRAME_MS
                    val raw = (elapsed.toFloat() / hopMillis).coerceIn(0f, 1f)
                    val eased = raw * raw * (3f - 2f * raw)
                    val lane = fromLane + (toLane - fromLane) * eased
                    val row = fromRow + (toRow - fromRow) * eased
                    mutableState.update { previous ->
                        previous.copy(
                            tokenLane = lane,
                            tokenRow = row,
                            ghosts = buildGhosts(previous.ghosts, lane, row)
                        )
                    }
                }
                fromLane = toLane
                fromRow = toRow
                if (hop < GameConfig.PEG_ROWS) {
                    mutableState.update { it.copy(flashRow = hop, flashLane = toLane.toInt()) }
                }
            }
            land(path.landing)
        }
    }

    private fun buildGhosts(
        existing: List<TokenGhost>,
        lane: Float,
        row: Float
    ): List<TokenGhost> {
        val faded = existing.map { it.copy(alpha = it.alpha - 0.16f) }.filter { it.alpha > 0.05f }
        return (listOf(TokenGhost(lane, row, 0.5f)) + faded).take(5)
    }

    private suspend fun land(landing: Int) {
        val current = mutableState.value
        val outcome = scoreDrop(
            stage = stage,
            landing = landing,
            chain = current.chain,
            overchargeLane = current.overchargeLane,
            calibrationSolved = bonusReady,
            strictBonusPercent = settings.settings.value.calibrationTimer.bonusPercent
        )
        bonusReady = false
        val nextChain = if (outcome.voided) {
            1.0
        } else {
            val index = GameConfig.CHAIN_STEPS.indexOfFirst { it >= current.chain }
            GameConfig.CHAIN_STEPS[minOf(index + 1, GameConfig.CHAIN_STEPS.lastIndex)]
        }
        if (!outcome.voided) {
            if (outcome.core) coreHits += 1
            if (outcome.overcharge) overchargeHits += 1
            bestChain = maxOf(bestChain, nextChain)
        } else {
            stableRun = false
        }
        mutableState.update {
            it.copy(
                phase = GamePhase.LANDING,
                landedLane = landing,
                lastGain = outcome.gain,
                shiftCharge = it.shiftCharge + outcome.gain,
                chain = nextChain,
                stability = if (outcome.voided) it.stability - 1 else it.stability,
                tokenVisible = false
            )
        }
        delay(GameConfig.LANDING_MS)
        val after = mutableState.value
        if (after.stability <= 0) {
            finishShift(false)
            return
        }
        if (after.dropIndex >= after.totalDrops) {
            finishShift(true)
            return
        }
        runCalibration()
    }

    private suspend fun runCalibration() {
        val seed = sessionSeed xor (mutableState.value.dropIndex * 31337L)
        val pattern = buildCalibration(seed)
        mutableState.update {
            it.copy(
                phase = GamePhase.CALIBRATING,
                calibrationPattern = pattern,
                calibrationEntered = 0,
                calibrationAcceptsInput = false,
                calibrationShown = -1,
                calibrationRemaining = 1f,
                ghosts = emptyList()
            )
        }
        pattern.forEach { pad ->
            mutableState.update { it.copy(calibrationShown = pad) }
            delay(GameConfig.CALIBRATION_FLASH_MS)
            mutableState.update { it.copy(calibrationShown = -1) }
            delay(GameConfig.CALIBRATION_GAP_MS)
        }
        val window = settings.settings.value.calibrationTimer.millis
        mutableState.update { it.copy(calibrationAcceptsInput = true, calibrationRemaining = 1f) }
        var left = window
        while (left > 0) {
            delay(GameConfig.FRAME_MS * 4)
            left -= GameConfig.FRAME_MS * 4
            mutableState.update {
                it.copy(calibrationRemaining = (left.toFloat() / window).coerceIn(0f, 1f))
            }
        }
        finishCalibration(false)
    }

    private fun finishCalibration(solved: Boolean) {
        loop?.cancel()
        loop = viewModelScope.launch {
            if (solved) {
                solvedCalibrations += 1
                calibrationStreak += 1
                bestStreak = maxOf(bestStreak, calibrationStreak)
                bonusReady = true
            } else {
                calibrationStreak = 0
                bonusReady = false
                stableRun = false
            }
            mutableState.update {
                it.copy(
                    phase = GamePhase.BANNER,
                    banner = if (solved) BannerKind.CALIBRATED else BannerKind.MISALIGNED,
                    calibrationAcceptsInput = false,
                    calibrationShown = -1,
                    stability = if (solved) it.stability else it.stability - 1
                )
            }
            delay(GameConfig.BANNER_MS)
            val current = mutableState.value
            if (current.stability <= 0) {
                finishShift(false)
                return@launch
            }
            val nextDrop = current.dropIndex + 1
            val torque = if (solved) GameConfig.TORQUE_BONUS else stage.torque
            mutableState.update {
                it.copy(
                    phase = GamePhase.AIM,
                    banner = BannerKind.NONE,
                    dropIndex = nextDrop,
                    torqueTotal = torque,
                    torqueLeft = torque,
                    landedLane = -1,
                    flashRow = -1,
                    flashLane = -1,
                    overchargeLane = buildCalibration.overchargeLane(
                        sessionSeed xor (nextDrop * 5171L),
                        stage.payouts
                    )
                )
            }
        }
    }

    private suspend fun finishShift(completed: Boolean) {
        val current = mutableState.value
        val saved = progress.progress.value
        val projected = unlockStage(saved.totalCharge + current.shiftCharge, SampleData.stages)
        val draft = ShiftResult(
            session = session,
            completed = completed,
            charge = current.shiftCharge,
            coreHits = coreHits,
            calibrations = solvedCalibrations,
            overchargeHits = overchargeHits,
            bestChain = bestChain,
            stableRun = stableRun,
            bestCalibrationStreak = bestStreak,
            stageId = stage.id,
            unlockedStageId = if (projected > saved.unlockedStages) projected else 0,
            completedMissions = emptyList()
        )
        val before = saved.missionProgress
        val after = advanceMissions(SampleData.missions, before, draft)
        val finished = draft.copy(
            completedMissions = advanceMissions.completedIn(SampleData.missions, before, after)
        )
        progress.applyShift(finished)
        mutableState.update {
            it.copy(
                phase = GamePhase.AIM,
                tokenVisible = false,
                ghosts = emptyList(),
                banner = BannerKind.NONE,
                calibrationAcceptsInput = false
            )
        }
        eventChannel.send(GameEvent.ShiftFinished(finished))
    }
}
