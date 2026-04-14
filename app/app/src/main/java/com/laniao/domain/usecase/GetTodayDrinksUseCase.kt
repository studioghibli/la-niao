package com.laniao.domain.usecase

import com.laniao.domain.model.DrinkEntry
import com.laniao.domain.repository.DrinkEntryRepository
import com.laniao.util.Clock
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.ZoneId
import javax.inject.Inject

/**
 * Use case for getting today's drink entries and total liters.
 */
class GetTodayDrinksUseCase @Inject constructor(
    private val repository: DrinkEntryRepository,
    private val clock: Clock
) {
    /** Flow of today's drink entries. */
    fun entries(): Flow<List<DrinkEntry>> {
        val today = clock.now().atZone(ZoneId.systemDefault()).toLocalDate()
        return repository.getByDate(today)
    }

    /** Flow of today's total liters consumed. */
    fun totalLiters(): Flow<Double> {
        return entries().map { list -> list.sumOf { it.liters } }
    }
}
