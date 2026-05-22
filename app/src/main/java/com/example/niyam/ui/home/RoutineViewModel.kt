package com.example.niyam.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.niyam.data.local.RoutineItem
import com.example.niyam.data.repository.RoutineRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoutineViewModel @Inject constructor(
    private val repository: RoutineRepository
) : ViewModel() {

    val routineItems: StateFlow<List<RoutineItem>> = repository.allItems
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        viewModelScope.launch {
            if (repository.allItems.first().isEmpty()) {
                repository.initializeDefaultTasks()
            }
        }
    }

    fun toggleTask(item: RoutineItem) {
        viewModelScope.launch {
            repository.update(item.copy(isCompleted = !item.isCompleted))
        }
    }

    fun addTask(title: String) {
        viewModelScope.launch {
            repository.insert(RoutineItem(title = title))
        }
    }

    fun deleteTask(item: RoutineItem) {
        viewModelScope.launch {
            repository.delete(item)
        }
    }
}
