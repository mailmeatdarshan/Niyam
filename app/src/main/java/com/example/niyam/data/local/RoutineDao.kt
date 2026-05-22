package com.example.niyam.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RoutineDao {
    @Query("SELECT * FROM routine_items ORDER BY createdAt ASC")
    fun getAllItems(): Flow<List<RoutineItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: RoutineItem)

    @Update
    suspend fun updateItem(item: RoutineItem)

    @Delete
    suspend fun deleteItem(item: RoutineItem)

    @Query("DELETE FROM routine_items")
    suspend fun deleteAll()
}
