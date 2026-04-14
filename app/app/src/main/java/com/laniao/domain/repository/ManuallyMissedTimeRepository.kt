package com.laniao.domain.repository

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalTime

/**
 * Repository for manually missed scheduled void times.
 */
interface ManuallyMissedTimeRepository {
    fun getByDate(date: LocalDate): Flow<List<LocalTime>>
    fun getByDateRange(startDate: LocalDate, endDate: LocalDate): Flow<List<Pair<LocalDate, LocalTime>>>
    suspend fun getByDateOnce(date: LocalDate): List<LocalTime>
    suspend fun markMissed(date: LocalDate, scheduledTime: LocalTime)
    suspend fun unmarkMissed(date: LocalDate, scheduledTime: LocalTime)
    suspend fun isMissed(date: LocalDate, scheduledTime: LocalTime): Boolean
    suspend fun deleteAll()
    suspend fun getAll(): List<Pair<LocalDate, LocalTime>>
    suspend fun insertAll(items: List<Pair<LocalDate, LocalTime>>)
}
