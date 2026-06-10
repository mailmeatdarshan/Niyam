package com.example.niyam.ui.focus

import android.content.Context
import android.media.RingtoneManager
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.niyam.ui.theme.SaffronPrimary
import com.example.niyam.ui.theme.SaffronSecondary
import com.example.niyam.ui.theme.SaffronTertiary
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FocusScreen(onBackClick: () -> Unit) {
    val context = LocalContext.current
    val sharedPreferences = remember {
        context.getSharedPreferences("niyam_focus_prefs", Context.MODE_PRIVATE)
    }

    val todayStr = remember {
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }

    var focusCount by remember {
        mutableIntStateOf(sharedPreferences.getInt("focus_$todayStr", 0))
    }

    // Timer modes
    var isFocusMode by remember { mutableStateOf(true) } // true for 25m Focus, false for 5m Break
    
    val focusTimeMs = 25 * 60 * 1000L
    val breakTimeMs = 5 * 60 * 1000L
    
    var totalTime by remember(isFocusMode) {
        mutableLongStateOf(if (isFocusMode) focusTimeMs else breakTimeMs)
    }
    
    var currentTime by remember(totalTime) { mutableLongStateOf(totalTime) }
    var isRunning by remember { mutableStateOf(false) }
    var showSessionCompleteDialog by remember { mutableStateOf(false) }

    // Persist when focus sessions increase
    LaunchedEffect(focusCount) {
        sharedPreferences.edit().putInt("focus_$todayStr", focusCount).apply()
    }

    // Countdown logic
    LaunchedEffect(currentTime, isRunning) {
        if (isRunning) {
            if (currentTime > 0) {
                delay(100L)
                currentTime -= 100L
            } else {
                isRunning = false
                if (isFocusMode) {
                    focusCount++
                }
                // Play notification beep sound
                try {
                    val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                    val r = RingtoneManager.getRingtone(context, notification)
                    r.play()
                } catch (e: Exception) {}
                showSessionCompleteDialog = true
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Focus Mode", fontWeight = FontWeight.Bold) },
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
                )
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Mode Selector tabs (only available when not running)
            AnimatedVisibility(visible = !isRunning) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    val focusSelected = isFocusMode
                    val breakSelected = !isFocusMode

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (focusSelected) SaffronPrimary else Color.Transparent)
                            .clickable {
                                isFocusMode = true
                                isRunning = false
                            }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Focus Session (25m)",
                            fontWeight = FontWeight.Bold,
                            color = if (focusSelected) Color.Black else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (breakSelected) SaffronPrimary else Color.Transparent)
                            .clickable {
                                isFocusMode = false
                                isRunning = false
                            }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Short Break (5m)",
                            fontWeight = FontWeight.Bold,
                            color = if (breakSelected) Color.Black else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Central Timer Circle
            Box(
                modifier = Modifier
                    .size(280.dp)
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                val progress = currentTime.toFloat() / totalTime.toFloat()
                val animatedProgress by animateFloatAsState(
                    targetValue = progress,
                    animationSpec = tween(durationMillis = 100),
                    label = "TimerProgress"
                )

                Canvas(modifier = Modifier.fillMaxSize()) {
                    // Back Ring
                    drawCircle(
                        color = SaffronPrimary.copy(alpha = 0.08f)
                    )
                    drawCircle(
                        color = SaffronPrimary.copy(alpha = 0.15f),
                        style = Stroke(width = 10.dp.toPx(), cap = StrokeCap.Round)
                    )
                    // Active Progress Ring
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
                    val totalSecs = currentTime / 1000
                    val mins = totalSecs / 60
                    val secs = totalSecs % 60
                    Text(
                        text = String.format(Locale.getDefault(), "%02d:%02d", mins, secs),
                        fontSize = 54.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = SaffronPrimary
                    )
                    Text(
                        text = if (isFocusMode) "FOCUSING" else "BREAK TIME",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        letterSpacing = 2.sp
                    )
                }
            }

            // Stats and Controls
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Today's completed Pomodoro sessions
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.padding(bottom = 24.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Today's Focus Sessions:",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "$focusCount completed",
                            fontWeight = FontWeight.Bold,
                            color = SaffronPrimary,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                // Buttons
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // Reset Button
                    IconButton(
                        onClick = {
                            isRunning = false
                            currentTime = totalTime
                        },
                        modifier = Modifier
                            .size(50.dp)
                            .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Reset",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    // Start/Pause Button
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
                }
            }
        }
    }

    // Session completion alert dialog
    if (showSessionCompleteDialog) {
        AlertDialog(
            onDismissRequest = { showSessionCompleteDialog = false },
            title = {
                Text(
                    text = if (isFocusMode) "Focus Session Finished! 🎉" else "Break Finished! 🔔",
                    fontWeight = FontWeight.Bold,
                    color = SaffronPrimary,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Text(
                    text = if (isFocusMode) {
                        "Congratulations! You completed a 25-minute focus session. Great job staying present and productive. Take a short break now!"
                    } else {
                        "Your break is over. Get ready to begin another focus session and clear your mind."
                    },
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSessionCompleteDialog = false
                        if (isFocusMode) {
                            isFocusMode = false // Switch to break
                        } else {
                            isFocusMode = true // Switch to focus
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary, contentColor = Color.Black)
                ) {
                    Text(
                        text = if (isFocusMode) "Start Break" else "Start Focus Session",
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showSessionCompleteDialog = false }) {
                    Text("Close")
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(20.dp)
        )
    }
}
