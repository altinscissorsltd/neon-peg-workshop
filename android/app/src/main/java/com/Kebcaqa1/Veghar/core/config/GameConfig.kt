package com.Kebcaqa1.Veghar.core.config

object GameConfig {
    const val LOADER_DURATION_MS = 8000L

    const val LANES = 9
    const val PEG_ROWS = 8
    const val CENTER_LANE = 4

    const val DROPS_PER_SHIFT = 5
    const val STABILITY_CELLS = 3
    const val TORQUE_BASE = 6
    const val TORQUE_BONUS = 8

    const val DROP_ANIM_MS = 1400L
    const val DROP_ANIM_REDUCED_MS = 600L
    const val FRAME_MS = 16L
    const val LANDING_MS = 520L
    const val BANNER_MS = 900L

    const val CALIBRATION_PADS = 4
    const val CALIBRATION_STEPS = 4
    const val CALIBRATION_FLASH_MS = 450L
    const val CALIBRATION_GAP_MS = 150L

    const val STAGE_BASE_PAYOUT = 40
    const val PEG_SLIP_CHANCE = 0.2
    const val GATE_SPAN = 1
    const val GATE_MIN_LANE = 1
    const val GATE_MAX_LANE = 7
    const val OVERCHARGE_FACTOR = 2.0
    const val CALIBRATION_FACTOR = 1.5
    const val STAGE_COUNT = 8

    val GATE_ROWS = listOf(2, 4, 6)
    val CHAIN_STEPS = listOf(1.0, 1.25, 1.5, 2.0)
}
