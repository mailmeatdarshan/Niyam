package com.example.niyam.ui.bhajan

import android.media.MediaPlayer
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.niyam.data.local.Bhajan
import com.example.niyam.data.local.BhajanProvider
import com.example.niyam.ui.theme.SaffronPrimary
import com.example.niyam.ui.theme.SaffronSecondary
import com.example.niyam.ui.theme.SaffronTertiary
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BhajanScreen(onBackClick: () -> Unit) {
    var selectedBhajan by remember { mutableStateOf<Bhajan?>(null) }
    val context = LocalContext.current
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    var isPlaying by remember { mutableStateOf(false) }

    // Safe initialization and lifecycle management of MediaPlayer linked to selectedBhajan
    LaunchedEffect(selectedBhajan) {
        if (selectedBhajan?.audioResId != null) {
            try {
                val mp = MediaPlayer()
                val afd = context.resources.openRawResourceFd(selectedBhajan!!.audioResId!!)
                mp.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                afd.close()
                mp.setOnPreparedListener {
                    isPlaying = false
                }
                mp.setOnCompletionListener {
                    isPlaying = false
                    try {
                        mp.seekTo(0)
                    } catch (e: Exception) {}
                }
                mp.prepare()
                mediaPlayer = mp
            } catch (e: Exception) {
                e.printStackTrace()
                mediaPlayer = null
                isPlaying = false
            }
        } else {
            mediaPlayer = null
            isPlaying = false
        }
    }

    // Safe release of MediaPlayer when selectedBhajan changes or screen is disposed
    DisposableEffect(selectedBhajan) {
        onDispose {
            mediaPlayer?.let { mp ->
                try {
                    if (mp.isPlaying) {
                        mp.stop()
                    }
                } catch (e: Exception) {}
                try {
                    mp.release()
                } catch (e: Exception) {}
            }
            mediaPlayer = null
            isPlaying = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = selectedBhajan?.title ?: "Bhajans & Mantras",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (selectedBhajan != null) {
                            selectedBhajan = null
                        } else {
                            onBackClick()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        bottomBar = {
            AnimatedVisibility(
                visible = selectedBhajan != null && mediaPlayer != null,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
            ) {
                mediaPlayer?.let { mp ->
                    BhajanAudioPlayer(
                        mediaPlayer = mp,
                        isPlaying = isPlaying,
                        onPlayingChange = { isPlaying = it }
                    )
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.background.copy(alpha = 0.95f)
                        )
                    )
                )
        ) {
            if (selectedBhajan == null) {
                BhajanCategoriesList(onBhajanClick = { selectedBhajan = it })
            } else {
                selectedBhajan?.let { bhajan ->
                    BhajanDetail(bhajan = bhajan)
                }
            }
        }
    }
}

@Composable
fun BhajanCategoriesList(onBhajanClick: (Bhajan) -> Unit) {
    // Grouping bhajans by category in UI
    val categories = remember {
        listOf(
            "Hanuman Bhakti" to BhajanProvider.bhajans.filter { it.id.contains("hanuman") },
            "Shri Ram Bhakti" to BhajanProvider.bhajans.filter { it.id.contains("ram") },
            "Shiva Stutis" to BhajanProvider.bhajans.filter { it.id.contains("shiva") || it.id.contains("gauram") || it.id.contains("tandav") },
            "Sacred Mantras" to BhajanProvider.bhajans.filter { it.id.contains("mantra") || it.id.contains("mrityunjaya") }
        )
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        categories.forEach { (categoryName, bhajanList) ->
            if (bhajanList.isNotEmpty()) {
                item {
                    Text(
                        text = categoryName,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                        color = SaffronPrimary,
                        modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                    )
                }
                items(bhajanList) { bhajan ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onBhajanClick(bhajan) },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val categoryIcon = when {
                                bhajan.id.contains("hanuman") -> Icons.Default.Favorite // Gada/Heart
                                bhajan.id.contains("ram") -> Icons.Default.Star // Star/Bow
                                bhajan.id.contains("shiva") || bhajan.id.contains("gauram") || bhajan.id.contains("tandav") -> Icons.Default.Spa // Spa/Lotus/Trident
                                else -> Icons.Default.WbSunny // Sun/OM
                            }
                            
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .background(
                                        brush = Brush.linearGradient(
                                            colors = listOf(SaffronPrimary, SaffronSecondary)
                                        ),
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = categoryIcon,
                                    contentDescription = null,
                                    tint = Color.Black,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            
                            Spacer(modifier = Modifier.width(16.dp))
                            
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = bhajan.title,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = SaffronTertiary
                                )
                                Text(
                                    text = bhajan.subtitle,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Play",
                                tint = SaffronPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
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
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 120.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = bhajan.title,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = SaffronPrimary,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = bhajan.subtitle,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Text(
                        text = bhajan.content,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            lineHeight = 30.sp,
                            letterSpacing = 0.5.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun BhajanAudioPlayer(
    mediaPlayer: MediaPlayer,
    isPlaying: Boolean,
    onPlayingChange: (Boolean) -> Unit
) {
    var progress by remember { mutableFloatStateOf(0f) }
    var currentTime by remember { mutableStateOf("00:00") }
    var totalTime by remember { mutableStateOf("00:00") }

    // Periodic progress check safely
    LaunchedEffect(mediaPlayer) {
        while (true) {
            try {
                if (mediaPlayer.isPlaying) {
                    onPlayingChange(true)
                    val duration = mediaPlayer.duration
                    if (duration > 0) {
                        progress = mediaPlayer.currentPosition.toFloat() / duration.toFloat()
                        currentTime = formatMediaPlayerTime(mediaPlayer.currentPosition)
                        totalTime = formatMediaPlayerTime(duration)
                    }
                } else {
                    onPlayingChange(false)
                }
            } catch (e: Exception) {
                onPlayingChange(false)
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
                onValueChange = { newValue ->
                    progress = newValue
                    try {
                        val duration = mediaPlayer.duration
                        if (duration > 0) {
                            mediaPlayer.seekTo((newValue * duration).toInt())
                        }
                    } catch (e: Exception) {}
                },
                colors = SliderDefaults.colors(
                    thumbColor = SaffronPrimary,
                    activeTrackColor = SaffronPrimary,
                    inactiveTrackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                ),
                modifier = Modifier.height(20.dp)
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(currentTime, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(totalTime, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { 
                    try {
                        val newPos = mediaPlayer.currentPosition - 10000
                        mediaPlayer.seekTo(if (newPos > 0) newPos else 0)
                    } catch (e: Exception) {}
                }) {
                    Icon(
                        imageVector = Icons.Default.Replay10,
                        contentDescription = "Rewind 10s",
                        tint = SaffronPrimary,
                        modifier = Modifier.size(32.dp)
                    )
                }
                
                Spacer(modifier = Modifier.width(24.dp))

                FilledIconButton(
                    onClick = { 
                        try {
                            if (mediaPlayer.isPlaying) {
                                mediaPlayer.pause()
                                onPlayingChange(false)
                            } else {
                                mediaPlayer.start()
                                onPlayingChange(true)
                            }
                        } catch (e: Exception) {
                            onPlayingChange(false)
                        }
                    },
                    modifier = Modifier.size(56.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = SaffronPrimary,
                        contentColor = Color.Black
                    )
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.width(24.dp))

                IconButton(onClick = { 
                    try {
                        val duration = mediaPlayer.duration
                        val newPos = mediaPlayer.currentPosition + 10000
                        mediaPlayer.seekTo(if (newPos < duration) newPos else duration)
                    } catch (e: Exception) {}
                }) {
                    Icon(
                        imageVector = Icons.Default.Forward10,
                        contentDescription = "Forward 10s",
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
