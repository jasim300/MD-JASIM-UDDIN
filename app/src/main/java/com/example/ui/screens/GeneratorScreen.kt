package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.AiQuality
import com.example.model.Chapter
import com.example.model.ExamDifficulty
import com.example.model.QuestionTypeConfig
import com.example.model.Subject
import com.example.ui.theme.Amber500
import com.example.ui.theme.Blue100
import com.example.ui.theme.Blue600
import com.example.ui.theme.Blue700
import com.example.ui.theme.Blue800
import com.example.viewmodel.ExamViewModel

@Composable
fun GeneratorScreen(
    viewModel: ExamViewModel,
    onOpenClassSubjectPicker: () -> Unit,
    onOpenCreditStore: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedSubject by viewModel.selectedSubject.collectAsState()
    val selectedClass by viewModel.selectedClass.collectAsState()
    val selectedMode by viewModel.selectedMode.collectAsState()
    val selectedChapterIds by viewModel.selectedChapterIds.collectAsState()
    val questionTypeCounts by viewModel.questionTypeCounts.collectAsState()
    val questionTypeEnabled by viewModel.questionTypeEnabled.collectAsState()
    val selectedSubOptions by viewModel.selectedSubOptions.collectAsState()
    val difficulty by viewModel.difficulty.collectAsState()
    val aiQuality by viewModel.aiQuality.collectAsState()
    val aiInstruction by viewModel.aiInstruction.collectAsState()
    val userCredits by viewModel.userCredits.collectAsState()
    val isGenerating by viewModel.isGenerating.collectAsState()
    val generationStep by viewModel.generationStep.collectAsState()

    val expectedCredits = viewModel.calculateExpectedCredits()
    val minRequiredCredits = (expectedCredits * 0.80).toInt().coerceAtLeast(10)
    val hasSufficientCredits = userCredits >= minRequiredCredits

    var areQuestionTypesExpanded by remember { mutableStateOf(true) }
    var areChaptersExpanded by remember { mutableStateOf(true) }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8FAFC))
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(6.dp))

                // Breadcrumb Navigation
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .clickable(onClick = onOpenClassSubjectPicker)
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Home",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "Subject",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = selectedSubject.name,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Blue700
                    )
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = selectedClass,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Title matching shortexam.com
                Text(
                    text = "Prepare a Question Paper for ${selectedSubject.name}, $selectedClass",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A),
                    lineHeight = 26.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Subject Book Cover & Tabs Card
                SubjectHeroCard(
                    subject = selectedSubject,
                    selectedMode = selectedMode,
                    onModeChanged = { viewModel.setSelectedMode(it) },
                    onChangeSubject = onOpenClassSubjectPicker
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Instruction helper note from video
                Text(
                    text = "Select chapters, set question counts, then press the 'Generate' button.",
                    fontSize = 13.sp,
                    color = Color(0xFF64748B),
                    fontWeight = FontWeight.Medium
                )
            }

            // SECTION 1: Select Chapters
            item {
                ChaptersSectionCard(
                    subject = selectedSubject,
                    selectedChapterIds = selectedChapterIds,
                    isExpanded = areChaptersExpanded,
                    onToggleExpand = { areChaptersExpanded = !areChaptersExpanded },
                    onToggleSelectAll = { viewModel.toggleSelectAllChapters() },
                    onToggleChapter = { viewModel.toggleChapter(it) }
                )
            }

            // SECTION 2: Question Types & Settings
            item {
                QuestionTypesSectionCard(
                    subject = selectedSubject,
                    questionTypeCounts = questionTypeCounts,
                    questionTypeEnabled = questionTypeEnabled,
                    selectedSubOptions = selectedSubOptions,
                    isExpanded = areQuestionTypesExpanded,
                    onToggleExpand = { areQuestionTypesExpanded = !areQuestionTypesExpanded },
                    onCountChanged = { id, count -> viewModel.setQuestionCount(id, count) },
                    onToggleEnabled = { viewModel.toggleQuestionType(it) },
                    onSubOptionChanged = { id, opt -> viewModel.setSubOption(id, opt) }
                )
            }

            // SECTION 3: Difficulty & AI Settings
            item {
                DifficultyAndAiSettingsCard(
                    difficulty = difficulty,
                    aiQuality = aiQuality,
                    aiInstruction = aiInstruction,
                    onDifficultyChanged = { viewModel.setDifficulty(it) },
                    onQualityChanged = { viewModel.setAiQuality(it) },
                    onInstructionChanged = { viewModel.setAiInstruction(it) }
                )
            }

            // SECTION 4: Credit Usage & Generation CTA (Matching Video Box!)
            item {
                CreditStatusAndGenerateCard(
                    expectedCredits = expectedCredits,
                    userCredits = userCredits,
                    minRequiredCredits = minRequiredCredits,
                    hasSufficientCredits = hasSufficientCredits,
                    onAddCredits = onOpenCreditStore,
                    onGenerate = { viewModel.generateQuestionPaper() }
                )
                Spacer(modifier = Modifier.height(30.dp))
            }
        }

        // Generating Overlay with step animation
        if (isGenerating) {
            GenerationOverlay(currentStep = generationStep)
        }
    }
}

@Composable
fun SubjectHeroCard(
    subject: Subject,
    selectedMode: String,
    onModeChanged: (String) -> Unit,
    onChangeSubject: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Book Cover Thumbnail
                Box(
                    modifier = Modifier
                        .size(width = 72.dp, height = 96.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(8.dp))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.book_cover_english_1791427207532),
                        contentDescription = "Book Cover",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = subject.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                    Text(
                        text = "${subject.className} • ${subject.bengaliName}",
                        fontSize = 12.sp,
                        color = Color(0xFF64748B)
                    )
                    Text(
                        text = "Academic Year ${subject.academicYear}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Blue700
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Change Subject Link
                    Row(
                        modifier = Modifier
                            .clickable(onClick = onChangeSubject)
                            .padding(vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Change Subject / বিষয় পরিবর্তন",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Blue700
                        )
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = null,
                            tint = Blue700,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Tabs / Pills: "Handnote" | "Question Paper" (Matches video)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF1F5F9))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Handnote Pill
                val isHandnote = selectedMode == "Handnote"
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isHandnote) Color.White else Color.Transparent)
                        .clickable { onModeChanged("Handnote") }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Handnote",
                        fontSize = 13.sp,
                        fontWeight = if (isHandnote) FontWeight.Bold else FontWeight.Medium,
                        color = if (isHandnote) Blue800 else Color(0xFF64748B)
                    )
                }

                // Question Paper Pill
                val isQuestionPaper = selectedMode == "Question Paper"
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isQuestionPaper) Color.White else Color.Transparent)
                        .clickable { onModeChanged("Question Paper") }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Question Paper",
                        fontSize = 13.sp,
                        fontWeight = if (isQuestionPaper) FontWeight.Bold else FontWeight.Medium,
                        color = if (isQuestionPaper) Blue800 else Color(0xFF64748B)
                    )
                }
            }
        }
    }
}

@Composable
fun ChaptersSectionCard(
    subject: Subject,
    selectedChapterIds: Set<String>,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onToggleSelectAll: () -> Unit,
    onToggleChapter: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onToggleExpand),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.MenuBook,
                        contentDescription = null,
                        tint = Blue700,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Select Chapters",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "(${selectedChapterIds.size}/${subject.chapters.size})",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Blue700
                    )
                }

                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Select All Checkbox
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF8FAFC))
                    .clickable(onClick = onToggleSelectAll)
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val allSelected = selectedChapterIds.size == subject.chapters.size
                Checkbox(
                    checked = allSelected,
                    onCheckedChange = { onToggleSelectAll() },
                    colors = CheckboxDefaults.colors(checkedColor = Blue700)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Select All",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    subject.chapters.forEach { chapter ->
                        val isChecked = selectedChapterIds.contains(chapter.id)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .clickable { onToggleChapter(chapter.id) }
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = isChecked,
                                onCheckedChange = { onToggleChapter(chapter.id) },
                                colors = CheckboxDefaults.colors(checkedColor = Blue700)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${chapter.name} (p. ${chapter.pageNumber})",
                                fontSize = 13.sp,
                                color = if (isChecked) Color(0xFF0F172A) else Color(0xFF64748B),
                                fontWeight = if (isChecked) FontWeight.Medium else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QuestionTypesSectionCard(
    subject: Subject,
    questionTypeCounts: Map<String, Int>,
    questionTypeEnabled: Map<String, Boolean>,
    selectedSubOptions: Map<String, String>,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onCountChanged: (String, Int) -> Unit,
    onToggleEnabled: (String) -> Unit,
    onSubOptionChanged: (String, String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = null,
                        tint = Blue700,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Question Types & Settings",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable(onClick = onToggleExpand)
                ) {
                    Text(
                        text = if (isExpanded) "Collapse" else "Expand",
                        fontSize = 12.sp,
                        color = Blue700,
                        fontWeight = FontWeight.SemiBold
                    )
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = Blue700
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    subject.questionTypes.forEach { qType ->
                        val isEnabled = questionTypeEnabled[qType.id] ?: true
                        val count = questionTypeCounts[qType.id] ?: qType.defaultCount

                        QuestionTypeItemCard(
                            config = qType,
                            isEnabled = isEnabled,
                            count = count,
                            subOption = selectedSubOptions[qType.id] ?: qType.selectedSubOption,
                            onToggleEnabled = { onToggleEnabled(qType.id) },
                            onCountChanged = { onCountChanged(qType.id, it) },
                            onSubOptionChanged = { onSubOptionChanged(qType.id, it) }
                        )
                    }

                    // Drag Hint Text from video
                    Text(
                        text = "Drag a question type here to generate a separate paper",
                        fontSize = 11.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun QuestionTypeItemCard(
    config: QuestionTypeConfig,
    isEnabled: Boolean,
    count: Int,
    subOption: String?,
    onToggleEnabled: () -> Unit,
    onCountChanged: (Int) -> Unit,
    onSubOptionChanged: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .border(
                width = 1.dp,
                color = if (isEnabled) Color(0xFFE2E8F0) else Color(0xFFF1F5F9),
                shape = RoundedCornerShape(8.dp)
            )
            .background(if (isEnabled) Color(0xFFFAFAFA) else Color(0xFFF8FAFC))
            .padding(10.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Drag handle icon from video
                Icon(
                    imageVector = Icons.Default.DragHandle,
                    contentDescription = "Drag Handle",
                    tint = Color.Gray,
                    modifier = Modifier.size(18.dp)
                )

                Checkbox(
                    checked = isEnabled,
                    onCheckedChange = { onToggleEnabled() },
                    colors = CheckboxDefaults.colors(checkedColor = Blue700)
                )

                Text(
                    text = config.name,
                    fontSize = 13.sp,
                    fontWeight = if (isEnabled) FontWeight.SemiBold else FontWeight.Normal,
                    color = if (isEnabled) Color(0xFF0F172A) else Color.Gray,
                    modifier = Modifier.weight(1f)
                )

                // Circular count badge from video
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .clip(CircleShape)
                        .background(if (isEnabled) Color(0xFF475569) else Color(0xFFCBD5E1)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$count",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            // Slider if maxCount > minCount
            if (isEnabled && config.maxCount > config.minCount) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Slider(
                        value = count.toFloat(),
                        onValueChange = { onCountChanged(it.toInt()) },
                        valueRange = config.minCount.toFloat()..config.maxCount.toFloat(),
                        steps = config.maxCount - config.minCount - 1,
                        colors = SliderDefaults.colors(
                            thumbColor = Color(0xFF475569),
                            activeTrackColor = Color(0xFF475569)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Sub options if any (e.g. Writing E-mail / Writing letter / Writing application)
            if (isEnabled && config.subOptions.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 28.dp, top = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    config.subOptions.forEach { opt ->
                        val isSelected = opt == subOption
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSubOptionChanged(opt) }
                                .padding(vertical = 2.dp)
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = { onSubOptionChanged(opt) },
                                colors = RadioButtonDefaults.colors(selectedColor = Blue700)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = opt,
                                fontSize = 13.sp,
                                color = if (isSelected) Blue800 else Color(0xFF334155),
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DifficultyAndAiSettingsCard(
    difficulty: ExamDifficulty,
    aiQuality: AiQuality,
    aiInstruction: String,
    onDifficultyChanged: (ExamDifficulty) -> Unit,
    onQualityChanged: (AiQuality) -> Unit,
    onInstructionChanged: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Difficulty Level (Matches blue slider in video)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Question Difficulty Level",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.HelpOutline,
                    contentDescription = "Help",
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
            }

            val diffSliderValue = when (difficulty) {
                ExamDifficulty.EASY -> 0f
                ExamDifficulty.MODERATE -> 1f
                ExamDifficulty.UNIQUE -> 2f
            }

            Slider(
                value = diffSliderValue,
                onValueChange = {
                    when (it.toInt()) {
                        0 -> onDifficultyChanged(ExamDifficulty.EASY)
                        1 -> onDifficultyChanged(ExamDifficulty.MODERATE)
                        else -> onDifficultyChanged(ExamDifficulty.UNIQUE)
                    }
                },
                valueRange = 0f..2f,
                steps = 1,
                colors = SliderDefaults.colors(
                    thumbColor = Blue700,
                    activeTrackColor = Blue700
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Easy",
                    fontSize = 12.sp,
                    color = if (difficulty == ExamDifficulty.EASY) Blue700 else Color.Gray,
                    fontWeight = if (difficulty == ExamDifficulty.EASY) FontWeight.Bold else FontWeight.Normal
                )
                Text(
                    text = "Moderate",
                    fontSize = 12.sp,
                    color = if (difficulty == ExamDifficulty.MODERATE) Blue700 else Color.Gray,
                    fontWeight = if (difficulty == ExamDifficulty.MODERATE) FontWeight.Bold else FontWeight.Normal
                )
                Text(
                    text = "Unique",
                    fontSize = 12.sp,
                    color = if (difficulty == ExamDifficulty.UNIQUE) Blue700 else Color.Gray,
                    fontWeight = if (difficulty == ExamDifficulty.UNIQUE) FontWeight.Bold else FontWeight.Normal
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // AI Model Quality (Matches video Basic 1x | Standard 3x | Premium 5x)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "AI Model Quality",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.HelpOutline,
                    contentDescription = "Help",
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF1F5F9))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                AiQuality.values().forEach { q ->
                    val isSelected = q == aiQuality
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isSelected) Color.White else Color.Transparent)
                            .clickable { onQualityChanged(q) }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = q.label,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Blue800 else Color(0xFF64748B)
                            )
                            Text(
                                text = "${q.multiplier}x",
                                fontSize = 10.sp,
                                color = if (isSelected) Amber500 else Color.Gray
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Instruction to AI (এআই-এর প্রতি নির্দেশনা)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Instruction to AI (এআই-এর প্রতি নির্দেশনা)",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )
                Text(
                    text = "${aiInstruction.length}/200",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = aiInstruction,
                onValueChange = onInstructionChanged,
                placeholder = {
                    Text(
                        text = "e.g. Focus on recent Dhaka Board standard, practical grammar rules...",
                        fontSize = 12.sp,
                        color = Color.LightGray
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .testTag("ai_instruction_input"),
                shape = RoundedCornerShape(8.dp)
            )
        }
    }
}

@Composable
fun CreditStatusAndGenerateCard(
    expectedCredits: Int,
    userCredits: Int,
    minRequiredCredits: Int,
    hasSufficientCredits: Boolean,
    onAddCredits: () -> Unit,
    onGenerate: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Expected credit usage row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Expected credit usage",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF334155)
            )
            Text(
                text = "$expectedCredits credits",
                fontSize = 15.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Blue700
            )
        }

        // Blue Alert Box (Matching shortexam.com video exactly!)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF2563EB)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "This question paper is expected to use $expectedCredits credits. You need at least $minRequiredCredits credits (80%) to start, but you have $userCredits.",
                    fontSize = 14.sp,
                    color = Color.White,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = onAddCredits,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Blue700
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .testTag("add_credit_banner_button")
                ) {
                    Text(
                        text = "Add Credit",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Primary "Generate Question Paper" Button
        Button(
            onClick = onGenerate,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("generate_question_paper_button"),
            colors = ButtonDefaults.buttonColors(
                containerColor = Blue700,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Generate Question Paper",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun GenerationOverlay(currentStep: String) {
    val infiniteTransition = rememberInfiniteTransition()
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f))
            .clickable(enabled = false) {},
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(Blue100),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = Blue700,
                        modifier = Modifier
                            .size(32.dp)
                            .rotate(rotation)
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "Shortexam AI Engine",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Blue800
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = currentStep,
                    fontSize = 13.sp,
                    color = Color(0xFF64748B),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                CircularProgressIndicator(
                    modifier = Modifier.size(36.dp),
                    color = Blue700,
                    strokeWidth = 3.dp
                )
            }
        }
    }
}
