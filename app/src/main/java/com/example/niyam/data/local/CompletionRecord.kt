package com.example.niyam.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "completion_records")
data class CompletionRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val type: String, // "task" or "routine"
    val itemId: Int,
    val title: String,
    val completedAt: Long = System.currentTimeMillis()
)
