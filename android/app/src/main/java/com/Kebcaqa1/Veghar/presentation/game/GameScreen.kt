package com.Kebcaqa1.Veghar.presentation.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Kebcaqa1.Veghar.R
import com.Kebcaqa1.Veghar.core.config.GameConfig
import com.Kebcaqa1.Veghar.core.di.ServiceLocator
import com.Kebcaqa1.Veghar.core.theme.AccentCyan
import com.Kebcaqa1.Veghar.core.theme.AccentMagenta
import com.Kebcaqa1.Veghar.core.theme.SurfaceDim
import com.Kebcaqa1.Veghar.core.theme.TextPrimary
import com.Kebcaqa1.Veghar.core.theme.TextSecondary
import com.Kebcaqa1.Veghar.core.ui.Labels
import com.Kebcaqa1.Veghar.core.ui.ScreenBackground
import com.Kebcaqa1.Veghar.core.ui.StabilityBar
import com.Kebcaqa1.Veghar.domain.model.ShiftResult
import com.Kebcaqa1.Veghar.presentation.dialog.PauseDialog
import com.Kebcaqa1.Veghar.presentation.game.components.AimPanel
import com.Kebcaqa1.Veghar.presentation.game.components.CalibrationPanel
import com.Kebcaqa1.Veghar.presentation.game.components.ControlBar
import com.Kebcaqa1.Veghar.presentation.game.components.PegBoard
import com.Kebcaqa1.Veghar.presentation.game.components.PhaseBanner
import kotlin.math.floor

private val GameOverlay = listOf(
    Color(0x9912121F),
    Color(0x660A0A0F),
    Color(0xE6050508)
)

@Composable
fun GameRoute(
    session: Long,
    onGameOver: (ShiftResult) -> Unit,
    onMenu: () -> Unit
) {
    val viewModel: GameViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(session) {
        viewModel.beginShift(session)
    }
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is GameEvent.ShiftFinished -> onGameOver(event.result)
            }
        }
    }
    DisposableEffect(Unit) {
        onDispose { viewModel.pause() }
    }
    GameScreen(
        state = state,
        onPrimaryAction = viewModel::onPrimaryAction,
        onReset = viewModel::onResetGates,
        onGateStep = viewModel::onGateStep,
        onGateFlip = viewModel::onGateFlip,
        onPad = viewModel::onPadTap,
        onPause = viewModel::onPauseRequested,
        onResume = viewModel::onResume,
        onMenu = onMenu
    )
}

@Composable
fun GameScreen(
    state: GameUiState,
    onPrimaryAction: () -> Unit,
    onReset: () -> Unit,
    onGateStep: (Int, Int) -> Unit,
    onGateFlip: (Int) -> Unit,
    onPad: (Int) -> Unit,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    val voidLabel = stringResource(R.string.slot_void)
    val lanePrefix = stringResource(R.string.multiplier_prefix)
    val facingLabels = listOf(
        stringResource(R.string.facing_left),
        stringResource(R.string.facing_right)
    )
    val gateDescriptions = state.gates.map { gate ->
        stringResource(
            R.string.cd_gate,
            gate.id + 1,
            gate.lane + 1,
            facingLabels[gate.facing.ordinal]
        )
    }
    val gateStateLabels = state.gates.map { gate ->
        stringResource(R.string.gate_state, gate.lane + 1, facingLabels[gate.facing.ordinal])
    }
    ScreenBackground(overlay = GameOverlay, modifier = modifier) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize().safeDrawingPadding()) {
            val frame = 12.dp
            val slotHeight = 44.dp
            val boardMaxWidth = minOf(maxWidth - 32.dp, 380.dp)
            val laneWidth = floor(
                ((boardMaxWidth - frame * 2).value / GameConfig.LANES).toDouble()
            ).toFloat().dp
            val boardWidth = laneWidth * GameConfig.LANES + frame * 2
            val reserved = 72.dp + 124.dp + 124.dp + 36.dp
            val boardBudget = maxOf(maxHeight - reserved, 232.dp)
            val rawRow = ((boardBudget - slotHeight - frame * 2).value / GameConfig.PEG_ROWS)
            val pegRowHeight = floor(rawRow.coerceIn(22f, 34f).toDouble()).toFloat().dp
            val boardHeight = pegRowHeight * GameConfig.PEG_ROWS + slotHeight + frame * 2

            Column(modifier = Modifier.fillMaxSize()) {
                GameHeader(state = state, onPause = onPause)
                Spacer(modifier = Modifier.weight(1f))
                PegBoard(
                    state = state,
                    boardWidth = boardWidth,
                    boardHeight = boardHeight,
                    laneWidth = laneWidth,
                    pegRowHeight = pegRowHeight,
                    slotHeight = slotHeight,
                    frame = frame,
                    voidLabel = voidLabel,
                    multiplierLabel = { payout -> lanePrefix + payout },
                    gateDescription = { id -> gateDescriptions.getOrElse(id) { lanePrefix } },
                    gateStateLabel = { id -> gateStateLabels.getOrElse(id) { lanePrefix } },
                    onGateStep = onGateStep,
                    onGateFlip = onGateFlip,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(116.dp)
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    PhaseContent(state = state, onPad = onPad)
                }
                ControlBar(
                    torqueLabel = stringResource(R.string.game_torque),
                    torqueTotal = state.torqueTotal,
                    torqueLeft = state.torqueLeft,
                    torqueState = stringResource(
                        R.string.game_torque_state,
                        state.torqueLeft,
                        state.torqueTotal
                    ),
                    chainText = if (state.chain > 1.0) {
                        stringResource(R.string.game_chain, Labels.chain(state.chain))
                    } else {
                        null
                    },
                    actionText = if (state.phase == GamePhase.CALIBRATING) {
                        stringResource(R.string.action_drop_anyway)
                    } else {
                        stringResource(R.string.action_drop)
                    },
                    actionState = when (state.phase) {
                        GamePhase.AIM -> stringResource(R.string.action_state_ready)
                        GamePhase.CALIBRATING -> stringResource(R.string.action_state_skip)
                        else -> stringResource(R.string.action_state_busy)
                    },
                    onAction = onPrimaryAction,
                    resetText = stringResource(R.string.action_reset),
                    resetEnabled = state.phase == GamePhase.AIM && !state.paused,
                    onReset = onReset,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(14.dp))
            }
        }
    }
    if (state.paused) {
        PauseDialog(
            title = stringResource(R.string.pause_title),
            message = stringResource(R.string.pause_message),
            resumeText = stringResource(R.string.pause_resume),
            menuText = stringResource(R.string.menu),
            onResume = onResume,
            onMenu = onMenu
        )
    }
}

@Composable
private fun PhaseContent(state: GameUiState, onPad: (Int) -> Unit) {
    val padLabels = (0 until GameConfig.CALIBRATION_PADS).map { index ->
        stringResource(R.string.cd_pad, index + 1)
    }
    when (state.phase) {
        GamePhase.CALIBRATING -> CalibrationPanel(
            title = stringResource(R.string.game_calibrate),
            pads = GameConfig.CALIBRATION_PADS,
            highlighted = state.calibrationShown,
            entered = state.calibrationEntered,
            accepting = state.calibrationAcceptsInput,
            remaining = state.calibrationRemaining,
            padDescription = { index -> padLabels[index] },
            onPad = onPad
        )
        GamePhase.BANNER -> PhaseBanner(
            text = if (state.banner == BannerKind.CALIBRATED) {
                stringResource(R.string.banner_calibrated)
            } else {
                stringResource(R.string.banner_misaligned)
            },
            accent = if (state.banner == BannerKind.CALIBRATED) AccentCyan else AccentMagenta
        )
        GamePhase.LANDING -> PhaseBanner(
            text = if (state.lastGain > 0) {
                stringResource(R.string.game_gain, Labels.charge(state.lastGain))
            } else {
                stringResource(R.string.game_void)
            },
            accent = if (state.lastGain > 0) AccentCyan else AccentMagenta
        )
        else -> AimPanel(
            hint = stringResource(R.string.game_hint),
            overchargeText = stringResource(R.string.game_overcharge)
        )
    }
}

@Composable
private fun GameHeader(state: GameUiState, onPause: () -> Unit) {
    val pauseLabel = stringResource(R.string.cd_pause)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceDim.copy(alpha = 0.72f))
            .heightIn(min = 72.dp)
            .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(AccentMagenta.copy(alpha = 0.16f), RoundedCornerShape(14.dp))
                .clickable { onPause() }
                .semantics { contentDescription = pauseLabel },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.icon_pause),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(26.dp)
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = stringResource(R.string.game_shift, state.dropIndex, state.totalDrops),
                style = MaterialTheme.typography.labelLarge,
                color = TextPrimary,
                modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
            )
            StabilityBar(
                total = GameConfig.STABILITY_CELLS,
                remaining = state.stability,
                description = stringResource(
                    R.string.cd_stability,
                    state.stability,
                    GameConfig.STABILITY_CELLS
                )
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(R.drawable.sprite_4),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = Labels.charge(state.shiftCharge),
                style = MaterialTheme.typography.titleMedium,
                color = AccentCyan,
                modifier = Modifier
                    .padding(start = 6.dp)
                    .semantics { liveRegion = LiveRegionMode.Polite }
            )
        }
    }
}
