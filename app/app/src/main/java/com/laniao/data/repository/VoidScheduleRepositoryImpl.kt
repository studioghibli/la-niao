package com.laniao.data.repository

import com.laniao.data.local.dao.VoidScheduleDao
import com.laniao.data.local.entity.VoidScheduleEntity
import com.laniao.domain.exception.ScheduleOverlapException
import com.laniao.domain.model.VoidSchedule
import com.laniao.domain.repository.VoidScheduleRepository
import com.laniao.util.Clock
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of [VoidScheduleRepository].
 */
@Singleton
class VoidScheduleRepositoryImpl @Inject constructor(
    private val dao: VoidScheduleDao,
    private val clock: Clock
) : VoidScheduleRepository {

    override suspend fun insert(schedule: VoidSchedule): Long {
        // Check for overlapping schedules
        if (hasOverlapping(schedule.createdAt, schedule.expiresAt)) {
            throw ScheduleOverlapException("An overlapping schedule already exists")
        }
        return dao.insert(schedule.toEntity())
    }

    override suspend fun update(schedule: VoidSchedule) {
        dao.update(schedule.toEntity())
    }

    override suspend fun delete(schedule: VoidSchedule) {
        dao.delete(schedule.toEntity())
    }

    override suspend fun deleteById(id: Long) {
        val entity = dao.getById(id)
        entity?.let { dao.delete(it) }
    }

    override suspend fun getById(id: Long): VoidSchedule? {
        return dao.getById(id)?.toDomain()
    }

    override fun getActive(): Flow<List<VoidSchedule>> {
        val today = clock.now().atZone(ZoneId.systemDefault()).toLocalDate()
        return dao.getActive(today).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getActiveForDate(date: LocalDate): VoidSchedule? {
        return dao.getActiveForDate(date)?.toDomain()
    }

    override fun getAll(): Flow<List<VoidSchedule>> {
        return dao.getAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun hasOverlapping(startDate: LocalDate, endDate: LocalDate): Boolean {
        return getOverlapping(startDate, endDate).isNotEmpty()
    }

    override suspend fun getOverlapping(startDate: LocalDate, endDate: LocalDate): List<VoidSchedule> {
        return dao.getOverlapping(startDate, endDate).map { it.toDomain() }
    }

    override suspend fun disableAll() {
        dao.disableAll()
    }

    private fun VoidSchedule.toEntity() = VoidScheduleEntity(
        id = id,
        startTime = startTime,
        endTime = endTime,
        intervalMinutes = intervalMinutes,
        enabled = enabled,
        createdAt = createdAt,
        expiresAt = expiresAt
    )

    private fun VoidScheduleEntity.toDomain() = VoidSchedule(
        id = id,
        startTime = startTime,
        endTime = endTime,
        intervalMinutes = intervalMinutes,
        enabled = enabled,
        createdAt = createdAt,
        expiresAt = expiresAt
    )
}
