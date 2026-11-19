package com.Kebcaqa1.Veghar.data.repository

import com.Kebcaqa1.Veghar.data.local.PreferencesStore
import com.Kebcaqa1.Veghar.data.sample.SampleData
import com.Kebcaqa1.Veghar.domain.model.ModuleKind
import com.Kebcaqa1.Veghar.domain.model.Progress
import com.Kebcaqa1.Veghar.domain.model.ShiftResult
import com.Kebcaqa1.Veghar.domain.repository.ProgressRepository
import com.Kebcaqa1.Veghar.domain.usecase.AdvanceMissionsUseCase
import com.Kebcaqa1.Veghar.domain.usecase.UnlockStageUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

private const val KEY_TOTAL_CHARGE = "total_charge"
private const val KEY_BEST_SHIFT = "best_shift"
private const val KEY_CORE_HITS = "core_hits"
private const val KEY_PERFECT_CALIBRATIONS = "perfect_calibrations"
private const val KEY_SHIFTS_FINISHED = "shifts_finished"
private const val KEY_STAGE = "stage_id"
private const val KEY_UNLOCKED = "unlocked_stages"
private const val KEY_MASTERED = "mastered_stages"
private const val KEY_OWNED = "owned_modules"
private const val KEY_EQUIPPED = "equipped_modules"
private const val KEY_MISSION_PROGRESS = "mission_progress"
private const val KEY_MISSION_CLAIMED = "mission_claimed"
private const val KEY_TRACK_PROGRESS = "track_progress"
private const val KEY_TRACK_CLAIMED = "track_claimed"
private const val KEY_TUTORIAL = "tutorial_seen"

class ProgressRepositoryImpl(
    private val store: PreferencesStore,
    private val advanceMissions: AdvanceMissionsUseCase,
    private val unlockStage: UnlockStageUseCase
) : ProgressRepository {

    private val state = MutableStateFlow(read())

    override val progress: StateFlow<Progress> = state.asStateFlow()

    private fun read(): Progress = Progress(
        totalCharge = store.int(KEY_TOTAL_CHARGE, 0),
        bestShift = store.int(KEY_BEST_SHIFT, 0),
        coreHits = store.int(KEY_CORE_HITS, 0),
        perfectCalibrations = store.int(KEY_PERFECT_CALIBRATIONS, 0),
        shiftsFinished = store.int(KEY_SHIFTS_FINISHED, 0),
        stageId = store.int(KEY_STAGE, 1),
        unlockedStages = store.int(KEY_UNLOCKED, 1),
        masteredStages = store.ids(KEY_MASTERED).mapNotNull { it.toIntOrNull() }.toSet(),
        ownedModules = store.ids(KEY_OWNED),
        equippedModules = store.pairs(KEY_EQUIPPED).mapNotNull { entry ->
            val kind = ModuleKind.entries.firstOrNull { it.name == entry.key }
            if (kind == null) null else kind to entry.value
        }.toMap(),
        missionProgress = store.counters(KEY_MISSION_PROGRESS),
        claimedMissions = store.ids(KEY_MISSION_CLAIMED),
        trackProgress = store.counters(KEY_TRACK_PROGRESS),
        claimedTracks = store.ids(KEY_TRACK_CLAIMED),
        tutorialSeen = store.bool(KEY_TUTORIAL, false)
    )

    private fun write(next: Progress) {
        store.putInt(KEY_TOTAL_CHARGE, next.totalCharge)
        store.putInt(KEY_BEST_SHIFT, next.bestShift)
        store.putInt(KEY_CORE_HITS, next.coreHits)
        store.putInt(KEY_PERFECT_CALIBRATIONS, next.perfectCalibrations)
        store.putInt(KEY_SHIFTS_FINISHED, next.shiftsFinished)
        store.putInt(KEY_STAGE, next.stageId)
        store.putInt(KEY_UNLOCKED, next.unlockedStages)
        store.putIds(KEY_MASTERED, next.masteredStages.map { it.toString() }.toSet())
        store.putIds(KEY_OWNED, next.ownedModules)
        store.putPairs(KEY_EQUIPPED, next.equippedModules.mapKeys { it.key.name })
        store.putCounters(KEY_MISSION_PROGRESS, next.missionProgress)
        store.putIds(KEY_MISSION_CLAIMED, next.claimedMissions)
        store.putCounters(KEY_TRACK_PROGRESS, next.trackProgress)
        store.putIds(KEY_TRACK_CLAIMED, next.claimedTracks)
        store.putBool(KEY_TUTORIAL, next.tutorialSeen)
        state.value = next
    }

    override fun applyShift(result: ShiftResult) {
        val current = state.value
        val total = current.totalCharge + result.charge
        val missions = advanceMissions(SampleData.missions, current.missionProgress, result)
        val tracks = HashMap(current.trackProgress)
        if (result.completed) {
            tracks["track_shift_streak"] = (tracks["track_shift_streak"] ?: 0) + 1
        }
        tracks["track_precision"] = (tracks["track_precision"] ?: 0) + result.calibrations
        val mastered = if (result.completed && result.coreHits >= 3) {
            current.masteredStages + result.stageId
        } else {
            current.masteredStages
        }
        write(
            current.copy(
                totalCharge = total,
                bestShift = maxOf(current.bestShift, result.charge),
                coreHits = current.coreHits + result.coreHits,
                perfectCalibrations = current.perfectCalibrations + result.calibrations,
                shiftsFinished = current.shiftsFinished + if (result.completed) 1 else 0,
                unlockedStages = unlockStage(total, SampleData.stages),
                masteredStages = mastered,
                missionProgress = missions,
                trackProgress = tracks
            )
        )
    }

    override fun selectStage(stageId: Int) {
        val current = state.value
        if (stageId > current.unlockedStages) return
        write(current.copy(stageId = stageId))
    }

    override fun buyModule(moduleId: String, price: Int): Boolean {
        val current = state.value
        if (current.ownedModules.contains(moduleId) || current.totalCharge < price) return false
        write(
            current.copy(
                totalCharge = current.totalCharge - price,
                ownedModules = current.ownedModules + moduleId
            )
        )
        return true
    }

    override fun equipModule(moduleId: String, kind: ModuleKind) {
        val current = state.value
        if (!current.ownedModules.contains(moduleId)) return
        write(current.copy(equippedModules = current.equippedModules + (kind to moduleId)))
    }

    override fun claimMission(missionId: String, reward: Int): Boolean {
        val current = state.value
        if (current.claimedMissions.contains(missionId)) return false
        val mission = SampleData.missions.firstOrNull { it.id == missionId } ?: return false
        if ((current.missionProgress[missionId] ?: 0) < mission.target) return false
        val total = current.totalCharge + reward
        write(
            current.copy(
                totalCharge = total,
                unlockedStages = unlockStage(total, SampleData.stages),
                claimedMissions = current.claimedMissions + missionId
            )
        )
        return true
    }

    override fun claimTrack(trackId: String, reward: Int): Boolean {
        val current = state.value
        if (current.claimedTracks.contains(trackId)) return false
        val track = SampleData.rewardTracks.firstOrNull { it.id == trackId } ?: return false
        if ((current.trackProgress[trackId] ?: 0) < track.target) return false
        val total = current.totalCharge + reward
        write(
            current.copy(
                totalCharge = total,
                unlockedStages = unlockStage(total, SampleData.stages),
                claimedTracks = current.claimedTracks + trackId
            )
        )
        return true
    }

    override fun registerVisit() {
        val current = state.value
        if ((current.trackProgress["track_daily_bench"] ?: 0) >= 1) return
        write(
            current.copy(
                trackProgress = current.trackProgress + ("track_daily_bench" to 1)
            )
        )
    }

    override fun markTutorialSeen() {
        val current = state.value
        if (current.tutorialSeen) return
        write(current.copy(tutorialSeen = true))
    }

    override fun eraseProgress() {
        store.clearAll(
            listOf(
                KEY_TOTAL_CHARGE, KEY_BEST_SHIFT, KEY_CORE_HITS, KEY_PERFECT_CALIBRATIONS,
                KEY_SHIFTS_FINISHED, KEY_STAGE, KEY_UNLOCKED, KEY_MASTERED, KEY_OWNED,
                KEY_EQUIPPED, KEY_MISSION_PROGRESS, KEY_MISSION_CLAIMED, KEY_TRACK_PROGRESS,
                KEY_TRACK_CLAIMED
            )
        )
        state.value = read()
    }
}
