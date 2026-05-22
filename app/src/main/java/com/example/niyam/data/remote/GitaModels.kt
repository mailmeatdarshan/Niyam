package com.example.niyam.data.remote

import com.google.gson.annotations.SerializedName

data class Chapter(
    @SerializedName("chapter_number") val chapterNumber: Int,
    @SerializedName("verses_count") val versesCount: Int,
    val name: String,
    val translation: String,
    val transliteration: String,
    val summary: Summary
)

data class Summary(
    val en: String,
    val hi: String
)

data class Verse(
    val chapter: Int,
    val verse: Int,
    val slok: String,
    val transliteration: String,
    val siva: TranslationAndCommentary?
)

data class TranslationAndCommentary(
    val author: String,
    val et: String? = null, // English translation
    val ec: String? = null  // English commentary
)
