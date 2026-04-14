package com.laniao.domain.usecase

import com.laniao.domain.repository.DrinkEntryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

data class DayLiquidIntake(
    val date: LocalDate,
    val totalLiters: Double
)

class GetLiquidIntakeUseCase @Inject constructor(
    private val drinkEntryRepository: DrinkEntryRepository
) {
    operator fun invoke(startDate: LocalDate, endDate: LocalDate): Flow<List<DayLiquidIntake>> {
        return drinkEntryRepository.getByDateRange(startDate, endDate).map { entries ->
            val zoneId = ZoneId.systemDefault()
            val entriesByDate = entries.groupBy {
                it.timestamp.atZone(zoneId).toLocalDate()
            }

            val days = mutableListOf<DayLiquidIntake>()
            var current = startDate
            while (!current.isAfter(endDate)) {
                val dayEntries = entriesByDate[current] ?: emptyList()
                days.add(DayLiquidIntake(
                    date = current,
                    totalLiters = dayEntries.sumOf { it.liters }
                ))
                current = current.plusDays(1)
            }
            days
        }
    }
}
