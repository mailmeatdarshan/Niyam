package com.example.niyam.ui.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.niyam.data.local.TaskItem
import com.example.niyam.data.local.TaskPriority
import com.example.niyam.data.local.TaskStatus
import com.example.niyam.data.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    val allTasks: StateFlow<List<TaskItem>> = repository.allTasks
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addTask(
        title: String,
        description: String = "",
        priority: TaskPriority = TaskPriority.MEDIUM,
        dueDate: Long? = null,
        category: String = "General",
        isRecurring: Boolean = false
    ) {
        viewModelScope.launch {
            try {
                repository.insert(
                    TaskItem(
                        title = title,
                        description = description,
                        priority = priority,
                        dueDate = dueDate,
                        category = category,
                        isRecurring = isRecurring
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateTaskStatus(task: TaskItem, status: TaskStatus) {
        val completedAt = if (status == TaskStatus.DONE) System.currentTimeMillis() else null
        viewModelScope.launch {
            repository.update(task.copy(status = status, completedAt = completedAt))
        }
    }

    fun toggleTaskCompletion(task: TaskItem) {
        viewModelScope.launch {
            val isDone = task.status == TaskStatus.DONE
            val newStatus = if (isDone) TaskStatus.TODO else TaskStatus.DONE
            val newCompletedAt = if (newStatus == TaskStatus.DONE) System.currentTimeMillis() else null
            
            val newStreak = if (newStatus == TaskStatus.DONE && task.isRecurring) {
                val lastCompleted = task.completedAt
                if (lastCompleted != null) {
                    val lastCal = Calendar.getInstance().apply { timeInMillis = lastCompleted }
                    val currentCal = Calendar.getInstance()
                    
                    // Subtract 1 day from today to check if lastCompleted was yesterday
                    currentCal.add(Calendar.DAY_OF_YEAR, -1)
                    
                    val wasYesterday = lastCal.get(Calendar.YEAR) == currentCal.get(Calendar.YEAR) &&
                            lastCal.get(Calendar.DAY_OF_YEAR) == currentCal.get(Calendar.DAY_OF_YEAR)
                    
                    val isSameDay = lastCal.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR) &&
                            lastCal.get(Calendar.DAY_OF_YEAR) == Calendar.getInstance().get(Calendar.DAY_OF_YEAR)

                    when {
                        isSameDay -> task.streak // Already completed today, maintain streak
                        wasYesterday -> task.streak + 1 // Consecutive day, increment
                        else -> 1 // Broken streak, reset to 1
                    }
                } else {
                    1 // First completion, start at 1
                }
            } else if (newStatus == TaskStatus.TODO) {
                0
            } else {
                task.streak
            }

            repository.update(
                task.copy(
                    status = newStatus,
                    completedAt = newCompletedAt,
                    streak = newStreak
                )
            )
        }
    }

    fun deleteTask(task: TaskItem) {
        viewModelScope.launch {
            repository.delete(task)
        }
    }
    
    fun updateTask(task: TaskItem) {
        viewModelScope.launch {
            repository.update(task)
        }
    }
}
