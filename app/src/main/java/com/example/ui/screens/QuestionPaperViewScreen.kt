package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ExamPaper
import com.example.model.GeneratedQuestion
import com.example.model.QuestionAnswer
import com.example.ui.dialogs.EditQuestionDialog
import com.example.ui.theme.Amber500
import com.example.ui.theme.Blue100
import com.example.ui.theme.Blue700
import com.example.ui.theme.Blue800
import com.example.util.PdfPrintHelper
import com.example.viewmodel.AppScreen
import com.example.viewmodel.ExamViewModel
import com.example.viewmodel.PaperViewTab

@Composable
fun QuestionPaperViewScreen(
    viewModel: ExamViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val currentPaper by viewModel.currentExamPaper.collectAsState()
    val activeTab by viewModel.paperViewTab.collectAsState()
    val schoolName by viewModel.schoolName.collectAsState()
    val examTitle by viewModel.examTitle.collectAsState()

    var isEditingHeader by remember { mutableStateOf(false) }
    var tempSchoolName by remember { mutableStateOf(schoolName) }
    var tempExamTitle by remember { mutableStateOf(examTitle) }

    var editingQuestion by remember { mutableStateOf<GeneratedQuestion?>(null) }

    BackHandler {
        viewModel.navigateTo(AppScreen.GENERATOR)
    }

    if (currentPaper == null) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("No question paper generated yet.")
        }
        return
    }

    val paper = currentPaper!!

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF1F5F9))
    ) {
        // Top Action Bar
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding(),
            color = Color.White,
            shadowElevation = 2.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { viewModel.navigateTo(AppScreen.GENERATOR) },
                        modifier = Modifier.testTag("back_to_generator_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF0F172A)
                        )
                    }

                    Column {
                        Text(
                            text = paper.subjectName,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = "Full Marks: ${paper.fullMarks} • ${paper.timeAllowed}",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }
                }

                // Action Buttons: Print, Share, Copy
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = {
                            val text = PdfPrintHelper.copyPaperAsText(
                                paper,
                                activeTab == PaperViewTab.HANDNOTE_ANSWERS
                            )
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clipboard.setPrimaryClip(ClipData.newPlainText("Question Paper", text))
                            Toast.makeText(context, "Copied question paper to clipboard!", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.testTag("copy_paper_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy text",
                            tint = Color(0xFF475569)
                        )
                    }

                    IconButton(
                        onClick = {
                            PdfPrintHelper.sharePaper(
                                context,
                                paper,
                                activeTab == PaperViewTab.HANDNOTE_ANSWERS
                            )
                        },
                        modifier = Modifier.testTag("share_paper_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = Color(0xFF475569)
                        )
                    }

                    IconButton(
                        onClick = {
                            PdfPrintHelper.printOrSavePdf(
                                context,
                                paper,
                                activeTab == PaperViewTab.HANDNOTE_ANSWERS
                            )
                        },
                        modifier = Modifier.testTag("print_pdf_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Print,
                            contentDescription = "Print or PDF",
                            tint = Blue700
                        )
                    }
                }
            }
        }

        // Tab Selector: "Question Paper" vs "Answer Key & Solutions"
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF1F5F9))
                    .padding(3.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                val isQP = activeTab == PaperViewTab.QUESTION_PAPER
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isQP) Blue700 else Color.Transparent)
                        .clickable { viewModel.setPaperViewTab(PaperViewTab.QUESTION_PAPER) }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Question Paper (প্রশ্নপত্র)",
                        fontSize = 13.sp,
                        fontWeight = if (isQP) FontWeight.Bold else FontWeight.Medium,
                        color = if (isQP) Color.White else Color(0xFF475569)
                    )
                }

                val isAns = activeTab == PaperViewTab.HANDNOTE_ANSWERS
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isAns) Blue700 else Color.Transparent)
                        .clickable { viewModel.setPaperViewTab(PaperViewTab.HANDNOTE_ANSWERS) }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Answer Key (উত্তরমালা)",
                        fontSize = 13.sp,
                        fontWeight = if (isAns) FontWeight.Bold else FontWeight.Medium,
                        color = if (isAns) Color.White else Color(0xFF475569)
                    )
                }
            }
        }

        // Main Paper Content (Sheet Layout styled like printed school exam!)
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(6.dp))

                // Printable A4 Sheet Header
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(10.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (isEditingHeader) {
                            OutlinedTextField(
                                value = tempSchoolName,
                                onValueChange = { tempSchoolName = it },
                                label = { Text("School / College Name") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = tempExamTitle,
                                onValueChange = { tempExamTitle = it },
                                label = { Text("Examination Name") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = {
                                    viewModel.setSchoolName(tempSchoolName)
                                    viewModel.setExamTitle(tempExamTitle)
                                    isEditingHeader = false
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Blue700)
                            ) {
                                Text("Save Header")
                            }
                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = paper.schoolName,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0F172A),
                                    textAlign = TextAlign.Center,
                                    fontFamily = FontFamily.Serif
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                IconButton(
                                    onClick = {
                                        tempSchoolName = paper.schoolName
                                        tempExamTitle = paper.title
                                        isEditingHeader = true
                                    },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Edit School Name",
                                        tint = Color.Gray,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }

                            Text(
                                text = paper.title,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF334155),
                                fontFamily = FontFamily.Serif
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Subject: ${paper.subjectName} (${paper.subjectCode})",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF1E293B)
                                )
                                Text(
                                    text = "Class: ${paper.className}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF1E293B)
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Time: ${paper.timeAllowed}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E293B)
                                )
                                Text(
                                    text = "Full Marks: ${paper.fullMarks}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E293B)
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))
                            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                            Text(
                                text = "[ ${paper.instructions} ]",
                                fontSize = 11.sp,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                color = Color.Gray,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            if (activeTab == PaperViewTab.QUESTION_PAPER) {
                // Questions List
                var lastSection = ""
                paper.questions.forEach { q ->
                    if (q.sectionTitle.isNotBlank() && q.sectionTitle != lastSection) {
                        lastSection = q.sectionTitle
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = q.sectionTitle,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Blue800,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }

                    item {
                        QuestionDisplayCard(
                            question = q,
                            onEdit = { editingQuestion = q }
                        )
                    }
                }
            } else {
                // Answer Key List
                items(paper.answers) { answer ->
                    AnswerDisplayCard(answer = answer)
                }
            }

            item {
                Spacer(modifier = Modifier.height(10.dp))
                // Bottom Print Action Banner
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Ready to print or save PDF?",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Blue800
                            )
                            Text(
                                text = "Standard A4 Board Exam Format",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                        }

                        Button(
                            onClick = {
                                PdfPrintHelper.printOrSavePdf(
                                    context,
                                    paper,
                                    activeTab == PaperViewTab.HANDNOTE_ANSWERS
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Blue700),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Print,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Print / PDF", fontSize = 13.sp)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    // Edit Question Modal
    editingQuestion?.let { q ->
        val answer = paper.answers.find { it.questionNumber == q.questionNumber }
        EditQuestionDialog(
            question = q,
            answer = answer,
            onSave = { updatedQ, updatedA ->
                viewModel.updateQuestion(q.questionNumber, updatedQ, updatedA)
            },
            onDismiss = { editingQuestion = null }
        )
    }
}

@Composable
fun QuestionDisplayCard(
    question: GeneratedQuestion,
    onEdit: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Header: Number + Instruction + Marks + Edit Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = "${question.questionNumber}. ",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A),
                        fontFamily = FontFamily.Serif
                    )
                    Text(
                        text = question.instruction,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF1E293B),
                        fontFamily = FontFamily.Serif,
                        lineHeight = 19.sp
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${question.marks}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Blue800,
                        fontFamily = FontFamily.Serif
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    IconButton(
                        onClick = onEdit,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Question",
                            tint = Color.Gray,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }
            }

            // Box Clues
            if (!question.boxClues.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(6.dp))
                        .background(Color(0xFFF8FAFC))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = question.boxClues.joinToString("   |   "),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF334155),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Passage
            if (!question.passage.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = question.passage,
                    fontSize = 13.sp,
                    color = Color(0xFF334155),
                    fontFamily = FontFamily.Serif,
                    lineHeight = 20.sp
                )
            }

            // Sub items
            if (question.subItems.isNotEmpty()) {
                Spacer(modifier = Modifier.height(6.dp))
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    question.subItems.forEach { sub ->
                        Text(
                            text = sub,
                            fontSize = 13.sp,
                            color = Color(0xFF1E293B),
                            fontFamily = FontFamily.Serif,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AnswerDisplayCard(answer: QuestionAnswer) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Blue700),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${answer.questionNumber}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = answer.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Blue800
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFFF8FAFC))
                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(6.dp))
                    .padding(10.dp)
            ) {
                Text(
                    text = answer.answerText,
                    fontSize = 13.sp,
                    color = Color(0xFF0F172A),
                    fontFamily = FontFamily.Monospace,
                    lineHeight = 19.sp
                )
            }

            if (!answer.explanation.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "💡 Explanation: ${answer.explanation}",
                    fontSize = 11.sp,
                    color = Color(0xFF475569),
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            }
        }
    }
}
