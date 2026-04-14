package com.laniao.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.laniao.data.local.entity.WaterIntakeEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/**
 * Data Access Object for WaterIntake operations.
 */
@Dao
interface WaterIntakeDao {

    /**
     * Insert a new water intake entry.
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(waterIntake: WaterIntakeEntity): Long

    /**
     * Upsert water intake - insert if not exists, update if exists for date.
     */
    @Upsert
    suspend fun upsert(waterIntake: WaterIntakeEntity)

    /**
     * Delete a water intake entry.
     */
    @Delete
    suspend fun delete(waterIntake: WaterIntakeEntity)

    /**
     * Get water intake by ID.
     */
    @Query("SELECT * FROM water_intake WHERE id = :id")
    suspend fun getById(id: Long): WaterIntakeEntity?

    /**
     * Get water intake for a specific date.
     */
    @Query("SELECT * FROM water_intake WHERE date = :date")
    suspend fun getByDate(date: LocalDate): WaterIntakeEntity?

    /**
     * Get water intake for a specific date as Flow (for reactive UI).
     */
    @Query("SELECT * FROM water_intake WHERE date = :date")
    fun observeByDate(date: LocalDate): Flow<WaterIntakeEntity?>

    /**
     * Get water intake entries for a date range.
     */
    @Query("""
        SELECT * FROM water_intake 
        WHERE date >= :startDate AND date <= :endDate 
        ORDER BY date DESC
    """)
    fun getByDateRange(startDate: LocalDate, endDate: LocalDate): Flow<List<WaterIntakeEntity>>

    /**
     * Get all water intake entries.
     */
    @Query("SELECT * FROM water_intake ORDER BY date DESC")
    fun getAll(): Flow<List<WaterIntakeEntity>>

    @Query("DELETE FROM water_intake")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(intakes: List<WaterIntakeEntity>)
}
