package com.Kebcaqa1.Veghar.domain.usecase

import kotlin.random.Random

class BuildCalibrationUseCase(
    private val pads: Int,
    private val steps: Int
) {

    operator fun invoke(seed: Long): List<Int> {
        val random = Random(seed)
        val sequence = ArrayList<Int>(steps)
        var previous = -1
        repeat(steps) {
            var pick = random.nextInt(pads)
            if (pick == previous) pick = (pick + 1 + random.nextInt(pads - 1)) % pads
            sequence.add(pick)
            previous = pick
        }
        return sequence
    }

    fun overchargeLane(seed: Long, payouts: List<Int>): Int {
        val candidates = payouts.indices.filter { payouts[it] > 0 }
        if (candidates.isEmpty()) return -1
        return candidates[Random(seed).nextInt(candidates.size)]
    }
}
