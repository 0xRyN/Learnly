package com.ryncorp.learnly.util

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.RECEIVER_EXPORTED
import android.content.Intent
import android.content.IntentFilter
import androidx.core.net.toUri
import com.ryncorp.learnly.database.QuizCategory
import com.ryncorp.learnly.database.QuizDatabase
import com.ryncorp.learnly.database.QuizQuestion
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.io.FileInputStream
import java.util.Date

// This class is responsible for downloading the quiz data from the server
// and storing it in the local database.
class QuizDownloader(private val context: Context) {

    private val url =
        "https://gist.github.com/0xRyN/4fc289def9b6e6207edfc8312c348887/raw/56728bcfd0c77bbe7ba47de9735716f555ee0b39/quiz.json"

    private val downloadManager = context.getSystemService(DownloadManager::class.java)

    private var downloadId: Long = 0

    private var downloadCompleteSignal = CompletableDeferred<Boolean>()

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (id == downloadId) {
                CoroutineScope(Dispatchers.IO).launch {
                    processFile()
                }
            }
        }
    }

    private val filter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)

    init {
        context.registerReceiver(receiver, filter, RECEIVER_EXPORTED)
    }

    @SuppressLint("Range")
    suspend fun downloadFileAndUpdateDatabase() {
        val request = DownloadManager.Request(url.toUri())
            .setMimeType("application/json")
            .setTitle("Quiz Data")
            .setDescription("Downloading quiz data...")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

        downloadId = downloadManager.enqueue(request)

        downloadCompleteSignal.await()
    }

    private suspend fun getCategoryId(categoryName: String): Int = withContext(Dispatchers.IO) {
        val db = QuizDatabase.getDatabase(context)
        val category = db.quizDao().findCategoryByName(categoryName)
        return@withContext if (category != null) {
            category.id
        } else {
            val newCategory = QuizCategory(categoryName = categoryName)
            val newId = db.quizDao().addCategory(newCategory)
            newId.toInt()
        }
    }

    suspend fun processFile() =
        withContext(Dispatchers.IO) {
            val fileDescriptor = downloadManager.openDownloadedFile(downloadId)
            val fileInputStream = FileInputStream(fileDescriptor.fileDescriptor)

            val jsonContent = fileInputStream.bufferedReader().use { it.readText() }
            fileInputStream.close()

            val jsonArray = JSONArray(jsonContent)
            val questions = mutableListOf<QuizQuestion>()

            for (i in 0 until jsonArray.length()) {
                val question = jsonArray.getJSONObject(i)
                val questionText = question.getString("question")
                val answer = question.getString("answer")
                val category = question.getString("category")
                val categoryId = getCategoryId(category)
                val newQuestion = QuizQuestion(
                    question = questionText,
                    answer = answer,
                    categoryId = categoryId,
                    streak = 0,
                    nextShowDate = Date().time
                )

                questions.add(newQuestion)
            }

            val db = QuizDatabase.getDatabase(context)
            questions.forEach { question ->
                db.quizDao().addQuestion(question)
            }

            downloadCompleteSignal.complete(true)
        }
}

