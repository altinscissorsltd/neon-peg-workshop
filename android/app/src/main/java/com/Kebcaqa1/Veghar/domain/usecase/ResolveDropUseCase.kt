package com.Kebcaqa1.Veghar.domain.usecase

import com.Kebcaqa1.Veghar.domain.model.DropPath
import com.Kebcaqa1.Veghar.domain.model.Gate
import com.Kebcaqa1.Veghar.domain.model.Stage
import kotlin.math.abs
import kotlin.random.Random

class ResolveDropUseCase(
    private val lanes: Int,
    private val rows: Int,
    private val centerLane: Int,
    private val slipChance: Double,
    private val gateSpan: Int
) {

    operator fun invoke(stage: Stage, gates: List<Gate>, seed: Long): DropPath {
        val random = Random(seed)
        var lane = centerLane
        val path = ArrayList<Int>(rows)
        for (row in 0 until rows) {
            val gate = gates.firstOrNull { it.row == row && abs(it.lane - lane) <= gateSpan }
            val step = if (gate != null) {
                gate.facing.step()
            } else {
                pegStep(lane, stage.driftBias, random)
            }
            lane = (lane + step).coerceIn(0, lanes - 1)
            path.add(lane)
        }
        return DropPath(lanes = path, landing = lane)
    }

    private fun pegStep(lane: Int, bias: Double, random: Random): Int {
        if (random.nextDouble() < slipChance) return 0
        if (lane == centerLane) return if (random.nextBoolean()) -1 else 1
        val outward = if (lane < centerLane) -1 else 1
        return if (random.nextDouble() < bias) outward else -outward
    }
}
