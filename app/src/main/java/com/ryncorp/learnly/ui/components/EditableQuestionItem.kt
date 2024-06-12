package com.ryncorp.learnly.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.ryncorp.learnly.database.QuizCategory
import com.ryncorp.learnly.database.QuizQuestion

@Composable
fun EditableQuestionItem(
    question: QuizQuestion,
    categories: List<QuizCategory>,
    onEditRequest: (QuizQuestion) -> Unit,
    onDeleteRequest: (QuizQuestion) -> Unit
) {
    val text = question.question
    val colors = MaterialTheme.colorScheme

    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        QuestionDialog(
            question = question,
            categories = categories,
            onDismiss = { showDialog = false },
            onConfirm = {
                onEditRequest(it)
            })
    }

    ListItem(
        headlineContent = { Text(text) },
        leadingContent = {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Question",
                tint = colors.primary
            )
        },
        trailingContent = {
            Row {
                IconButton(onClick = { showDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = colors.primary
                    )
                }
                IconButton(onClick = { onDeleteRequest(question) }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = colors.primary
                    )
                }
            }
        },
        colors = ListItemDefaults.colors(
            headlineColor = colors.onBackground,
            containerColor = colors.background
        ),
        shadowElevation = 3.dp
    )
    Divider()
}