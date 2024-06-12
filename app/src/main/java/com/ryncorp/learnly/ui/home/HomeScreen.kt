package com.ryncorp.learnly.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ryncorp.learnly.R
import com.ryncorp.learnly.navigation.LocalNavHostController
import com.ryncorp.learnly.navigation.Routes
import com.ryncorp.learnly.ui.components.LearnlyTopBar
import com.ryncorp.learnly.ui.components.QuizSelectionCard
import com.ryncorp.learnly.ui.settings.InitialData

@Composable
fun HomeScreen(viewModel: HomeViewModel = viewModel()) {
    val uiState = viewModel.uiState.collectAsState()

    val navController = LocalNavHostController.current

    val colors = MaterialTheme.colorScheme

    val name by viewModel.nameFlow.collectAsState(initial = InitialData.name)

    LaunchedEffect(Unit) {
        // Refresh categories on each load, in case the database was updated
        viewModel.refreshCategories()
    }

    Scaffold(
        containerColor = colors.background,
        modifier = Modifier.fillMaxSize(),
        topBar = {
            LearnlyTopBar(
                title = "Learnly",
                iconActions = mapOf(
                    R.drawable.ic_settings to { navController.navigate(Routes.Settings.route) },
                    R.drawable.ic_download to { viewModel.downloadQuestions() },
                    R.drawable.ic_delete to { viewModel.deleteDatabase() }
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { navController.navigate(Routes.EditQuiz.route) },
                modifier = Modifier
                    .width(150.dp)
                    .height(60.dp),
                containerColor = colors.onPrimary,
                contentColor = colors.primary,
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    modifier = Modifier.size(22.dp),
                    tint = colors.primary,
                    contentDescription = null
                )
                Text(
                    modifier = Modifier,
                    text = "Quiz",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = "Bienvenue $name",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.primary
                )
                if (uiState.value.categories.isEmpty()) {
                    Text(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        text = "Aucun sujet trouvé. Veuillez télécharger les questions.",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = colors.primary
                    )
                } else {
                    Text(
                        modifier = Modifier.align(Alignment.Start),
                        text = "Choisissez un sujet:",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = colors.primary
                    )
                    LazyColumn(
                        modifier = Modifier,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.value.categories) {
                            QuizSelectionCard(
                                category = it,
                                onClick = { navController.navigate("${Routes.Quiz.route}/$it") }
                            )
                        }
                    }
                }
            }

            if (uiState.value.isLoading) {
                Column(
                    modifier = Modifier
                        .background(Color.Black.copy(alpha = 0.5f))
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        text = "Téléchargement en cours...",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = colors.primary
                    )

                    CircularProgressIndicator(
                        modifier = Modifier.size(60.dp),
                        color = colors.primary,
                        strokeWidth = 4.dp
                    )
                }
            }
        }
    }
}