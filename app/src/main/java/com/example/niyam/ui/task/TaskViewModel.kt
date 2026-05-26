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
        category: String = "General"
    ) {
        viewModelScope.launch {
            repository.insert(
                TaskItem(
                    title = title,
                    description = description,
                    priority = priority,
                    dueDate = dueDate,
                    category = category
                )
            )
        }
    }

    fun updateTaskStatus(task: TaskItem, status: TaskStatus) {
        viewModelScope.launch {
            repository.update(task.copy(status = status))
        }
    }

    fun toggleTaskCompletion(task: TaskItem) {
        val newStatus = if (task.status == TaskStatus.DONE) TaskStatus.TODO else TaskStatus.DONE
        updateTaskStatus(task, newStatus)
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
