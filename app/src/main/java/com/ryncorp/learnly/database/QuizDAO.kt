package com.ryncorp.learnly.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface QuizDao {
    // Add a new category
    @Insert
    suspend fun addCategory(category: QuizCategory): Long

    // Remove a category
    @Delete
    suspend fun removeCategory(category: QuizCategory)

    // Remove all the questions from a category
    @Query("DELETE FROM QuizQuestion WHERE categoryId = :categoryId")
    suspend fun removeQuestionsFromCategory(categoryId: Int)

    // Add a new question
    @Insert
    suspend fun addQuestion(question: QuizQuestion): Long

    // Remove a question
    @Delete
    suspend fun removeQuestion(question: QuizQuestion)

    // Select all questions from a category
    @Query("SELECT * FROM quizquestion WHERE categoryId = :categoryId")
    suspend fun getQuestionsForCategory(categoryId: Int): List<QuizQuestion>

    // Select a category by name
    @Query("SELECT * FROM QuizCategory WHERE categoryName = :name LIMIT 1")
    suspend fun findCategoryByName(name: String): QuizCategory?

    // Select all categories
    @Query("SELECT * FROM QuizCategory")
    suspend fun getAllCategories(): List<QuizCategory>

    // Select all non-empty categories
    @Query("SELECT * FROM QuizCategory WHERE id IN (SELECT DISTINCT categoryId FROM QuizQuestion)")
    suspend fun getAllNonEmptyCategories(): List<QuizCategory>

    // Select all questions
    @Query("SELECT * FROM QuizQuestion")
    suspend fun getAllQuestions(): List<QuizQuestion>

    // Update question streak and nextShowDate
    @Query("UPDATE QuizQuestion SET streak = :streak, nextShowDate = :nextShowDate WHERE id = :id")
    suspend fun updateQuestion(id: Int, streak: Int, nextShowDate: Long)

    // Update all the fields of a question
    @Query("UPDATE QuizQuestion SET question = :question, answer = :answer, categoryId = :categoryId, streak = :streak, nextShowDate = :nextShowDate WHERE id = :id")
    suspend fun updateQuestion(
        id: Int,
        question: String,
        answer: String,
        categoryId: Int,
        streak: Int,
        nextShowDate: Long
    )

    // Update category name
    @Query("UPDATE QuizCategory SET categoryName = :name WHERE id = :id")
    suspend fun updateCategory(id: Int, name: String)
}
