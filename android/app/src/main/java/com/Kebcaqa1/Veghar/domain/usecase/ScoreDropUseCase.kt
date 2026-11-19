package com.Kebcaqa1.Veghar.domain.usecase

import com.Kebcaqa1.Veghar.domain.model.DropOutcome
import com.Kebcaqa1.Veghar.domain.model.Stage
import kotlin.math.roundToInt

class ScoreDropUseCase(
    private val basePayout: Int,
    private val overchargeFactor: Double,
    private val calibrationFactor: Double
) {

    operator fun invoke(
        stage: Stage,
        landing: Int,
        chain: Double,
        overchargeLane: Int,
        calibrationSolved: Boolean,
        strictBonusPercent: Int
    ): DropOutcome {
        val payout = stage.payoutAt(landing)
        val voided = payout == 0
        val overcharge = !voided && landing == overchargeLane
        var value = payout.toDouble() * basePayout * chain
        if (overcharge) value *= overchargeFactor
        if (calibrationSolved) {
            value *= calibrationFactor
            value *= 1.0 + strictBonusPercent / 100.0
        }
        return DropOutcome(
            landing = landing,
            payout = payout,
            gain = value.roundToInt(),
            voided = voided,
            core = stage.isCore(landing),
            overcharge = overcharge,
            chain = chain
        )
    }
}
