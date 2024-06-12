package com.ryncorp.learnly.ui.settings

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ryncorp.learnly.ui.components.LearnlyButton
import com.ryncorp.learnly.ui.components.LearnlyTextField

fun isValidColor(color: String): Boolean {
    return try {
        Color(android.graphics.Color.parseColor(color))
        true
    } catch (e: Exception) {
        false
    }
}

@Composable
fun SettingsScreen(viewModel: SettingsViewModel = viewModel()) {


    val name by viewModel.nameFlow.collectAsState(initial = InitialData.name)
    val quizDelay by viewModel.quizDelayFlow.collectAsState(initial = InitialData.quizDelay)
    val bgColor by viewModel.bgColorFlow.collectAsState(initial = InitialData.bgColor)

    var formName by remember { mutableStateOf(name) }
    var formQuizDelay by remember { mutableIntStateOf(quizDelay) }
    var formBgColor by remember { mutableStateOf(bgColor) }

    LaunchedEffect(Unit) {
        formName = name
        formQuizDelay = quizDelay
        formBgColor = bgColor
    }

    Column (Modifier.padding(16.dp)){
        LearnlyTextField(value = formName, onValueChange = { formName = it }, label = "Nom")
        LearnlyTextField(value = formQuizDelay.toString(), onValueChange = { formQuizDelay = it.toIntOrNull() ?: 0 }, label = "Délai entre les questions", keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
        LearnlyTextField(value = formBgColor, onValueChange = { formBgColor = it }, label = "Couleur de fond")
        LearnlyButton(text = "Enregistrer", onClick = {
            if (isValidColor(formBgColor)) {
                viewModel.saveSettings(formName, formQuizDelay, formBgColor)
            }
            else {
                Toast.makeText(
                    viewModel.getApplication(),
                    "Couleur invalide",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

}
