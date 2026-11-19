package com.Kebcaqa1.Veghar.domain.model

enum class ModuleKind {
    GATE_SKIN,
    PEG_FILAMENT,
    TOKEN_CORE,
    SLOT_PLATE
}

data class WorkshopModule(
    val id: String,
    val kind: ModuleKind,
    val price: Int,
    val tint: Long
)
