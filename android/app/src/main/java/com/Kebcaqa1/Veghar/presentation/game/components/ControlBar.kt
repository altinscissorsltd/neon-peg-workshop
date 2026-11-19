package com.Kebcaqa1.Veghar.presentation.game.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.Kebcaqa1.Veghar.core.theme.AccentCyan
import com.Kebcaqa1.Veghar.core.theme.GlassBorder
import com.Kebcaqa1.Veghar.core.theme.SurfaceDim
import com.Kebcaqa1.Veghar.core.ui.PrimaryButton
import com.Kebcaqa1.Veghar.core.ui.SecondaryButton
import com.Kebcaqa1.Veghar.core.ui.TorquePips

@Composable
fun ControlBar(
    torqueLabel: String,
    torqueTotal: Int,
    torqueLeft: Int,
    torqueState: String,
    chainText: String?,
    actionText: String,
    actionState: String,
    onAction: () -> Unit,
    resetText: String,
    resetEnabled: Boolean,
    onReset: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(24.dp)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(12.dp, shape)
            .background(SurfaceDim.copy(alpha = 0.9f), shape)
            .border(1.dp, GlassBorder, shape)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TorquePips(
                label = torqueLabel,
                total = torqueTotal,
                remaining = torqueLeft,
                stateLabel = torqueState
            )
            if (chainText != null) {
                Text(
                    text = chainText,
                    style = MaterialTheme.typography.labelSmall,
                    color = AccentCyan,
                    modifier = Modifier
                        .background(
                            AccentCyan.copy(alpha = 0.14f),
                            RoundedCornerShape(10.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SecondaryButton(
                text = resetText,
                onClick = onReset,
                enabled = resetEnabled,
                modifier = Modifier.weight(1f)
            )
            PrimaryButton(
                text = actionText,
                onClick = onAction,
                stateLabel = actionState,
                modifier = Modifier.weight(1.4f)
            )
        }
    }
}
