package com.Kebcaqa1.Veghar.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Kebcaqa1.Veghar.domain.model.CalibrationTimer
import com.Kebcaqa1.Veghar.domain.model.GameSettings
import com.Kebcaqa1.Veghar.domain.model.GateControl
import com.Kebcaqa1.Veghar.domain.model.MotionLevel
import com.Kebcaqa1.Veghar.domain.repository.ProgressRepository
import com.Kebcaqa1.Veghar.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val progress: ProgressRepository,
    private val settings: SettingsRepository
) : ViewModel() {

    private val mutableState = MutableStateFlow(build(settings.settings.value, false))
    val state: StateFlow<SettingsUiState> = mutableState.asStateFlow()

    init {
        viewModelScope.launch {
            settings.settings.collect { options ->
                mutableState.update { current -> build(options, current.confirmReset) }
            }
        }
    }

    private fun build(options: GameSettings, confirmReset: Boolean) = SettingsUiState(
        calibrationTimer = options.calibrationTimer,
        gateControl = options.gateControl,
        motion = options.motion,
        highContrast = options.highContrastSlots,
        confirmReset = confirmReset
    )

    fun onTimerSelected(value: CalibrationTimer) {
        settings.setCalibrationTimer(value)
    }

    fun onControlSelected(value: GateControl) {
        settings.setGateControl(value)
    }

    fun onMotionSelected(value: MotionLevel) {
        settings.setMotion(value)
    }

    fun onContrastSelected(value: Boolean) {
        settings.setHighContrastSlots(value)
    }

    fun onResetRequested() {
        mutableState.update { it.copy(confirmReset = true) }
    }

    fun onResetCancelled() {
        mutableState.update { it.copy(confirmReset = false) }
    }

    fun onResetConfirmed() {
        progress.eraseProgress()
        mutableState.update { it.copy(confirmReset = false) }
    }
}
