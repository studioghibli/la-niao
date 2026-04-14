package com.laniao.domain.repository

import com.laniao.domain.model.VoidSchedule
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/**
 * Repository interface for VoidSchedule operations.
 */
interface VoidScheduleRepository {

    /**
     * Insert a new schedule. Returns the generated ID.
     * @throws ScheduleOverlapException if an overlapping active schedule exists
     */
    suspend fun insert(schedule: VoidSchedule): Long

    /**
     * Update an existing schedule.
     */
    suspend fun update(schedule: VoidSchedule)

    /**
     * Delete a schedule.
     */
    suspend fun delete(schedule: VoidSchedule)

    /**
     * Delete schedule by ID.
     */
    suspend fun deleteById(id: Long)

    /**
     * Get schedule by ID.
     */
    suspend fun getById(id: Long): VoidSchedule?

    /**
     * Get all active schedules (enabled and not expired).
     */
    fun getActive(): Flow<List<VoidSchedule>>

    /**
     * Get active schedule for a specific date.
     */
    suspend fun getActiveForDate(date: LocalDate): VoidSchedule?

    /**
     * Get all schedules (including inactive/expired).
     */
    fun getAll(): Flow<List<VoidSchedule>>

    /**
     * Check for overlapping schedules in a date range.
     */
    suspend fun hasOverlapping(startDate: LocalDate, endDate: LocalDate): Boolean

    /**
     * Get overlapping schedules in a date range.
     */
    suspend fun getOverlapping(startDate: LocalDate, endDate: LocalDate): List<VoidSchedule>

    /**
     * Disable all active schedules (used when replacing with new schedule).
     */
    suspend fun disableAll()
}
