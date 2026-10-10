package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_exam_papers")
data class ExamPaperEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val schoolName: String,
    val className: String,
    val subjectName: String,
    val academicYear: String,
    val timeAllowed: String,
    val fullMarks: Int,
    val instructions: String,
    val questionsJson: String,
    val answersJson: String,
    val createdAt: Long = System.currentTimeMillis(),
    val creditUsed: Int = 0
)
