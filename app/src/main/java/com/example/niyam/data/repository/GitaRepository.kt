package com.example.niyam.data.repository

import com.example.niyam.data.remote.Chapter
import com.example.niyam.data.remote.GitaApi
import com.example.niyam.data.remote.Verse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GitaRepository @Inject constructor(
    private val api: GitaApi
) {
    suspend fun getChapters(): Result<List<Chapter>> = runCatching {
        api.getAllChapters()
    }

    suspend fun getChapter(chapterNumber: Int): Result<Chapter> = runCatching {
        api.getChapter(chapterNumber)
    }

    suspend fun getVerse(chapterNumber: Int, verseNumber: Int): Result<Verse> = runCatching {
        api.getVerse(chapterNumber, verseNumber)
    }
}
