package com.Kebcaqa1.Veghar.data.sample

import com.Kebcaqa1.Veghar.domain.model.Mission
import com.Kebcaqa1.Veghar.domain.model.MissionKind
import com.Kebcaqa1.Veghar.domain.model.ModuleKind
import com.Kebcaqa1.Veghar.domain.model.RewardTrack
import com.Kebcaqa1.Veghar.domain.model.RewardTrackKind
import com.Kebcaqa1.Veghar.domain.model.Stage
import com.Kebcaqa1.Veghar.domain.model.WorkshopModule

object SampleData {

    val stages: List<Stage> = listOf(
        Stage(1, "starter_bench", 0.54, 6, 0, listOf(0, 1, 2, 3, 5, 3, 2, 1, 0)),
        Stage(2, "copper_line", 0.56, 6, 800, listOf(0, 1, 2, 4, 5, 3, 2, 1, 0)),
        Stage(3, "orbit_bay", 0.58, 6, 2200, listOf(0, 2, 3, 5, 2, 3, 2, 1, 0)),
        Stage(4, "split_forge", 0.60, 6, 4200, listOf(0, 1, 3, 2, 5, 2, 3, 1, 0)),
        Stage(5, "magnet_row", 0.62, 5, 7000, listOf(0, 2, 1, 3, 2, 5, 3, 1, 0)),
        Stage(6, "ghost_lanes", 0.63, 5, 10500, listOf(0, 1, 4, 2, 3, 2, 4, 1, 0)),
        Stage(7, "overdrive", 0.65, 5, 15000, listOf(0, 3, 1, 4, 2, 4, 1, 3, 0)),
        Stage(8, "the_core", 0.66, 5, 21000, listOf(0, 2, 4, 1, 5, 1, 4, 2, 0))
    )

    val missions: List<Mission> = listOf(
        Mission("mission_core_run", MissionKind.CORE_HITS, 3, 320),
        Mission("mission_steady_hands", MissionKind.CALIBRATION_STREAK, 5, 420),
        Mission("mission_flawless_shift", MissionKind.STABLE_SHIFT, 1, 500),
        Mission("mission_overcharged", MissionKind.OVERCHARGE_HITS, 2, 380),
        Mission("mission_chain_two", MissionKind.CHAIN_REACH, 1, 450),
        Mission("mission_deep_bench", MissionKind.CLEAR_STAGE, 4, 700)
    )

    val modules: List<WorkshopModule> = listOf(
        WorkshopModule("module_gate_copper", ModuleKind.GATE_SKIN, 450, 0xFFFF6B9D),
        WorkshopModule("module_gate_ion", ModuleKind.GATE_SKIN, 900, 0xFF00F5FF),
        WorkshopModule("module_gate_obsidian", ModuleKind.GATE_SKIN, 1400, 0xFFB388FF),
        WorkshopModule("module_peg_amber", ModuleKind.PEG_FILAMENT, 450, 0xFFFFC46B),
        WorkshopModule("module_peg_frost", ModuleKind.PEG_FILAMENT, 900, 0xFF7FE7FF),
        WorkshopModule("module_peg_ember", ModuleKind.PEG_FILAMENT, 1400, 0xFFFF4D4D),
        WorkshopModule("module_core_pulse", ModuleKind.TOKEN_CORE, 450, 0xFFFF006E),
        WorkshopModule("module_core_lime", ModuleKind.TOKEN_CORE, 900, 0xFFB6FF3C),
        WorkshopModule("module_core_violet", ModuleKind.TOKEN_CORE, 1400, 0xFF9B5CFF),
        WorkshopModule("module_plate_steel", ModuleKind.SLOT_PLATE, 450, 0xFF9AA3C7),
        WorkshopModule("module_plate_neon", ModuleKind.SLOT_PLATE, 900, 0xFFFF4D8D),
        WorkshopModule("module_plate_haze", ModuleKind.SLOT_PLATE, 1400, 0xFF5E6689)
    )

    val rewardTracks: List<RewardTrack> = listOf(
        RewardTrack("track_daily_bench", RewardTrackKind.DAILY_BENCH, 1, 120),
        RewardTrack("track_shift_streak", RewardTrackKind.SHIFT_STREAK, 3, 400),
        RewardTrack("track_precision", RewardTrackKind.PRECISION, 10, 650)
    )

    fun stageOf(id: Int): Stage = stages.firstOrNull { it.id == id } ?: stages.first()
}
