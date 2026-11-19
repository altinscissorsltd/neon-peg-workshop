package com.Kebcaqa1.Veghar.core.ui

import com.Kebcaqa1.Veghar.R
import com.Kebcaqa1.Veghar.domain.model.CalibrationTimer
import com.Kebcaqa1.Veghar.domain.model.GateControl
import com.Kebcaqa1.Veghar.domain.model.GateFacing
import com.Kebcaqa1.Veghar.domain.model.ModuleKind
import com.Kebcaqa1.Veghar.domain.model.MotionLevel

object Labels {

    fun stageName(key: String): Int = when (key) {
        "starter_bench" -> R.string.stage_starter_bench
        "copper_line" -> R.string.stage_copper_line
        "orbit_bay" -> R.string.stage_orbit_bay
        "split_forge" -> R.string.stage_split_forge
        "magnet_row" -> R.string.stage_magnet_row
        "ghost_lanes" -> R.string.stage_ghost_lanes
        "overdrive" -> R.string.stage_overdrive
        else -> R.string.stage_the_core
    }

    fun missionName(id: String): Int = when (id) {
        "mission_core_run" -> R.string.mission_core_run
        "mission_steady_hands" -> R.string.mission_steady_hands
        "mission_flawless_shift" -> R.string.mission_flawless_shift
        "mission_overcharged" -> R.string.mission_overcharged
        "mission_chain_two" -> R.string.mission_chain_two
        else -> R.string.mission_deep_bench
    }

    fun moduleName(id: String): Int = when (id) {
        "module_gate_copper" -> R.string.module_gate_copper
        "module_gate_ion" -> R.string.module_gate_ion
        "module_gate_obsidian" -> R.string.module_gate_obsidian
        "module_peg_amber" -> R.string.module_peg_amber
        "module_peg_frost" -> R.string.module_peg_frost
        "module_peg_ember" -> R.string.module_peg_ember
        "module_core_pulse" -> R.string.module_core_pulse
        "module_core_lime" -> R.string.module_core_lime
        "module_core_violet" -> R.string.module_core_violet
        "module_plate_steel" -> R.string.module_plate_steel
        "module_plate_neon" -> R.string.module_plate_neon
        else -> R.string.module_plate_haze
    }

    fun moduleKind(kind: ModuleKind): Int = when (kind) {
        ModuleKind.GATE_SKIN -> R.string.kind_gate_skin
        ModuleKind.PEG_FILAMENT -> R.string.kind_peg_filament
        ModuleKind.TOKEN_CORE -> R.string.kind_token_core
        ModuleKind.SLOT_PLATE -> R.string.kind_slot_plate
    }

    fun moduleArt(kind: ModuleKind): Int = when (kind) {
        ModuleKind.GATE_SKIN -> R.drawable.sprite_2
        ModuleKind.PEG_FILAMENT -> R.drawable.sprite_3
        ModuleKind.TOKEN_CORE -> R.drawable.sprite_1
        ModuleKind.SLOT_PLATE -> R.drawable.sprite_5
    }

    fun trackName(id: String): Int = when (id) {
        "track_daily_bench" -> R.string.track_daily_bench
        "track_shift_streak" -> R.string.track_shift_streak
        else -> R.string.track_precision
    }

    fun facing(facing: GateFacing): Int =
        if (facing == GateFacing.LEFT) R.string.facing_left else R.string.facing_right

    fun calibrationTimer(value: CalibrationTimer): Int = when (value) {
        CalibrationTimer.RELAXED -> R.string.timer_relaxed
        CalibrationTimer.STANDARD -> R.string.timer_standard
        CalibrationTimer.STRICT -> R.string.timer_strict
    }

    fun gateControl(value: GateControl): Int =
        if (value == GateControl.DRAG_AND_TAP) R.string.control_drag else R.string.control_tap

    fun motion(value: MotionLevel): Int =
        if (value == MotionLevel.FULL) R.string.motion_full else R.string.motion_reduced

    fun charge(value: Int): String {
        val digits = value.toString()
        if (digits.length <= 3) return digits
        val builder = StringBuilder()
        val offset = digits.length % 3
        digits.forEachIndexed { index, ch ->
            if (index > 0 && index % 3 == offset) builder.append(' ')
            builder.append(ch)
        }
        return builder.toString()
    }

    fun chain(value: Double): String {
        val rounded = (value * 100).toInt()
        return if (rounded % 100 == 0) (rounded / 100).toString() else (rounded / 100.0).toString()
    }

    fun percent(value: Double): String = (value * 100).toInt().toString()
}
