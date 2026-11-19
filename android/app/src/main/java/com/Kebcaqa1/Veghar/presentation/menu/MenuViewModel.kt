package com.Kebcaqa1.Veghar.presentation.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Kebcaqa1.Veghar.data.sample.SampleData
import com.Kebcaqa1.Veghar.domain.model.GameSettings
import com.Kebcaqa1.Veghar.domain.model.MotionLevel
import com.Kebcaqa1.Veghar.domain.model.Progress
import com.Kebcaqa1.Veghar.domain.repository.ProgressRepository
import com.Kebcaqa1.Veghar.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class MenuViewModel(
    progress: ProgressRepository,
    settings: SettingsRepository
) : ViewModel() {

    val state: StateFlow<MenuUiState> =
        combine(progress.progress, settings.settings) { current, options ->
            toUiState(current, options)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = toUiState(progress.progress.value, settings.settings.value)
        )

    private fun toUiState(progress: Progress, options: GameSettings): MenuUiState {
        val stage = SampleData.stageOf(progress.stageId)
        val active = SampleData.missions.count { mission ->
            !progress.claimedMissions.contains(mission.id)
        }
        return MenuUiState(
            charge = progress.totalCharge,
            stageId = stage.id,
            stageNameKey = stage.nameKey,
            unlockedStages = progress.unlockedStages,
            totalStages = SampleData.stages.size,
            activeMissions = active,
            ownedModules = progress.ownedModules.size,
            totalModules = SampleData.modules.size,
            bestShift = progress.bestShift,
            coreHits = progress.coreHits,
            perfectCalibrations = progress.perfectCalibrations,
            tutorialSeen = progress.tutorialSeen,
            reducedMotion = options.motion == MotionLevel.REDUCED
        )
    }
}
