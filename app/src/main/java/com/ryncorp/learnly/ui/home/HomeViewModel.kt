package com.ryncorp.learnly.ui.home

import android.app.Application
import android.util.Log
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ryncorp.learnly.database.QuizCategory
import com.ryncorp.learnly.database.QuizDatabase
import com.ryncorp.learnly.ui.settings.InitialData
import com.ryncorp.learnly.ui.settings.dataStore
import com.ryncorp.learnly.util.QuizDownloader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

data class HomeUiState(
    val categories: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = ""
)

class HomeViewModel(private val application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(HomeUiState())
    private val db = QuizDatabase.getDatabase(application).quizDao()
    val uiState = _uiState.asStateFlow()

    private val dataStore = application.dataStore
    private val KEY_NAME = stringPreferencesKey("name")
    val nameFlow = dataStore.data.map { preferences ->
        preferences[KEY_NAME] ?: InitialData.name
    }

    // This function returns only available categories (with questions that are ready to be shown)
    private suspend fun getAllAvailableCategories(): List<QuizCategory> {
        return db.getAllNonEmptyCategories().filter {
            db.getQuestionsForCategory(it.id).any { question ->
                question.nextShowDate <= Date().time
            }
        }
    }

    fun refreshCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            // Check if database is empty
            val categories = getAllAvailableCategories()
            if (categories.isNotEmpty()) {
                _uiState.update { it.copy(categories = categories.map { it.categoryName }) }
            }
        }
    }

    fun downloadQuestions() {
        viewModelScope.launch(Dispatchers.IO) {
            if (db.getAllCategories().isNotEmpty()) {
                Log.d("HomeViewModel", "Database already exists")
                return@launch
            }
            _uiState.update { it.copy(isLoading = true) }
            QuizDownloader(application.applicationContext).downloadFileAndUpdateDatabase()
            val categories = QuizDatabase.getDatabase(application).quizDao().getAllCategories()
            _uiState.update { it -> it.copy(categories = categories.map { it.categoryName }) }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun deleteDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            // Close database and destroy instance
            QuizDatabase.destroyInstance()
            val databaseDeleted = application.deleteDatabase("quiz_database")
            Log.d("HomeViewModel", "Database deleted: $databaseDeleted")
            // Clean up state
            _uiState.update { it.copy(categories = emptyList()) }
        }
    }
}