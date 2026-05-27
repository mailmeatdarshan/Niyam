package com.example.niyam.data.remote

import com.google.gson.annotations.SerializedName

data class Chapter(
    @SerializedName("chapter_number") val chapterNumber: Int = 0,
    @SerializedName("verses_count") val versesCount: Int = 0,
    val name: String? = null,
    @SerializedName("name_translation") val translation: String? = null,
    @SerializedName("name_transliterated") val transliteration: String? = null,
    @SerializedName("chapter_summary") val summaryEn: String? = null,
    @SerializedName("chapter_summary_hindi") val summaryHi: String? = null
)

data class Verse(
    @SerializedName("chapter_number") val chapterNumber: Int = 0,
    @SerializedName("verse_number") val verseNumber: Int = 0,
    @SerializedName("verse_id") val verseId: Int = 0,
    @SerializedName("text") val slok: String? = null,
    val transliteration: String? = null,
    @SerializedName("word_meanings") val wordMeanings: String? = null,
    val translations: List<Translation>? = emptyList()
)

data class Translation(
    @SerializedName("verse_id") val verseId: Int = 0,
    @SerializedName("lang") val language: String? = null,
    val description: String? = null,
    val authorName: String? = null
)
