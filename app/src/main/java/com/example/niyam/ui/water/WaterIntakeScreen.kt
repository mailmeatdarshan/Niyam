package com.example.niyam.ui.water

import android.content.Context
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.niyam.ui.theme.SaffronPrimary
import com.example.niyam.ui.theme.SaffronSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WaterIntakeScreen(onBackClick: () -> Unit) {
    val context = LocalContext.current
    val sharedPreferences = remember {
        context.getSharedPreferences("niyam_water_prefs", Context.MODE_PRIVATE)
    }

    val todayStr = remember {
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }

    var goalGlasses by remember { mutableIntStateOf(8) }
    var currentGlasses by remember {
        mutableIntStateOf(sharedPreferences.getInt("water_$todayStr", 0))
    }

    // Persist when glasses change
    LaunchedEffect(currentGlasses) {
        sharedPreferences.edit().putInt("water_$todayStr", currentGlasses).apply()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Water Intake Tracker", fontWeight = FontWeight.Bold) },
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
            // Top Section - Goal and stats
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Hydration Goal",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$currentGlasses / $goalGlasses Glasses",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = SaffronPrimary
                    )
                    Text(
                        text = "${currentGlasses * 250} ml / ${goalGlasses * 250} ml",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Goal Adjusters
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Daily Goal:",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        IconButton(
                            onClick = { if (goalGlasses > 1) goalGlasses-- },
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                                .size(32.dp)
                        ) {
                            Icon(Icons.Default.Remove, contentDescription = "Decrease", tint = MaterialTheme.colorScheme.onSurface)
                        }
                        Text(
                            text = goalGlasses.toString(),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                        IconButton(
                            onClick = { goalGlasses++ },
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                                .size(32.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Increase", tint = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }
            }

            // Middle Section - Interactive Fluid Glass Canvas
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                WaterCupAnimation(
                    fillRatio = currentGlasses.toFloat() / goalGlasses.toFloat(),
                    modifier = Modifier
                        .width(180.dp)
                        .height(260.dp)
                )
            }

            // Bottom Section - Controls
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Tap to record your intake",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "One glass equals 250 ml",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Minus Button
                    IconButton(
                        onClick = { if (currentGlasses > 0) currentGlasses-- },
                        modifier = Modifier
                            .size(54.dp)
                            .background(MaterialTheme.colorScheme.surface, CircleShape)
                            .border(1.dp, SaffronPrimary.copy(alpha = 0.4f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Remove Glass",
                            tint = SaffronPrimary,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    // Plus (Drink) Button
                    Button(
                        onClick = { currentGlasses++ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SaffronPrimary,
                            contentColor = Color.Black
                        ),
                        shape = CircleShape,
                        modifier = Modifier
                            .height(64.dp)
                            .width(140.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(Icons.Default.LocalDrink, contentDescription = null, modifier = Modifier.size(22.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Drink", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WaterCupAnimation(fillRatio: Float, modifier: Modifier = Modifier) {
    // Smooth transition for fill height
    val animatedFillRatio by animateFloatAsState(
        targetValue = Math.min(1.0f, Math.max(0.0f, fillRatio.toFloat())),
        animationSpec = tween(durationMillis = 800),
        label = "FluidHeight"
    )

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        
        // Define points of the glass shape
        val glassTopY = 20f
        val glassBottomY = height - 20f
        val glassLeftTopX = 25f
        val glassRightTopX = width - 25f
        val glassLeftBottomX = 50f
        val glassRightBottomX = width - 50f
        
        // 1. Draw Liquid Area first (clipped by glass inner boundaries)
        if (animatedFillRatio > 0.01f) {
            val liquidBottomY = glassBottomY - 10f
            val liquidTopY = glassBottomY - (glassBottomY - glassTopY - 20f) * animatedFillRatio
            
            // Interpolate left and right boundaries of water level
            val liquidLeftX = glassLeftBottomX - (glassLeftBottomX - glassLeftTopX) * animatedFillRatio
            val liquidRightX = glassRightBottomX + (glassRightTopX - glassRightBottomX) * animatedFillRatio
            
            val path = Path().apply {
                moveTo(liquidLeftX, liquidTopY)
                lineTo(liquidRightX, liquidTopY)
                lineTo(glassRightBottomX - 10f, liquidBottomY)
                lineTo(glassLeftBottomX + 10f, liquidBottomY)
                close()
            }
            
            // Neon-blue fluid gradient
            val fluidGradient = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF80DEEA), // Ice/light blue
                    Color(0xFF00ACC1), // Deep sky blue
                    Color(0xFF00838F)  // Teal
                ),
                startY = liquidTopY,
                endY = liquidBottomY
            )
            
            drawPath(
                path = path,
                brush = fluidGradient
            )
        }
        
        // 2. Draw Glass Silhouette Outline
        val glassOutlinePath = Path().apply {
            moveTo(glassLeftTopX, glassTopY)
            lineTo(glassLeftBottomX, glassBottomY)
            lineTo(glassRightBottomX, glassBottomY)
            lineTo(glassRightTopX, glassTopY)
        }
        
        // Translucent glow color for glass outline
        drawPath(
            path = glassOutlinePath,
            color = Color.White.copy(alpha = 0.25f),
            style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}
