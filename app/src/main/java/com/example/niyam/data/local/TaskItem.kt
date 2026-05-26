package com.example.niyam.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter

enum class TaskPriority { LOW, MEDIUM, HIGH }
enum class TaskStatus { TODO, IN_PROGRESS, DONE }

@Entity(tableName = "tasks")
data class TaskItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String = "",
    val status: TaskStatus = TaskStatus.TODO,
    val priority: TaskPriority = TaskPriority.MEDIUM,
    val dueDate: Long? = null,
    val category: String = "General",
    val createdAt: Long = System.currentTimeMillis()
)

class TaskConverters {
    @TypeConverter
    fun fromPriority(priority: TaskPriority): String = priority.name

    @TypeConverter
    fun toPriority(priority: String): TaskPriority = TaskPriority.valueOf(priority)

    @TypeConverter
    fun fromStatus(status: TaskStatus): String = status.name

    @TypeConverter
    fun toStatus(status: String): TaskStatus = TaskStatus.valueOf(status)
}
