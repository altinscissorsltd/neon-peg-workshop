package com.Kebcaqa1.Veghar.domain.model

data class Progress(
    val totalCharge: Int = 0,
    val bestShift: Int = 0,
    val coreHits: Int = 0,
    val perfectCalibrations: Int = 0,
    val shiftsFinished: Int = 0,
    val stageId: Int = 1,
    val unlockedStages: Int = 1,
    val masteredStages: Set<Int> = emptySet(),
    val ownedModules: Set<String> = emptySet(),
    val equippedModules: Map<ModuleKind, String> = emptyMap(),
    val missionProgress: Map<String, Int> = emptyMap(),
    val claimedMissions: Set<String> = emptySet(),
    val trackProgress: Map<String, Int> = emptyMap(),
    val claimedTracks: Set<String> = emptySet(),
    val tutorialSeen: Boolean = false
)
