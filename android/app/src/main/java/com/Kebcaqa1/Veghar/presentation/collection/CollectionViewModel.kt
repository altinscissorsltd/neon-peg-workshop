package com.Kebcaqa1.Veghar.presentation.collection

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

class CollectionViewModel(
    private val progress: ProgressRepository
) : ViewModel() {

    private val mutableState = MutableStateFlow(build(progress.progress.value, CollectionNotice.NONE))
    val state: StateFlow<CollectionUiState> = mutableState.asStateFlow()

    init {
        viewModelScope.launch {
            progress.progress.collect { saved ->
                mutableState.update { current -> build(saved, current.notice) }
            }
        }
    }

    private fun build(saved: Progress, notice: CollectionNotice): CollectionUiState =
        CollectionUiState(
            modules = SampleData.modules.map { module ->
                ModuleCard(
                    id = module.id,
                    kind = module.kind,
                    price = module.price,
                    tint = module.tint,
                    owned = saved.ownedModules.contains(module.id),
                    equipped = saved.equippedModules[module.kind] == module.id,
                    affordable = saved.totalCharge >= module.price
                )
            },
            charge = saved.totalCharge,
            ownedCount = saved.ownedModules.size,
            notice = notice
        )

    fun onModuleAction(moduleId: String) {
        val card = mutableState.value.modules.firstOrNull { it.id == moduleId } ?: return
        when {
            card.equipped -> Unit
            card.owned -> {
                mutableState.update { it.copy(notice = CollectionNotice.NONE) }
                progress.equipModule(card.id, card.kind)
            }
            card.affordable -> {
                mutableState.update { it.copy(notice = CollectionNotice.NONE) }
                progress.buyModule(card.id, card.price)
                progress.equipModule(card.id, card.kind)
            }
            else -> mutableState.update { it.copy(notice = CollectionNotice.NOT_ENOUGH_CHARGE) }
        }
    }
}
