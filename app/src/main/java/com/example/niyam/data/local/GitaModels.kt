package com.example.niyam.data.local

data class Chapter(
    val chapterNumber: Int = 0,
    val versesCount: Int = 0,
    val name: String? = null,
    val translation: String? = null,
    val transliteration: String? = null,
    val summaryEn: String? = null,
    val summaryHi: String? = null
)

data class Verse(
    val chapterNumber: Int = 0,
    val verseNumber: Int = 0,
    val verseId: Int = 0,
    val slok: String? = null,
    val transliteration: String? = null,
    val wordMeanings: String? = null,
    val translations: List<Translation>? = emptyList()
)

data class Translation(
    val verseId: Int = 0,
    val language: String? = null,
    val description: String? = null,
    val authorName: String? = null
)
