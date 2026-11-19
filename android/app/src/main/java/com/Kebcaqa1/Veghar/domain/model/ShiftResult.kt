package com.Kebcaqa1.Veghar.domain.model

data class ShiftResult(
    val session: Long,
    val completed: Boolean,
    val charge: Int,
    val coreHits: Int,
    val calibrations: Int,
    val overchargeHits: Int,
    val bestChain: Double,
    val stableRun: Boolean,
    val bestCalibrationStreak: Int,
    val stageId: Int,
    val unlockedStageId: Int,
    val completedMissions: List<String>
)
