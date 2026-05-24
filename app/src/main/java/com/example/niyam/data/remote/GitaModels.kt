package com.example.niyam.data.remote

import com.google.gson.annotations.SerializedName

data class Chapter(
    @SerializedName("chapter_number") val chapterNumber: Int,
    @SerializedName("verses_count") val versesCount: Int,
    val name: String,
    @SerializedName("name_translation") val translation: String,
    @SerializedName("name_transliterated") val transliteration: String,
    @SerializedName("chapter_summary") val summaryEn: String,
    @SerializedName("chapter_summary_hindi") val summaryHi: String
)

data class Verse(
    @SerializedName("chapter_number") val chapterNumber: Int,
    @SerializedName("verse_number") val verseNumber: Int,
    @SerializedName("verse_id") val verseId: Int,
    @SerializedName("text") val slok: String,
    val transliteration: String,
    @SerializedName("word_meanings") val wordMeanings: String,
    val translations: List<Translation> = emptyList()
)

data class Translation(
    @SerializedName("verse_id") val verseId: Int,
    @SerializedName("lang") val language: String,
    val description: String,
    val authorName: String
)
