package com.laniao.domain.repository

import com.laniao.domain.model.WaterIntake
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/**
 * Repository interface for WaterIntake operations.
 */
interface WaterIntakeRepository {

    /**
     * Insert or update water intake for a date.
     * Uses upsert behavior - updates if entry exists for date.
     */
    suspend fun upsert(waterIntake: WaterIntake)

    /**
     * Delete water intake entry.
     */
    suspend fun delete(waterIntake: WaterIntake)

    /**
     * Get water intake by ID.
     */
    suspend fun getById(id: Long): WaterIntake?

    /**
     * Get water intake for a specific date.
     */
    suspend fun getByDate(date: LocalDate): WaterIntake?

    /**
     * Observe water intake for a specific date (reactive).
     */
    fun observeByDate(date: LocalDate): Flow<WaterIntake?>

    /**
     * Get water intake entries for a date range.
     */
    fun getByDateRange(startDate: LocalDate, endDate: LocalDate): Flow<List<WaterIntake>>

    /**
     * Get all water intake entries.
     */
    fun getAll(): Flow<List<WaterIntake>>
}
