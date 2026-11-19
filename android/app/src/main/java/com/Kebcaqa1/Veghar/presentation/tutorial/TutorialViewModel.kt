package com.Kebcaqa1.Veghar.presentation.tutorial

import androidx.lifecycle.ViewModel
import com.Kebcaqa1.Veghar.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TutorialViewModel(
    private val progress: ProgressRepository
) : ViewModel() {

    private val mutableState = MutableStateFlow(
        TutorialUiState(seen = progress.progress.value.tutorialSeen)
    )
    val state: StateFlow<TutorialUiState> = mutableState.asStateFlow()

    fun onNext() {
        val current = mutableState.value
        if (current.lastStep) {
            markSeen()
            return
        }
        mutableState.update { it.copy(step = it.step + 1) }
    }

    fun onStepSelected(step: Int) {
        mutableState.update { it.copy(step = step.coerceIn(0, it.totalSteps - 1)) }
    }

    fun markSeen() {
        progress.markTutorialSeen()
        mutableState.update { it.copy(seen = true) }
    }
}
