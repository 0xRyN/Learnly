package com.ryncorp.learnly.ui.quiz.play

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ryncorp.learnly.navigation.LocalNavHostController
import com.ryncorp.learnly.navigation.Routes
import com.ryncorp.learnly.ui.components.LearnlyButton
import com.ryncorp.learnly.ui.components.LearnlyTextField
import com.ryncorp.learnly.ui.components.LearnlyTopBar
import com.ryncorp.learnly.ui.components.QuestionFeedbackSnackbar
import com.ryncorp.learnly.ui.components.QuizEndDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun QuizScreen(category: String, viewModel: QuizViewModel = viewModel()) {

    val navController = LocalNavHostController.current
    val uiState = viewModel.uiState.collectAsState()
    val colors = MaterialTheme.colorScheme

    var answer by remember { mutableStateOf("") }

    var showAnswer by remember { mutableStateOf(false) }
    var showAnswerTimer by remember { mutableIntStateOf(0) }
    var textFieldEnabled by remember { mutableStateOf(true) }

    var snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState) {
        Log.d("QuizScreen", "LaunchedEffect")
        viewModel.loadQuestions(category)
    }

    // Effect to show snackbar feedback when the question is answered
    LaunchedEffect(uiState.value.currentQuestion) {
        // Show snackbar when the question is answered
        // Don't show on first question
        if (uiState.value.questions.indexOf(uiState.value.currentQuestion) != 0) {
            val isCorrect = viewModel.isLastQuestionCorrect
            val message = if (isCorrect) "Exact. Bien joué !" else "Faux. Combo perdu :("
            snackbarHostState.showSnackbar(
                message = message,
                actionLabel = "OK",
                duration = SnackbarDuration.Short
            )
        }
    }

    val handleShowAnswer: suspend () -> Unit = suspend {
        showAnswer = !showAnswer
        textFieldEnabled = !textFieldEnabled
        // Submit empty answer in 3 seconds
        showAnswerTimer = 3
        while (showAnswerTimer > 0) {
            delay(1000)
            showAnswerTimer--
        }
        // Reset answer
        textFieldEnabled = true
        showAnswer = false
        viewModel.submitAnswer("")
    }

    if (uiState.value.quizFinished) {
        val totalQuestions = uiState.value.questions.size
        Log.d("QuizScreen", "Quiz Finished, total questions: $totalQuestions")
        val wrong = uiState.value.failedQuestions.size
        val right = totalQuestions - wrong

        QuizEndDialog(
            right = right,
            wrong = wrong,
            goHome = { navController.navigate(Routes.Home.route) }
        )
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 8.dp),
        containerColor = colors.background,
        topBar = {
            LearnlyTopBar(title = "Quiz de $category", null)
        },
        bottomBar = {
            LearnlyButton(text = "Valider",
                enabled = textFieldEnabled,
                onClick = {
                    viewModel.submitAnswer(answer)
                    answer = ""
                })
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState, snackbar = { snackbarData ->
                QuestionFeedbackSnackbar(
                    snackbarData = snackbarData,
                )
            })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier,
                    text = "Il vous reste ${viewModel.currentQuestionTimer} secondes...",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.primary
                )
                Text(
                    modifier = Modifier,
                    text = "${uiState.value.questions.indexOf(uiState.value.currentQuestion) + 1}/${uiState.value.questions.size}",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.tertiary
                )
            }

            Text(
                modifier = Modifier,
                text = "Question - Combo : ${uiState.value.currentQuestion?.streak}",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = colors.primary
            )
            Text(
                modifier = Modifier,
                text = "${uiState.value.currentQuestion?.question}",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = colors.primary
            )
            Text(
                modifier = Modifier,
                text = "Réponse:",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = colors.primary
            )
            LearnlyTextField(
                value = answer,
                onValueChange = { answer = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colors.onPrimary, RoundedCornerShape(24.dp)),
                label = "Votre réponse",
                enabled = textFieldEnabled
            )

            LearnlyButton(
                text = "Afficher la réponse",
                onClick = {
                    viewModel.viewModelScope.launch {
                        handleShowAnswer()
                    }
                },
                modifier = Modifier.padding(top = 16.dp),
                enabled = textFieldEnabled
            )

            if (showAnswer) {
                Text(
                    modifier = Modifier,
                    text = "${uiState.value.currentQuestion?.answer}.\n\nProchaine question dans $showAnswerTimer secondes...",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.primary
                )
            }

        }

    }
}