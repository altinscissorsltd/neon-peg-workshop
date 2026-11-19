package com.Kebcaqa1.Veghar.presentation.settings

import com.Kebcaqa1.Veghar.domain.model.CalibrationTimer
import com.Kebcaqa1.Veghar.domain.model.GateControl
import com.Kebcaqa1.Veghar.domain.model.MotionLevel

data class SettingsUiState(
    val calibrationTimer: CalibrationTimer = CalibrationTimer.STANDARD,
    val gateControl: GateControl = GateControl.DRAG_AND_TAP,
    val motion: MotionLevel = MotionLevel.FULL,
    val highContrast: Boolean = false,
    val confirmReset: Boolean = false
)
