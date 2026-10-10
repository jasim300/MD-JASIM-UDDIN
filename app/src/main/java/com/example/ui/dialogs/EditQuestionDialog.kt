package com.example.ui.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.GeneratedQuestion
import com.example.model.QuestionAnswer
import com.example.ui.theme.Blue700
import com.example.ui.theme.Blue800

@Composable
fun EditQuestionDialog(
    question: GeneratedQuestion,
    answer: QuestionAnswer?,
    onSave: (GeneratedQuestion, QuestionAnswer?) -> Unit,
    onDismiss: () -> Unit
) {
    var instruction by remember { mutableStateOf(question.instruction) }
    var passage by remember { mutableStateOf(question.passage ?: "") }
    var marksText by remember { mutableStateOf(question.marks.toString()) }
    var answerText by remember { mutableStateOf(answer?.answerText ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Edit Question ${question.questionNumber}",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Blue800
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = instruction,
                    onValueChange = { instruction = it },
                    label = { Text("Question Instruction") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )

                if (question.passage != null) {
                    OutlinedTextField(
                        value = passage,
                        onValueChange = { passage = it },
                        label = { Text("Passage / Content") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    )
                }

                OutlinedTextField(
                    value = marksText,
                    onValueChange = { marksText = it },
                    label = { Text("Marks") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )

                OutlinedTextField(
                    value = answerText,
                    onValueChange = { answerText = it },
                    label = { Text("Answer Key") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val updatedMarks = marksText.toIntOrNull() ?: question.marks
                    val updatedQ = question.copy(
                        instruction = instruction,
                        passage = if (passage.isNotBlank()) passage else null,
                        marks = updatedMarks
                    )
                    val updatedA = answer?.copy(answerText = answerText)
                    onSave(updatedQ, updatedA)
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Blue700)
            ) {
                Text("Save Changes")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
