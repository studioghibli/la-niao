package com.laniao.data.repository

import com.laniao.data.local.dao.PeeEntryDao
import com.laniao.data.local.entity.PeeEntryEntity
import com.laniao.domain.model.PeeEntry
import com.laniao.domain.repository.PeeEntryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of [PeeEntryRepository].
 */
@Singleton
class PeeEntryRepositoryImpl @Inject constructor(
    private val dao: PeeEntryDao
) : PeeEntryRepository {

    override suspend fun insert(entry: PeeEntry): Long {
        return dao.insert(entry.toEntity())
    }

    override suspend fun update(entry: PeeEntry) {
        dao.update(entry.toEntity())
    }

    override suspend fun delete(entry: PeeEntry) {
        dao.delete(entry.toEntity())
    }

    override suspend fun deleteById(id: Long) {
        val entry = dao.getById(id)
        entry?.let { dao.delete(it) }
    }

    override suspend fun getById(id: Long): PeeEntry? {
        return dao.getById(id)?.toDomain()
    }

    override fun getByDate(date: LocalDate): Flow<List<PeeEntry>> {
        val zoneId = ZoneId.systemDefault()
        val startOfDay = date.atStartOfDay(zoneId).toInstant()
        val endOfDay = date.plusDays(1).atStartOfDay(zoneId).toInstant()
        return dao.getByDateRange(startOfDay, endOfDay).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getByDateRange(startDate: LocalDate, endDate: LocalDate): Flow<List<PeeEntry>> {
        val zoneId = ZoneId.systemDefault()
        val startInstant = startDate.atStartOfDay(zoneId).toInstant()
        val endInstant = endDate.plusDays(1).atStartOfDay(zoneId).toInstant()
        return dao.getByDateRange(startInstant, endInstant).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getAll(): Flow<List<PeeEntry>> {
        return dao.getAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getCountForDate(date: LocalDate): Int {
        val zoneId = ZoneId.systemDefault()
        val startOfDay = date.atStartOfDay(zoneId).toInstant()
        val endOfDay = date.plusDays(1).atStartOfDay(zoneId).toInstant()
        return dao.getCountByDateRange(startOfDay, endOfDay)
    }

    override suspend fun existsInSameMinute(timestamp: Instant): Boolean {
        // Truncate to minute boundaries
        val epochSecond = timestamp.epochSecond
        val minuteStart = Instant.ofEpochSecond(epochSecond - (epochSecond % 60))
        val minuteEnd = minuteStart.plusSeconds(60)
        
        val count = dao.getByMinuteRange(minuteStart, minuteEnd)
        return count > 0
    }

    override suspend fun getFirstVoidOfDay(date: LocalDate): PeeEntry? {
        val zoneId = ZoneId.systemDefault()
        val startOfDay = date.atStartOfDay(zoneId).toInstant()
        val endOfDay = date.plusDays(1).atStartOfDay(zoneId).toInstant()
        return dao.getFirstVoidOfDay(startOfDay, endOfDay)?.toDomain()
    }

    override suspend fun getByScheduledTime(date: LocalDate, scheduledTime: LocalTime): List<PeeEntry> {
        val zoneId = ZoneId.systemDefault()
        val startOfDay = date.atStartOfDay(zoneId).toInstant()
        val endOfDay = date.plusDays(1).atStartOfDay(zoneId).toInstant()
        return dao.getByScheduledTime(startOfDay, endOfDay, scheduledTime).map { it.toDomain() }
    }

    override suspend fun getEntriesWithScheduledTime(date: LocalDate): List<PeeEntry> {
        val zoneId = ZoneId.systemDefault()
        val startOfDay = date.atStartOfDay(zoneId).toInstant()
        val endOfDay = date.plusDays(1).atStartOfDay(zoneId).toInstant()
        return dao.getEntriesWithScheduledTime(startOfDay, endOfDay).map { it.toDomain() }
    }

    override fun getVoidsOnlyByDate(date: LocalDate): Flow<List<PeeEntry>> {
        val zoneId = ZoneId.systemDefault()
        val startOfDay = date.atStartOfDay(zoneId).toInstant()
        val endOfDay = date.plusDays(1).atStartOfDay(zoneId).toInstant()
        return dao.getVoidsOnlyByDateRange(startOfDay, endOfDay).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getUrgesOnlyByDate(date: LocalDate): Flow<List<PeeEntry>> {
        val zoneId = ZoneId.systemDefault()
        val startOfDay = date.atStartOfDay(zoneId).toInstant()
        val endOfDay = date.plusDays(1).atStartOfDay(zoneId).toInstant()
        return dao.getUrgesOnlyByDateRange(startOfDay, endOfDay).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getLeaksOnlyByDate(date: LocalDate): Flow<List<PeeEntry>> {
        val zoneId = ZoneId.systemDefault()
        val startOfDay = date.atStartOfDay(zoneId).toInstant()
        val endOfDay = date.plusDays(1).atStartOfDay(zoneId).toInstant()
        return dao.getLeaksOnlyByDateRange(startOfDay, endOfDay).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun clearScheduledTimesForDateRange(startDate: LocalDate, endDate: LocalDate) {
        val zoneId = ZoneId.systemDefault()
        val rangeStart = startDate.atStartOfDay(zoneId).toInstant()
        val rangeEnd = endDate.plusDays(1).atStartOfDay(zoneId).toInstant()
        dao.clearScheduledTimesInRange(rangeStart, rangeEnd)
    }

    private fun PeeEntry.toEntity() = PeeEntryEntity(
        id = id,
        timestamp = timestamp,
        didVoid = didVoid,
        leakAmount = leakAmount,
        volumeSize = volumeSize,
        color = color,
        urgency = urgency,
        activityContext = activityContext,
        notes = notes,
        scheduledTime = scheduledTime
    )

    private fun PeeEntryEntity.toDomain() = PeeEntry(
        id = id,
        timestamp = timestamp,
        didVoid = didVoid,
        leakAmount = leakAmount,
        volumeSize = volumeSize,
        color = color,
        urgency = urgency,
        activityContext = activityContext,
        notes = notes,
        scheduledTime = scheduledTime
    )
}
