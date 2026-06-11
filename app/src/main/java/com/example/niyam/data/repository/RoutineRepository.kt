package com.example.niyam.data.repository

import android.content.Context
import com.example.niyam.data.local.RoutineDao
import com.example.niyam.data.local.RoutineItem
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoutineRepository @Inject constructor(
    private val routineDao: RoutineDao
) {
    val allItems: Flow<List<RoutineItem>> = routineDao.getAllItems()

    suspend fun getAllItemsOnce(): List<RoutineItem> = routineDao.getAllItemsOnce()

    suspend fun insert(item: RoutineItem) {
        routineDao.insertItem(item)
    }

    suspend fun update(item: RoutineItem) {
        routineDao.updateItem(item)
    }

    suspend fun delete(item: RoutineItem) {
        routineDao.deleteItem(item)
    }

    suspend fun deduplicateRoutines() {
        val allItems = routineDao.getAllItemsOnce()
        val seen = mutableSetOf<String>()
        allItems.forEach { item ->
            val key = "${item.title.trim().lowercase()}_${item.timeOfDay}"
            if (seen.contains(key)) {
                routineDao.deleteItem(item)
            } else {
                seen.add(key)
            }
        }
    }
    
    suspend fun initializeDefaultTasks() {
        val defaults = listOf(
            // Morning
            RoutineItem(title = "Meditation (App)", timeOfDay = "morning"),
            RoutineItem(title = "Bhagavad Gita (App)", timeOfDay = "morning"),
            
            // Afternoon
            RoutineItem(title = "Swadhyaya (Self-study)", timeOfDay = "afternoon"),
            RoutineItem(title = "Hydrate (Log Water)", timeOfDay = "afternoon"),
            
            // Evening
            RoutineItem(title = "Walk (500m - 1km)", timeOfDay = "evening"),
            RoutineItem(title = "Sandhyavandanam", timeOfDay = "evening")
        )
        defaults.forEach { routineDao.insertItem(it) }
    }

    suspend fun resetDailyRoutinesIfNeeded(context: Context) {
        val prefs = context.getSharedPreferences("niyam_routine_prefs", Context.MODE_PRIVATE)
        val sdf = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val todayStr = sdf.format(Date())
        val lastReset = prefs.getString("last_reset_date", "")
        
        if (lastReset != todayStr) {
            val currentItems = routineDao.getAllItemsOnce()
            currentItems.forEach {
                if (it.isCompleted) {
                    routineDao.updateItem(it.copy(isCompleted = false))
                }
            }
            prefs.edit().putString("last_reset_date", todayStr).apply()
        }
    }
}
