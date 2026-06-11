package com.example.niyam.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [RoutineItem::class, TaskItem::class, CompletionRecord::class],
    version = 4,
    exportSchema = false
)
@TypeConverters(TaskConverters::class)
abstract class NiyamDatabase : RoomDatabase() {
    abstract fun routineDao(): RoutineDao
    abstract fun taskDao(): TaskDao
    abstract fun completionDao(): CompletionDao
}
