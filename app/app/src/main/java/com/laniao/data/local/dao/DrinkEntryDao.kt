package com.laniao.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.laniao.data.local.entity.DrinkEntryEntity
import kotlinx.coroutines.flow.Flow
import java.time.Instant

/**
 * Data Access Object for drink entries.
 */
@Dao
interface DrinkEntryDao {

    @Insert
    suspend fun insert(entry: DrinkEntryEntity): Long

    @Update
    suspend fun update(entry: DrinkEntryEntity)

    @Delete
    suspend fun delete(entry: DrinkEntryEntity)

    @Query("SELECT * FROM drink_entries WHERE id = :id")
    suspend fun getById(id: Long): DrinkEntryEntity?

    @Query("""
        SELECT * FROM drink_entries
        WHERE timestamp >= :startOfDay AND timestamp < :endOfDay
        ORDER BY timestamp DESC
    """)
    fun getByDateRange(startOfDay: Instant, endOfDay: Instant): Flow<List<DrinkEntryEntity>>

    @Query("SELECT * FROM drink_entries ORDER BY timestamp DESC")
    fun getAll(): Flow<List<DrinkEntryEntity>>

    @Query("DELETE FROM drink_entries")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entries: List<DrinkEntryEntity>)
}
