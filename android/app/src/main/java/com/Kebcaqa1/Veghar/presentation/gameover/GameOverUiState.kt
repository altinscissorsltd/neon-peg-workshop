package com.Kebcaqa1.Veghar.presentation.gameover

data class GameOverUiState(
    val totalCharge: Int = 0,
    val stageId: Int = 1,
    val stageNameKey: String = "starter_bench",
    val nextThreshold: Int = 0,
    val hasNextStage: Boolean = true
)
