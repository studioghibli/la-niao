package com.laniao.domain.usecase

import com.laniao.domain.model.PeeColor
import com.laniao.domain.repository.PeeEntryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

/**
 * Data for a single day in the void frequency chart.
 */
data class DayVoidFrequency(
    val date: LocalDate,
    val voidCount: Int,
    val dominantColor: PeeColor,
    val colorCounts: Map<PeeColor, Int> = emptyMap()
)

/**
 * Gets void count per day for a date range.
 * Each day includes the count and the most common pee color for bar coloring.
 */
class GetVoidFrequencyUseCase @Inject constructor(
    private val peeEntryRepository: PeeEntryRepository
) {
    operator fun invoke(startDate: LocalDate, endDate: LocalDate): Flow<List<DayVoidFrequency>> {
        return peeEntryRepository.getByDateRange(startDate, endDate).map { entries ->
            val zoneId = ZoneId.systemDefault()
            val entriesByDate = entries
                .filter { it.didVoid }
                .groupBy { it.timestamp.atZone(zoneId).toLocalDate() }

            // Generate all days in range
            val days = mutableListOf<DayVoidFrequency>()
            var current = startDate
            while (!current.isAfter(endDate)) {
                val dayEntries = entriesByDate[current] ?: emptyList()
                val dominantColor = if (dayEntries.isNotEmpty()) {
                    dayEntries
                        .map { it.color }
                        .filter { it != PeeColor.UNKNOWN }
                        .groupBy { it }
                        .maxByOrNull { it.value.size }
                        ?.key ?: PeeColor.UNKNOWN
                } else {
                    PeeColor.UNKNOWN
                }

                val colorCounts = dayEntries
                    .groupBy { it.color }
                    .mapValues { it.value.size }

                days.add(DayVoidFrequency(
                    date = current,
                    voidCount = dayEntries.size,
                    dominantColor = dominantColor,
                    colorCounts = colorCounts
                ))
                current = current.plusDays(1)
            }
            days
        }
    }
}
