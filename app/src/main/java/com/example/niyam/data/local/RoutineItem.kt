package com.example.niyam.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "routine_items")
data class RoutineItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val isCompleted: Boolean = false,
    val timeOfDay: String = "morning", // "morning", "afternoon", "evening"
    val createdAt: Long = System.currentTimeMillis()
)
