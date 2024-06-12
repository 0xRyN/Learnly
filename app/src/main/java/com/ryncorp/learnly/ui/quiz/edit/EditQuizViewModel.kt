package com.ryncorp.learnly.ui.quiz.edit

import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ryncorp.learnly.database.QuizCategory
import com.ryncorp.learnly.database.QuizDatabase
import com.ryncorp.learnly.database.QuizQuestion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EditQuizState(
    val questions: List<QuizQuestion> = emptyList(),
    val categories: List<QuizCategory> = emptyList(),
)

class EditQuizViewModel(private val application: android.app.Application) :
    AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(EditQuizState())
    val uiState = _uiState

    private val db = QuizDatabase.getDatabase(application).quizDao()

    fun loadQuestionsAndCategories() {
        viewModelScope.launch {
            val questions = db.getAllQuestions()
            val categories = db.getAllCategories()
            _uiState.update { it.copy(questions = questions, categories = categories) }
        }
    }

    fun editQuestion(question: QuizQuestion) {
        Log.d("EditQuizViewModel", "Editing question: $question")
        viewModelScope.launch {
            db.updateQuestion(
                id = question.id,
                question = question.question,
                answer = question.answer,
                categoryId = question.categoryId,
                streak = question.streak,
                nextShowDate = question.nextShowDate
            )
            loadQuestionsAndCategories()
        }
    }

    fun editCategory(category: QuizCategory) {
        Log.d("EditQuizViewModel", "Editing category: $category")
        viewModelScope.launch {
            db.updateCategory(
                id = category.id,
                name = category.categoryName
            )
            loadQuestionsAndCategories()
        }
    }

    fun deleteCategory(category: QuizCategory) {
        viewModelScope.launch {
            db.removeCategory(category)
            // Remove all questions from this category
            // This should be done automatically by the foreign key constraint
            // db.removeQuestionsFromCategory(category.id)
            loadQuestionsAndCategories()
        }
    }

    fun deleteQuestion(question: QuizQuestion) {
        viewModelScope.launch {
            db.removeQuestion(question)
            loadQuestionsAndCategories()
        }
    }

    fun addNewQuestion(question: QuizQuestion) {
        viewModelScope.launch {
            db.addQuestion(question)
            loadQuestionsAndCategories()
        }
    }

    fun addNewCategory(category: QuizCategory) {
        viewModelScope.launch {
            db.addCategory(category)
            loadQuestionsAndCategories()
        }
    }

}
