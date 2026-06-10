package com.example.niyam.ui.home

import android.graphics.BitmapFactory
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.niyam.data.local.RoutineItem
import com.example.niyam.ui.theme.SaffronPrimary
import com.example.niyam.ui.theme.SaffronSecondary
import com.example.niyam.ui.theme.SaffronTertiary
import java.io.File
import java.util.Calendar

@Composable
fun HomeScreen(
    onNavigateToMeditation: () -> Unit,
    onNavigateToGita: () -> Unit,
    onNavigateToBhajan: () -> Unit,
    onNavigateToTasks: () -> Unit,
    onNavigateToWater: () -> Unit,
    onNavigateToFocus: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToAbout: () -> Unit,
    userName: String,
    userPfpPath: String?,
    routineViewModel: RoutineViewModel
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val routineItems by routineViewModel.routineItems.collectAsState()

    val timeGreeting = remember {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val timeWord = when (hour) {
            in 4..11 -> "Shubhaprabhat"      // Good Morning
            in 12..16 -> "Shubhadopahar"    // Good Afternoon
            in 17..21 -> "Shubhasandhya"    // Good Evening
            else -> "Shubh Ratri"            // Good Night
        }
        val spiritualWord = when (hour % 3) {
            0 -> "Hari Om"
            1 -> "Jai Shri Krishna"
            else -> "Namaste"
        }
        "$spiritualWord, $timeWord"
    }

    val dailyQuote = remember {
        val quotes = listOf(
            Triple(
                "कर्मण्येवाधिकारस्ते मा फलेषु कदाचन ।\nमा कर्मफलहेतुर्भूर्मा ते सङ्गोऽस्त्वकर्मणि ॥",
                "Karmanye vadhikaraste ma phaleshu kadachana.",
                "Perform your duty, but do not claim the fruits of your actions. (Ch 2, Verse 47)"
            ),
            Triple(
                "यदा यदा हि धर्मस्य ग्लानिर्भवति भारत ।\nअभ्युत्थानमधर्मस्य तदात्मानं सृजाम्यहम् ॥",
                "Yada yada hi dharmasya glanir bhavati bharata.",
                "Whenever there is a decline in righteousness, I manifest Myself. (Ch 4, Verse 7)"
            ),
            Triple(
                "उद्धरेदात्मनात्मानं नात्मानमवसादयेत् ।\nआत्मैव ह्यात्मनो बन्धुरात्मैव रिपुरात्मनः ॥",
                "Uddhared atmanatmanam natmanam avasadayet.",
                "Elevate yourself by your own mind; do not degrade yourself. (Ch 6, Verse 5)"
            ),
            Triple(
                "मन्मना भव मद्भक्तो मद्याजी मां नमस्कुरु ।\nमामेवैष्यसि सत्यं ते प्रतिजाने प्रियोऽसि मे ॥",
                "Man-mana bhava mad-bhakto mad-yaji mam namaskuru.",
                "Always think of Me, become My devotee, worship Me, and bow to Me. (Ch 18, Verse 65)"
            ),
            Triple(
                "सर्वधर्मान्परित्यज्य मामेकं शरणं व्रज ।\nअहं त्वां सर्वपापेभ्यो मोक्षयिष्यामि मा शुचः ॥",
                "Sarva-dharman parityajya mam ekam saranam vraja.",
                "Abandon all varieties of duties and surrender unto Me alone. I will deliver you. (Ch 18, Verse 66)"
            )
        )
        quotes.random()
    }

    Scaffold(
        topBar = {
            HomeTopBar(
                pfpPath = userPfpPath,
                onNavigateToProfile = onNavigateToProfile,
                onNavigateToSettings = onNavigateToSettings,
                onNavigateToAbout = onNavigateToAbout
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Box(
            modifier = Modifier
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
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                contentPadding = PaddingValues(bottom = 32.dp)
            ) {
                // 1. Dynamic time-based & spiritual greeting
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                    GreetingSection(greetingText = timeGreeting, userName = userName)
                }
                
                // 2. Gita Quote Card
                item {
                    DailyQuoteCard(
                        sanskrit = dailyQuote.first,
                        transliteration = dailyQuote.second,
                        translation = dailyQuote.third
                    )
                }
 
                // 3. Quick Actions 2x3 Grid
                item {
                    QuickActionsGrid(
                        onActionClick = { action ->
                            when (action) {
                                "Meditation" -> onNavigateToMeditation()
                                "Gita" -> onNavigateToGita()
                                "Bhajan" -> onNavigateToBhajan()
                                "Tasks" -> onNavigateToTasks()
                                "Water" -> onNavigateToWater()
                                "Focus" -> onNavigateToFocus()
                            }
                        }
                    )
                }
 
                // 4. Routine completion visual section
                item {
                    DailyProgressSection(
                        items = routineItems,
                        onToggle = { routineViewModel.toggleTask(it) },
                        onAddTask = { routineViewModel.addTask(it) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    pfpPath: String?,
    onNavigateToProfile: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToAbout: () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

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

    CenterAlignedTopAppBar(
        title = {
            Text(
                "NIYAM",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 6.sp,
                    color = SaffronPrimary
                )
            )
        },
        actions = {
            Box {
                IconButton(onClick = { menuExpanded = true }) {
                    if (imageBitmap != null) {
                        Image(
                            bitmap = imageBitmap,
                            contentDescription = "Profile Menu",
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .border(BorderStroke(1.5.dp, SaffronPrimary), CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Profile Menu",
                            tint = SaffronPrimary,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
                
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Profile") },
                        onClick = {
                            menuExpanded = false
                            onNavigateToProfile()
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = SaffronPrimary
                            )
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Settings") },
                        onClick = {
                            menuExpanded = false
                            onNavigateToSettings()
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = null,
                                tint = SaffronPrimary
                            )
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("About") },
                        onClick = {
                            menuExpanded = false
                            onNavigateToAbout()
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = SaffronPrimary
                            )
                        }
                    )
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent
        )
    )
}

@Composable
fun GreetingSection(greetingText: String, userName: String) {
    Column {
        Text(
            text = greetingText,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Light,
                color = MaterialTheme.colorScheme.onBackground
            )
        )
        Text(
            text = userName.ifEmpty { "User" },
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                color = SaffronPrimary,
                letterSpacing = 1.sp
            )
        )
    }
}

@Composable
fun DailyQuoteCard(
    sanskrit: String,
    transliteration: String,
    translation: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, SaffronPrimary.copy(alpha = 0.25f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Icon(
                imageVector = Icons.Default.FormatQuote,
                contentDescription = null,
                tint = SaffronPrimary,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = sanskrit,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = SaffronTertiary,
                    lineHeight = 24.sp
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = transliteration,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = translation,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "— Bhagavad Gita",
                style = MaterialTheme.typography.labelSmall,
                color = SaffronPrimary,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

@Composable
fun QuickActionsGrid(onActionClick: (String) -> Unit) {
    Column {
        Text(
            text = "Daily Path",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge,
            color = SaffronPrimary,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // Grid Rows
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            QuickActionItem(
                icon = Icons.Default.SelfImprovement,
                label = "Meditation",
                onClick = { onActionClick("Meditation") },
                modifier = Modifier.weight(1f)
            )
            QuickActionItem(
                icon = Icons.Default.MenuBook,
                label = "Gita",
                onClick = { onActionClick("Gita") },
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            QuickActionItem(
                icon = Icons.Default.MusicNote,
                label = "Bhajan",
                onClick = { onActionClick("Bhajan") },
                modifier = Modifier.weight(1f)
            )
            QuickActionItem(
                icon = Icons.Default.DoneAll,
                label = "Tasks",
                onClick = { onActionClick("Tasks") },
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            QuickActionItem(
                icon = Icons.Default.LocalDrink,
                label = "Water Intake",
                onClick = { onActionClick("Water") },
                modifier = Modifier.weight(1f)
            )
            QuickActionItem(
                icon = Icons.Default.Timer,
                label = "Focus Mode",
                onClick = { onActionClick("Focus") },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun QuickActionItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, SaffronPrimary.copy(alpha = 0.15f))
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(SaffronPrimary.copy(alpha = 0.12f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = SaffronPrimary,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = SaffronTertiary
            )
        }
    }
}

@Composable
fun DailyProgressSection(
    items: List<RoutineItem>,
    onToggle: (RoutineItem) -> Unit,
    onAddTask: (String) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }

    val completedCount = items.count { it.isCompleted }
    val progressPercent = if (items.isNotEmpty()) (completedCount.toFloat() / items.size.toFloat() * 100).toInt() else 0

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Today's Routine",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge,
                    color = SaffronPrimary
                )
                if (items.isNotEmpty()) {
                    Text(
                        text = "$completedCount of ${items.size} activities completed ($progressPercent%)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Small progress indicator
                if (items.isNotEmpty()) {
                    CircularProgressIndicator(
                        progress = completedCount.toFloat() / items.size.toFloat(),
                        color = SaffronPrimary,
                        trackColor = SaffronPrimary.copy(alpha = 0.15f),
                        strokeWidth = 3.dp,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                }
                
                IconButton(
                    onClick = { showAddDialog = true },
                    modifier = Modifier
                        .background(SaffronPrimary.copy(alpha = 0.1f), CircleShape)
                        .size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add, 
                        contentDescription = "Add Task", 
                        tint = SaffronPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, SaffronPrimary.copy(alpha = 0.1f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                if (items.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Checklist,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                                modifier = Modifier.size(36.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "No routine tasks created yet.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    items.forEachIndexed { index, item ->
                        if (index > 0) {
                            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))
                        }
                        RoutineItemRow(
                            item = item,
                            onToggle = { onToggle(item) }
                        )
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        var taskTitle by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Add New Routine Task") },
            text = {
                OutlinedTextField(
                    value = taskTitle,
                    onValueChange = { taskTitle = it },
                    placeholder = { Text("e.g., Sandhyavandhanam") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SaffronPrimary,
                        focusedLabelColor = SaffronPrimary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (taskTitle.isNotBlank()) {
                            onAddTask(taskTitle)
                            showAddDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary, contentColor = Color.Black)
                ) {
                    Text("Add", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Cancel")
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(20.dp)
        )
    }
}

@Composable
fun RoutineItemRow(item: RoutineItem, onToggle: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(22.dp)
                .clip(CircleShape)
                .background(if (item.isCompleted) SaffronPrimary else Color.Transparent)
                .border(2.dp, SaffronPrimary, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (item.isCompleted) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = item.title,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.SemiBold,
                textDecoration = if (item.isCompleted) androidx.compose.ui.text.style.TextDecoration.LineThrough else null
            ),
            color = if (item.isCompleted) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f) else MaterialTheme.colorScheme.onSurface
        )
    }
}
