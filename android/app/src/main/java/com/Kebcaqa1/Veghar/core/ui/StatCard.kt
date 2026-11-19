package com.Kebcaqa1.Veghar.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.Kebcaqa1.Veghar.core.theme.TextSecondary

data class StatItem(
    val iconRes: Int,
    val amount: Int,
    val value: String,
    val label: String,
    val accent: Color
)

@Composable
fun StatCard(
    iconRes: Int,
    value: String,
    label: String,
    accent: Color,
    modifier: Modifier = Modifier
) {
    GlassCard(
        modifier = modifier.heightIn(min = 76.dp),
        accent = accent,
        brackets = false,
        radius = 16.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(iconRes),
                    contentDescription = null,
                    tint = accent,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleMedium,
                    color = accent,
                    modifier = Modifier.padding(start = 6.dp)
                )
            }
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun StatStrip(items: List<StatItem>, modifier: Modifier = Modifier) {
    val visible = items.filter { it.amount > 0 }
    if (visible.size < 2) return
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        visible.forEach { item ->
            StatCard(
                iconRes = item.iconRes,
                value = item.value,
                label = item.label,
                accent = item.accent,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun AccentDot(color: Color, modifier: Modifier = Modifier) {
    Row(modifier = modifier.size(8.dp).background(color, CircleShape)) {}
}
