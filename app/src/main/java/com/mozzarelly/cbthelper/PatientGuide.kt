package com.mozzarelly.cbthelper

class PatientGuide {
    enum class Page(val url: String) {
        Intro("cbt-patient-guide"),
        OneA("chapter-1-identification/1a-what-does-it-mean-to-be-emotionally-healthy"),
        OneB("chapter-1-identification/1b-emotional-experience-vs-emotional-expression"),
        OneC("chapter-1-identification/1c-what-am-i-feeling"),
        TwoA("chapter-2-observation/2a-where-do-emotions-come-from"),
        TwoB("chapter-2-observation/2b-making-sense-of-our-senses"),
        TwoC("chapter-2-observation/2c-a-note-about-complexity"),
        ThreeA("chapter-3-evaluation/3a-which-emotions-are-healthy"),
        ThreeB("chapter-3-evaluation/3b-finding-rotten-apples"),
        ThreeC("chapter-3-evaluation/3c-common-thinking-errors"),
        ThreeD("chapter-3-evaluation/3d-irrational-behaviors"),
        FourA("chapter-4-change/4a-replacing-rotten-apples"),
        FourB1("chapter-4-change/4b-trouble-shooting/interpretations-not-perceptions"),
        FourB2("chapter-4-change/4b-trouble-shooting/emotion-first-method"),
        FourB3("chapter-4-change/4b-trouble-shooting/schemas");

        fun number() = ordinal
    }
}