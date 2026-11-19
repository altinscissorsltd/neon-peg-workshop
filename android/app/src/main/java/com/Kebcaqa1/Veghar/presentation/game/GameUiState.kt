package com.Kebcaqa1.Veghar.presentation.game

import com.Kebcaqa1.Veghar.core.config.GameConfig
import com.Kebcaqa1.Veghar.domain.model.Gate

enum class GamePhase {
    AIM,
    FALLING,
    LANDING,
    CALIBRATING,
    BANNER
}

enum class BannerKind {
    NONE,
    CALIBRATED,
    MISALIGNED
}

data class TokenGhost(
    val lane: Float,
    val row: Float,
    val alpha: Float
)

data class GameUiState(
    val phase: GamePhase = GamePhase.AIM,
    val stageId: Int = 1,
    val stageNameKey: String = "starter_bench",
    val payouts: List<Int> = listOf(0, 1, 2, 3, 5, 3, 2, 1, 0),
    val dropIndex: Int = 1,
    val totalDrops: Int = GameConfig.DROPS_PER_SHIFT,
    val stability: Int = GameConfig.STABILITY_CELLS,
    val shiftCharge: Int = 0,
    val chain: Double = 1.0,
    val torqueTotal: Int = GameConfig.TORQUE_BASE,
    val torqueLeft: Int = GameConfig.TORQUE_BASE,
    val gates: List<Gate> = emptyList(),
    val tokenLane: Float = GameConfig.CENTER_LANE.toFloat(),
    val tokenRow: Float = -0.3f,
    val tokenVisible: Boolean = false,
    val ghosts: List<TokenGhost> = emptyList(),
    val flashRow: Int = -1,
    val flashLane: Int = -1,
    val overchargeLane: Int = -1,
    val landedLane: Int = -1,
    val lastGain: Int = 0,
    val calibrationPattern: List<Int> = emptyList(),
    val calibrationShown: Int = -1,
    val calibrationEntered: Int = 0,
    val calibrationAcceptsInput: Boolean = false,
    val calibrationRemaining: Float = 0f,
    val banner: BannerKind = BannerKind.NONE,
    val paused: Boolean = false,
    val highContrast: Boolean = false,
    val reducedMotion: Boolean = false,
    val tapOnly: Boolean = false,
    val gateTint: Long = 0xFFFF006E,
    val pegTint: Long = 0xFF00F5FF,
    val tokenTint: Long = 0xFFFF4D8D,
    val plateTint: Long = 0xFF9AA3C7
) {
    val gatesLocked: Boolean get() = phase != GamePhase.AIM || paused
}
