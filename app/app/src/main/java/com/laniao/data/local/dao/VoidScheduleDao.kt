package com.laniao.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.laniao.data.local.entity.VoidScheduleEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/**
 * Data Access Object for VoidSchedule operations.
 */
@Dao
interface VoidScheduleDao {

    /**
     * Insert a new schedule. Returns the generated ID.
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(schedule: VoidScheduleEntity): Long

    /**
     * Update an existing schedule.
     */
    @Update
    suspend fun update(schedule: VoidScheduleEntity)

    /**
     * Delete a schedule.
     */
    @Delete
    suspend fun delete(schedule: VoidScheduleEntity)

    /**
     * Delete schedule by ID.
     */
    @Query("DELETE FROM void_schedules WHERE id = :id")
    suspend fun deleteById(id: Long)

    /**
     * Get schedule by ID.
     */
    @Query("SELECT * FROM void_schedules WHERE id = :id")
    suspend fun getById(id: Long): VoidScheduleEntity?

    /**
     * Get all active schedules (enabled and not expired).
     */
    @Query("""
        SELECT * FROM void_schedules 
        WHERE enabled = 1 
        AND createdAt <= :today
        AND expiresAt >= :today 
        ORDER BY createdAt DESC
    """)
    fun getActive(today: LocalDate): Flow<List<VoidScheduleEntity>>

    /**
     * Get active schedule for a specific date (single active schedule expected).
     */
    @Query("""
        SELECT * FROM void_schedules 
        WHERE enabled = 1 
        AND createdAt <= :date 
        AND expiresAt >= :date 
        ORDER BY createdAt DESC 
        LIMIT 1
    """)
    suspend fun getActiveForDate(date: LocalDate): VoidScheduleEntity?

    /**
     * Get all schedules (including inactive/expired).
     */
    @Query("SELECT * FROM void_schedules ORDER BY createdAt DESC")
    fun getAll(): Flow<List<VoidScheduleEntity>>

    /**
     * Check for overlapping schedules in a date range.
     * Used to detect conflicts before creating new schedule.
     */
    @Query("""
        SELECT * FROM void_schedules 
        WHERE enabled = 1 
        AND createdAt <= :endDate 
        AND expiresAt >= :startDate
    """)
    suspend fun getOverlapping(startDate: LocalDate, endDate: LocalDate): List<VoidScheduleEntity>

    /**
     * Disable all active schedules (used when replacing with new schedule).
     */
    @Query("UPDATE void_schedules SET enabled = 0 WHERE enabled = 1")
    suspend fun disableAll()

    @Query("DELETE FROM void_schedules")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(schedules: List<VoidScheduleEntity>)
}
