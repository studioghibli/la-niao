package com.laniao.data.repository

import com.laniao.data.local.dao.DrinkEntryDao
import com.laniao.data.local.entity.DrinkEntryEntity
import com.laniao.domain.model.DrinkEntry
import com.laniao.domain.repository.DrinkEntryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DrinkEntryRepositoryImpl @Inject constructor(
    private val dao: DrinkEntryDao
) : DrinkEntryRepository {

    override suspend fun insert(entry: DrinkEntry): Long {
        return dao.insert(entry.toEntity())
    }

    override suspend fun update(entry: DrinkEntry) {
        dao.update(entry.toEntity())
    }

    override suspend fun delete(entry: DrinkEntry) {
        dao.delete(entry.toEntity())
    }

    override suspend fun getById(id: Long): DrinkEntry? {
        return dao.getById(id)?.toDomain()
    }

    override fun getByDate(date: LocalDate): Flow<List<DrinkEntry>> {
        val zoneId = ZoneId.systemDefault()
        val start = date.atStartOfDay(zoneId).toInstant()
        val end = date.plusDays(1).atStartOfDay(zoneId).toInstant()
        return dao.getByDateRange(start, end).map { list -> list.map { it.toDomain() } }
    }

    override fun getByDateRange(startDate: LocalDate, endDate: LocalDate): Flow<List<DrinkEntry>> {
        val zoneId = ZoneId.systemDefault()
        val start = startDate.atStartOfDay(zoneId).toInstant()
        val end = endDate.plusDays(1).atStartOfDay(zoneId).toInstant()
        return dao.getByDateRange(start, end).map { list -> list.map { it.toDomain() } }
    }

    override fun getAll(): Flow<List<DrinkEntry>> {
        return dao.getAll().map { list -> list.map { it.toDomain() } }
    }

    private fun DrinkEntry.toEntity() = DrinkEntryEntity(
        id = id,
        timestamp = timestamp,
        type = type,
        amount = amount,
        unit = unit,
        customName = customName
    )

    private fun DrinkEntryEntity.toDomain() = DrinkEntry(
        id = id,
        timestamp = timestamp,
        type = type,
        amount = amount,
        unit = unit,
        customName = customName
    )
}
