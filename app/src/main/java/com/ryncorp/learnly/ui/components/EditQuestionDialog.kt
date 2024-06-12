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
import com.ryncorp.learnly.database.QuizQuestion
import java.util.Date

// UNUSED. Please use QuestionDialog.kt instead.
@Composable
fun EditQuestionDialog(
    question: QuizQuestion,
    categories: List<QuizCategory>,
    onDismiss: () -> Unit,
    onConfirm: (QuizQuestion) -> Unit
) {
    val colors = MaterialTheme.colorScheme

    val currentCategory = categories.first { it.id == question.id }

    var questionText by remember { mutableStateOf(question.question) }
    var answer by remember { mutableStateOf(question.answer) }
    var category by remember { mutableStateOf(currentCategory) }

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
            Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
            Text(
                text = "Éditer la question",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            LearnlyTextField(
                value = questionText,
                onValueChange = { questionText = it },
                label = "Nouvelle question",
                modifier = textFieldModifier
            )
            LearnlyTextField(
                value = answer,
                onValueChange = { answer = it },
                label = "Nouvelle réponse",
                modifier = textFieldModifier
            )

            Text(
                text = "Nouvelle catégorie",
                fontSize = 14.sp,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 8.dp)
            )

            CategoryDropdownMenu(
                categories = categories,
                selectedCategoryId = category.id,
                onCategorySelected = { category = it }
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
                        onConfirm(
                            QuizQuestion(
                                id = question.id,
                                question = questionText,
                                answer = answer,
                                categoryId = category.id,
                                streak = 0,
                                nextShowDate = Date().time
                            )
                        )
                        onDismiss()
                    }
                )
            }

        }
    }
}
