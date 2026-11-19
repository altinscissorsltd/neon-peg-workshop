package com.Kebcaqa1.Veghar.presentation.game.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Kebcaqa1.Veghar.R
import com.Kebcaqa1.Veghar.core.config.GameConfig
import com.Kebcaqa1.Veghar.core.theme.AccentMagenta
import com.Kebcaqa1.Veghar.core.theme.SurfaceAccent
import com.Kebcaqa1.Veghar.core.theme.TextSecondary
import com.Kebcaqa1.Veghar.presentation.game.GameUiState

@Composable
fun PegBoard(
    state: GameUiState,
    boardWidth: Dp,
    boardHeight: Dp,
    laneWidth: Dp,
    pegRowHeight: Dp,
    slotHeight: Dp,
    frame: Dp,
    voidLabel: String,
    multiplierLabel: (Int) -> String,
    gateDescription: (Int) -> String,
    gateStateLabel: (Int) -> String,
    onGateStep: (Int, Int) -> Unit,
    onGateFlip: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(20.dp)
    val pegColor = Color(state.pegTint)
    val tokenColor = Color(state.tokenTint)
    val gateColor = Color(state.gateTint)
    val laneWidthPx = with(LocalDensity.current) { laneWidth.toPx() }
    Box(
        modifier = modifier
            .size(width = boardWidth, height = boardHeight)
            .background(SurfaceAccent.copy(alpha = 0.55f), shape)
            .border(2.dp, AccentMagenta.copy(alpha = 0.45f), shape)
    ) {
        Box(modifier = Modifier.fillMaxSize().padding(frame)) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val lanePx = laneWidth.toPx()
                val rowPx = pegRowHeight.toPx()
                for (row in 0 until GameConfig.PEG_ROWS) {
                    for (lane in 0 until GameConfig.LANES) {
                        val center = Offset(lanePx * (lane + 0.5f), rowPx * (row + 0.5f))
                        val flashing = row == state.flashRow && lane == state.flashLane
                        drawCircle(
                            color = pegColor.copy(alpha = 0.18f),
                            radius = 9.dp.toPx(),
                            center = center
                        )
                        drawCircle(
                            color = if (flashing) Color.White else TextSecondary,
                            radius = if (flashing) 7.dp.toPx() else 2.5.dp.toPx(),
                            center = center
                        )
                    }
                }
                state.ghosts.forEach { ghost ->
                    drawCircle(
                        color = tokenColor.copy(alpha = ghost.alpha * 0.55f),
                        radius = 7.dp.toPx(),
                        center = Offset(
                            lanePx * (ghost.lane + 0.5f),
                            rowPx * (ghost.row + 0.5f)
                        )
                    )
                }
            }
            if (state.tokenVisible) {
                Image(
                    painter = painterResource(R.drawable.sprite_1),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .offset(
                            x = laneWidth * (state.tokenLane + 0.5f) - 13.dp,
                            y = pegRowHeight * (state.tokenRow + 0.5f) - 13.dp
                        )
                        .size(26.dp)
                )
            }
            state.gates.forEach { gate ->
                GateHandle(
                    gate = gate,
                    enabled = !state.gatesLocked,
                    tapOnly = state.tapOnly,
                    laneWidthPx = laneWidthPx,
                    tint = gateColor,
                    description = gateDescription(gate.id),
                    stateLabel = gateStateLabel(gate.id),
                    onStep = { direction -> onGateStep(gate.id, direction) },
                    onFlip = { onGateFlip(gate.id) },
                    handleWidth = laneWidth * 3,
                    modifier = Modifier.offset(
                        x = laneWidth * (gate.lane - 1f),
                        y = pegRowHeight * (gate.row + 0.5f) - 24.dp
                    )
                )
            }
            SlotRow(
                payouts = state.payouts,
                laneWidth = laneWidth,
                overchargeLane = state.overchargeLane,
                landedLane = state.landedLane,
                plateTint = Color(state.plateTint),
                highContrast = state.highContrast,
                voidLabel = voidLabel,
                multiplierLabel = multiplierLabel,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .height(slotHeight)
            )
        }
    }
}
