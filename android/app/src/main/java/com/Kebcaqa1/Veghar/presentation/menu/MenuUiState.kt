package com.Kebcaqa1.Veghar.presentation.menu

data class MenuUiState(
    val charge: Int = 0,
    val stageId: Int = 1,
    val stageNameKey: String = "starter_bench",
    val unlockedStages: Int = 1,
    val totalStages: Int = 8,
    val activeMissions: Int = 0,
    val ownedModules: Int = 0,
    val totalModules: Int = 12,
    val bestShift: Int = 0,
    val coreHits: Int = 0,
    val perfectCalibrations: Int = 0,
    val tutorialSeen: Boolean = false,
    val reducedMotion: Boolean = false
)
