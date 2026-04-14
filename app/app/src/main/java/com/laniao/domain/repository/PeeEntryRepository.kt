package com.laniao.domain.repository

import com.laniao.domain.model.PeeEntry
import kotlinx.coroutines.flow.Flow
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime

/**
 * Repository interface for PeeEntry operations.
 */
interface PeeEntryRepository {

    /**
     * Insert a new entry. Returns the generated ID.
     * @throws MaxEntriesException if 50 entries already exist for the day
     * @throws DuplicateMinuteException if an entry exists in the same minute
     * @throws ValidationException if notes exceed 500 characters
     */
    suspend fun insert(entry: PeeEntry): Long

    /**
     * Update an existing entry.
     * @throws ValidationException if notes exceed 500 characters
     */
    suspend fun update(entry: PeeEntry)

    /**
     * Delete an entry.
     */
    suspend fun delete(entry: PeeEntry)

    /**
     * Delete entry by ID.
     */
    suspend fun deleteById(id: Long)

    /**
     * Get entry by ID.
     */
    suspend fun getById(id: Long): PeeEntry?

    /**
     * Get all entries for a specific date.
     * Entries are ordered by timestamp descending (newest first).
     */
    fun getByDate(date: LocalDate): Flow<List<PeeEntry>>

    /**
     * Get all entries for a date range.
     */
    fun getByDateRange(startDate: LocalDate, endDate: LocalDate): Flow<List<PeeEntry>>

    /**
     * Get all entries ordered by timestamp descending.
     */
    fun getAll(): Flow<List<PeeEntry>>

    /**
     * Get count of entries for a specific date.
     */
    suspend fun getCountForDate(date: LocalDate): Int

    /**
     * Check if an entry exists in the same minute as the given timestamp.
     */
    suspend fun existsInSameMinute(timestamp: Instant): Boolean

    /**
     * Get first void entry of the day (earliest timestamp where didVoid = true).
     */
    suspend fun getFirstVoidOfDay(date: LocalDate): PeeEntry?

    /**
     * Get entries associated with a specific scheduled time for a date.
     */
    suspend fun getByScheduledTime(date: LocalDate, scheduledTime: LocalTime): List<PeeEntry>

    /**
     * Get all entries for a date that have a non-null scheduledTime.
     */
    suspend fun getEntriesWithScheduledTime(date: LocalDate): List<PeeEntry>

    /**
     * Get only void entries (no urge-only) for a date.
     */
    fun getVoidsOnlyByDate(date: LocalDate): Flow<List<PeeEntry>>

    /**
     * Get only urge-only entries for a date.
     */
    fun getUrgesOnlyByDate(date: LocalDate): Flow<List<PeeEntry>>

    /**
     * Get only leak-only entries for a date.
     */
    fun getLeaksOnlyByDate(date: LocalDate): Flow<List<PeeEntry>>

    /**
     * Clear scheduledTime for all entries in the given date range (inclusive).
     * Used when a schedule is created, edited, or deleted to force manual re-association.
     */
    suspend fun clearScheduledTimesForDateRange(startDate: LocalDate, endDate: LocalDate)
}
