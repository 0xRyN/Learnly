package com.ryncorp.learnly.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ryncorp.learnly.ui.home.HomeScreen
import com.ryncorp.learnly.ui.quiz.edit.EditQuizScreen
import com.ryncorp.learnly.ui.quiz.play.QuizScreen
import com.ryncorp.learnly.ui.settings.SettingsScreen

val LocalNavHostController =
    compositionLocalOf<NavHostController> { throw IllegalStateException("Not provided yet") }

@Composable
fun Navigator(navHostController: NavHostController) {
    CompositionLocalProvider(LocalNavHostController provides navHostController) {
        NavHost(navController = navHostController, startDestination = Routes.Home.route) {

            composable(Routes.Home.route) {
                HomeScreen()
            }

            composable(Routes.Settings.route) {
                SettingsScreen()
            }

            composable(Routes.EditQuiz.route) {
                EditQuizScreen()
            }

            // We cannot access a Quiz without a category, so we need to pass it as an argument
            composable(
                "${Routes.Quiz.route}/{category}",
                arguments = listOf(navArgument("category") { type = NavType.StringType })
            ) {
                val category = it.arguments?.getString("category") ?: "Programmation"
                QuizScreen(category = category)
            }
        }
    }
}
