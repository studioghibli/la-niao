package com.laniao.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.laniao.data.local.entity.ExerciseScheduleEntity
import com.laniao.data.local.entity.ExerciseScheduleItemEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface ExerciseScheduleDao {

    @Insert
    suspend fun insertSchedule(schedule: ExerciseScheduleEntity): Long

    @Update
    suspend fun updateSchedule(schedule: ExerciseScheduleEntity)

    @Delete
    suspend fun deleteSchedule(schedule: ExerciseScheduleEntity)

    @Query("DELETE FROM exercise_schedules WHERE id = :scheduleId")
    suspend fun deleteScheduleById(scheduleId: Long)

    @Query("SELECT * FROM exercise_schedules WHERE id = :id")
    suspend fun getScheduleById(id: Long): ExerciseScheduleEntity?

    @Query("SELECT * FROM exercise_schedules ORDER BY startDate DESC")
    fun getAllSchedules(): Flow<List<ExerciseScheduleEntity>>

    @Query("""
        SELECT * FROM exercise_schedules 
        WHERE enabled = 1 AND startDate <= :date AND (endDate IS NULL OR endDate >= :date)
    """)
    suspend fun getActiveForDate(date: LocalDate): ExerciseScheduleEntity?

    @Query("""
        SELECT * FROM exercise_schedules 
        WHERE enabled = 1 AND startDate <= :date AND (endDate IS NULL OR endDate >= :date)
    """)
    fun getActiveForDateFlow(date: LocalDate): Flow<ExerciseScheduleEntity?>

    @Query("""
        SELECT EXISTS(
            SELECT 1 FROM exercise_schedules 
            WHERE enabled = 1 AND id != :excludeId 
            AND startDate <= :endDate AND (endDate IS NULL OR endDate >= :startDate)
        )
    """)
    suspend fun hasOverlap(startDate: LocalDate, endDate: LocalDate, excludeId: Long = 0): Boolean

    // Schedule items

    @Insert
    suspend fun insertItem(item: ExerciseScheduleItemEntity): Long

    @Insert
    suspend fun insertItems(items: List<ExerciseScheduleItemEntity>)

    @Query("SELECT * FROM exercise_schedule_items WHERE scheduleId = :scheduleId")
    suspend fun getItemsForSchedule(scheduleId: Long): List<ExerciseScheduleItemEntity>

    @Query("SELECT * FROM exercise_schedule_items WHERE scheduleId = :scheduleId")
    fun getItemsForScheduleFlow(scheduleId: Long): Flow<List<ExerciseScheduleItemEntity>>

    @Query("DELETE FROM exercise_schedule_items WHERE scheduleId = :scheduleId")
    suspend fun deleteItemsForSchedule(scheduleId: Long)

    @Query("DELETE FROM exercise_schedules")
    suspend fun deleteAllSchedules()

    @Query("DELETE FROM exercise_schedule_items")
    suspend fun deleteAllItems()
}
