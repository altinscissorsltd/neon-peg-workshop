package com.Kebcaqa1.Veghar.presentation.missions

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Kebcaqa1.Veghar.R
import com.Kebcaqa1.Veghar.core.di.ServiceLocator
import com.Kebcaqa1.Veghar.core.theme.AccentCyan
import com.Kebcaqa1.Veghar.core.theme.AccentMagenta
import com.Kebcaqa1.Veghar.core.theme.DividerSpent
import com.Kebcaqa1.Veghar.core.theme.TextPrimary
import com.Kebcaqa1.Veghar.core.theme.TextSecondary
import com.Kebcaqa1.Veghar.core.ui.EmptyState
import com.Kebcaqa1.Veghar.core.ui.GlassCard
import com.Kebcaqa1.Veghar.core.ui.Labels
import com.Kebcaqa1.Veghar.core.ui.ScreenScaffold
import com.Kebcaqa1.Veghar.core.ui.SecondaryButton
import com.Kebcaqa1.Veghar.core.ui.SectionHeader

private val MissionsOverlay = listOf(
    Color(0xE6120A24),
    Color(0xB31A1A2E),
    Color(0xF20A0A0F)
)

@Composable
fun MissionsRoute(onBack: () -> Unit, onPlay: () -> Unit) {
    val viewModel: MissionsViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by viewModel.state.collectAsStateWithLifecycle()
    MissionsScreen(
        state = state,
        onClaimMission = viewModel::onClaimMission,
        onClaimTrack = viewModel::onClaimTrack,
        onBack = onBack,
        onPlay = onPlay
    )
}

@Composable
fun MissionsScreen(
    state: MissionsUiState,
    onClaimMission: (String) -> Unit,
    onClaimTrack: (String) -> Unit,
    onBack: () -> Unit,
    onPlay: () -> Unit,
    modifier: Modifier = Modifier
) {
    ScreenScaffold(
        title = stringResource(R.string.missions_title),
        caption = stringResource(R.string.missions_caption),
        menuLabel = stringResource(R.string.menu),
        onBack = onBack,
        overlay = MissionsOverlay,
        modifier = modifier
    ) {
        if (state.allClaimed) {
            EmptyState(
                imageRes = R.drawable.sprite_5,
                imageDescription = stringResource(R.string.cd_emblem),
                title = stringResource(R.string.missions_empty_title),
                message = stringResource(R.string.missions_empty_message),
                actionText = stringResource(R.string.play),
                onAction = onPlay
            )
        } else {
            if (state.notice == MissionsNotice.NOT_READY) {
                Text(
                    text = stringResource(R.string.track_not_ready),
                    style = MaterialTheme.typography.labelSmall,
                    color = AccentMagenta,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            AccentMagenta.copy(alpha = 0.12f),
                            RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                )
            }
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    SectionHeader(
                        title = stringResource(R.string.missions_section),
                        accent = AccentMagenta
                    )
                }
                items(state.missions.size) { index ->
                    val card = state.missions[index]
                    ProgressCard(
                        title = stringResource(Labels.missionName(card.id)),
                        progress = card.progress,
                        target = card.target,
                        reward = card.reward,
                        claimed = card.claimed,
                        ready = card.ready,
                        artRes = R.drawable.sprite_5,
                        accent = AccentMagenta,
                        onClaim = { onClaimMission(card.id) }
                    )
                }
                item {
                    SectionHeader(
                        title = stringResource(R.string.tracks_section),
                        accent = AccentCyan
                    )
                }
                items(state.tracks.size) { index ->
                    val card = state.tracks[index]
                    ProgressCard(
                        title = stringResource(Labels.trackName(card.id)),
                        progress = card.progress,
                        target = card.target,
                        reward = card.reward,
                        claimed = card.claimed,
                        ready = card.ready,
                        artRes = R.drawable.sprite_4,
                        accent = AccentCyan,
                        onClaim = { onClaimTrack(card.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ProgressCard(
    title: String,
    progress: Int,
    target: Int,
    reward: Int,
    claimed: Boolean,
    ready: Boolean,
    artRes: Int,
    accent: Color,
    onClaim: () -> Unit
) {
    val fraction = if (target <= 0) 0f else (progress.toFloat() / target).coerceIn(0f, 1f)
    GlassCard(modifier = Modifier.fillMaxWidth(), accent = accent, brackets = false) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(artRes),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(38.dp)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelLarge,
                    color = TextPrimary
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.dp)
                        .background(DividerSpent, RoundedCornerShape(3.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(fraction)
                            .fillMaxHeight()
                            .background(accent, RoundedCornerShape(3.dp))
                    )
                }
                Text(
                    text = stringResource(
                        R.string.mission_progress,
                        progress,
                        target,
                        Labels.charge(reward)
                    ),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )
            }
            SecondaryButton(
                text = if (claimed) {
                    stringResource(R.string.track_claimed)
                } else {
                    stringResource(R.string.mission_claim)
                },
                onClick = onClaim,
                enabled = !claimed,
                accent = accent,
                stateLabel = when {
                    claimed -> stringResource(R.string.track_claimed)
                    ready -> stringResource(R.string.mission_ready)
                    else -> stringResource(R.string.mission_in_progress)
                }
            )
        }
    }
}
