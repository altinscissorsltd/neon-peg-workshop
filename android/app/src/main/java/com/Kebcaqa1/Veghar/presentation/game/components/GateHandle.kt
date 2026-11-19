package com.Kebcaqa1.Veghar.presentation.game.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Kebcaqa1.Veghar.R
import com.Kebcaqa1.Veghar.domain.model.Gate
import com.Kebcaqa1.Veghar.domain.model.GateFacing

@Composable
fun GateHandle(
    gate: Gate,
    enabled: Boolean,
    tapOnly: Boolean,
    laneWidthPx: Float,
    tint: Color,
    description: String,
    stateLabel: String,
    handleWidth: Dp,
    onStep: (Int) -> Unit,
    onFlip: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dragModifier = if (enabled && !tapOnly) {
        Modifier.pointerInput(gate.id, laneWidthPx) {
            var accumulated = 0f
            detectHorizontalDragGestures(
                onDragStart = { accumulated = 0f },
                onDragEnd = { accumulated = 0f },
                onDragCancel = { accumulated = 0f }
            ) { change, amount ->
                change.consume()
                accumulated += amount
                while (accumulated >= laneWidthPx) {
                    onStep(1)
                    accumulated -= laneWidthPx
                }
                while (accumulated <= -laneWidthPx) {
                    onStep(-1)
                    accumulated += laneWidthPx
                }
            }
        }
    } else {
        Modifier
    }
    val tapModifier = if (enabled) {
        Modifier.pointerInput(gate.id, tapOnly) {
            detectTapGestures { offset ->
                if (!tapOnly) {
                    onFlip()
                } else {
                    val fraction = offset.x / size.width.toFloat()
                    when {
                        fraction < 0.34f -> onStep(-1)
                        fraction > 0.66f -> onStep(1)
                        else -> onFlip()
                    }
                }
            }
        }
    } else {
        Modifier
    }
    Box(
        modifier = modifier
            .size(width = handleWidth, height = 48.dp)
            .semantics {
                contentDescription = description
                stateDescription = stateLabel
            }
            .then(dragModifier)
            .then(tapModifier),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.sprite_2),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(width = handleWidth - 10.dp, height = 20.dp)
                .graphicsLayer {
                    rotationZ = if (gate.facing == GateFacing.LEFT) -7f else 7f
                    alpha = if (enabled) 1f else 0.55f
                }
        )
        Spacer(
            modifier = Modifier
                .size(width = handleWidth - 14.dp, height = 4.dp)
                .graphicsLayer {
                    rotationZ = if (gate.facing == GateFacing.LEFT) -7f else 7f
                    translationY = 14.dp.toPx()
                    alpha = if (enabled) 0.95f else 0.4f
                }
                .background(tint, RoundedCornerShape(2.dp))
        )
    }
}
