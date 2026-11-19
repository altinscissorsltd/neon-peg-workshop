package com.Kebcaqa1.Veghar.core.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import com.Kebcaqa1.Veghar.core.theme.AccentMagenta
import com.Kebcaqa1.Veghar.core.theme.DividerSpent
import com.Kebcaqa1.Veghar.core.theme.TextSecondary

@Composable
fun TorquePips(
    label: String,
    total: Int,
    remaining: Int,
    stateLabel: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.semantics {
            stateDescription = stateLabel
            liveRegion = LiveRegionMode.Polite
        },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary
        )
        repeat(total) { index ->
            val lit = index < remaining
            val color by animateColorAsState(if (lit) AccentMagenta else DividerSpent)
            Spacer(
                modifier = Modifier
                    .size(12.dp)
                    .graphicsLayer { rotationZ = 45f }
                    .background(color, RoundedCornerShape(3.dp))
            )
        }
    }
}
