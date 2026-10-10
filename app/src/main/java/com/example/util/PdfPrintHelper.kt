package com.example.util

import android.content.Context
import android.content.Intent
import android.print.PrintAttributes
import android.print.PrintManager
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.model.ExamPaper

object PdfPrintHelper {

    fun generateHtml(paper: ExamPaper, includeAnswers: Boolean = false): String {
        val sb = StringBuilder()
        sb.append("""
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="utf-8">
            <title>${paper.title}</title>
            <style>
                @page { size: A4 portrait; margin: 15mm; }
                body {
                    font-family: 'Times New Roman', 'Kalpurush', 'SutonnyMJ', serif, sans-serif;
                    font-size: 14px;
                    line-height: 1.5;
                    color: #111;
                    margin: 0;
                    padding: 10px;
                }
                .header {
                    text-align: center;
                    border-bottom: 2px solid #222;
                    padding-bottom: 8px;
                    margin-bottom: 14px;
                }
                .school-name {
                    font-size: 22px;
                    font-weight: bold;
                    text-transform: uppercase;
                    letter-spacing: 0.5px;
                    margin-bottom: 4px;
                }
                .exam-title {
                    font-size: 16px;
                    font-weight: bold;
                    margin-bottom: 4px;
                }
                .meta-row {
                    display: flex;
                    justify-content: space-between;
                    font-size: 14px;
                    font-weight: 600;
                    margin-top: 4px;
                }
                .instructions {
                    font-style: italic;
                    font-size: 12px;
                    text-align: center;
                    margin-top: 6px;
                    color: #444;
                }
                .section-header {
                    text-align: center;
                    font-weight: bold;
                    font-size: 15px;
                    text-decoration: underline;
                    margin-top: 16px;
                    margin-bottom: 10px;
                }
                .question-item {
                    margin-bottom: 16px;
                    page-break-inside: avoid;
                }
                .q-header {
                    font-weight: bold;
                    display: flex;
                    justify-content: space-between;
                }
                .clue-box {
                    border: 1px solid #444;
                    padding: 6px 10px;
                    margin: 6px 0;
                    border-radius: 4px;
                    text-align: center;
                    font-weight: 500;
                    background: #fdfdfd;
                }
                .passage {
                    text-align: justify;
                    margin: 6px 0;
                    line-height: 1.6;
                }
                .sub-list {
                    margin: 4px 0;
                    padding-left: 0;
                    list-style-type: none;
                }
                .sub-list li {
                    margin-bottom: 4px;
                }
                .answer-box {
                    background: #f8f9fa;
                    border-left: 3px solid #1E3A8A;
                    padding: 8px 12px;
                    margin-top: 12px;
                    border-radius: 2px;
                }
                .footer {
                    margin-top: 30px;
                    text-align: center;
                    font-size: 11px;
                    color: #777;
                    border-top: 1px solid #ddd;
                    padding-top: 6px;
                }
            </style>
        </head>
        <body>
            <div class="header">
                <div class="school-name">${paper.schoolName}</div>
                <div class="exam-title">${paper.title}</div>
                <div class="meta-row">
                    <span>Subject: ${paper.subjectName} (Code: ${paper.subjectCode})</span>
                    <span>Class: ${paper.className}</span>
                </div>
                <div class="meta-row">
                    <span>Time Allowed: ${paper.timeAllowed}</span>
                    <span>Full Marks: ${paper.fullMarks}</span>
                </div>
                <div class="instructions">[ ${paper.instructions} ]</div>
            </div>
        """.trimIndent())

        var currentSection = ""
        for (q in paper.questions) {
            if (q.sectionTitle.isNotBlank() && q.sectionTitle != currentSection) {
                currentSection = q.sectionTitle
                sb.append("<div class=\"section-header\">$currentSection</div>\n")
            }

            sb.append("<div class=\"question-item\">\n")
            sb.append("  <div class=\"q-header\">")
            sb.append("    <span>${q.questionNumber}. ${q.instruction}</span>")
            sb.append("    <span>${q.marks}</span>")
            sb.append("  </div>\n")

            if (!q.boxClues.isNullOrEmpty()) {
                sb.append("  <div class=\"clue-box\">[ ${q.boxClues.joinToString(" &nbsp; | &nbsp; ")} ]</div>\n")
            }

            if (!q.passage.isNullOrBlank()) {
                sb.append("  <div class=\"passage\">${q.passage}</div>\n")
            }

            if (q.subItems.isNotEmpty()) {
                sb.append("  <ul class=\"sub-list\">\n")
                for (sub in q.subItems) {
                    sb.append("    <li>$sub</li>\n")
                }
                sb.append("  </ul>\n")
            }

            sb.append("</div>\n")
        }

        if (includeAnswers && paper.answers.isNotEmpty()) {
            sb.append("<div style=\"page-break-before: always;\"></div>\n")
            sb.append("<div class=\"header\">\n")
            sb.append("  <div class=\"school-name\">${paper.schoolName}</div>\n")
            sb.append("  <div class=\"exam-title\">Answer Key & Solutions - ${paper.title}</div>\n")
            sb.append("  <div class=\"meta-row\"><span>Subject: ${paper.subjectName}</span><span>Full Marks: ${paper.fullMarks}</span></div>\n")
            sb.append("</div>\n")

            for (a in paper.answers) {
                sb.append("<div class=\"answer-box\">\n")
                sb.append("  <b>${a.title}</b><br/>\n")
                sb.append("  <div style=\"white-space: pre-wrap; margin-top: 4px;\">${a.answerText}</div>\n")
                if (!a.explanation.isNullOrBlank()) {
                    sb.append("  <div style=\"margin-top: 4px; font-size: 12px; color: #555;\"><i>Explanation: ${a.explanation}</i></div>\n")
                }
                sb.append("</div>\n")
            }
        }

        sb.append("""
            <div class="footer">
                Generated with Shortexam &bull; Authentic NCTB Question Paper &bull; Printed on: ${java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.US).format(java.util.Date())}
            </div>
        </body>
        </html>
        """.trimIndent())

        return sb.toString()
    }

    fun printOrSavePdf(context: Context, paper: ExamPaper, includeAnswers: Boolean = false) {
        val htmlContent = generateHtml(paper, includeAnswers)
        val webView = WebView(context)
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                val printManager = context.getSystemService(Context.PRINT_SERVICE) as? PrintManager
                val jobName = "Shortexam_${paper.subjectName.replace(" ", "_")}"
                val printAdapter = webView.createPrintDocumentAdapter(jobName)
                val printAttributes = PrintAttributes.Builder()
                    .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                    .setResolution(PrintAttributes.Resolution("id", "print", 300, 300))
                    .setMinMargins(PrintAttributes.Margins.NO_MARGINS)
                    .build()
                printManager?.print(jobName, printAdapter, printAttributes)
            }
        }
        webView.loadDataWithBaseURL(null, htmlContent, "text/html", "utf-8", null)
    }

    fun copyPaperAsText(paper: ExamPaper, includeAnswers: Boolean = false): String {
        val sb = StringBuilder()
        sb.append("${paper.schoolName}\n")
        sb.append("${paper.title}\n")
        sb.append("Class: ${paper.className} | Subject: ${paper.subjectName} (${paper.subjectCode})\n")
        sb.append("Time Allowed: ${paper.timeAllowed} | Full Marks: ${paper.fullMarks}\n")
        sb.append("[ ${paper.instructions} ]\n\n")

        var curSec = ""
        for (q in paper.questions) {
            if (q.sectionTitle.isNotBlank() && q.sectionTitle != curSec) {
                curSec = q.sectionTitle
                sb.append("--- $curSec ---\n\n")
            }
            sb.append("${q.questionNumber}. ${q.instruction} [${q.marks}]\n")
            if (!q.boxClues.isNullOrEmpty()) {
                sb.append("Clues: [ ${q.boxClues.joinToString(" | ")} ]\n")
            }
            if (!q.passage.isNullOrBlank()) {
                sb.append("${q.passage}\n")
            }
            for (sub in q.subItems) {
                sb.append("  $sub\n")
            }
            sb.append("\n")
        }

        if (includeAnswers && paper.answers.isNotEmpty()) {
            sb.append("\n========================================\n")
            sb.append("       ANSWER KEY & SOLUTIONS           \n")
            sb.append("========================================\n\n")
            for (a in paper.answers) {
                sb.append("${a.title}:\n")
                sb.append("${a.answerText}\n")
                if (!a.explanation.isNullOrBlank()) {
                    sb.append("Note: ${a.explanation}\n")
                }
                sb.append("\n")
            }
        }

        return sb.toString()
    }

    fun sharePaper(context: Context, paper: ExamPaper, includeAnswers: Boolean = false) {
        val text = copyPaperAsText(paper, includeAnswers)
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "Share Question Paper via")
        context.startActivity(shareIntent)
    }
}
