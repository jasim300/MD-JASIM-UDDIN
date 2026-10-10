package com.example.data

import com.example.model.Chapter
import com.example.model.QuestionTypeConfig
import com.example.model.Subject

object CurriculumData {

    val availableClasses = listOf(
        "Classes Nine and Ten",
        "Class Eight (JSC)",
        "Class Seven",
        "Class Six",
        "Class Eleven and Twelve (HSC)"
    )

    // English 2nd Paper (Matches shortexam.com video exactly)
    val english2ndChapters = listOf(
        Chapter("en2_1", "The Noun", "বিশেষ্য পদ", 1),
        Chapter("en2_2", "Pronouns and Possessives", "সর্বনাম ও সম্বন্ধবাচক", 14),
        Chapter("en2_3", "Adjectives", "বিশেষণ পদ", 22),
        Chapter("en2_4", "Verbs and Tenses", "ক্রিয়া ও কাল", 33),
        Chapter("en2_5", "Verbs: Modals", "মডেল অক্সিলিয়ারি", 74),
        Chapter("en2_6", "Kinds of Verbs", "ক্রিয়ার প্রকারভেদ", 97),
        Chapter("en2_7", "The Adverb", "ক্রিয়া বিশেষণ", 111),
        Chapter("en2_8", "The Preposition", "পদান্বয়ী অব্যয়", 118),
        Chapter("en2_9", "Sentences", "বাক্যের গঠন ও প্রকার", 126),
        Chapter("en2_10", "Introductory 'it' & 'there'", "সূচনামূলক 'it' ও 'there'", 167),
        Chapter("en2_11", "Conditionals", "শর্তবাচক বাক্য", 172),
        Chapter("en2_12", "The Passive", "কর্মবাচ্য (ভয়েস)", 180),
        Chapter("en2_13", "Speech", "উক্তি পরিবর্তন (ন্যারেশন)", 191),
        Chapter("en2_14", "Letters", "চিঠিপত্র লিখন", 209),
        Chapter("en2_15", "CV & Cover Letter", "সিভি ও কাভার লেটার", 223),
        Chapter("en2_16", "E-mails", "ই-মেইল লিখন", 232),
        Chapter("en2_17", "Short Composition: Paragraphs", "অনুচ্ছেদ রচনা", 237),
        Chapter("en2_18", "Completing Stories", "গল্প সম্পন্নকরণ", 273),
        Chapter("en2_19", "Writing Summaries", "সারাংশ লিখন", 277),
        Chapter("en2_20", "Describing Graphs and Charts", "গ্রাফ ও চার্ট বর্ণনা", 287),
        Chapter("en2_21", "Answer key", "উত্তরমালা", 300),
        Chapter("en2_22", "Sample Question", "নমুনা প্রশ্নপত্র", 302)
    )

    val english2ndQuestionTypes = listOf(
        QuestionTypeConfig(
            id = "gap_clues",
            name = "Gap filling with clues",
            bengaliName = "ক্লু সহ শূন্যস্থান পূরণ",
            defaultCount = 10,
            minCount = 5,
            maxCount = 10,
            unitMarks = 1
        ),
        QuestionTypeConfig(
            id = "right_form_verbs",
            name = "Right form of Verbs",
            bengaliName = "ক্রিয়ার সঠিক রূপ ব্যবহার",
            defaultCount = 10,
            minCount = 5,
            maxCount = 10,
            unitMarks = 1
        ),
        QuestionTypeConfig(
            id = "changing_sentences",
            name = "Changing sentences",
            bengaliName = "বাক্য রূপান্তর (ভয়েস, ডিগ্রি, সিম্পল-কমপ্লেক্স)",
            defaultCount = 10,
            minCount = 5,
            maxCount = 10,
            unitMarks = 1
        ),
        QuestionTypeConfig(
            id = "tag_questions",
            name = "Tag questions",
            bengaliName = "ট্যাগ কোয়েশ্চেন",
            defaultCount = 5,
            minCount = 5,
            maxCount = 7,
            unitMarks = 1
        ),
        QuestionTypeConfig(
            id = "suffixes_prefixes",
            name = "Suffixes and Prefixes",
            bengaliName = "উপসর্গ ও প্রত্যয় যোগে শব্দ গঠন",
            defaultCount = 5,
            minCount = 5,
            maxCount = 10,
            unitMarks = 1
        ),
        QuestionTypeConfig(
            id = "preposition",
            name = "Preposition",
            bengaliName = "উপযুক্ত প্রেপোজিশন বসানো",
            defaultCount = 5,
            minCount = 5,
            maxCount = 10,
            unitMarks = 1
        ),
        QuestionTypeConfig(
            id = "connectors",
            name = "Connectors / Linkers",
            bengaliName = "বাক্য সংযোগকারী শব্দ",
            defaultCount = 5,
            minCount = 5,
            maxCount = 10,
            unitMarks = 1
        ),
        QuestionTypeConfig(
            id = "punctuations",
            name = "Punctuation and Capitalization",
            bengaliName = "বিরামচিহ্ন ও বড় হাতের অক্ষর প্রয়োগ",
            defaultCount = 1,
            minCount = 1,
            maxCount = 5,
            unitMarks = 5
        ),
        QuestionTypeConfig(
            id = "writing_paragraph",
            name = "Writing paragraph",
            bengaliName = "অনুচ্ছেদ রচনা (প্যারাগ্রাফ)",
            defaultCount = 1,
            minCount = 1,
            maxCount = 2,
            unitMarks = 10
        ),
        QuestionTypeConfig(
            id = "writing_communication",
            name = "Writing- E-mail / Letter / Application",
            bengaliName = "ইমেইল / চিঠি / আবেদনপত্র লিখন",
            defaultCount = 1,
            minCount = 1,
            maxCount = 2,
            unitMarks = 10,
            subOptions = listOf("Writing E-mail", "Writing letter", "Writing application"),
            selectedSubOption = "Writing E-mail"
        ),
        QuestionTypeConfig(
            id = "writing_composition",
            name = "Writing short composition / Story",
            bengaliName = "সংক্ষিপ্ত রচনা / গল্প লিখন",
            defaultCount = 1,
            minCount = 1,
            maxCount = 2,
            unitMarks = 10
        )
    )

    // English 1st Paper
    val english1stChapters = listOf(
        Chapter("en1_1", "Unit 1: Good Citizens", "সুনাগরিক", 1),
        Chapter("en1_2", "Unit 2: Pastimes", "অবসর যাপন", 18),
        Chapter("en1_3", "Unit 3: Events and Festivals", "অনুষ্ঠান ও উৎসব", 34),
        Chapter("en1_4", "Unit 4: Are We Aware?", "আমরা কি সচেতন?", 49),
        Chapter("en1_5", "Unit 5: Nature and Environment", "প্রকৃতি ও পরিবেশ", 62),
        Chapter("en1_6", "Unit 7: People Who Stand Out", "অনন্য ব্যক্তিত্ব", 88),
        Chapter("en1_7", "Unit 8: World Heritage", "বিশ্ব ঐতিহ্য", 102),
        Chapter("en1_8", "Unit 10: Dreams", "স্বপ্ন ও আকাঙ্ক্ষা", 130),
        Chapter("en1_9", "Unit 11: Renewable Energy", "নবায়নযোগ্য শক্তি", 145),
        Chapter("en1_10", "Unit 12: Roots", "শিকড় ও ঐতিহ্য", 160)
    )

    val english1stQuestionTypes = listOf(
        QuestionTypeConfig("en1_mcq", "Multiple Choice Questions (MCQ)", "বহুনির্বাচনী প্রশ্ন", 7, 5, 10, 1),
        QuestionTypeConfig("en1_short_q", "Answering Questions", "প্রশ্নের উত্তর দান", 5, 4, 6, 2),
        QuestionTypeConfig("en1_summary", "Summary Writing", "সারাংশ লিখন", 1, 1, 1, 10),
        QuestionTypeConfig("en1_cloze_no_clues", "Cloze Test without Clues", "ক্লু ছাড়া শূন্যস্থান পূরণ", 10, 5, 10, 1),
        QuestionTypeConfig("en1_rearrange", "Rearranging Sentences", "বাক্য পুনর্গঠন", 1, 1, 1, 10),
        QuestionTypeConfig("en1_paragraph", "Paragraph Writing", "অনুচ্ছেদ লিখন", 1, 1, 1, 10),
        QuestionTypeConfig("en1_story", "Completing a Story", "গল্প সম্পন্নকরণ", 1, 1, 1, 10)
    )

    // Bangla 2nd Paper
    val bangla2ndChapters = listOf(
        Chapter("bn2_1", "ভাষা ও বাংলা ভাষা", "ভাষা তত্ত্ব", 1),
        Chapter("bn2_2", "ধ্বনি ও বর্ণ", "ধ্বনিবিজ্ঞান", 12),
        Chapter("bn2_3", "সন্ধি", "সন্ধি প্রকরণ", 26),
        Chapter("bn2_4", "শব্দ গঠন ও উপসর্গ", "শব্দতত্ত্ব", 42),
        Chapter("bn2_5", "সমাস", "সমাস প্রকরণ", 58),
        Chapter("bn2_6", "কারক ও বিভক্তি", "কারক তত্ত্ব", 76),
        Chapter("bn2_7", "বাক্য ও বাক্য রূপান্তর", "বাক্য প্রকরণ", 95),
        Chapter("bn2_8", "বিরামচিহ্ন প্রয়োগ", "যতি বা বিরামচিহ্ন", 112),
        Chapter("bn2_9", "বানানের নিয়ম", "বাংলা একাডেমি বানানরীতি", 124),
        Chapter("bn2_10", "ভাবসম্প্রসারণ", "ভাবসম্প্রসারণ", 150),
        Chapter("bn2_11", "অনুচ্ছেদ রচনা", "অনুচ্ছেদ লিখন", 175),
        Chapter("bn2_12", "আবেদনপত্র ও প্রতিবেদন", "পত্র লিখন", 195)
    )

    val bangla2ndQuestionTypes = listOf(
        QuestionTypeConfig("bn2_dhoni", "ধ্বনি ও বর্ণের পরিবর্তন", "ধ্বনি ও উচ্চারণ", 5, 5, 10, 1),
        QuestionTypeConfig("bn2_shondhi", "সন্ধি বিচ্ছেদ / তৈরি", "সন্ধি নির্ণয়", 5, 5, 10, 1),
        QuestionTypeConfig("bn2_karok", "কারক ও বিভক্তি নির্ণয়", "কারক-বিভক্তি", 5, 5, 10, 1),
        QuestionTypeConfig("bn2_shomash", "ব্যাসবাক্যসহ সমাস নির্ণয়", "সমাস নির্ণয়", 5, 5, 10, 1),
        QuestionTypeConfig("bn2_bakyo", "বাক্য রূপান্তর (সরল, জটিল, যৌগিক)", "বাক্য রূপান্তর", 5, 5, 10, 1),
        QuestionTypeConfig("bn2_shuddhikoron", "শুদ্ধিকরণ ও বানান সংশোধন", "বানান শুদ্ধিকরণ", 5, 5, 10, 1),
        QuestionTypeConfig("bn2_bhabshompro", "ভাবসম্প্রসারণ লিখন", "ভাবসম্প্রসারণ", 1, 1, 2, 10),
        QuestionTypeConfig("bn2_abedon", "আবেদনপত্র / দাপ্তরিক পত্র", "পত্র লিখন", 1, 1, 2, 10),
        QuestionTypeConfig("bn2_protibedon", "সংবাদ প্রতিবেদন রচনা", "প্রতিবেদন লিখন", 1, 1, 2, 10)
    )

    // Mathematics
    val mathChapters = listOf(
        Chapter("math_1", "বাস্তব সংখ্যা (Real Numbers)", "বাস্তব সংখ্যা", 1),
        Chapter("math_2", "সেট ও ফাংশন (Set & Function)", "সেট ও ফাংশন", 20),
        Chapter("math_3", "বীজগাণিতিক রাশি (Algebraic Expressions)", "বীজগাণিতিক রাশি", 42),
        Chapter("math_4", "সূচক ও লগারিদম (Exponents & Logarithms)", "সূচক ও লগারিদম", 75),
        Chapter("math_5", "এক চলকবিশিষ্ট সমীকরণ (Linear Equations)", "সমীকরণ", 92),
        Chapter("math_6", "রেখা, কোণ ও ত্রিভুজ (Geometry)", "জ্যামিতি", 110),
        Chapter("math_7", "ব্যবহারিক জ্যামিতি (Practical Geometry)", "ব্যবহারিক জ্যামিতি", 135),
        Chapter("math_8", "বৃত্ত (Circle)", "বৃত্ত সংক্রান্ত উপপাদ্য", 152),
        Chapter("math_9", "ত্রিকোণমিতিক অনুপাত (Trigonometry)", "ত্রিকোণমিতি", 175),
        Chapter("math_10", "দূরত্ব ও উচ্চতা (Distance & Elevation)", "দূরত্ব ও উচ্চতা", 201),
        Chapter("math_11", "বীজগাণিতিক অনুপাত ও সমানুপাত", "অনুপাত", 215),
        Chapter("math_16", "পরিমিতি (Mensuration)", "ক্ষেত্রফল ও পরিমিতি", 295),
        Chapter("math_17", "পরিসংখ্যান (Statistics)", "গড়, মধ্যক, প্রচুরক", 325)
    )

    val mathQuestionTypes = listOf(
        QuestionTypeConfig("math_cq_alg", "সৃজনশীল প্রশ্ন: বীজগণিত (CQ)", "বীজগণিত সৃজনশীল", 2, 1, 3, 10),
        QuestionTypeConfig("math_cq_geo", "সৃজনশীল প্রশ্ন: জ্যামিতি (CQ)", "জ্যামিতি সৃজনশীল", 2, 1, 3, 10),
        QuestionTypeConfig("math_cq_trig", "সৃজনশীল প্রশ্ন: ত্রিকোণমিতি ও পরিমিতি", "ত্রিকোণমিতি সৃজনশীল", 2, 1, 3, 10),
        QuestionTypeConfig("math_cq_stat", "সৃজনশীল প্রশ্ন: পরিসংখ্যান (CQ)", "পরিসংখ্যান সৃজনশীল", 1, 1, 2, 10),
        QuestionTypeConfig("math_mcq", "বহুনির্বাচনী প্রশ্ন (MCQ)", "বহুনির্বাচনী প্রশ্ন", 30, 10, 30, 1)
    )

    // ICT
    val ictChapters = listOf(
        Chapter("ict_1", "তথ্য ও যোগাযোগ প্রযুক্তি এবং আমাদের বাংলাদেশ", "প্রথম অধ্যায়", 1),
        Chapter("ict_2", "কম্পিউটার ও কম্পিউটার ব্যবহারকারীর নিরাপত্তা", "দ্বিতীয় অধ্যায়", 18),
        Chapter("ict_3", "আমার শিক্ষায় ইন্টারনেট ও ডিজিটাল কনটেন্ট", "তৃতীয় অধ্যায়", 43),
        Chapter("ict_4", "আমার লেখালেখি ও হিসাব (Word & Excel)", "চতুর্থ অধ্যায়", 55),
        Chapter("ict_5", "মাল্টিমিডিয়া ও গ্রাফিক্স (PowerPoint & Photoshop)", "পঞ্চম অধ্যায়", 68),
        Chapter("ict_6", "ডেটাবেজ-এর ব্যবহার (Database Management)", "ষষ্ঠ অধ্যায়", 87)
    )

    val ictQuestionTypes = listOf(
        QuestionTypeConfig("ict_mcq", "বহুনির্বাচনী প্রশ্ন (MCQ)", "MCQ প্রশ্ন", 25, 10, 25, 1),
        QuestionTypeConfig("ict_short", "সংক্ষিপ্ত জ্ঞান ও অনুধাবনমূলক প্রশ্ন", "সংক্ষিপ্ত প্রশ্ন", 5, 2, 8, 2),
        QuestionTypeConfig("ict_cq", "সৃজনশীল প্রশ্ন (CQ)", "সৃজনশীল প্রয়োগ", 2, 1, 4, 10)
    )

    val allSubjects = listOf(
        Subject(
            id = "sub_eng_2",
            name = "English 2nd Paper",
            bengaliName = "ইংরেজি ২য় পত্র",
            className = "Classes Nine and Ten",
            chapters = english2ndChapters,
            questionTypes = english2ndQuestionTypes
        ),
        Subject(
            id = "sub_eng_1",
            name = "English 1st Paper",
            bengaliName = "ইংরেজি ১ম পত্র",
            className = "Classes Nine and Ten",
            chapters = english1stChapters,
            questionTypes = english1stQuestionTypes
        ),
        Subject(
            id = "sub_bangla_2",
            name = "Bangla 2nd Paper",
            bengaliName = "বাংলা ২য় পত্র",
            className = "Classes Nine and Ten",
            chapters = bangla2ndChapters,
            questionTypes = bangla2ndQuestionTypes
        ),
        Subject(
            id = "sub_math",
            name = "General Mathematics",
            bengaliName = "সাধারণ গণিত",
            className = "Classes Nine and Ten",
            chapters = mathChapters,
            questionTypes = mathQuestionTypes
        ),
        Subject(
            id = "sub_ict",
            name = "ICT (Information & Communication Tech)",
            bengaliName = "তথ্য ও যোগাযোগ প্রযুক্তি",
            className = "Classes Nine and Ten",
            chapters = ictChapters,
            questionTypes = ictQuestionTypes
        )
    )
}
