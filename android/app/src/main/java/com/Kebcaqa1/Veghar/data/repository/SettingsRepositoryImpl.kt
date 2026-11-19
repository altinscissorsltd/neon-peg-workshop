package com.Kebcaqa1.Veghar.data.repository

import com.Kebcaqa1.Veghar.data.local.PreferencesStore
import com.Kebcaqa1.Veghar.domain.model.CalibrationTimer
import com.Kebcaqa1.Veghar.domain.model.GameSettings
import com.Kebcaqa1.Veghar.domain.model.GateControl
import com.Kebcaqa1.Veghar.domain.model.MotionLevel
import com.Kebcaqa1.Veghar.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

private const val KEY_TIMER = "setting_timer"
private const val KEY_CONTROL = "setting_control"
private const val KEY_MOTION = "setting_motion"
private const val KEY_CONTRAST = "setting_contrast"

class SettingsRepositoryImpl(
    private val store: PreferencesStore
) : SettingsRepository {

    private val state = MutableStateFlow(read())

    override val settings: StateFlow<GameSettings> = state.asStateFlow()

    private fun read(): GameSettings = GameSettings(
        calibrationTimer = CalibrationTimer.entries
            .firstOrNull { it.name == store.text(KEY_TIMER, CalibrationTimer.STANDARD.name) }
            ?: CalibrationTimer.STANDARD,
        gateControl = GateControl.entries
            .firstOrNull { it.name == store.text(KEY_CONTROL, GateControl.DRAG_AND_TAP.name) }
            ?: GateControl.DRAG_AND_TAP,
        motion = MotionLevel.entries
            .firstOrNull { it.name == store.text(KEY_MOTION, MotionLevel.FULL.name) }
            ?: MotionLevel.FULL,
        highContrastSlots = store.bool(KEY_CONTRAST, false)
    )

    override fun setCalibrationTimer(value: CalibrationTimer) {
        store.putText(KEY_TIMER, value.name)
        state.value = state.value.copy(calibrationTimer = value)
    }

    override fun setGateControl(value: GateControl) {
        store.putText(KEY_CONTROL, value.name)
        state.value = state.value.copy(gateControl = value)
    }

    override fun setMotion(value: MotionLevel) {
        store.putText(KEY_MOTION, value.name)
        state.value = state.value.copy(motion = value)
    }

    override fun setHighContrastSlots(value: Boolean) {
        store.putBool(KEY_CONTRAST, value)
        state.value = state.value.copy(highContrastSlots = value)
    }
}
