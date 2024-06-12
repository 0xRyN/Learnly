package com.ryncorp.learnly.ui.settings

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

object InitialData {
    const val name: String = "Rayan"
    const val quizDelay: Int = 30
    const val bgColor: String = "#FFFFFF"
}

class SettingsViewModel(private val application: Application) : AndroidViewModel(application) {
    private val dataStore = application.dataStore
    private val KEY_NAME = stringPreferencesKey("name")
    private val KEY_QUIZ_DELAY = intPreferencesKey("quiz_delay")
    private val KEY_BG_COLOR = stringPreferencesKey("bg_color")

    val nameFlow = dataStore.data.map { preferences ->
        preferences[KEY_NAME] ?: InitialData.name
    }

    val quizDelayFlow = dataStore.data.map { preferences ->
        preferences[KEY_QUIZ_DELAY] ?: InitialData.quizDelay
    }

    val bgColorFlow = dataStore.data.map { preferences ->
        preferences[KEY_BG_COLOR] ?: InitialData.bgColor
    }

    fun saveSettings(name: String, quizDelay: Int, bgColor: String) {
        viewModelScope.launch {
            application.dataStore.edit { settings ->
                settings[KEY_NAME] = name
                settings[KEY_QUIZ_DELAY] = quizDelay
                settings[KEY_BG_COLOR] = bgColor
            }
        }
    }
}