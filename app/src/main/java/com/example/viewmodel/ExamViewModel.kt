package com.example.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.CurriculumData
import com.example.data.db.AppDatabase
import com.example.data.db.ExamPaperEntity
import com.example.model.AiQuality
import com.example.model.ExamDifficulty
import com.example.model.ExamPaper
import com.example.model.GeneratedQuestion
import com.example.model.QuestionAnswer
import com.example.model.Subject
import com.example.service.AiQuestionGenerator
import com.example.util.JsonHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

enum class AppScreen {
    GENERATOR,
    QUESTION_PAPER_VIEW,
    HISTORY,
    CREDIT_STORE
}

enum class PaperViewTab {
    QUESTION_PAPER,
    HANDNOTE_ANSWERS
}

class ExamViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val examPaperDao = db.examPaperDao()
    private val generator = AiQuestionGenerator()
    private val prefs = application.getSharedPreferences("shortexam_prefs", Context.MODE_PRIVATE)

    // Navigation
    private val _currentScreen = MutableStateFlow(AppScreen.GENERATOR)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _paperViewTab = MutableStateFlow(PaperViewTab.QUESTION_PAPER)
    val paperViewTab: StateFlow<PaperViewTab> = _paperViewTab.asStateFlow()

    // Subject & Class
    private val _selectedClass = MutableStateFlow("Classes Nine and Ten")
    val selectedClass: StateFlow<String> = _selectedClass.asStateFlow()

    private val _selectedSubject = MutableStateFlow(CurriculumData.allSubjects[0])
    val selectedSubject: StateFlow<Subject> = _selectedSubject.asStateFlow()

    private val _selectedMode = MutableStateFlow("Question Paper") // "Question Paper" or "Handnote"
    val selectedMode: StateFlow<String> = _selectedMode.asStateFlow()

    // Chapter selections
    private val _selectedChapterIds = MutableStateFlow<Set<String>>(
        CurriculumData.english2ndChapters.map { it.id }.toSet()
    )
    val selectedChapterIds: StateFlow<Set<String>> = _selectedChapterIds.asStateFlow()

    // Question types settings
    private val _questionTypeCounts = MutableStateFlow<Map<String, Int>>(
        CurriculumData.english2ndQuestionTypes.associate { it.id to it.defaultCount }
    )
    val questionTypeCounts: StateFlow<Map<String, Int>> = _questionTypeCounts.asStateFlow()

    private val _questionTypeEnabled = MutableStateFlow<Map<String, Boolean>>(
        CurriculumData.english2ndQuestionTypes.associate { it.id to true }
    )
    val questionTypeEnabled: StateFlow<Map<String, Boolean>> = _questionTypeEnabled.asStateFlow()

    private val _selectedSubOptions = MutableStateFlow<Map<String, String>>(
        mapOf("writing_communication" to "Writing E-mail")
    )
    val selectedSubOptions: StateFlow<Map<String, String>> = _selectedSubOptions.asStateFlow()

    // AI & Difficulty settings
    private val _difficulty = MutableStateFlow(ExamDifficulty.UNIQUE)
    val difficulty: StateFlow<ExamDifficulty> = _difficulty.asStateFlow()

    private val _aiQuality = MutableStateFlow(AiQuality.BASIC)
    val aiQuality: StateFlow<AiQuality> = _aiQuality.asStateFlow()

    private val _aiInstruction = MutableStateFlow("")
    val aiInstruction: StateFlow<String> = _aiInstruction.asStateFlow()

    // Credits
    private val _userCredits = MutableStateFlow(prefs.getInt("user_credits", 80))
    val userCredits: StateFlow<Int> = _userCredits.asStateFlow()

    // Generation State
    private val _isGenerating = MutableStateFlow(false)
    val isGenerating: StateFlow<Boolean> = _isGenerating.asStateFlow()

    private val _generationStep = MutableStateFlow("")
    val generationStep: StateFlow<String> = _generationStep.asStateFlow()

    // Current Exam Paper
    private val _currentExamPaper = MutableStateFlow<ExamPaper?>(null)
    val currentExamPaper: StateFlow<ExamPaper?> = _currentExamPaper.asStateFlow()

    // School & Exam header details
    private val _schoolName = MutableStateFlow("Dhaka Residential Model College")
    val schoolName: StateFlow<String> = _schoolName.asStateFlow()

    private val _examTitle = MutableStateFlow("Half Yearly Examination - 2025")
    val examTitle: StateFlow<String> = _examTitle.asStateFlow()

    // History of saved papers
    val savedPapers = examPaperDao.getAllExamPapers().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    fun setPaperViewTab(tab: PaperViewTab) {
        _paperViewTab.value = tab
    }

    fun setSelectedMode(mode: String) {
        _selectedMode.value = mode
        if (mode == "Handnote") {
            _paperViewTab.value = PaperViewTab.HANDNOTE_ANSWERS
        } else {
            _paperViewTab.value = PaperViewTab.QUESTION_PAPER
        }
    }

    fun selectSubject(subject: Subject) {
        _selectedSubject.value = subject
        _selectedChapterIds.value = subject.chapters.map { it.id }.toSet()
        _questionTypeCounts.value = subject.questionTypes.associate { it.id to it.defaultCount }
        _questionTypeEnabled.value = subject.questionTypes.associate { it.id to true }
    }

    fun selectClass(className: String) {
        _selectedClass.value = className
    }

    fun toggleSelectAllChapters() {
        val allIds = _selectedSubject.value.chapters.map { it.id }.toSet()
        if (_selectedChapterIds.value.size == allIds.size) {
            _selectedChapterIds.value = emptySet()
        } else {
            _selectedChapterIds.value = allIds
        }
    }

    fun toggleChapter(chapterId: String) {
        val current = _selectedChapterIds.value.toMutableSet()
        if (current.contains(chapterId)) {
            current.remove(chapterId)
        } else {
            current.add(chapterId)
        }
        _selectedChapterIds.value = current
    }

    fun setQuestionCount(typeId: String, count: Int) {
        val map = _questionTypeCounts.value.toMutableMap()
        map[typeId] = count
        _questionTypeCounts.value = map
    }

    fun toggleQuestionType(typeId: String) {
        val map = _questionTypeEnabled.value.toMutableMap()
        val current = map[typeId] ?: true
        map[typeId] = !current
        _questionTypeEnabled.value = map
    }

    fun setSubOption(typeId: String, option: String) {
        val map = _selectedSubOptions.value.toMutableMap()
        map[typeId] = option
        _selectedSubOptions.value = map
    }

    fun setDifficulty(diff: ExamDifficulty) {
        _difficulty.value = diff
    }

    fun setAiQuality(quality: AiQuality) {
        _aiQuality.value = quality
    }

    fun setAiInstruction(text: String) {
        if (text.length <= 200) {
            _aiInstruction.value = text
        }
    }

    fun setSchoolName(name: String) {
        _schoolName.value = name
        _currentExamPaper.value?.let { paper ->
            _currentExamPaper.value = paper.copy(schoolName = name)
        }
    }

    fun setExamTitle(title: String) {
        _examTitle.value = title
        _currentExamPaper.value?.let { paper ->
            _currentExamPaper.value = paper.copy(title = title)
        }
    }

    // Dynamic Credit Calculation matching Shortexam logic:
    // Base credits calculated from question items * multiplier + quality cost
    fun calculateExpectedCredits(): Int {
        var basePoints = 0
        val subject = _selectedSubject.value
        for (qType in subject.questionTypes) {
            if (_questionTypeEnabled.value[qType.id] == true) {
                val count = _questionTypeCounts.value[qType.id] ?: qType.defaultCount
                basePoints += count * 4
            }
        }
        if (basePoints == 0) basePoints = 20

        val diffMultiplier = _difficulty.value.multiplier
        val qualMultiplier = _aiQuality.value.multiplier

        val total = (basePoints * diffMultiplier * qualMultiplier).roundToInt()
        return total.coerceAtLeast(15)
    }

    fun addCredits(amount: Int) {
        val newCredits = _userCredits.value + amount
        _userCredits.value = newCredits
        prefs.edit().putInt("user_credits", newCredits).apply()
    }

    fun generateQuestionPaper() {
        val expected = calculateExpectedCredits()
        // Deduct credits
        if (_userCredits.value >= expected) {
            val remaining = _userCredits.value - expected
            _userCredits.value = remaining
            prefs.edit().putInt("user_credits", remaining).apply()
        }

        viewModelScope.launch {
            _isGenerating.value = true
            _generationStep.value = "Analyzing curriculum & selected chapters..."
            delay(400)
            _generationStep.value = "Constructing board standard grammar & compositions..."
            delay(500)
            _generationStep.value = "Generating step-by-step answer key & marking rules..."
            delay(400)

            val activeCounts = _questionTypeCounts.value.filter { (typeId, _) ->
                _questionTypeEnabled.value[typeId] != false
            }

            val paper = generator.generateExamPaper(
                subject = _selectedSubject.value,
                selectedChapterIds = _selectedChapterIds.value,
                questionTypeCounts = activeCounts,
                selectedSubOptions = _selectedSubOptions.value,
                difficulty = _difficulty.value,
                aiQuality = _aiQuality.value,
                aiInstruction = _aiInstruction.value,
                schoolName = _schoolName.value,
                examTitle = _examTitle.value,
                creditUsed = expected
            )

            _currentExamPaper.value = paper
            _isGenerating.value = false
            _currentScreen.value = AppScreen.QUESTION_PAPER_VIEW

            // Auto-save to Room database
            savePaperToDb(paper)
        }
    }

    private fun savePaperToDb(paper: ExamPaper) {
        viewModelScope.launch {
            val entity = ExamPaperEntity(
                title = paper.title,
                schoolName = paper.schoolName,
                className = paper.className,
                subjectName = paper.subjectName,
                academicYear = paper.academicYear,
                timeAllowed = paper.timeAllowed,
                fullMarks = paper.fullMarks,
                instructions = paper.instructions,
                questionsJson = JsonHelper.questionsToJson(paper.questions),
                answersJson = JsonHelper.answersToJson(paper.answers),
                createdAt = paper.createdAt,
                creditUsed = paper.creditUsed
            )
            examPaperDao.insertExamPaper(entity)
        }
    }

    fun loadPaperFromEntity(entity: ExamPaperEntity) {
        val paper = ExamPaper(
            id = entity.id,
            title = entity.title,
            schoolName = entity.schoolName,
            className = entity.className,
            subjectName = entity.subjectName,
            academicYear = entity.academicYear,
            timeAllowed = entity.timeAllowed,
            fullMarks = entity.fullMarks,
            instructions = entity.instructions,
            questions = JsonHelper.jsonToQuestions(entity.questionsJson),
            answers = JsonHelper.jsonToAnswers(entity.answersJson),
            createdAt = entity.createdAt,
            creditUsed = entity.creditUsed
        )
        _schoolName.value = paper.schoolName
        _examTitle.value = paper.title
        _currentExamPaper.value = paper
        _currentScreen.value = AppScreen.QUESTION_PAPER_VIEW
    }

    fun deleteSavedPaper(id: Long) {
        viewModelScope.launch {
            examPaperDao.deleteById(id)
        }
    }

    fun updateQuestion(questionNumber: Int, updatedQuestion: GeneratedQuestion, updatedAnswer: QuestionAnswer?) {
        val curPaper = _currentExamPaper.value ?: return
        val newQuestions = curPaper.questions.map {
            if (it.questionNumber == questionNumber) updatedQuestion else it
        }
        val newAnswers = curPaper.answers.map {
            if (it.questionNumber == questionNumber && updatedAnswer != null) updatedAnswer else it
        }
        _currentExamPaper.value = curPaper.copy(
            questions = newQuestions,
            answers = newAnswers
        )
    }
}
