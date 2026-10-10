package com.example.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ExamPaperDao {
    @Query("SELECT * FROM saved_exam_papers ORDER BY createdAt DESC")
    fun getAllExamPapers(): Flow<List<ExamPaperEntity>>

    @Query("SELECT * FROM saved_exam_papers WHERE id = :id LIMIT 1")
    suspend fun getExamPaperById(id: Long): ExamPaperEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExamPaper(paper: ExamPaperEntity): Long

    @Delete
    suspend fun deleteExamPaper(paper: ExamPaperEntity)

    @Query("DELETE FROM saved_exam_papers WHERE id = :id")
    suspend fun deleteById(id: Long)
}
