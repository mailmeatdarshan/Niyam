package com.example.niyam.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.niyam.data.local.RoutineItem
import com.example.niyam.ui.theme.SaffronPrimary
import com.example.niyam.ui.theme.SaffronLight
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    onNavigateToMeditation: () -> Unit,
    onNavigateToGita: () -> Unit,
    onNavigateToBhajan: () -> Unit,
    onNavigateToTasks: () -> Unit,
    routineViewModel: RoutineViewModel
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val routineItems by routineViewModel.routineItems.collectAsState()

    fun showNotImplementedMessage(feature: String) {
        scope.launch {
            snackbarHostState.showSnackbar("$feature coming soon!")
        }
    }

    Scaffold(
        topBar = {
            HomeTopBar(onProfileClick = { showNotImplementedMessage("Profile") })
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                GreetingSection()
            }
            
            item {
                DailyQuoteCard(onClick = { showNotImplementedMessage("Daily Quote") })
            }

            item {
                QuickActionsGrid(
                    onActionClick = { 
                        when (it) {
                            "Meditation" -> onNavigateToMeditation()
                            "Gita" -> onNavigateToGita()
                            "Bhajan" -> onNavigateToBhajan()
                            "Tasks" -> onNavigateToTasks()
                            else -> showNotImplementedMessage(it)
                        }
                    }
                )
            }

            item {
                DailyProgressSection(
                    items = routineItems,
                    onToggle = { routineViewModel.toggleTask(it) },
                    onAddTask = { routineViewModel.addTask(it) }
                )
            }
            
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(onProfileClick: () -> Unit) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                "NIYAM",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 4.sp,
                    color = SaffronPrimary
                )
            )
        },
        actions = {
            IconButton(onClick = onProfileClick) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    tint = SaffronPrimary
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}

@Composable
fun GreetingSection() {
    Column {
        Text(
            "Hari Om,",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Light,
                color = MaterialTheme.colorScheme.onBackground
            )
        )
        Text(
            "Darshan",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                color = SaffronPrimary
            )
        )
    }
}

@Composable
fun DailyQuoteCard(onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SaffronLight),
        shape = RoundedCornerShape(24.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.FormatQuote,
                contentDescription = null,
                tint = SaffronPrimary,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Karmanye vadhikaraste ma phaleshu kadachana.",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "- Bhagavad Gita",
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

@Composable
fun QuickActionsGrid(onActionClick: (String) -> Unit) {
    Column {
        Text(
            "Daily Path",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )
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
    }
}

@Composable
fun QuickActionItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = SaffronPrimary,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
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

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Today's Routine",
                style = MaterialTheme.typography.titleLarge
            )
            IconButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Task", tint = SaffronPrimary)
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            border = BorderStroke(1.dp, SaffronLight)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                if (items.isEmpty()) {
                    Text(
                        "No tasks added yet.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                } else {
                    items.forEach { item ->
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
            title = { Text("Add New Task") },
            text = {
                TextField(
                    value = taskTitle,
                    onValueChange = { taskTitle = it },
                    placeholder = { Text("e.g., Evening Puja") },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = SaffronPrimary,
                        cursorColor = SaffronPrimary
                    )
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (taskTitle.isNotBlank()) {
                            onAddTask(taskTitle)
                            showAddDialog = false
                        }
                    }
                ) {
                    Text("Add", color = SaffronPrimary)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun RoutineItemRow(item: RoutineItem, onToggle: () -> Unit) {
    Surface(
        onClick = onToggle,
        color = Color.Transparent,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = item.isCompleted,
                onCheckedChange = { onToggle() },
                colors = CheckboxDefaults.colors(checkedColor = SaffronPrimary)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = item.title,
                style = MaterialTheme.typography.bodyLarge,
                color = if (item.isCompleted) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f) else MaterialTheme.colorScheme.onSurface,
                textDecoration = if (item.isCompleted) androidx.compose.ui.text.style.TextDecoration.LineThrough else null
            )
        }
    }
}
