package com.example.niyam.data.repository

import com.example.niyam.data.local.Chapter
import com.example.niyam.data.local.Verse
import com.example.niyam.data.local.GitaDataProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GitaRepository @Inject constructor() {

    suspend fun getChapters(): Result<List<Chapter>> = withContext(Dispatchers.IO) {
        runCatching {
            GitaDataProvider.chapters
        }
    }

    suspend fun getVerse(chapterNumber: Int, verseNumber: Int): Result<Verse> = withContext(Dispatchers.IO) {
        runCatching {
            GitaDataProvider.getVerse(chapterNumber, verseNumber)
                ?: throw NoSuchElementException("Verse $chapterNumber.$verseNumber not found")
        }
    }
}
