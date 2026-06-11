package com.example.niyam.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CompletionDao {
    @Query("SELECT * FROM completion_records ORDER BY completedAt DESC")
    fun getAllRecords(): Flow<List<CompletionRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: CompletionRecord)

    @Query("DELETE FROM completion_records WHERE type = :type AND itemId = :itemId")
    suspend fun deleteRecordByItemId(type: String, itemId: Int)

    @Query("DELETE FROM completion_records WHERE type = :type AND itemId = :itemId AND completedAt >= :startOfDay AND completedAt <= :endOfDay")
    suspend fun deleteRecordToday(type: String, itemId: Int, startOfDay: Long, endOfDay: Long)
}
