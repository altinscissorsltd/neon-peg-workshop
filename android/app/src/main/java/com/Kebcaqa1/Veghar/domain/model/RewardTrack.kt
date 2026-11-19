package com.Kebcaqa1.Veghar.domain.model

enum class RewardTrackKind {
    DAILY_BENCH,
    SHIFT_STREAK,
    PRECISION
}

data class RewardTrack(
    val id: String,
    val kind: RewardTrackKind,
    val target: Int,
    val reward: Int
)

data class RewardTrackProgress(
    val track: RewardTrack,
    val progress: Int,
    val claimed: Boolean
) {
    val ready: Boolean get() = !claimed && progress >= track.target
    val fraction: Float
        get() = if (track.target <= 0) 0f else (progress.toFloat() / track.target).coerceIn(0f, 1f)
}
