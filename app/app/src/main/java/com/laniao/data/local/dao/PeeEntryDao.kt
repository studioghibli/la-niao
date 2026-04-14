package com.laniao.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.laniao.data.local.entity.PeeEntryEntity
import kotlinx.coroutines.flow.Flow
import java.time.Instant
import java.time.LocalTime

/**
 * Data Access Object for PeeEntry operations.
 */
@Dao
interface PeeEntryDao {

    /**
     * Insert a new entry. Returns the generated ID.
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(entry: PeeEntryEntity): Long

    /**
     * Update an existing entry.
     */
    @Update
    suspend fun update(entry: PeeEntryEntity)

    /**
     * Delete an entry.
     */
    @Delete
    suspend fun delete(entry: PeeEntryEntity)

    /**
     * Delete entry by ID.
     */
    @Query("DELETE FROM pee_entries WHERE id = :id")
    suspend fun deleteById(id: Long)

    /**
     * Get entry by ID.
     */
    @Query("SELECT * FROM pee_entries WHERE id = :id")
    suspend fun getById(id: Long): PeeEntryEntity?

    /**
     * Get all entries for a specific date (based on timestamp range).
     * Entries are ordered by timestamp descending (newest first).
     */
    @Query("""
        SELECT * FROM pee_entries 
        WHERE timestamp >= :startOfDay AND timestamp < :endOfDay 
        ORDER BY timestamp DESC
    """)
    fun getByDateRange(startOfDay: Instant, endOfDay: Instant): Flow<List<PeeEntryEntity>>

    /**
     * Get all entries ordered by timestamp descending.
     */
    @Query("SELECT * FROM pee_entries ORDER BY timestamp DESC")
    fun getAll(): Flow<List<PeeEntryEntity>>

    /**
     * Get count of entries for a specific date range.
     */
    @Query("""
        SELECT COUNT(*) FROM pee_entries 
        WHERE timestamp >= :startOfDay AND timestamp < :endOfDay
    """)
    suspend fun getCountByDateRange(startOfDay: Instant, endOfDay: Instant): Int

    /**
     * Get entries within a specific minute (for duplicate detection).
     * Used to prevent multiple entries in the same minute.
     */
    @Query("""
        SELECT COUNT(*) FROM pee_entries 
        WHERE timestamp >= :startOfMinute AND timestamp < :endOfMinute
    """)
    suspend fun getByMinuteRange(startOfMinute: Instant, endOfMinute: Instant): Int

    /**
     * Get first void entry of the day (earliest timestamp where didVoid = true).
     */
    @Query("""
        SELECT * FROM pee_entries 
        WHERE timestamp >= :startOfDay AND timestamp < :endOfDay 
        AND didVoid = 1 
        ORDER BY timestamp ASC 
        LIMIT 1
    """)
    suspend fun getFirstVoidOfDay(startOfDay: Instant, endOfDay: Instant): PeeEntryEntity?

    /**
     * Get entries with a specific scheduled time for a date range.
     */
    @Query("""
        SELECT * FROM pee_entries 
        WHERE timestamp >= :startOfDay AND timestamp < :endOfDay 
        AND scheduledTime = :scheduledTime
    """)
    suspend fun getByScheduledTime(startOfDay: Instant, endOfDay: Instant, scheduledTime: LocalTime): List<PeeEntryEntity>

    /**
     * Get all entries with a non-null scheduledTime for a date range.
     */
    @Query("""
        SELECT * FROM pee_entries 
        WHERE timestamp >= :startOfDay AND timestamp < :endOfDay 
        AND scheduledTime IS NOT NULL
        ORDER BY timestamp ASC
    """)
    suspend fun getEntriesWithScheduledTime(startOfDay: Instant, endOfDay: Instant): List<PeeEntryEntity>

    /**
     * Get all voids only (no urge-only entries) for a date range.
     */
    @Query("""
        SELECT * FROM pee_entries 
        WHERE timestamp >= :startOfDay AND timestamp < :endOfDay 
        AND didVoid = 1 
        ORDER BY timestamp DESC
    """)
    fun getVoidsOnlyByDateRange(startOfDay: Instant, endOfDay: Instant): Flow<List<PeeEntryEntity>>

    /**
     * Get all urge-only entries for a date range.
     */
    @Query("""
        SELECT * FROM pee_entries 
        WHERE timestamp >= :startOfDay AND timestamp < :endOfDay 
        AND didVoid = 0 
        AND leakAmount = 'NONE'
        ORDER BY timestamp DESC
    """)
    fun getUrgesOnlyByDateRange(startOfDay: Instant, endOfDay: Instant): Flow<List<PeeEntryEntity>>

    /**
     * Get only leak-only entries for a date range.
     */
    @Query("""
        SELECT * FROM pee_entries
        WHERE timestamp >= :startOfDay AND timestamp < :endOfDay
        AND didVoid = 0
        AND leakAmount != 'NONE'
        ORDER BY timestamp DESC
    """)
    fun getLeaksOnlyByDateRange(startOfDay: Instant, endOfDay: Instant): Flow<List<PeeEntryEntity>>

    /**
     * Clear scheduledTime for all entries in a timestamp range.
     * Used when a schedule is created, edited, or deleted to force re-association.
     */
    @Query("""
        UPDATE pee_entries 
        SET scheduledTime = NULL 
        WHERE timestamp >= :rangeStart AND timestamp < :rangeEnd 
        AND scheduledTime IS NOT NULL
    """)
    suspend fun clearScheduledTimesInRange(rangeStart: Instant, rangeEnd: Instant)

    @Query("DELETE FROM pee_entries")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entries: List<PeeEntryEntity>)
}
