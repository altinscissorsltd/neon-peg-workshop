package com.Kebcaqa1.Veghar.presentation.game.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.Kebcaqa1.Veghar.core.theme.AccentCyan
import com.Kebcaqa1.Veghar.core.theme.AccentMagenta
import com.Kebcaqa1.Veghar.core.theme.SurfaceDim
import com.Kebcaqa1.Veghar.core.theme.TextMuted
import com.Kebcaqa1.Veghar.core.theme.TextPrimary

@Composable
fun SlotRow(
    payouts: List<Int>,
    laneWidth: Dp,
    overchargeLane: Int,
    landedLane: Int,
    plateTint: Color,
    highContrast: Boolean,
    voidLabel: String,
    multiplierLabel: (Int) -> String,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        payouts.forEachIndexed { lane, payout ->
            val empty = payout == 0
            val landed = lane == landedLane
            val overcharged = lane == overchargeLane
            val fill by animateColorAsState(
                when {
                    landed && empty -> TextMuted.copy(alpha = 0.45f)
                    landed -> AccentMagenta.copy(alpha = 0.9f)
                    empty -> SurfaceDim.copy(alpha = 0.75f)
                    else -> plateTint.copy(alpha = 0.22f)
                }
            )
            val outline = when {
                overcharged -> AccentCyan
                highContrast -> Color.White
                else -> Color.Transparent
            }
            Box(
                modifier = Modifier
                    .width(laneWidth)
                    .fillMaxHeight()
                    .padding(horizontal = 1.dp, vertical = 2.dp)
                    .background(fill, RoundedCornerShape(8.dp))
                    .border(
                        if (overcharged || highContrast) 2.dp else 0.dp,
                        outline,
                        RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (empty) voidLabel else multiplierLabel(payout),
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = if (highContrast) 15.sp else 12.sp,
                    fontWeight = if (highContrast) FontWeight.W800 else FontWeight.W600,
                    color = when {
                        empty -> TextMuted
                        overcharged -> AccentCyan
                        else -> TextPrimary
                    }
                )
            }
        }
    }
}
