package com.ryncorp.learnly.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import java.util.Date

@Entity(
    foreignKeys = [ForeignKey(
        entity = QuizCategory::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("categoryId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class QuizQuestion(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val question: String,
    val answer: String,
    val categoryId: Int,
    val streak: Int,
    val nextShowDate: Long // Unix timestamp
)