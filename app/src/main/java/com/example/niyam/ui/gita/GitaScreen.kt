package com.example.niyam.ui.gita

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Book
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.niyam.data.remote.Chapter
import com.example.niyam.ui.theme.SaffronPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GitaScreen(
    viewModel: GitaViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val verseState by viewModel.verseState.collectAsState()

    var selectedChapter by remember { mutableStateOf<Chapter?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(selectedChapter?.name ?: "Bhagavad Gita", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = {
                        if (selectedChapter != null) {
                            selectedChapter = null
                            viewModel.clearVerse()
                        } else {
                            onBackClick()
                        }
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (val state = uiState) {
                is GitaUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = SaffronPrimary)
                }
                is GitaUiState.Success -> {
                    if (selectedChapter == null) {
                        ChapterList(chapters = state.chapters) { chapter ->
                            selectedChapter = chapter
                        }
                    } else {
                        VerseView(
                            chapter = selectedChapter!!,
                            verseState = verseState,
                            onFetchVerse = { ch, vs -> viewModel.fetchVerse(ch, vs) }
                        )
                    }
                }
                is GitaUiState.Error -> {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center).padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ChapterList(chapters: List<Chapter>, onChapterClick: (Chapter) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(chapters) { chapter ->
            Card(
                modifier = Modifier.fillMaxWidth().clickable { onChapterClick(chapter) },
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = MaterialTheme.shapes.small,
                        color = SaffronPrimary.copy(alpha = 0.1f)
                    ) {
                        Text(
                            text = chapter.chapterNumber.toString(),
                            modifier = Modifier.padding(8.dp),
                            fontWeight = FontWeight.Bold,
                            color = SaffronPrimary
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(chapter.name ?: "Unknown Chapter", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        Text(chapter.translation ?: "", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}

@Composable
fun VerseView(
    chapter: Chapter,
    verseState: VerseUiState,
    onFetchVerse: (Int, Int) -> Unit
) {
    var verseNumber by remember(chapter.chapterNumber) { mutableIntStateOf(1) }

    LaunchedEffect(verseNumber, chapter.chapterNumber) {
        onFetchVerse(chapter.chapterNumber, verseNumber)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Verse Selector
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { if (verseNumber > 1) verseNumber-- },
                enabled = verseNumber > 1,
                colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary)
            ) {
                Text("Prev")
            }
            Text("Verse $verseNumber / ${chapter.versesCount}", fontWeight = FontWeight.Bold)
            Button(
                onClick = { if (verseNumber < chapter.versesCount) verseNumber++ },
                enabled = verseNumber < chapter.versesCount,
                colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary)
            ) {
                Text("Next")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        when (val state = verseState) {
            is VerseUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = SaffronPrimary)
                }
            }
            is VerseUiState.Success -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    item {
                        Text(
                            text = state.verse.slok ?: "",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = SaffronPrimary,
                            lineHeight = 32.sp,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = state.verse.transliteration ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        if (!state.verse.translations.isNullOrEmpty()) {
                            Text("Translations", fontWeight = FontWeight.Bold, color = SaffronPrimary, style = MaterialTheme.typography.titleMedium)
                            state.verse.translations?.forEach { trans ->
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(trans.authorName ?: "Unknown", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                                Text(trans.description ?: "", style = MaterialTheme.typography.bodyMedium)
                            }
                        }

                        if (!state.verse.wordMeanings.isNullOrBlank()) {
                            Spacer(modifier = Modifier.height(24.dp))
                            Text("Word Meanings", fontWeight = FontWeight.Bold, color = SaffronPrimary, style = MaterialTheme.typography.titleMedium)
                            Text(state.verse.wordMeanings ?: "", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
            is VerseUiState.Error -> {
                Text(state.message, color = MaterialTheme.colorScheme.error)
            }
            else -> {}
        }
    }
}
