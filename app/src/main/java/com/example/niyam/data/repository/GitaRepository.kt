package com.example.niyam.data.repository

import com.example.niyam.data.remote.Chapter
import com.example.niyam.data.remote.GitaApi
import com.example.niyam.data.remote.Verse
import com.example.niyam.data.remote.Translation
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GitaRepository @Inject constructor(
    private val api: GitaApi
) {
    private var chaptersCache: List<Chapter>? = null
    private var versesCache: List<Verse>? = null
    private var translationsCache: List<Translation>? = null

    suspend fun getChapters(): Result<List<Chapter>> = runCatching {
        chaptersCache ?: api.getAllChapters().also { chaptersCache = it }
    }

    suspend fun getChapter(chapterNumber: Int): Result<Chapter> = runCatching {
        val chapters = chaptersCache ?: api.getAllChapters().also { chaptersCache = it }
        chapters.find { it.chapterNumber == chapterNumber } 
            ?: throw NoSuchElementException("Chapter $chapterNumber not found")
    }

    suspend fun getVerse(chapterNumber: Int, verseNumber: Int): Result<Verse> = runCatching {
        val verses = versesCache ?: api.getAllVerses().also { versesCache = it }
        val verse = verses.find { it.chapterNumber == chapterNumber && it.verseNumber == verseNumber }
            ?: throw NoSuchElementException("Verse $chapterNumber.$verseNumber not found")
        
        val translations = translationsCache ?: api.getAllTranslations().also { translationsCache = it }
        
        verse.copy(translations = translations.filter { it.verseId == verse.verseId })
    }
}
