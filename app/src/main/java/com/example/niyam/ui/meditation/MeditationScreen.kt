package com.example.niyam.ui.meditation

import android.media.MediaPlayer
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.niyam.R
import com.example.niyam.ui.theme.SaffronPrimary
import com.example.niyam.ui.theme.SaffronSecondary
import com.example.niyam.ui.theme.SaffronTertiary
import kotlinx.coroutines.delay
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeditationScreen(onBackClick: () -> Unit) {
    val context = LocalContext.current
    var totalTime by remember { mutableLongStateOf(10 * 60 * 1000L) } // 10 minutes default
    var currentTime by remember { mutableLongStateOf(totalTime) }
    var isRunning by remember { mutableStateOf(false) }
    var playMusic by remember { mutableStateOf(false) }
    var showCompletionDialog by remember { mutableStateOf(false) }

    // Breathing guide text state
    var breathingText by remember { mutableStateOf("Ready to start") }

    // MediaPlayer state
    var meditationPlayer by remember { mutableStateOf<MediaPlayer?>(null) }

    // Manage breathing guide text cycling when running
    LaunchedEffect(isRunning) {
        if (isRunning) {
            while (true) {
                breathingText = "Breathe In"
                delay(4000)
                breathingText = "Hold"
                delay(2000)
                breathingText = "Breathe Out"
                delay(4000)
                breathingText = "Hold"
                delay(2000)
            }
        } else {
            breathingText = "Focus on your breath"
        }
    }

    // Countdown logic
    LaunchedEffect(currentTime, isRunning) {
        if (isRunning) {
            if (currentTime > 0) {
                delay(100L)
                currentTime -= 100L
            } else {
                isRunning = false
                showCompletionDialog = true
            }
        }
    }

    // Play/Pause background music
    LaunchedEffect(isRunning, playMusic) {
        if (isRunning && playMusic) {
            if (meditationPlayer == null) {
                try {
                    val mp = MediaPlayer()
                    val afd = context.resources.openRawResourceFd(R.raw.hanuman_chalisa)
                    mp.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                    afd.close()
                    mp.isLooping = true
                    mp.prepare()
                    meditationPlayer = mp
                } catch (e: Exception) {
                    e.printStackTrace();
                }
            }
            try {
                meditationPlayer?.start()
            } catch (e: Exception) {}
        } else {
            try {
                if (meditationPlayer?.isPlaying == true) {
                    meditationPlayer?.pause()
                }
            } catch (e: Exception) {}
        }
    }

    // Release player on screen exit
    DisposableEffect(Unit) {
        onDispose {
            meditationPlayer?.let { mp ->
                try {
                    if (mp.isPlaying) {
                        mp.stop()
                    }
                } catch (e: Exception) {}
                try {
                    mp.release()
                } catch (e: Exception) {}
            }
            meditationPlayer = null
        }
    }

    // Pulsating animation for breathing guide circle
    val infiniteTransition = rememberInfiniteTransition(label = "PulsatingMeditation")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isRunning) 1.08f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "PulseScale"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Meditation & Breath", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
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
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.background.copy(alpha = 0.95f)
                        )
                    )
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // 1. Time Presets (Visible only when not running)
            AnimatedVisibility(
                visible = !isRunning,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Select Duration",
                        style = MaterialTheme.typography.titleSmall,
                        color = SaffronTertiary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        listOf(5, 10, 15, 20, 30).forEach { mins ->
                            val selected = totalTime == mins * 60 * 1000L
                            val containerColor = if (selected) SaffronPrimary else MaterialTheme.colorScheme.surface
                            val textColor = if (selected) Color.Black else MaterialTheme.colorScheme.onSurface
                            
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(containerColor)
                                    .clickable {
                                        totalTime = mins * 60 * 1000L
                                        currentTime = totalTime
                                    }
                                    .padding(horizontal = 14.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = "${mins}m",
                                    fontWeight = FontWeight.Bold,
                                    color = textColor,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))

                    // Fine adjustment (+/- 1 minute)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        IconButton(
                            onClick = {
                                if (totalTime > 1 * 60 * 1000L) {
                                    totalTime -= 60 * 1000L
                                    currentTime = totalTime
                                }
                            },
                            modifier = Modifier
                                .border(1.dp, SaffronPrimary.copy(alpha = 0.5f), CircleShape)
                                .size(36.dp)
                        ) {
                            Icon(Icons.Default.Remove, contentDescription = "Decrease", tint = SaffronPrimary)
                        }

                        Text(
                            text = "Adjust Minutes",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        IconButton(
                            onClick = {
                                totalTime += 60 * 1000L
                                currentTime = totalTime
                            },
                            modifier = Modifier
                                .border(1.dp, SaffronPrimary.copy(alpha = 0.5f), CircleShape)
                                .size(36.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Increase", tint = SaffronPrimary)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // 2. Animated breathing guide display circle with pulsating scale
            Box(
                modifier = Modifier
                    .size(280.dp)
                    .scale(pulseScale)
                    .align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center
            ) {
                TimerDisplay(
                    currentTime = currentTime,
                    totalTime = totalTime,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 3. Dynamic breathing prompt or session details
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)),
                modifier = Modifier.padding(horizontal = 32.dp)
            ) {
                Text(
                    text = breathingText,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = SaffronTertiary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // 4. Music and Player Controls
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Background Music Toggle Button
                IconButton(
                    onClick = { playMusic = !playMusic },
                    modifier = Modifier
                        .size(50.dp)
                        .background(
                            color = if (playMusic) SaffronPrimary.copy(alpha = 0.15f) else Color.Transparent,
                            shape = CircleShape
                        )
                        .border(1.dp, if (playMusic) SaffronPrimary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f), CircleShape)
                ) {
                    Icon(
                        imageVector = if (playMusic) Icons.Default.MusicNote else Icons.Default.MusicOff,
                        contentDescription = "Toggle Background Music",
                        tint = if (playMusic) SaffronPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(26.dp)
                    )
                }

                // Play / Pause Main Timer Button
                FilledIconButton(
                    onClick = { isRunning = !isRunning },
                    modifier = Modifier.size(76.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = SaffronPrimary,
                        contentColor = Color.Black
                    ),
                    shape = CircleShape
                ) {
                    Icon(
                        imageVector = if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isRunning) "Pause" else "Play",
                        modifier = Modifier.size(36.dp)
                    )
                }

                // Reset Button
                IconButton(
                    onClick = {
                        currentTime = totalTime
                        isRunning = false
                    },
                    modifier = Modifier
                        .size(50.dp)
                        .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Reset",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
        }
    }

    // 5. Completion Dialog
    if (showCompletionDialog) {
        AlertDialog(
            onDismissRequest = { showCompletionDialog = false },
            title = {
                Text(
                    text = "Dhyana Complete 🙏",
                    fontWeight = FontWeight.Bold,
                    color = SaffronPrimary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Text(
                    text = "You have successfully completed your meditation session. Peace be with you.",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showCompletionDialog = false
                        currentTime = totalTime // reset time
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary, contentColor = Color.Black),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Dhanyavaad (Thank you)", fontWeight = FontWeight.Bold)
                }
            },
            shape = RoundedCornerShape(20.dp),
            containerColor = MaterialTheme.colorScheme.surface
        )
    }
}

@Composable
fun TimerDisplay(
    currentTime: Long,
    totalTime: Long,
    modifier: Modifier = Modifier
) {
    val progress = currentTime.toFloat() / totalTime.toFloat()
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = progressSemanticsDecay(),
        label = "TimerProgress"
    )

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize().padding(12.dp)) {
            // Background Circle with glowing color
            drawCircle(
                color = SaffronPrimary.copy(alpha = 0.08f)
            )
            
            drawCircle(
                color = SaffronPrimary.copy(alpha = 0.15f),
                style = Stroke(width = 10.dp.toPx(), cap = StrokeCap.Round)
            )
            // Progress Arc
            drawArc(
                brush = Brush.sweepGradient(
                    colors = listOf(SaffronPrimary, SaffronSecondary, SaffronTertiary, SaffronPrimary)
                ),
                startAngle = -90f,
                sweepAngle = 360f * animatedProgress,
                useCenter = false,
                style = Stroke(width = 10.dp.toPx(), cap = StrokeCap.Round)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = formatTime(currentTime),
                fontSize = 44.sp,
                fontWeight = FontWeight.ExtraBold,
                color = SaffronPrimary
            )
            Text(
                text = "remaining",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// Custom animation spec to match progress smoothly
private fun progressSemanticsDecay(): AnimationSpec<Float> = tween(
    durationMillis = 100,
    easing = LinearEasing
)
