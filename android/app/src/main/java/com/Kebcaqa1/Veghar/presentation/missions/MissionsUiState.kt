package com.Kebcaqa1.Veghar.presentation.missions

enum class MissionsNotice {
    NONE,
    NOT_READY
}

data class MissionCard(
    val id: String,
    val progress: Int,
    val target: Int,
    val reward: Int,
    val claimed: Boolean,
    val ready: Boolean
)

data class TrackCard(
    val id: String,
    val progress: Int,
    val target: Int,
    val reward: Int,
    val claimed: Boolean,
    val ready: Boolean
)

data class MissionsUiState(
    val missions: List<MissionCard> = emptyList(),
    val tracks: List<TrackCard> = emptyList(),
    val notice: MissionsNotice = MissionsNotice.NONE,
    val allClaimed: Boolean = false
)
