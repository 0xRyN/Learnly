package com.ryncorp.learnly.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class QuizCategory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val categoryName: String
)
