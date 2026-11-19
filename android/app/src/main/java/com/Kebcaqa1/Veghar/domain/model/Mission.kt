package com.Kebcaqa1.Veghar.domain.model

enum class MissionKind {
    CORE_HITS,
    CALIBRATION_STREAK,
    STABLE_SHIFT,
    OVERCHARGE_HITS,
    CHAIN_REACH,
    CLEAR_STAGE
}

data class Mission(
    val id: String,
    val kind: MissionKind,
    val target: Int,
    val reward: Int
)

data class MissionProgress(
    val mission: Mission,
    val progress: Int,
    val claimed: Boolean
) {
    val ready: Boolean get() = !claimed && progress >= mission.target
    val fraction: Float
        get() = if (mission.target <= 0) 0f else (progress.toFloat() / mission.target).coerceIn(0f, 1f)
}
