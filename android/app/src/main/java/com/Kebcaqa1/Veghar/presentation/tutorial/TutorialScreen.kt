package com.Kebcaqa1.Veghar.presentation.tutorial

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import com.Kebcaqa1.Veghar.core.ui.PrimaryButton
import com.Kebcaqa1.Veghar.core.ui.ScreenScaffold
import com.Kebcaqa1.Veghar.core.ui.SecondaryButton

private val TutorialOverlay = listOf(
    Color(0xE60A0A0F),
    Color(0xCC1A0030),
    Color(0xF212121F)
)

private val StepArt = listOf(
    R.drawable.sprite_1,
    R.drawable.sprite_2,
    R.drawable.sprite_5,
    R.drawable.sprite_3
)

private val StepTitles = listOf(
    R.string.tutorial_title_1,
    R.string.tutorial_title_2,
    R.string.tutorial_title_3,
    R.string.tutorial_title_4
)

private val StepBodies = listOf(
    R.string.tutorial_body_1,
    R.string.tutorial_body_2,
    R.string.tutorial_body_3,
    R.string.tutorial_body_4
)

@Composable
fun TutorialRoute(onBack: () -> Unit, onPlay: () -> Unit) {
    val viewModel: TutorialViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by viewModel.state.collectAsStateWithLifecycle()
    TutorialScreen(
        state = state,
        onNext = viewModel::onNext,
        onStep = viewModel::onStepSelected,
        onBack = onBack,
        onPlay = {
            viewModel.markSeen()
            onPlay()
        }
    )
}

@Composable
fun TutorialScreen(
    state: TutorialUiState,
    onNext: () -> Unit,
    onStep: (Int) -> Unit,
    onBack: () -> Unit,
    onPlay: () -> Unit,
    modifier: Modifier = Modifier
) {
    ScreenScaffold(
        title = stringResource(R.string.tutorial_title),
        caption = stringResource(R.string.tutorial_caption, state.step + 1, state.totalSteps),
        menuLabel = stringResource(R.string.menu),
        onBack = onBack,
        overlay = TutorialOverlay,
        modifier = modifier
    ) {
        AnimatedContent(
            targetState = state.step,
            transitionSpec = { fadeIn(tween(280)) togetherWith fadeOut(tween(200)) }
        ) { step ->
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                GlassCard(modifier = Modifier.fillMaxWidth(), accent = AccentCyan) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(188.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(StepArt[step]),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(110.dp)
                        )
                    }
                }
                Text(
                    text = stringResource(StepTitles[step]),
                    style = MaterialTheme.typography.headlineMedium,
                    color = TextPrimary,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = stringResource(StepBodies[step]),
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            repeat(state.totalSteps) { index ->
                val active = index == state.step
                Spacer(
                    modifier = Modifier
                        .size(if (active) 10.dp else 8.dp)
                        .background(
                            if (active) AccentMagenta else DividerSpent,
                            CircleShape
                        )
                        .clickable { onStep(index) }
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        if (state.lastStep) {
            PrimaryButton(
                text = stringResource(R.string.play),
                onClick = onPlay,
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            PrimaryButton(
                text = stringResource(R.string.tutorial_next),
                onClick = onNext,
                modifier = Modifier.fillMaxWidth()
            )
        }
        SecondaryButton(
            text = stringResource(R.string.menu),
            onClick = onBack,
            leadingIcon = R.drawable.icon_home,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(4.dp))
    }
}
