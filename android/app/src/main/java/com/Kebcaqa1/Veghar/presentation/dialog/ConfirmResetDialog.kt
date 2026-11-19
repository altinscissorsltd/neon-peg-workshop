package com.Kebcaqa1.Veghar.presentation.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.DialogProperties
import com.Kebcaqa1.Veghar.core.theme.AccentMagenta
import com.Kebcaqa1.Veghar.core.theme.SurfaceRaised
import com.Kebcaqa1.Veghar.core.theme.TextPrimary
import com.Kebcaqa1.Veghar.core.theme.TextSecondary
import com.Kebcaqa1.Veghar.core.ui.PrimaryButton
import com.Kebcaqa1.Veghar.core.ui.SecondaryButton

@Composable
fun ConfirmResetDialog(
    title: String,
    message: String,
    confirmText: String,
    cancelText: String,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancel,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        ),
        containerColor = SurfaceRaised,
        titleContentColor = TextPrimary,
        textContentColor = TextSecondary,
        iconContentColor = AccentMagenta,
        title = {
            Text(text = title, style = MaterialTheme.typography.headlineMedium)
        },
        text = {
            Text(text = message, style = MaterialTheme.typography.bodyLarge)
        },
        confirmButton = {
            PrimaryButton(text = confirmText, onClick = onConfirm)
        },
        dismissButton = {
            SecondaryButton(text = cancelText, onClick = onCancel)
        }
    )
}
