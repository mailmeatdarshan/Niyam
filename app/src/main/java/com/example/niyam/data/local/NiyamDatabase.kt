package com.example.niyam.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RoutineItem::class], version = 1, exportSchema = false)
abstract class NiyamDatabase : RoomDatabase() {
    abstract fun routineDao(): RoutineDao
}
