package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.TopNavBar
import com.example.ui.dialogs.AddCreditDialog
import com.example.ui.dialogs.SubjectClassPickerModal
import com.example.ui.screens.GeneratorScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.QuestionPaperViewScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.AppScreen
import com.example.viewmodel.ExamViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                ShortexamApp()
            }
        }
    }
}

@Composable
fun ShortexamApp(viewModel: ExamViewModel = viewModel()) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val userCredits by viewModel.userCredits.collectAsState()
    val savedPapers by viewModel.savedPapers.collectAsState()
    val selectedSubject by viewModel.selectedSubject.collectAsState()
    val selectedClass by viewModel.selectedClass.collectAsState()

    var showCreditDialog by remember { mutableStateOf(false) }
    var showSubjectPicker by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        topBar = {
            if (currentScreen == AppScreen.GENERATOR) {
                TopNavBar(
                    userCredits = userCredits,
                    savedPapersCount = savedPapers.size,
                    onOpenCredits = { showCreditDialog = true },
                    onOpenHistory = { viewModel.navigateTo(AppScreen.HISTORY) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF8FAFC))
        ) {
            when (currentScreen) {
                AppScreen.GENERATOR -> {
                    GeneratorScreen(
                        viewModel = viewModel,
                        onOpenClassSubjectPicker = { showSubjectPicker = true },
                        onOpenCreditStore = { showCreditDialog = true }
                    )
                }
                AppScreen.QUESTION_PAPER_VIEW -> {
                    QuestionPaperViewScreen(viewModel = viewModel)
                }
                AppScreen.HISTORY -> {
                    HistoryScreen(viewModel = viewModel)
                }
                AppScreen.CREDIT_STORE -> {
                    GeneratorScreen(
                        viewModel = viewModel,
                        onOpenClassSubjectPicker = { showSubjectPicker = true },
                        onOpenCreditStore = { showCreditDialog = true }
                    )
                }
            }

            // Subject & Class Picker Modal
            if (showSubjectPicker) {
                SubjectClassPickerModal(
                    selectedSubject = selectedSubject,
                    selectedClass = selectedClass,
                    onSubjectSelected = { viewModel.selectSubject(it) },
                    onClassSelected = { viewModel.selectClass(it) },
                    onDismiss = { showSubjectPicker = false }
                )
            }

            // Credits Recharge Dialog
            if (showCreditDialog) {
                AddCreditDialog(
                    currentCredits = userCredits,
                    onAddCredits = { viewModel.addCredits(it) },
                    onDismiss = { showCreditDialog = false }
                )
            }
        }
    }
}
