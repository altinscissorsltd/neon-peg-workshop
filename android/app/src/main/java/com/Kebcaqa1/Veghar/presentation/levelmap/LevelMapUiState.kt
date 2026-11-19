package com.Kebcaqa1.Veghar.presentation.levelmap

data class StageNode(
    val id: Int,
    val nameKey: String,
    val threshold: Int,
    val torque: Int,
    val bias: Double,
    val unlocked: Boolean,
    val current: Boolean,
    val mastered: Boolean
)

data class LevelMapUiState(
    val nodes: List<StageNode> = emptyList(),
    val charge: Int = 0,
    val selectedId: Int = 1,
    val lockedNoticeFor: Int = 0
)
