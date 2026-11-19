package com.Kebcaqa1.Veghar.presentation.game.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.Kebcaqa1.Veghar.R
import com.Kebcaqa1.Veghar.core.theme.AccentCyan
import com.Kebcaqa1.Veghar.core.theme.AccentMagenta
import com.Kebcaqa1.Veghar.core.theme.DividerSpent
import com.Kebcaqa1.Veghar.core.theme.SurfaceRaised
import com.Kebcaqa1.Veghar.core.theme.TextPrimary
import com.Kebcaqa1.Veghar.core.theme.TextSecondary

@Composable
fun CalibrationPanel(
    title: String,
    pads: Int,
    highlighted: Int,
    entered: Int,
    accepting: Boolean,
    remaining: Float,
    padDescription: (Int) -> String,
    onPad: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            color = AccentCyan,
            textAlign = TextAlign.Center
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .background(DividerSpent, RoundedCornerShape(2.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(remaining.coerceIn(0f, 1f))
                    .height(4.dp)
                    .background(AccentCyan, RoundedCornerShape(2.dp))
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            repeat(pads) { index ->
                val lit = index == highlighted
                val solved = index < entered
                val fill by animateColorAsState(
                    when {
                        lit -> AccentCyan.copy(alpha = 0.42f)
                        solved -> AccentMagenta.copy(alpha = 0.26f)
                        else -> SurfaceRaised.copy(alpha = 0.72f)
                    }
                )
                Box(
                    modifier = Modifier
                        .size(width = 62.dp, height = 56.dp)
                        .background(fill, RoundedCornerShape(14.dp))
                        .border(
                            1.dp,
                            if (lit) AccentCyan else Color.White.copy(alpha = 0.10f),
                            RoundedCornerShape(14.dp)
                        )
                        .clickable(enabled = accepting) { onPad(index) }
                        .semantics { contentDescription = padDescription(index) },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.sprite_3),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(if (lit) 38.dp else 30.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun AimPanel(
    hint: String,
    overchargeText: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = overchargeText,
            style = MaterialTheme.typography.labelSmall,
            color = AccentCyan,
            textAlign = TextAlign.Center
        )
        Text(
            text = hint,
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
    }
}

@Composable
fun PhaseBanner(
    text: String,
    accent: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(accent.copy(alpha = 0.18f), RoundedCornerShape(14.dp))
            .border(1.dp, accent, RoundedCornerShape(14.dp))
            .padding(horizontal = 20.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = TextPrimary
        )
    }
}
