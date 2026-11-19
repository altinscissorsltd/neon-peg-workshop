package com.Kebcaqa1.Veghar.domain.repository

import com.Kebcaqa1.Veghar.domain.model.CalibrationTimer
import com.Kebcaqa1.Veghar.domain.model.GameSettings
import com.Kebcaqa1.Veghar.domain.model.GateControl
import com.Kebcaqa1.Veghar.domain.model.MotionLevel
import kotlinx.coroutines.flow.StateFlow

interface SettingsRepository {
    val settings: StateFlow<GameSettings>

    fun setCalibrationTimer(value: CalibrationTimer)

    fun setGateControl(value: GateControl)

    fun setMotion(value: MotionLevel)

    fun setHighContrastSlots(value: Boolean)
}
