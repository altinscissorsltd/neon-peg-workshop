package com.Kebcaqa1.Veghar.presentation.levelmap

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Kebcaqa1.Veghar.R
import com.Kebcaqa1.Veghar.core.di.ServiceLocator
import com.Kebcaqa1.Veghar.core.theme.AccentCyan
import com.Kebcaqa1.Veghar.core.theme.AccentMagenta
import com.Kebcaqa1.Veghar.core.theme.SurfaceRaised
import com.Kebcaqa1.Veghar.core.theme.TextMuted
import com.Kebcaqa1.Veghar.core.theme.TextPrimary
import com.Kebcaqa1.Veghar.core.theme.TextSecondary
import com.Kebcaqa1.Veghar.core.ui.GlassCard
import com.Kebcaqa1.Veghar.core.ui.Labels
import com.Kebcaqa1.Veghar.core.ui.PrimaryButton
import com.Kebcaqa1.Veghar.core.ui.ScreenScaffold

private val LevelOverlay = listOf(
    Color(0xE60A0A0F),
    Color(0xB3102033),
    Color(0xF212121F)
)

@Composable
fun LevelMapRoute(onBack: () -> Unit, onPlay: () -> Unit) {
    val viewModel: LevelMapViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by viewModel.state.collectAsStateWithLifecycle()
    LevelMapScreen(
        state = state,
        onNode = viewModel::onNodeSelected,
        onBack = onBack,
        onPlay = onPlay
    )
}

@Composable
fun LevelMapScreen(
    state: LevelMapUiState,
    onNode: (Int) -> Unit,
    onBack: () -> Unit,
    onPlay: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selected = state.nodes.firstOrNull { it.id == state.selectedId }
    ScreenScaffold(
        title = stringResource(R.string.levels_title),
        caption = stringResource(R.string.levels_caption),
        menuLabel = stringResource(R.string.menu),
        onBack = onBack,
        overlay = LevelOverlay,
        modifier = modifier
    ) {
        val rows = state.nodes.chunked(2)
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(rows.size) { index ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rows[index].forEach { node ->
                        StageCard(
                            node = node,
                            onClick = { onNode(node.id) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
        if (state.lockedNoticeFor > 0) {
            val locked = state.nodes.firstOrNull { it.id == state.lockedNoticeFor }
            Text(
                text = stringResource(
                    R.string.stage_locked,
                    Labels.charge(locked?.threshold ?: 0)
                ),
                style = MaterialTheme.typography.labelSmall,
                color = AccentMagenta,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AccentMagenta.copy(alpha = 0.12f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            )
        }
        if (selected != null) {
            GlassCard(modifier = Modifier.fillMaxWidth(), accent = AccentCyan) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = stringResource(Labels.stageName(selected.nameKey)),
                        style = MaterialTheme.typography.headlineMedium,
                        color = TextPrimary
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text(
                            text = stringResource(
                                R.string.stage_drift,
                                Labels.percent(selected.bias)
                            ),
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                        Text(
                            text = stringResource(R.string.stage_torque, selected.torque),
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                    }
                    PrimaryButton(
                        text = stringResource(R.string.play),
                        onClick = onPlay,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun StageCard(node: StageNode, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val border = when {
        node.current -> AccentMagenta
        node.unlocked -> AccentCyan.copy(alpha = 0.55f)
        else -> TextMuted.copy(alpha = 0.35f)
    }
    val stateLabel = when {
        node.mastered -> stringResource(R.string.stage_mastered)
        node.current -> stringResource(R.string.stage_current)
        node.unlocked -> stringResource(R.string.stage_unlocked)
        else -> stringResource(R.string.stage_locked_short)
    }
    Box(
        modifier = modifier
            .height(96.dp)
            .background(SurfaceRaised.copy(alpha = if (node.unlocked) 0.7f else 0.4f), RoundedCornerShape(18.dp))
            .border(if (node.current) 2.dp else 1.dp, border, RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .semantics { stateDescription = stateLabel }
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (node.unlocked) {
                Image(
                    painter = painterResource(R.drawable.sprite_3),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(30.dp)
                )
            } else {
                Icon(
                    painter = painterResource(R.drawable.ic_lock),
                    contentDescription = null,
                    tint = TextMuted,
                    modifier = Modifier.size(26.dp)
                )
            }
            Column(modifier = Modifier.padding(start = 10.dp)) {
                Text(
                    text = stringResource(Labels.stageName(node.nameKey)),
                    style = MaterialTheme.typography.labelLarge,
                    color = if (node.unlocked) TextPrimary else TextMuted
                )
                Text(
                    text = stateLabel,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (node.current) AccentMagenta else TextSecondary,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
        if (node.mastered) {
            Icon(
                painter = painterResource(R.drawable.ic_star),
                contentDescription = null,
                tint = AccentCyan,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(18.dp)
            )
        }
    }
}
