package com.ryncorp.learnly.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun QuestionFeedbackSnackbar(
    snackbarData: SnackbarData,
    onDismiss: () -> Unit = {}
) {
    val colors = MaterialTheme.colorScheme

    Snackbar(
        action = {
            TextButton(onClick = onDismiss) {
                Text("Ok")
            }
        },
        modifier = Modifier.padding(16.dp),
        containerColor = colors.primary,
    ) {
        Text(
            snackbarData.visuals.message,
            color = colors.onPrimary,
        )
    }
}
