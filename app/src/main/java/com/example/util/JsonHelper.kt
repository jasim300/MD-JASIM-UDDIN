package com.example.util

import com.example.model.GeneratedQuestion
import com.example.model.QuestionAnswer
import org.json.JSONArray
import org.json.JSONObject

object JsonHelper {

    fun questionsToJson(questions: List<GeneratedQuestion>): String {
        val array = JSONArray()
        for (q in questions) {
            val obj = JSONObject()
            obj.put("id", q.id)
            obj.put("questionNumber", q.questionNumber)
            obj.put("sectionTitle", q.sectionTitle)
            obj.put("typeName", q.typeName)
            obj.put("instruction", q.instruction)
            obj.put("marks", q.marks)
            if (q.passage != null) obj.put("passage", q.passage)
            if (q.boxClues != null) {
                val boxArr = JSONArray()
                q.boxClues.forEach { boxArr.put(it) }
                obj.put("boxClues", boxArr)
            }
            val subItemsArr = JSONArray()
            q.subItems.forEach { subItemsArr.put(it) }
            obj.put("subItems", subItemsArr)
            array.put(obj)
        }
        return array.toString()
    }

    fun jsonToQuestions(jsonString: String): List<GeneratedQuestion> {
        val list = mutableListOf<GeneratedQuestion>()
        try {
            val array = JSONArray(jsonString)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                val boxClues = if (obj.has("boxClues")) {
                    val bArr = obj.getJSONArray("boxClues")
                    val bList = mutableListOf<String>()
                    for (j in 0 until bArr.length()) bList.add(bArr.getString(j))
                    bList
                } else null

                val subItems = mutableListOf<String>()
                if (obj.has("subItems")) {
                    val sArr = obj.getJSONArray("subItems")
                    for (j in 0 until sArr.length()) subItems.add(sArr.getString(j))
                }

                list.add(
                    GeneratedQuestion(
                        id = obj.optString("id", System.currentTimeMillis().toString()),
                        questionNumber = obj.optInt("questionNumber", i + 1),
                        sectionTitle = obj.optString("sectionTitle", ""),
                        typeName = obj.optString("typeName", ""),
                        instruction = obj.optString("instruction", ""),
                        boxClues = boxClues,
                        passage = if (obj.has("passage")) obj.getString("passage") else null,
                        subItems = subItems,
                        marks = obj.optInt("marks", 10)
                    )
                )
            }
        } catch (_: Exception) { }
        return list
    }

    fun answersToJson(answers: List<QuestionAnswer>): String {
        val array = JSONArray()
        for (a in answers) {
            val obj = JSONObject()
            obj.put("questionNumber", a.questionNumber)
            obj.put("title", a.title)
            obj.put("answerText", a.answerText)
            if (a.explanation != null) obj.put("explanation", a.explanation)
            array.put(obj)
        }
        return array.toString()
    }

    fun jsonToAnswers(jsonString: String): List<QuestionAnswer> {
        val list = mutableListOf<QuestionAnswer>()
        try {
            val array = JSONArray(jsonString)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                list.add(
                    QuestionAnswer(
                        questionNumber = obj.optInt("questionNumber", i + 1),
                        title = obj.optString("title", ""),
                        answerText = obj.optString("answerText", ""),
                        explanation = if (obj.has("explanation")) obj.getString("explanation") else null
                    )
                )
            }
        } catch (_: Exception) { }
        return list
    }
}
