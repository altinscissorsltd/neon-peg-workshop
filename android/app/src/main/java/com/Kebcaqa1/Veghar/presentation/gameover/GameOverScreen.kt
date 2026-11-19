package com.Kebcaqa1.Veghar.presentation.gameover

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
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
import com.Kebcaqa1.Veghar.core.ui.GlassCard
import com.Kebcaqa1.Veghar.core.ui.Labels
import com.Kebcaqa1.Veghar.core.ui.PrimaryButton
import com.Kebcaqa1.Veghar.core.ui.ScreenBackground
import com.Kebcaqa1.Veghar.core.ui.SecondaryButton
import com.Kebcaqa1.Veghar.core.ui.StatItem
import com.Kebcaqa1.Veghar.core.ui.StatStrip
import com.Kebcaqa1.Veghar.domain.model.ShiftResult

private val WinOverlay = listOf(
    Color(0xE6001A26),
    Color(0xCC12121F),
    Color(0xF20A0A0F)
)

private val LoseOverlay = listOf(
    Color(0xE62A0014),
    Color(0xCC12121F),
    Color(0xF2050508)
)

@Composable
fun GameOverRoute(
    result: ShiftResult,
    onReplay: () -> Unit,
    onMenu: () -> Unit,
    onLevels: () -> Unit
) {
    val viewModel: GameOverViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by viewModel.state.collectAsStateWithLifecycle()
    GameOverScreen(
        result = result,
        state = state,
        onReplay = onReplay,
        onMenu = onMenu,
        onLevels = onLevels
    )
}

@Composable
fun GameOverScreen(
    result: ShiftResult,
    state: GameOverUiState,
    onReplay: () -> Unit,
    onMenu: () -> Unit,
    onLevels: () -> Unit,
    modifier: Modifier = Modifier
) {
    ScreenBackground(
        overlay = if (result.completed) WinOverlay else LoseOverlay,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = if (result.completed) {
                    stringResource(R.string.result_complete)
                } else {
                    stringResource(R.string.result_broken)
                },
                style = MaterialTheme.typography.displaySmall,
                color = if (result.completed) AccentCyan else AccentMagenta,
                textAlign = TextAlign.Center
            )
            GlassCard(modifier = Modifier.fillMaxWidth(), accent = AccentCyan) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 22.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.sprite_4),
                        contentDescription = stringResource(R.string.cd_charge),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(44.dp)
                    )
                    Column(modifier = Modifier.padding(start = 16.dp)) {
                        Text(
                            text = Labels.charge(result.charge),
                            style = MaterialTheme.typography.displayLarge,
                            color = AccentCyan
                        )
                        Text(
                            text = stringResource(R.string.result_total_label),
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                    }
                }
            }
            StatStrip(
                items = listOf(
                    StatItem(
                        iconRes = R.drawable.ic_target,
                        amount = result.coreHits,
                        value = result.coreHits.toString(),
                        label = stringResource(R.string.stat_core_hits),
                        accent = AccentMagenta
                    ),
                    StatItem(
                        iconRes = R.drawable.ic_check,
                        amount = result.calibrations,
                        value = result.calibrations.toString(),
                        label = stringResource(R.string.stat_calibrations),
                        accent = AccentCyan
                    ),
                    StatItem(
                        iconRes = R.drawable.ic_bolt,
                        amount = if (result.bestChain > 1.0) 1 else 0,
                        value = Labels.chain(result.bestChain),
                        label = stringResource(R.string.stat_best_chain),
                        accent = TextPrimary
                    )
                )
            )
            StageProgress(state = state)
            if (result.unlockedStageId > 0) {
                Text(
                    text = stringResource(R.string.result_unlocked),
                    style = MaterialTheme.typography.labelLarge,
                    color = AccentCyan,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            AccentCyan.copy(alpha = 0.14f),
                            RoundedCornerShape(14.dp)
                        )
                        .border(1.dp, AccentCyan.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    textAlign = TextAlign.Center
                )
            }
            result.completedMissions.forEach { missionId ->
                Text(
                    text = stringResource(
                        R.string.result_mission,
                        stringResource(Labels.missionName(missionId))
                    ),
                    style = MaterialTheme.typography.labelSmall,
                    color = AccentCyan
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            PrimaryButton(
                text = stringResource(R.string.play_again),
                onClick = onReplay,
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SecondaryButton(
                    text = stringResource(R.string.menu),
                    onClick = onMenu,
                    leadingIcon = R.drawable.icon_home,
                    modifier = Modifier.weight(1f)
                )
                if (result.unlockedStageId > 0) {
                    SecondaryButton(
                        text = stringResource(R.string.route_levels),
                        onClick = onLevels,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun StageProgress(state: GameOverUiState) {
    val fraction = if (state.hasNextStage && state.nextThreshold > 0) {
        (state.totalCharge.toFloat() / state.nextThreshold).coerceIn(0f, 1f)
    } else {
        1f
    }
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(
                R.string.result_stage,
                state.stageId,
                stringResource(Labels.stageName(state.stageNameKey))
            ),
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .background(DividerSpent, RoundedCornerShape(3.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction)
                    .fillMaxHeight()
                    .background(AccentMagenta, RoundedCornerShape(3.dp))
            )
        }
        Text(
            text = if (state.hasNextStage) {
                stringResource(
                    R.string.result_progress,
                    Labels.charge(state.totalCharge),
                    Labels.charge(state.nextThreshold)
                )
            } else {
                stringResource(R.string.result_max_stage)
            },
            style = MaterialTheme.typography.labelSmall,
            color = AccentCyan
        )
    }
}
