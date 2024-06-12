package com.ryncorp.learnly.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LearnlyButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    fontSize: Int = 26,
    enabled: Boolean = true,
) {
    val colors = MaterialTheme.colorScheme

    val defaultModifier = Modifier
        .fillMaxWidth()
        .height(64.dp)
        .padding(horizontal = 40.dp)

    val combinedModifier = if (modifier == Modifier) {
        defaultModifier
    } else {
        modifier
    }

    Button(
        enabled = enabled,
        onClick = onClick,
        modifier = combinedModifier,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colors.primary,
            contentColor = colors.onPrimary,
            disabledContainerColor = Color.Gray,
            disabledContentColor = Color.DarkGray
        )
    ) {
        Text(
            text = text, modifier = Modifier, fontSize = fontSize.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}