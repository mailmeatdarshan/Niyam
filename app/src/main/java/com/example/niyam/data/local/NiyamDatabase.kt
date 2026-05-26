package com.example.niyam.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [RoutineItem::class, TaskItem::class], version = 2, exportSchema = false)
@TypeConverters(TaskConverters::class)
abstract class NiyamDatabase : RoomDatabase() {
    abstract fun routineDao(): RoutineDao
    abstract fun taskDao(): TaskDao
}
