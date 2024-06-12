package com.ryncorp.learnly.ui.quiz.edit

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ryncorp.learnly.database.QuizCategory
import com.ryncorp.learnly.database.QuizQuestion
import com.ryncorp.learnly.ui.components.CategoryDialog
import com.ryncorp.learnly.ui.components.EditableCategoryItem
import com.ryncorp.learnly.ui.components.EditableQuestionItem
import com.ryncorp.learnly.ui.components.LearnlyButton
import com.ryncorp.learnly.ui.components.LearnlyTopBar
import com.ryncorp.learnly.ui.components.QuestionDialog

@Composable
fun QuestionListScreen(viewModel: EditQuizViewModel) {
    val uiState = viewModel.uiState.collectAsState()
    val questions = uiState.value.questions

    var showDialog by remember { mutableStateOf(false) }


    if (showDialog) {
        QuestionDialog(
            question = null,
            categories = uiState.value.categories,
            onDismiss = { showDialog = false },
            onConfirm = {
                viewModel.addNewQuestion(it)
            })
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        LazyColumn(modifier = Modifier.fillMaxHeight(0.85f)) {
            items(questions) { question ->
                QuestionItem(question, viewModel)
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)

        ) {
            LearnlyButton(
                text = "Ajouter une question",
                onClick = {
                    showDialog = true
                },
                modifier = Modifier.align(alignment = Alignment.Center)
            )
        }
    }
}

@Composable
fun QuestionItem(question: QuizQuestion, viewModel: EditQuizViewModel) {
    val uiState = viewModel.uiState.collectAsState()
    EditableQuestionItem(
        question = question,
        categories = uiState.value.categories,
        onEditRequest = { viewModel.editQuestion(it) },
        onDeleteRequest = { viewModel.deleteQuestion(it) }
    )
}

@Composable
fun CategoryListScreen(viewModel: EditQuizViewModel) {
    val uiState = viewModel.uiState.collectAsState()

    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        CategoryDialog(
            category = null,
            onDismiss = { showDialog = false },
            onConfirm = {
                viewModel.addNewCategory(it)
            })
    }
    
    Column(modifier = Modifier.fillMaxWidth()) {
        LazyColumn(modifier = Modifier.fillMaxHeight(0.85f)) {
            items(uiState.value.categories) { category ->
                CategoryItem(category, viewModel)
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)

        ) {
            LearnlyButton(
                text = "Ajouter une catégorie",
                onClick = {
                    showDialog = true
                },
                modifier = Modifier.align(alignment = Alignment.Center)
            )
        }
    }
}

@Composable
fun CategoryItem(category: QuizCategory, viewModel: EditQuizViewModel) {
    val uiState = viewModel.uiState.collectAsState()
    EditableCategoryItem(
        category = category,
        onEditRequest = { viewModel.editCategory(it) },
        onDeleteRequest = { viewModel.deleteCategory(it) }
    )
}

@Composable
fun EditQuizScreen(viewModel: EditQuizViewModel = viewModel()) {
    var tabIndex by remember { mutableIntStateOf(0) }

    val tabs = listOf("Questions", "Catégories")

    val colors = MaterialTheme.colorScheme

    LaunchedEffect(Unit) {
        viewModel.loadQuestionsAndCategories()
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = colors.background,
        topBar = {
            LearnlyTopBar(title = "Edition de ${tabs[tabIndex]}", null)
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            TabRow(selectedTabIndex = tabIndex) {
                tabs.forEachIndexed { index, title ->
                    Tab(text = { Text(text = title) },
                        selected = tabIndex == index,
                        onClick = { tabIndex = index },
                        icon = {
                            when (index) {
                                0 -> Icon(
                                    imageVector = Icons.Default.Create,
                                    contentDescription = null
                                )

                                1 -> Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = null
                                )
                            }
                        }
                    )
                }
            }
            when (tabIndex) {
                0 -> QuestionListScreen(viewModel = viewModel)
                1 -> CategoryListScreen(viewModel = viewModel)
            }
        }
    }
}
