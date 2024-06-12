package com.ryncorp.learnly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.ryncorp.learnly.database.QuizDatabase
import com.ryncorp.learnly.navigation.Navigator
import com.ryncorp.learnly.ui.theme.LearnlyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            LearnlyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigator(navController)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        QuizDatabase.destroyInstance()
    }
}
