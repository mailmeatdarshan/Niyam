package com.example.niyam.ui.task

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Repeat
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.niyam.data.local.TaskItem
import com.example.niyam.data.local.TaskPriority
import com.example.niyam.data.local.TaskStatus
import com.example.niyam.ui.theme.SaffronPrimary
import com.example.niyam.ui.theme.SaffronSecondary
import com.example.niyam.ui.theme.SaffronTertiary
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
    viewModel: TaskViewModel,
    onBackClick: () -> Unit
) {
    val tasks by viewModel.allTasks.collectAsState()
    var showAddSheet by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("All") }
    
    val categories = remember { listOf("All", "Spiritual", "Health", "Study", "Personal", "General") }

    // Filter tasks based on category tab selection
    val filteredTasks = remember(tasks, selectedCategory) {
        if (selectedCategory == "All") {
            tasks
        } else {
            tasks.filter { it.category.equals(selectedCategory, ignoreCase = true) }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Task & Habit Tracker", fontWeight = FontWeight.Bold) },
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
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddSheet = true },
                containerColor = SaffronPrimary,
                contentColor = Color.Black,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        }
    ) { padding ->
        Column(
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
            // Category Tab Row
            ScrollableTabRow(
                selectedTabIndex = categories.indexOf(selectedCategory),
                edgePadding = 16.dp,
                containerColor = Color.Transparent,
                divider = {},
                indicator = { tabPositions ->
                    if (categories.indexOf(selectedCategory) in tabPositions.indices) {
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[categories.indexOf(selectedCategory)]),
                            color = SaffronPrimary
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                categories.forEach { cat ->
                    Tab(
                        selected = selectedCategory == cat,
                        onClick = { selectedCategory = cat },
                        text = {
                            Text(
                                text = cat,
                                fontWeight = if (selectedCategory == cat) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedCategory == cat) SaffronPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Task List
            if (filteredTasks.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    EmptyTasksView()
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 80.dp, top = 8.dp)
                ) {
                    items(filteredTasks, key = { it.id }) { task ->
                        TaskItemCard(
                            task = task,
                            onToggle = { viewModel.toggleTaskCompletion(task) },
                            onDelete = { viewModel.deleteTask(task) }
                        )
                    }
                }
            }
        }
    }

    if (showAddSheet) {
        AddTaskBottomSheet(
            onDismiss = { showAddSheet = false },
            onAddTask = { title, desc, priority, category, isRecurring ->
                viewModel.addTask(
                    title = title,
                    description = desc,
                    priority = priority,
                    category = category,
                    isRecurring = isRecurring
                )
                showAddSheet = false
            }
        )
    }
}

@Composable


@Composable
fun TaskItemCard(
    task: TaskItem,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    val isDone = task.status == TaskStatus.DONE

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = if (isDone) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f) else MaterialTheme.colorScheme.surface,
        tonalElevation = if (isDone) 0.dp else 2.dp,
        onClick = onToggle
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(26.dp)
                    .clip(CircleShape)
                    .background(if (isDone) SaffronPrimary else Color.Transparent)
                    .border(2.dp, SaffronPrimary, CircleShape)
                    .clickable { onToggle() },
                contentAlignment = Alignment.Center
            ) {
                if (isDone) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            textDecoration = if (isDone) TextDecoration.LineThrough else null
                        ),
                        color = if (isDone) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f) else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    
                    if (task.isRecurring) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Outlined.Repeat,
                            contentDescription = "Daily Habit",
                            tint = SaffronPrimary,
                            modifier = Modifier.size(14.dp)
                        )
                    }

                    if (task.streak > 0) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            color = SaffronPrimary.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Whatshot,
                                    contentDescription = "Streak",
                                    tint = SaffronSecondary,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = "${task.streak}d",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = SaffronPrimary
                                )
                            }
                        }
                    }
                }
                
                if (task.description.isNotBlank()) {
                    Text(
                        text = task.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
                
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 6.dp)
                ) {
                    PriorityBadge(task.priority)
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    Surface(
                        color = MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = task.category,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    if (task.dueDate != null) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            Icons.Default.CalendarToday,
                            contentDescription = null,
                            modifier = Modifier.size(12.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = SimpleDateFormat("MMM dd", Locale.getDefault()).format(Date(task.dueDate)),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Outlined.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
fun PriorityBadge(priority: TaskPriority) {
    val color = when (priority) {
        TaskPriority.HIGH -> Color(0xFFE57373)
        TaskPriority.MEDIUM -> Color(0xFFFFB74D)
        TaskPriority.LOW -> Color(0xFF81C784)
    }
    
    Surface(
        color = color.copy(alpha = 0.15f),
        shape = RoundedCornerShape(4.dp)
    ) {
        Text(
            text = priority.name,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                color = color
            )
        )
    }
}

@Composable
fun EmptyTasksView() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.Assignment,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "No tasks in this category",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            "Tap + to create a new task or habit",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskBottomSheet(
    onDismiss: () -> Unit,
    onAddTask: (String, String, TaskPriority, String, Boolean) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf(TaskPriority.MEDIUM) }
    var category by remember { mutableStateOf("Spiritual") }
    var isRecurring by remember { mutableStateOf(false) }

    val categories = remember { listOf("Spiritual", "Health", "Study", "Personal", "General") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
                .navigationBarsPadding()
        ) {
            Text(
                "New Task / Habit",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = SaffronPrimary
            )
            Spacer(modifier = Modifier.height(20.dp))
            
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("What needs to be done?") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = SaffronPrimary,
                    focusedLabelColor = SaffronPrimary
                )
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description (optional)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = SaffronPrimary,
                    focusedLabelColor = SaffronPrimary
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Category Selection
            Text("Category", style = MaterialTheme.typography.titleSmall, color = SaffronTertiary)
            Spacer(modifier = Modifier.height(6.dp))
            ScrollableTabRow(
                selectedTabIndex = categories.indexOf(category),
                edgePadding = 0.dp,
                containerColor = Color.Transparent,
                divider = {},
                indicator = {},
                modifier = Modifier.fillMaxWidth()
            ) {
                categories.forEach { cat ->
                    val selected = category == cat
                    Tab(
                        selected = selected,
                        onClick = { category = cat },
                        text = {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(if (selected) SaffronPrimary else MaterialTheme.colorScheme.surfaceVariant)
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = cat,
                                    fontWeight = FontWeight.Bold,
                                    color = if (selected) Color.Black else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Habit Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Daily Habit", style = MaterialTheme.typography.titleSmall, color = SaffronTertiary)
                    Text(
                        "Repeat this task daily to build a streak",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = isRecurring,
                    onCheckedChange = { isRecurring = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.Black,
                        checkedTrackColor = SaffronPrimary
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            
            Text("Priority", style = MaterialTheme.typography.titleSmall, color = SaffronTertiary)
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TaskPriority.entries.forEach { p ->
                    val selected = priority == p
                    val containerColor = if (selected) SaffronPrimary else MaterialTheme.colorScheme.surfaceVariant
                    val textColor = if (selected) Color.Black else MaterialTheme.colorScheme.onSurface
                    
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(containerColor)
                            .clickable { priority = p }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(p.name, fontWeight = FontWeight.Bold, color = textColor)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = { if (title.isNotBlank()) onAddTask(title, description, priority, category, isRecurring) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary, contentColor = Color.Black),
                enabled = title.isNotBlank()
            ) {
                Text("Create Task / Habit", fontWeight = FontWeight.Bold, modifier = Modifier.padding(8.dp))
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
