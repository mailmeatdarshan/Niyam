package com.example.niyam.data.repository

import com.example.niyam.data.remote.Chapter
import com.example.niyam.data.remote.GitaApi
import com.example.niyam.data.remote.Verse
import com.example.niyam.data.remote.Translation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GitaRepository @Inject constructor(
    private val api: GitaApi
) {
    private var chaptersCache: List<Chapter>? = null
    private var versesCache: Map<Int, List<Verse>>? = null // ChapterNumber to Verses
    private var translationsCache: Map<Int, List<Translation>>? = null // VerseId to Translations

    suspend fun getChapters(): Result<List<Chapter>> = withContext(Dispatchers.IO) {
        runCatching {
            chaptersCache ?: api.getAllChapters().also { chaptersCache = it }
        }
    }

    suspend fun getVerse(chapterNumber: Int, verseNumber: Int): Result<Verse> = withContext(Dispatchers.IO) {
        runCatching {
            val allVerses = if (versesCache == null) {
                api.getAllVerses().groupBy { it.chapterNumber }.also { versesCache = it }
            } else {
                versesCache!!
            }
            
            val verse = allVerses[chapterNumber]?.find { it.verseNumber == verseNumber }
                ?: throw NoSuchElementException("Verse $chapterNumber.$verseNumber not found")
            
            val allTranslations = if (translationsCache == null) {
                api.getAllTranslations().groupBy { it.verseId }.also { translationsCache = it }
            } else {
                translationsCache!!
            }
            
            verse.copy(translations = allTranslations[verse.verseId] ?: emptyList())
        }
    }
}
