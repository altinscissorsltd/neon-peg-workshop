package com.Kebcaqa1.Veghar.domain.usecase

import com.Kebcaqa1.Veghar.domain.model.Mission
import com.Kebcaqa1.Veghar.domain.model.MissionKind
import com.Kebcaqa1.Veghar.domain.model.ShiftResult

class AdvanceMissionsUseCase {

    operator fun invoke(
        pool: List<Mission>,
        current: Map<String, Int>,
        result: ShiftResult
    ): Map<String, Int> {
        val next = HashMap(current)
        pool.forEach { mission ->
            val gained = gainFor(mission, result)
            if (gained > 0) {
                val stored = next[mission.id] ?: 0
                next[mission.id] = minOf(mission.target, stored + gained)
            }
        }
        return next
    }

    fun completedIn(
        pool: List<Mission>,
        before: Map<String, Int>,
        after: Map<String, Int>
    ): List<String> = pool.filter { mission ->
        val old = before[mission.id] ?: 0
        val new = after[mission.id] ?: 0
        old < mission.target && new >= mission.target
    }.map { it.id }

    private fun gainFor(mission: Mission, result: ShiftResult): Int = when (mission.kind) {
        MissionKind.CORE_HITS -> result.coreHits
        MissionKind.CALIBRATION_STREAK -> result.bestCalibrationStreak
        MissionKind.STABLE_SHIFT -> if (result.completed && result.stableRun) 1 else 0
        MissionKind.OVERCHARGE_HITS -> result.overchargeHits
        MissionKind.CHAIN_REACH -> if (result.bestChain >= 2.0) 1 else 0
        MissionKind.CLEAR_STAGE -> if (result.completed && result.stageId >= mission.target) mission.target else 0
    }
}
