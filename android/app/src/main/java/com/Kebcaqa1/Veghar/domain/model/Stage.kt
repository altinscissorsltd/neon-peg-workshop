package com.Kebcaqa1.Veghar.domain.model

data class Stage(
    val id: Int,
    val nameKey: String,
    val driftBias: Double,
    val torque: Int,
    val chargeThreshold: Int,
    val payouts: List<Int>
) {
    fun payoutAt(lane: Int): Int = payouts.getOrElse(lane) { 0 }

    fun isVoid(lane: Int): Boolean = payoutAt(lane) == 0

    fun isCore(lane: Int): Boolean = payoutAt(lane) == payouts.max()
}
