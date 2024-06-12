package com.ryncorp.learnly.navigation

sealed class Routes(val route: String) {
    data object Home : Routes("home")
    data object Quiz : Routes("quiz")
    data object Settings : Routes("settings")
    data object EditQuiz : Routes("edit_quiz")
}