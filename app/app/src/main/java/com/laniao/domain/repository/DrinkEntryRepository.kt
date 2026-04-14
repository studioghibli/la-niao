package com.laniao.domain.repository

import com.laniao.domain.model.DrinkEntry
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/**
 * Repository interface for drink entry operations.
 */
interface DrinkEntryRepository {
    suspend fun insert(entry: DrinkEntry): Long
    suspend fun update(entry: DrinkEntry)
    suspend fun delete(entry: DrinkEntry)
    suspend fun getById(id: Long): DrinkEntry?
    fun getByDate(date: LocalDate): Flow<List<DrinkEntry>>
    fun getByDateRange(startDate: LocalDate, endDate: LocalDate): Flow<List<DrinkEntry>>
    fun getAll(): Flow<List<DrinkEntry>>
}
