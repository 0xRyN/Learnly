package com.ryncorp.learnly.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LearnlyTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {

    val defaultModifier = Modifier
        .fillMaxWidth()
        .height(64.dp)
        .padding(horizontal = 40.dp)

    val combinedModifier = if (modifier == Modifier) {
        defaultModifier
    } else {
        modifier
    }

    Box {
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = combinedModifier,
            label = { label?.let { Text(it) } },
            placeholder = { placeholder?.let { Text(it) } },
            singleLine = true,
            enabled = enabled,
            keyboardOptions = keyboardOptions
        )
    }

}