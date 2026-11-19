package com.Kebcaqa1.Veghar.presentation.menu

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
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
import com.Kebcaqa1.Veghar.core.theme.AccentMagentaDeep
import com.Kebcaqa1.Veghar.core.theme.GlassBorder
import com.Kebcaqa1.Veghar.core.theme.SurfaceRaised
import com.Kebcaqa1.Veghar.core.theme.TextPrimary
import com.Kebcaqa1.Veghar.core.theme.TextSecondary
import com.Kebcaqa1.Veghar.core.ui.Labels
import com.Kebcaqa1.Veghar.core.ui.ScreenBackground
import com.Kebcaqa1.Veghar.core.ui.StatItem
import com.Kebcaqa1.Veghar.core.ui.StatStrip

private val MenuOverlay = listOf(
    Color(0xCC12121F),
    Color(0x9916213E),
    Color(0xF20A0A0F)
)

@Composable
fun MenuRoute(
    onPlay: () -> Unit,
    onLevels: () -> Unit,
    onMissions: () -> Unit,
    onCollection: () -> Unit,
    onSettings: () -> Unit,
    onTutorial: () -> Unit
) {
    val viewModel: MenuViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by viewModel.state.collectAsStateWithLifecycle()
    MenuScreen(
        state = state,
        onPlay = onPlay,
        onLevels = onLevels,
        onMissions = onMissions,
        onCollection = onCollection,
        onSettings = onSettings,
        onTutorial = onTutorial
    )
}

@Composable
fun MenuScreen(
    state: MenuUiState,
    onPlay: () -> Unit,
    onLevels: () -> Unit,
    onMissions: () -> Unit,
    onCollection: () -> Unit,
    onSettings: () -> Unit,
    onTutorial: () -> Unit,
    modifier: Modifier = Modifier
) {
    val glow = Brush.radialGradient(
        colors = listOf(AccentCyan.copy(alpha = 0.18f), Color.Transparent),
        center = Offset(920f, 240f),
        radius = 760f
    )
    ScreenBackground(overlay = MenuOverlay, glow = glow, modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MenuHeader(charge = state.charge, pulse = !state.reducedMotion)
            HeroTile(
                stageId = state.stageId,
                stageNameKey = state.stageNameKey,
                onPlay = onPlay
            )
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                RouteTile(
                    label = stringResource(R.string.route_levels),
                    caption = stringResource(
                        R.string.caption_levels,
                        state.unlockedStages,
                        state.totalStages
                    ),
                    artRes = R.drawable.sprite_3,
                    accent = AccentCyan,
                    onClick = onLevels,
                    modifier = Modifier.weight(1f)
                )
                RouteTile(
                    label = stringResource(R.string.route_missions),
                    caption = if (state.activeMissions > 0) {
                        stringResource(R.string.caption_missions, state.activeMissions)
                    } else {
                        null
                    },
                    artRes = R.drawable.sprite_5,
                    accent = AccentMagentaDeep,
                    onClick = onMissions,
                    modifier = Modifier.weight(1f)
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                RouteTile(
                    label = stringResource(R.string.route_collection),
                    caption = stringResource(
                        R.string.caption_collection,
                        state.ownedModules,
                        state.totalModules
                    ),
                    artRes = R.drawable.sprite_2,
                    accent = AccentMagenta,
                    onClick = onCollection,
                    modifier = Modifier.weight(1f)
                )
                RouteTile(
                    label = stringResource(R.string.route_settings),
                    caption = stringResource(R.string.caption_settings),
                    iconRes = R.drawable.ic_gear,
                    accent = AccentCyan,
                    onClick = onSettings,
                    modifier = Modifier.weight(1f)
                )
            }
            WideRouteTile(
                label = stringResource(R.string.route_tutorial),
                caption = if (state.tutorialSeen) {
                    stringResource(R.string.caption_tutorial_seen)
                } else {
                    stringResource(R.string.caption_tutorial_new)
                },
                onClick = onTutorial
            )
            Spacer(modifier = Modifier.weight(1f))
            StatStrip(
                items = listOf(
                    StatItem(
                        iconRes = R.drawable.ic_bolt,
                        amount = state.bestShift,
                        value = Labels.charge(state.bestShift),
                        label = stringResource(R.string.stat_best_shift),
                        accent = AccentMagenta
                    ),
                    StatItem(
                        iconRes = R.drawable.ic_target,
                        amount = state.coreHits,
                        value = state.coreHits.toString(),
                        label = stringResource(R.string.stat_core_hits),
                        accent = AccentCyan
                    ),
                    StatItem(
                        iconRes = R.drawable.ic_check,
                        amount = state.perfectCalibrations,
                        value = state.perfectCalibrations.toString(),
                        label = stringResource(R.string.stat_calibrations),
                        accent = TextPrimary
                    )
                )
            )
        }
    }
}

@Composable
private fun MenuHeader(charge: Int, pulse: Boolean) {
    val transition = rememberInfiniteTransition()
    val breathe by transition.animateFloat(
        initialValue = 1f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(tween(1600, easing = FastOutSlowInEasing), RepeatMode.Reverse)
    )
    val scale = if (pulse) breathe else 1f
    Row(
        modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = stringResource(R.string.cd_logo),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(40.dp)
            )
            Text(
                text = stringResource(R.string.app_title),
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                modifier = Modifier.padding(start = 10.dp)
            )
        }
        Row(
            modifier = Modifier
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
                .background(SurfaceRaised.copy(alpha = 0.72f), RoundedCornerShape(18.dp))
                .border(1.dp, AccentCyan.copy(alpha = 0.35f), RoundedCornerShape(18.dp))
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.sprite_4),
                contentDescription = stringResource(R.string.cd_charge),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = Labels.charge(charge),
                style = MaterialTheme.typography.titleMedium,
                color = AccentCyan,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

@Composable
private fun HeroTile(stageId: Int, stageNameKey: String, onPlay: () -> Unit) {
    val shape = RoundedCornerShape(24.dp)
    Button(
        onClick = onPlay,
        shape = shape,
        contentPadding = PaddingValues(horizontal = 22.dp, vertical = 16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = TextPrimary
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .shadow(20.dp, shape, spotColor = AccentMagenta)
            .background(
                Brush.linearGradient(listOf(AccentMagenta, AccentMagentaDeep)),
                shape
            )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .background(Color.White.copy(alpha = 0.12f), RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.sprite_1),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(56.dp)
                )
            }
            Column(modifier = Modifier.padding(start = 18.dp)) {
                Text(
                    text = stringResource(R.string.play),
                    style = MaterialTheme.typography.displaySmall,
                    color = TextPrimary
                )
                Text(
                    text = stringResource(
                        R.string.menu_stage,
                        stageId,
                        stringResource(Labels.stageName(stageNameKey))
                    ),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextPrimary.copy(alpha = 0.82f),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun RouteTile(
    label: String,
    caption: String?,
    accent: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    artRes: Int? = null,
    iconRes: Int? = null
) {
    val shape = RoundedCornerShape(20.dp)
    Button(
        onClick = onClick,
        shape = shape,
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = TextPrimary
        ),
        modifier = modifier
            .height(104.dp)
            .background(SurfaceRaised.copy(alpha = 0.66f), shape)
            .border(1.dp, GlassBorder, shape)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (artRes != null) {
                Image(
                    painter = painterResource(artRes),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(34.dp)
                )
            }
            if (iconRes != null) {
                Icon(
                    painter = painterResource(iconRes),
                    contentDescription = null,
                    tint = accent,
                    modifier = Modifier.size(30.dp)
                )
            }
            Column(modifier = Modifier.padding(start = 10.dp)) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelLarge,
                    color = TextPrimary
                )
                if (caption != null) {
                    Text(
                        text = caption,
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun WideRouteTile(label: String, caption: String, onClick: () -> Unit) {
    val shape = RoundedCornerShape(18.dp)
    Button(
        onClick = onClick,
        shape = shape,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = TextPrimary
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(SurfaceRaised.copy(alpha = 0.58f), shape)
            .border(1.dp, AccentCyan.copy(alpha = 0.28f), shape)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(R.drawable.icon_home),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(26.dp)
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelLarge,
                    color = TextPrimary,
                    modifier = Modifier.padding(start = 12.dp)
                )
            }
            Text(
                text = caption,
                style = MaterialTheme.typography.labelSmall,
                color = AccentCyan
            )
        }
    }
}
