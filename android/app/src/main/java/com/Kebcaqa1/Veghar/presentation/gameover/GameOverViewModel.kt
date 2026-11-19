package com.Kebcaqa1.Veghar.presentation.gameover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Kebcaqa1.Veghar.data.sample.SampleData
import com.Kebcaqa1.Veghar.domain.model.Progress
import com.Kebcaqa1.Veghar.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class GameOverViewModel(
    progress: ProgressRepository
) : ViewModel() {

    val state: StateFlow<GameOverUiState> = progress.progress
        .map { toUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = toUiState(progress.progress.value)
        )

    private fun toUiState(progress: Progress): GameOverUiState {
        val stage = SampleData.stageOf(progress.stageId)
        val next = SampleData.stages.firstOrNull { it.chargeThreshold > progress.totalCharge }
        return GameOverUiState(
            totalCharge = progress.totalCharge,
            stageId = stage.id,
            stageNameKey = stage.nameKey,
            nextThreshold = next?.chargeThreshold ?: 0,
            hasNextStage = next != null
        )
    }
}
