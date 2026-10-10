package com.example.model

enum class ExamDifficulty(val displayName: String, val multiplier: Float) {
    EASY("Easy (সহজ)", 1.0f),
    MODERATE("Moderate (মাঝারি)", 1.2f),
    UNIQUE("Unique (অনন্য ও বোর্ড মান)", 1.5f)
}

enum class AiQuality(val label: String, val multiplier: Int, val description: String) {
    BASIC("Basic", 1, "1x Credit - Standard generation"),
    STANDARD("Standard", 3, "3x Credit - Enhanced accuracy & board pattern"),
    PREMIUM("Premium", 5, "5x Credit - Deep thinking, error-free, custom notes")
}

data class Chapter(
    val id: String,
    val name: String,
    val bengaliName: String,
    val pageNumber: Int
)

data class QuestionTypeConfig(
    val id: String,
    val name: String,
    val bengaliName: String,
    val defaultCount: Int,
    val minCount: Int = 1,
    val maxCount: Int = 10,
    val unitMarks: Int = 1,
    val subOptions: List<String> = emptyList(),
    val selectedSubOption: String? = null
)

data class Subject(
    val id: String,
    val name: String,
    val bengaliName: String,
    val className: String,
    val academicYear: String = "2025-2026",
    val chapters: List<Chapter>,
    val questionTypes: List<QuestionTypeConfig>
)

data class GeneratedQuestion(
    val id: String,
    val questionNumber: Int,
    val sectionTitle: String, // e.g. "Part A: Grammar (60 Marks)"
    val typeName: String, // e.g. "Right form of Verbs"
    val instruction: String, // e.g. "Fill in the gaps with the right form of verbs from the box."
    val boxClues: List<String>? = null, // e.g. ["prepare", "be", "waste", "succeed", ...]
    val passage: String? = null, // e.g. "Time is very valuable. Those who (a)___ proper use..."
    val subItems: List<String> = emptyList(), // e.g. (a) ...(b) ... or tag questions list
    val marks: Int = 10
)

data class QuestionAnswer(
    val questionNumber: Int,
    val title: String,
    val answerText: String,
    val explanation: String? = null
)

data class ExamPaper(
    val id: Long = System.currentTimeMillis(),
    val title: String = "Half Yearly Examination - 2025",
    val schoolName: String = "Dhaka Residential Model College",
    val className: String = "Classes Nine and Ten (SSC)",
    val subjectName: String = "English 2nd Paper",
    val subjectCode: String = "108",
    val academicYear: String = "2025-2026",
    val timeAllowed: String = "3 Hours",
    val fullMarks: Int = 100,
    val instructions: String = "Figures in the right margin indicate full marks. Answer all the questions.",
    val questions: List<GeneratedQuestion> = emptyList(),
    val answers: List<QuestionAnswer> = emptyList(),
    val createdAt: Long = System.currentTimeMillis(),
    val creditUsed: Int = 64
)
