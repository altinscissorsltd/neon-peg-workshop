package com.Kebcaqa1.Veghar.presentation.collection

import com.Kebcaqa1.Veghar.domain.model.ModuleKind

enum class CollectionNotice {
    NONE,
    NOT_ENOUGH_CHARGE
}

data class ModuleCard(
    val id: String,
    val kind: ModuleKind,
    val price: Int,
    val tint: Long,
    val owned: Boolean,
    val equipped: Boolean,
    val affordable: Boolean
)

data class CollectionUiState(
    val modules: List<ModuleCard> = emptyList(),
    val charge: Int = 0,
    val ownedCount: Int = 0,
    val notice: CollectionNotice = CollectionNotice.NONE
)
