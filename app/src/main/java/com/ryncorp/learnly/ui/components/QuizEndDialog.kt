package com.ryncorp.learnly.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun QuizEndDialog(
    right: Int,
    wrong: Int,
    goHome: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    val total = right + wrong

    Dialog(
        onDismissRequest = goHome
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(colors.onPrimary, RoundedCornerShape(24.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Quiz terminé", modifier = Modifier, fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Quiz de $total questions", modifier = Modifier, fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                )
                Text(
                    text = "Réponses correctes : $right", modifier = Modifier, fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                )
                Text(
                    text = "Réponses incorrectes: $wrong", modifier = Modifier, fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                )
            }

            LearnlyButton("Retour", onClick = goHome)
        }
    }
}