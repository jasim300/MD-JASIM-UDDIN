package com.example

import com.example.data.CurriculumData
import com.example.model.GeneratedQuestion
import com.example.model.QuestionAnswer
import com.example.util.JsonHelper
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun curriculum_english2nd_hasCorrectChapters() {
    assertTrue(CurriculumData.english2ndChapters.isNotEmpty())
    val nounChapter = CurriculumData.english2ndChapters.find { it.name == "The Noun" }
    assertNotNull(nounChapter)
    assertEquals(1, nounChapter?.pageNumber)
  }

  @Test
  fun jsonHelper_serialization_worksCorrectly() {
    val questions = listOf(
        GeneratedQuestion(
            id = "test_1",
            questionNumber = 1,
            sectionTitle = "Part A: Grammar",
            typeName = "Right form of Verbs",
            instruction = "Fill in the gaps with the right form of verbs.",
            boxClues = listOf("be", "take", "make"),
            passage = "Student life is the golden period...",
            marks = 10
        )
    )

    val json = JsonHelper.questionsToJson(questions)
    assertFalse(json.isEmpty())
    val parsed = JsonHelper.jsonToQuestions(json)
    assertEquals(1, parsed.size)
    assertEquals("Right form of Verbs", parsed[0].typeName)
    assertEquals(10, parsed[0].marks)
  }
}

