package com.example.niyam.ui.gita

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.niyam.data.remote.Chapter
import com.example.niyam.data.remote.Verse
import com.example.niyam.data.repository.GitaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class GitaUiState {
    object Loading : GitaUiState()
    data class Success(val chapters: List<Chapter>) : GitaUiState()
    data class Error(val message: String) : GitaUiState()
}

sealed class VerseUiState {
    object Idle : VerseUiState()
    object Loading : VerseUiState()
    data class Success(val verse: Verse) : VerseUiState()
    data class Error(val message: String) : VerseUiState()
}

@HiltViewModel
class GitaViewModel @Inject constructor(
    private val repository: GitaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<GitaUiState>(GitaUiState.Loading)
    val uiState: StateFlow<GitaUiState> = _uiState.asStateFlow()

    private val _verseState = MutableStateFlow<VerseUiState>(VerseUiState.Idle)
    val verseState: StateFlow<VerseUiState> = _verseState.asStateFlow()

    init {
        fetchChapters()
    }

    private fun fetchChapters() {
        viewModelScope.launch {
            try {
                _uiState.value = GitaUiState.Loading
                repository.getChapters()
                    .onSuccess { chapters ->
                        _uiState.value = GitaUiState.Success(chapters)
                    }
                    .onFailure { error ->
                        _uiState.value = GitaUiState.Error(error.message ?: "Unknown error")
                    }
            } catch (e: Exception) {
                _uiState.value = GitaUiState.Error(e.message ?: "Connection error")
            }
        }
    }

    fun fetchVerse(chapterNumber: Int, verseNumber: Int) {
        viewModelScope.launch {
            try {
                _verseState.value = VerseUiState.Loading
                repository.getVerse(chapterNumber, verseNumber)
                    .onSuccess { verse ->
                        _verseState.value = VerseUiState.Success(verse)
                    }
                    .onFailure { error ->
                        _verseState.value = VerseUiState.Error(error.message ?: "Unknown error")
                    }
            } catch (e: Exception) {
                _verseState.value = VerseUiState.Error(e.message ?: "Connection error")
            }
        }
    }

    fun clearVerse() {
        _verseState.value = VerseUiState.Idle
    }
}
