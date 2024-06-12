package com.ryncorp.learnly.ui.quiz.play

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ryncorp.learnly.database.QuizDatabase
import com.ryncorp.learnly.database.QuizQuestion
import com.ryncorp.learnly.ui.settings.InitialData
import com.ryncorp.learnly.ui.settings.dataStore
import com.ryncorp.learnly.util.AnswerValidator
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import kotlin.math.pow

data class QuizState(
    val questions: List<QuizQuestion> = emptyList(),
    val currentQuestion: QuizQuestion? = null,
    val quizFinished: Boolean = false,
    val failedQuestions: List<QuizQuestion> = emptyList(),
)

class QuizViewModel(private val application: android.app.Application) :
    AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(QuizState())
    val uiState = _uiState

    private val db = QuizDatabase.getDatabase(application).quizDao()

    private val dataStore = application.dataStore
    private val QUIZ_DELAY_KEY = intPreferencesKey("quiz_delay")
    private val quizDelayFlow : Flow<Int> = dataStore.data.map { preferences ->
        preferences[QUIZ_DELAY_KEY] ?: InitialData.quizDelay
    }

    private var TIMER: Int = InitialData.quizDelay

    init {
        viewModelScope.launch {
            quizDelayFlow.collect { delay ->
                TIMER = delay
            }
        }
    }

    private val MAX_TYPOS = 2

    private var timerJob: Job? = null

    var currentQuestionTimer by mutableIntStateOf(TIMER)
        private set

    var isLastQuestionCorrect by mutableStateOf(false)
        private set

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (currentQuestionTimer > 0) {
                delay(1000)
                currentQuestionTimer--
            }
            // User ran out of time
            submitAnswer("", true)
        }
    }

    private fun resetTimer() {
        timerJob?.cancel()
        currentQuestionTimer = TIMER
    }

    suspend fun loadQuestions(category: String) {
        Log.d("QuizViewModel", "Loading questions for category: $category")
        val categoryId = db.findCategoryByName(category)?.id
        val allQuestions = db.getQuestionsForCategory(categoryId!!)
        val questions = allQuestions.filter { it.nextShowDate <= Date().time }.shuffled()
        _uiState.update { it.copy(questions = questions, currentQuestion = questions.first()) }
        startTimer()
    }

    private fun nextQuestion() {
        val questionIndex = _uiState.value.questions.indexOf(_uiState.value.currentQuestion)
        val nextQuestion = _uiState.value.questions.getOrElse(questionIndex + 1) { null }
        if (nextQuestion == null) {
            _uiState.update { it.copy(quizFinished = true) }
            resetTimer()
            return
        }
        _uiState.update { it.copy(currentQuestion = nextQuestion) }
        resetTimer()
        startTimer()
    }

    private fun updateQuestionStatus(question: QuizQuestion, hasFailed: Boolean) {
        viewModelScope.launch {
            val nextShowDate = if (hasFailed) {
                Date()
            } else {
                val daysToAdd = 2.0.pow(question.streak.toDouble()).toInt()
                val newDate = Date()
                // Need Long to avoid overflow
                val millisecondsToAdd: Long = daysToAdd * 24L * 60 * 60 * 1000
                newDate.time += millisecondsToAdd
                newDate
            }
            val newStreak = if (hasFailed) 0 else question.streak + 1
            db.updateQuestion(question.id, newStreak, nextShowDate.time)
        }
    }

    fun submitAnswer(answer: String, timeout: Boolean = false) {
        val currentQuestion = _uiState.value.currentQuestion!!
        if (timeout) {
            _uiState.update { it.copy(failedQuestions = it.failedQuestions + currentQuestion) }
            isLastQuestionCorrect = false
        } else {
            val answerIsCorrect = AnswerValidator.isAnswerCorrect(
                answer,
                currentQuestion.answer,
                MAX_TYPOS
            )
            Log.d("QuizViewModel", "Answer is correct: $answerIsCorrect")
            Log.d("QuizViewModel", "Current questions size: ${_uiState.value.questions.size}")
            if (answerIsCorrect) {
                isLastQuestionCorrect = true
            } else {
                _uiState.update { it.copy(failedQuestions = it.failedQuestions + currentQuestion) }
                isLastQuestionCorrect = false
            }
        }
        updateQuestionStatus(currentQuestion, !isLastQuestionCorrect)
        nextQuestion()
    }
}