package com.example.niyam.ui.profile

import android.graphics.BitmapFactory
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.niyam.data.local.CompletionRecord
import com.example.niyam.ui.theme.SaffronPrimary
import com.example.niyam.ui.theme.SaffronSecondary
import com.example.niyam.ui.theme.SaffronTertiary
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onEditProfileClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val name by viewModel.userName.collectAsState()
    val height by viewModel.userHeight.collectAsState()
    val weight by viewModel.userWeight.collectAsState()
    val gender by viewModel.userGender.collectAsState()
    val pfpPath by viewModel.userPfpPath.collectAsState()

    val completions by viewModel.allCompletionRecords.collectAsState()
    val totalCount by viewModel.totalCompletions.collectAsState()
    val streak by viewModel.currentStreak.collectAsState()

    val imageBitmap: ImageBitmap? = remember(pfpPath) {
        try {
            if (!pfpPath.isNullOrEmpty()) {
                val file = File(pfpPath)
                if (file.exists()) {
                    BitmapFactory.decodeFile(file.absolutePath)?.asImageBitmap()
                } else null
            } else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    val levelBadge = remember(totalCount) {
        when {
            totalCount < 15 -> Pair("Sadhaka", "Level 1")
            totalCount < 50 -> Pair("Yogi", "Level 2")
            else -> Pair("Acharya", "Level 3")
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "MY PROFILE",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp,
                            color = SaffronPrimary
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = SaffronPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.background.copy(alpha = 0.95f)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Bio Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    border = BorderStroke(1.dp, SaffronPrimary.copy(alpha = 0.15f))
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // PFP Circle
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    .border(BorderStroke(1.5.dp, SaffronPrimary), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                if (imageBitmap != null) {
                                    Image(
                                        bitmap = imageBitmap,
                                        contentDescription = "Profile Picture",
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = "Default Avatar",
                                        modifier = Modifier.size(44.dp),
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(20.dp))

                            // Name & Badge Pill
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = name.ifEmpty { "User Profile" },
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                )
                                
                                // Level badge pill
                                Surface(
                                    color = SaffronPrimary.copy(alpha = 0.12f),
                                    shape = RoundedCornerShape(50.dp),
                                    border = BorderStroke(1.dp, SaffronPrimary.copy(alpha = 0.3f)),
                                    modifier = Modifier.wrapContentSize()
                                ) {
                                    Text(
                                        text = "${levelBadge.first} (${levelBadge.second})",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            color = SaffronPrimary,
                                            fontWeight = FontWeight.Bold
                                        ),
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            // Edit Button
                            IconButton(
                                onClick = onEditProfileClick,
                                modifier = Modifier
                                    .background(SaffronPrimary.copy(alpha = 0.15f), CircleShape)
                                    .size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit Profile",
                                    tint = SaffronPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        HorizontalDivider(color = SaffronPrimary.copy(alpha = 0.08f))

                        // Body Parameters (Height, Weight, Gender)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            ParamItem(label = "Gender", value = gender.ifEmpty { "N/A" })
                            ParamItem(label = "Height", value = if (height > 0f) "${height.toInt()} cm" else "N/A")
                            ParamItem(label = "Weight", value = if (weight > 0f) "${weight.toInt()} kg" else "N/A")
                        }
                    }
                }

                // Stats Cards
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Total Completions Card
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, SaffronPrimary.copy(alpha = 0.1f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Total Completed",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = totalCount.toString(),
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = SaffronPrimary
                                )
                            )
                        }
                    }

                    // Streak Card
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, SaffronPrimary.copy(alpha = 0.1f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Current Streak",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "$streak days",
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = SaffronPrimary
                                )
                            )
                        }
                    }
                }

                // Consistency Heatmap Card
                ProfileHeatmap(completions = completions)
            }
        }
    }
}

@Composable
fun ParamItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun ProfileHeatmap(completions: List<CompletionRecord>) {
    val sdf = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }

    val completionCounts = remember(completions) {
        val counts = mutableMapOf<String, Int>()
        completions.forEach { record ->
            val dateStr = sdf.format(Date(record.completedAt))
            counts[dateStr] = (counts[dateStr] ?: 0) + 1
        }
        counts
    }

    val weeks = remember {
        val list = mutableListOf<List<String>>()
        val cal = Calendar.getInstance()
        cal.add(Calendar.WEEK_OF_YEAR, -11)
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)

        for (w in 0 until 12) {
            val weekDays = mutableListOf<String>()
            for (d in 0 until 7) {
                weekDays.add(sdf.format(cal.time))
                cal.add(Calendar.DAY_OF_YEAR, 1)
            }
            list.add(weekDays)
        }
        list
    }

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(1.dp, SaffronPrimary.copy(alpha = 0.15f))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Consistency Grid",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                color = SaffronPrimary,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                weeks.forEach { week ->
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        week.forEach { dateStr ->
                            val count = completionCounts[dateStr] ?: 0
                            val color = when {
                                count == 0 -> MaterialTheme.colorScheme.surfaceVariant
                                count == 1 -> SaffronPrimary.copy(alpha = 0.2f)
                                count == 2 -> SaffronPrimary.copy(alpha = 0.4f)
                                count == 3 -> SaffronPrimary.copy(alpha = 0.6f)
                                count == 4 -> SaffronPrimary.copy(alpha = 0.8f)
                                else -> SaffronPrimary
                            }

                            Box(
                                modifier = Modifier
                                    .size(14.dp)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(color)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Grid Legend
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Less ",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                listOf(
                    0.0f, // SurfaceVariant/Empty
                    0.2f,
                    0.4f,
                    0.6f,
                    0.8f,
                    1.0f  // Fully Filled Saffron
                ).forEach { alpha ->
                    val color = if (alpha == 0f) {
                        MaterialTheme.colorScheme.surfaceVariant
                    } else {
                        SaffronPrimary.copy(alpha = alpha)
                    }
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 2.dp)
                            .size(10.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(color)
                    )
                }
                Text(
                    text = " More",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
