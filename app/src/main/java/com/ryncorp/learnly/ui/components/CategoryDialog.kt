package com.ryncorp.learnly.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.ryncorp.learnly.database.QuizCategory

@Composable
fun CategoryDialog(
    category: QuizCategory?,
    onDismiss: () -> Unit,
    onConfirm: (QuizCategory) -> Unit
) {
    val colors = MaterialTheme.colorScheme

    // Determine if we're adding a new question or editing an existing one.
    val isEditing = category != null

    var categoryText by remember { mutableStateOf(category?.categoryName ?: "") }

    val textFieldModifier = Modifier
        .fillMaxWidth()

    val btnModifier = Modifier.height(48.dp)

    Dialog(
        onDismissRequest = onDismiss,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(colors.onPrimary, RoundedCornerShape(24.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = if (isEditing) Icons.Default.Edit else Icons.Default.Add,
                contentDescription = if (isEditing) "Edit" else "Add"
            )
            Text(
                text = if (isEditing) "Éditer la catégorie" else "Ajouter une catégorie",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            LearnlyTextField(
                value = categoryText,
                onValueChange = { categoryText = it },
                label = "Nom de la catégorie",
                modifier = textFieldModifier
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                LearnlyButton(
                    text = "Annuler",
                    modifier = btnModifier,
                    fontSize = 16,
                    onClick = onDismiss
                )
                LearnlyButton(
                    text = "Confirmer",
                    modifier = btnModifier,
                    fontSize = 16,
                    onClick = {
                        val newOrUpdatedCategory = if (isEditing && category != null) {
                            QuizCategory(
                                id = category.id,
                                categoryName = categoryText
                            )
                        } else {
                            QuizCategory(
                                categoryName = categoryText
                            )
                        }
                        onConfirm(newOrUpdatedCategory)
                        onDismiss()
                    }
                )
            }
        }
    }
}
