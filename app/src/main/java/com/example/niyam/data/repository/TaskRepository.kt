package com.example.niyam.data.repository

import com.example.niyam.data.local.TaskDao
import com.example.niyam.data.local.TaskItem
import com.example.niyam.data.local.TaskStatus
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(
    private val taskDao: TaskDao
) {
    val allTasks: Flow<List<TaskItem>> = taskDao.getAllTasks()

    fun getTasksByStatus(status: TaskStatus): Flow<List<TaskItem>> = 
        taskDao.getTasksByStatus(status)

    suspend fun insert(task: TaskItem) {
        taskDao.insertTask(task)
    }

    suspend fun update(task: TaskItem) {
        taskDao.updateTask(task)
    }

    suspend fun delete(task: TaskItem) {
        taskDao.deleteTask(task)
    }
}
