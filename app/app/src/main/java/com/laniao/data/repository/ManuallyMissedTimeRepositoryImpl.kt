package com.laniao.data.repository

import com.laniao.data.local.dao.ManuallyMissedTimeDao
import com.laniao.data.local.entity.ManuallyMissedTimeEntity
import com.laniao.domain.repository.ManuallyMissedTimeRepository
import com.laniao.util.Clock
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

class ManuallyMissedTimeRepositoryImpl @Inject constructor(
    private val dao: ManuallyMissedTimeDao,
    private val clock: Clock
) : ManuallyMissedTimeRepository {

    override fun getByDate(date: LocalDate): Flow<List<LocalTime>> =
        dao.getByDate(date).map { entities -> entities.map { it.scheduledTime } }

    override fun getByDateRange(startDate: LocalDate, endDate: LocalDate): Flow<List<Pair<LocalDate, LocalTime>>> =
        dao.getByDateRange(startDate, endDate).map { entities -> entities.map { it.date to it.scheduledTime } }

    override suspend fun getByDateOnce(date: LocalDate): List<LocalTime> =
        dao.getByDateOnce(date).map { it.scheduledTime }

    override suspend fun markMissed(date: LocalDate, scheduledTime: LocalTime) {
        dao.insert(ManuallyMissedTimeEntity(date = date, scheduledTime = scheduledTime, createdAt = clock.now()))
    }

    override suspend fun unmarkMissed(date: LocalDate, scheduledTime: LocalTime) {
        dao.delete(date, scheduledTime)
    }

    override suspend fun isMissed(date: LocalDate, scheduledTime: LocalTime): Boolean =
        dao.exists(date, scheduledTime)

    override suspend fun deleteAll() = dao.deleteAll()

    override suspend fun getAll(): List<Pair<LocalDate, LocalTime>> =
        dao.getAll().map { it.date to it.scheduledTime }

    override suspend fun insertAll(items: List<Pair<LocalDate, LocalTime>>) {
        val entities = items.map { (date, time) ->
            ManuallyMissedTimeEntity(date = date, scheduledTime = time, createdAt = clock.now())
        }
        dao.insertAll(entities)
    }
}
