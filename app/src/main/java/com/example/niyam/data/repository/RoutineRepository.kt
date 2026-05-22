package com.example.niyam.data.repository

import com.example.niyam.data.local.RoutineDao
import com.example.niyam.data.local.RoutineItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoutineRepository @Inject constructor(
    private val routineDao: RoutineDao
) {
    val allItems: Flow<List<RoutineItem>> = routineDao.getAllItems()

    suspend fun insert(item: RoutineItem) {
        routineDao.insertItem(item)
    }

    suspend fun update(item: RoutineItem) {
        routineDao.updateItem(item)
    }

    suspend fun delete(item: RoutineItem) {
        routineDao.deleteItem(item)
    }
    
    suspend fun initializeDefaultTasks() {
        val defaults = listOf(
            RoutineItem(title = "Sandhyavandanam"),
            RoutineItem(title = "Meditation (15 min)"),
            RoutineItem(title = "Bhagavad Gita Reading"),
            RoutineItem(title = "Surya Namaskar")
        )
        defaults.forEach { routineDao.insertItem(it) }
    }
}
