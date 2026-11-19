package com.Kebcaqa1.Veghar.domain.model

enum class CalibrationTimer(val millis: Long, val bonusPercent: Int) {
    RELAXED(7000L, 0),
    STANDARD(5000L, 0),
    STRICT(3500L, 25)
}

enum class GateControl {
    DRAG_AND_TAP,
    TAP_ONLY
}

enum class MotionLevel {
    FULL,
    REDUCED
}

data class GameSettings(
    val calibrationTimer: CalibrationTimer = CalibrationTimer.STANDARD,
    val gateControl: GateControl = GateControl.DRAG_AND_TAP,
    val motion: MotionLevel = MotionLevel.FULL,
    val highContrastSlots: Boolean = false
)
