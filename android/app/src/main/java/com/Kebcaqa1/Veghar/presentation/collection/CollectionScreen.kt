package com.Kebcaqa1.Veghar.presentation.collection

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.ui.text.style.TextAlign
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
import com.Kebcaqa1.Veghar.core.ui.EmptyState
import com.Kebcaqa1.Veghar.core.ui.Labels
import com.Kebcaqa1.Veghar.core.ui.ScreenScaffold
import com.Kebcaqa1.Veghar.core.ui.SecondaryButton

private val CollectionOverlay = listOf(
    Color(0xE61A0030),
    Color(0xB30A0A0F),
    Color(0xF212121F)
)

@Composable
fun CollectionRoute(onBack: () -> Unit, onPlay: () -> Unit) {
    val viewModel: CollectionViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by viewModel.state.collectAsStateWithLifecycle()
    CollectionScreen(
        state = state,
        onModuleAction = viewModel::onModuleAction,
        onBack = onBack,
        onPlay = onPlay
    )
}

@Composable
fun CollectionScreen(
    state: CollectionUiState,
    onModuleAction: (String) -> Unit,
    onBack: () -> Unit,
    onPlay: () -> Unit,
    modifier: Modifier = Modifier
) {
    ScreenScaffold(
        title = stringResource(R.string.collection_title),
        caption = stringResource(
            R.string.collection_caption,
            state.ownedCount,
            state.modules.size
        ),
        menuLabel = stringResource(R.string.menu),
        onBack = onBack,
        overlay = CollectionOverlay,
        modifier = modifier
    ) {
        if (state.ownedCount == 0 && state.charge == 0) {
            EmptyState(
                imageRes = R.drawable.sprite_4,
                imageDescription = stringResource(R.string.cd_charge),
                title = stringResource(R.string.collection_empty_title),
                message = stringResource(R.string.collection_empty_message),
                actionText = stringResource(R.string.play),
                onAction = onPlay
            )
        } else {
            if (state.notice == CollectionNotice.NOT_ENOUGH_CHARGE) {
                Text(
                    text = stringResource(R.string.not_enough_charge),
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
            val rows = state.modules.chunked(2)
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(rows.size) { index ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        rows[index].forEach { card ->
                            ModuleTile(
                                card = card,
                                onAction = { onModuleAction(card.id) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ModuleTile(card: ModuleCard, onAction: () -> Unit, modifier: Modifier = Modifier) {
    val tint = Color(card.tint)
    Column(
        modifier = modifier
            .heightIn(min = 196.dp)
            .background(
                SurfaceRaised.copy(alpha = if (card.owned) 0.72f else 0.42f),
                RoundedCornerShape(18.dp)
            )
            .border(
                1.dp,
                if (card.equipped) AccentCyan else tint.copy(alpha = 0.35f),
                RoundedCornerShape(18.dp)
            )
            .padding(horizontal = 12.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(tint.copy(alpha = if (card.owned) 0.22f else 0.08f), RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(Labels.moduleArt(card.kind)),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(40.dp)
            )
        }
        Text(
            text = stringResource(Labels.moduleName(card.id)),
            style = MaterialTheme.typography.labelSmall,
            color = if (card.owned) TextPrimary else TextMuted,
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(Labels.moduleKind(card.kind)),
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )
        SecondaryButton(
            text = when {
                card.equipped -> stringResource(R.string.module_equipped)
                card.owned -> stringResource(R.string.module_set)
                else -> stringResource(R.string.module_price, Labels.charge(card.price))
            },
            onClick = onAction,
            enabled = !card.equipped,
            accent = tint,
            stateLabel = if (card.equipped) {
                stringResource(R.string.module_equipped)
            } else {
                null
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
