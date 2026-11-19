package com.Kebcaqa1.Veghar.presentation.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Kebcaqa1.Veghar.R
import com.Kebcaqa1.Veghar.core.di.ServiceLocator
import com.Kebcaqa1.Veghar.core.theme.AccentCyan
import com.Kebcaqa1.Veghar.core.theme.AccentMagenta
import com.Kebcaqa1.Veghar.core.theme.TextPrimary
import com.Kebcaqa1.Veghar.core.theme.TextSecondary
import com.Kebcaqa1.Veghar.core.ui.GlassCard
import com.Kebcaqa1.Veghar.core.ui.Labels
import com.Kebcaqa1.Veghar.core.ui.ScreenScaffold
import com.Kebcaqa1.Veghar.core.ui.SecondaryButton
import com.Kebcaqa1.Veghar.domain.model.CalibrationTimer
import com.Kebcaqa1.Veghar.domain.model.GateControl
import com.Kebcaqa1.Veghar.domain.model.MotionLevel
import com.Kebcaqa1.Veghar.presentation.dialog.ConfirmResetDialog

private val SettingsOverlay = listOf(
    Color(0xE6071A1F),
    Color(0xB312121F),
    Color(0xF20A0A0F)
)

@Composable
fun SettingsRoute(onBack: () -> Unit) {
    val viewModel: SettingsViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by viewModel.state.collectAsStateWithLifecycle()
    SettingsScreen(
        state = state,
        onTimer = viewModel::onTimerSelected,
        onControl = viewModel::onControlSelected,
        onMotion = viewModel::onMotionSelected,
        onContrast = viewModel::onContrastSelected,
        onResetRequested = viewModel::onResetRequested,
        onResetCancelled = viewModel::onResetCancelled,
        onResetConfirmed = viewModel::onResetConfirmed,
        onBack = onBack
    )
}

@Composable
fun SettingsScreen(
    state: SettingsUiState,
    onTimer: (CalibrationTimer) -> Unit,
    onControl: (GateControl) -> Unit,
    onMotion: (MotionLevel) -> Unit,
    onContrast: (Boolean) -> Unit,
    onResetRequested: () -> Unit,
    onResetCancelled: () -> Unit,
    onResetConfirmed: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    ScreenScaffold(
        title = stringResource(R.string.settings_title),
        caption = stringResource(R.string.settings_caption),
        menuLabel = stringResource(R.string.menu),
        onBack = onBack,
        overlay = SettingsOverlay,
        modifier = modifier
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                OptionRow(
                    title = stringResource(R.string.setting_timer),
                    hint = stringResource(R.string.setting_timer_hint)
                ) {
                    CalibrationTimer.entries.forEach { value ->
                        ChoiceChip(
                            text = stringResource(Labels.calibrationTimer(value)),
                            selected = state.calibrationTimer == value,
                            accent = AccentCyan,
                            onClick = { onTimer(value) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
            item {
                OptionRow(
                    title = stringResource(R.string.setting_control),
                    hint = stringResource(R.string.setting_control_hint)
                ) {
                    GateControl.entries.forEach { value ->
                        ChoiceChip(
                            text = stringResource(Labels.gateControl(value)),
                            selected = state.gateControl == value,
                            accent = AccentMagenta,
                            onClick = { onControl(value) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
            item {
                OptionRow(
                    title = stringResource(R.string.setting_motion),
                    hint = stringResource(R.string.setting_motion_hint)
                ) {
                    MotionLevel.entries.forEach { value ->
                        ChoiceChip(
                            text = stringResource(Labels.motion(value)),
                            selected = state.motion == value,
                            accent = AccentCyan,
                            onClick = { onMotion(value) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
            item {
                OptionRow(
                    title = stringResource(R.string.setting_contrast),
                    hint = stringResource(R.string.setting_contrast_hint)
                ) {
                    ChoiceChip(
                        text = stringResource(R.string.option_on),
                        selected = state.highContrast,
                        accent = AccentCyan,
                        onClick = { onContrast(true) },
                        modifier = Modifier.weight(1f)
                    )
                    ChoiceChip(
                        text = stringResource(R.string.option_off),
                        selected = !state.highContrast,
                        accent = AccentCyan,
                        onClick = { onContrast(false) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            item {
                GlassCard(modifier = Modifier.fillMaxWidth(), accent = AccentMagenta) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.setting_reset_hint),
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                        TextButton(onClick = onResetRequested) {
                            Text(
                                text = stringResource(R.string.setting_reset),
                                style = MaterialTheme.typography.labelLarge,
                                color = AccentMagenta
                            )
                        }
                    }
                }
            }
        }
    }
    if (state.confirmReset) {
        ConfirmResetDialog(
            title = stringResource(R.string.reset_title),
            message = stringResource(R.string.reset_message),
            confirmText = stringResource(R.string.reset_confirm),
            cancelText = stringResource(R.string.reset_cancel),
            onConfirm = onResetConfirmed,
            onCancel = onResetCancelled
        )
    }
}

@Composable
private fun OptionRow(
    title: String,
    hint: String,
    content: @Composable androidx.compose.foundation.layout.RowScope.() -> Unit
) {
    GlassCard(modifier = Modifier.fillMaxWidth(), accent = AccentCyan, brackets = false) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                color = TextPrimary
            )
            Text(
                text = hint,
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                content = content
            )
        }
    }
}

@Composable
private fun ChoiceChip(
    text: String,
    selected: Boolean,
    accent: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    SecondaryButton(
        text = text,
        onClick = onClick,
        accent = if (selected) accent else TextSecondary,
        stateLabel = if (selected) {
            stringResource(R.string.option_selected)
        } else {
            stringResource(R.string.option_not_selected)
        },
        modifier = modifier
    )
}
