package com.Kebcaqa1.Veghar.presentation.levelmap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Kebcaqa1.Veghar.data.sample.SampleData
import com.Kebcaqa1.Veghar.domain.model.Progress
import com.Kebcaqa1.Veghar.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LevelMapViewModel(
    private val progress: ProgressRepository
) : ViewModel() {

    private val mutableState = MutableStateFlow(build(progress.progress.value, 0))
    val state: StateFlow<LevelMapUiState> = mutableState.asStateFlow()

    init {
        viewModelScope.launch {
            progress.progress.collect { saved ->
                mutableState.update { current -> build(saved, current.lockedNoticeFor) }
            }
        }
    }

    private fun build(saved: Progress, notice: Int): LevelMapUiState = LevelMapUiState(
        nodes = SampleData.stages.map { stage ->
            StageNode(
                id = stage.id,
                nameKey = stage.nameKey,
                threshold = stage.chargeThreshold,
                torque = stage.torque,
                bias = stage.driftBias,
                unlocked = stage.id <= saved.unlockedStages,
                current = stage.id == saved.stageId,
                mastered = saved.masteredStages.contains(stage.id)
            )
        },
        charge = saved.totalCharge,
        selectedId = saved.stageId,
        lockedNoticeFor = notice
    )

    fun onNodeSelected(stageId: Int) {
        val node = mutableState.value.nodes.firstOrNull { it.id == stageId } ?: return
        if (!node.unlocked) {
            mutableState.update { it.copy(lockedNoticeFor = stageId) }
            return
        }
        mutableState.update { it.copy(lockedNoticeFor = 0) }
        progress.selectStage(stageId)
    }
}
