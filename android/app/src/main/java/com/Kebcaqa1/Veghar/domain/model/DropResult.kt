package com.Kebcaqa1.Veghar.domain.model

data class DropPath(
    val lanes: List<Int>,
    val landing: Int
)

data class DropOutcome(
    val landing: Int,
    val payout: Int,
    val gain: Int,
    val voided: Boolean,
    val core: Boolean,
    val overcharge: Boolean,
    val chain: Double
)
