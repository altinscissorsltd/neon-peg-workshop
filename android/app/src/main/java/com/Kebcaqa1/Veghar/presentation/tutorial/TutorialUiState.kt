package com.Kebcaqa1.Veghar.presentation.tutorial

data class TutorialUiState(
    val step: Int = 0,
    val totalSteps: Int = 4,
    val seen: Boolean = false
) {
    val lastStep: Boolean get() = step >= totalSteps - 1
}
