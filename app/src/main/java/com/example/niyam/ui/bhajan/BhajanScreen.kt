package com.example.niyam.ui.bhajan

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.niyam.data.local.Bhajan
import com.example.niyam.data.local.BhajanProvider
import com.example.niyam.ui.theme.SaffronPrimary
import com.example.niyam.ui.theme.SaffronLight
import kotlinx.coroutines.delay

import android.media.MediaPlayer
import androidx.compose.ui.platform.LocalContext
import com.example.niyam.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BhajanScreen(onBackClick: () -> Unit) {
    var selectedBhajan by remember { mutableStateOf<Bhajan?>(null) }
    val context = LocalContext.current
    val mediaPlayer = remember { MediaPlayer() }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer.release()
        }
    }

    LaunchedEffect(selectedBhajan) {
        if (selectedBhajan?.audioResId != null) {
            mediaPlayer.reset()
            val afd = context.resources.openRawResourceFd(selectedBhajan!!.audioResId!!)
            mediaPlayer.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
            afd.close()
            mediaPlayer.prepare()
        } else {
            mediaPlayer.stop()
            mediaPlayer.reset()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(selectedBhajan?.title ?: "Bhajans", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = {
                        if (selectedBhajan != null) {
                            selectedBhajan = null
                            mediaPlayer.stop()
                            mediaPlayer.reset()
                        }
                        else onBackClick()
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            if (selectedBhajan?.audioResId != null) {
                BhajanAudioPlayer(mediaPlayer)
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (selectedBhajan == null) {
                BhajanList(onBhajanClick = { selectedBhajan = it })
            } else {
                selectedBhajan?.let { bhajan ->
                    BhajanDetail(bhajan = bhajan)
                }
            }
        }
    }
}

@Composable
fun BhajanList(onBhajanClick: (Bhajan) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(BhajanProvider.bhajans) { bhajan ->
            Card(
                modifier = Modifier.fillMaxWidth().clickable { onBhajanClick(bhajan) },
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = SaffronPrimary,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MusicNote,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(bhajan.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        Text(bhajan.subtitle, style = MaterialTheme.typography.bodySmall, color = SaffronPrimary)
                    }
                }
            }
        }
    }
}

@Composable
fun BhajanDetail(bhajan: Bhajan) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 100.dp)
    ) {
        item {
            Text(
                text = bhajan.title,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = SaffronPrimary,
                modifier = Modifier.fillMaxWidth(),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Text(
                text = bhajan.subtitle,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            
            Text(
                text = bhajan.content,
                style = MaterialTheme.typography.bodyLarge.copy(
                    lineHeight = 32.sp,
                    letterSpacing = 0.5.sp
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun BhajanAudioPlayer(mediaPlayer: MediaPlayer) {
    var isPlaying by remember { mutableStateOf(false) }
    var progress by remember { mutableFloatStateOf(0f) }
    var currentTime by remember { mutableStateOf("00:00") }
    var totalTime by remember { mutableStateOf("00:00") }

    LaunchedEffect(mediaPlayer) {
        while (true) {
            if (mediaPlayer.isPlaying) {
                isPlaying = true
                progress = mediaPlayer.currentPosition.toFloat() / mediaPlayer.duration.toFloat()
                currentTime = formatMediaPlayerTime(mediaPlayer.currentPosition)
                totalTime = formatMediaPlayerTime(mediaPlayer.duration)
            } else {
                isPlaying = false
            }
            delay(500)
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp,
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .navigationBarsPadding()
        ) {
            // Seek Bar
            Slider(
                value = progress,
                onValueChange = { 
                    progress = it
                    mediaPlayer.seekTo((it * mediaPlayer.duration).toInt())
                },
                colors = SliderDefaults.colors(
                    thumbColor = SaffronPrimary,
                    activeTrackColor = SaffronPrimary,
                    inactiveTrackColor = SaffronLight
                ),
                modifier = Modifier.height(20.dp)
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(currentTime, style = MaterialTheme.typography.labelSmall)
                Text(totalTime, style = MaterialTheme.typography.labelSmall)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { 
                    val newPos = mediaPlayer.currentPosition - 10000
                    mediaPlayer.seekTo(if (newPos > 0) newPos else 0)
                }) {
                    Icon(
                        imageVector = Icons.Default.Replay,
                        contentDescription = "Rewind",
                        tint = SaffronPrimary,
                        modifier = Modifier.size(32.dp)
                    )
                }
                
                Spacer(modifier = Modifier.width(24.dp))

                FilledIconButton(
                    onClick = { 
                        if (mediaPlayer.isPlaying) {
                            mediaPlayer.pause()
                            isPlaying = false
                        } else {
                            mediaPlayer.start()
                            isPlaying = true
                        }
                    },
                    modifier = Modifier.size(56.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(containerColor = SaffronPrimary)
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.width(24.dp))

                IconButton(onClick = { 
                    val newPos = mediaPlayer.currentPosition + 10000
                    mediaPlayer.seekTo(if (newPos < mediaPlayer.duration) newPos else mediaPlayer.duration)
                }) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Forward",
                        tint = SaffronPrimary,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}

fun formatMediaPlayerTime(milliseconds: Int): String {
    val totalSeconds = milliseconds / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}
