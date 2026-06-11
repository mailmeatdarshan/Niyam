package com.example.niyam.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.niyam.data.local.CompletionDao
import com.example.niyam.data.local.CompletionRecord
import com.example.niyam.data.local.RoutineItem
import com.example.niyam.data.repository.RoutineRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class RoutineViewModel @Inject constructor(
    private val repository: RoutineRepository,
    private val completionDao: CompletionDao,
    @ApplicationContext private val context: Context
) : ViewModel() {

    val routineItems: StateFlow<List<RoutineItem>> = repository.allItems
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        viewModelScope.launch {
            try {
                // Check and reset daily routines first
                repository.resetDailyRoutinesIfNeeded(context)
                
                // Initialize default routines if database is empty
                if (repository.allItems.first().isEmpty()) {
                    repository.initializeDefaultTasks()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun toggleTask(item: RoutineItem) {
        viewModelScope.launch {
            try {
                val newCompleted = !item.isCompleted
                repository.update(item.copy(isCompleted = newCompleted))
                
                if (newCompleted) {
                    // Log completion record
                    completionDao.insertRecord(
                        CompletionRecord(
                            type = "routine",
                            itemId = item.id,
                            title = item.title,
                            completedAt = System.currentTimeMillis()
                        )
                    )
                } else {
                    // Remove completion record registered today
                    val cal = Calendar.getInstance()
                    cal.set(Calendar.HOUR_OF_DAY, 0)
                    cal.set(Calendar.MINUTE, 0)
                    cal.set(Calendar.SECOND, 0)
                    val startOfDay = cal.timeInMillis
                    
                    cal.set(Calendar.HOUR_OF_DAY, 23)
                    cal.set(Calendar.MINUTE, 59)
                    cal.set(Calendar.SECOND, 59)
                    val endOfDay = cal.timeInMillis
                    
                    completionDao.deleteRecordToday("routine", item.id, startOfDay, endOfDay)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun addTask(title: String, timeOfDay: String = "morning") {
        viewModelScope.launch {
            try {
                repository.insert(RoutineItem(title = title, timeOfDay = timeOfDay))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteTask(item: RoutineItem) {
        viewModelScope.launch {
            repository.delete(item)
            // Clean up all history for this routine
            completionDao.deleteRecordByItemId("routine", item.id)
        }
    }
}
