package com.ryncorp.learnly.ui.theme

import android.app.Application
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.AndroidViewModel
import com.ryncorp.learnly.ui.settings.InitialData
import com.ryncorp.learnly.ui.settings.dataStore
import kotlinx.coroutines.flow.map

class ThemeViewModel(private val application: Application) : AndroidViewModel(application) {
    private val dataStore = application.dataStore
    private val KEY_BG_COLOR = stringPreferencesKey("bg_color")

    val bgColorFlow = dataStore.data.map { preferences ->
        preferences[KEY_BG_COLOR] ?: InitialData.bgColor
    }
}