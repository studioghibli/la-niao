package com.laniao.domain.usecase

import com.laniao.domain.repository.PeeEntryRepository
import com.laniao.domain.repository.VoidScheduleRepository
import kotlinx.coroutines.flow.first
import java.time.Duration
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

data class DailyVoidGap(
    val date: LocalDate,
    val averageGapMinutes: Double,
    val voidCount: Int,
    val scheduleGoalMinutes: Int? = null
)

/**
 * Calculates the average time gap between consecutive void entries per day.
 * Urge-only and leak-only entries are excluded — only actual voids count.
 */
class GetAverageVoidGapUseCase @Inject constructor(
    private val repository: PeeEntryRepository,
    private val voidScheduleRepository: VoidScheduleRepository
) {
    suspend operator fun invoke(startDate: LocalDate, endDate: LocalDate): List<DailyVoidGap> {
        val zoneId = ZoneId.systemDefault()
        val entries = repository.getByDateRange(startDate, endDate).first()

        val gapsByDate = entries
            .filter { it.didVoid }
            .groupBy { it.timestamp.atZone(zoneId).toLocalDate() }
            .mapNotNull { (date, dayEntries) ->
                if (dayEntries.size < 2) return@mapNotNull null

                val sorted = dayEntries.sortedBy { it.timestamp }
                val gaps = sorted.zipWithNext { a, b ->
                    Duration.between(a.timestamp, b.timestamp).toMinutes().toDouble()
                }

                date to DailyVoidGap(
                    date = date,
                    averageGapMinutes = gaps.average(),
                    voidCount = dayEntries.size
                )
            }
            .toMap()

        // Fill all dates in range, using 0 for days without gap data
        // Batch fetch all schedules overlapping the range (single query instead of N queries)
        val schedules = voidScheduleRepository.getOverlapping(startDate, endDate)

        val allDates = generateSequence(startDate) { it.plusDays(1) }
            .takeWhile { !it.isAfter(endDate) }
            .toList()

        return allDates.map { date ->
            val schedule = schedules.firstOrNull { it.isActive(date) }
            val base = gapsByDate[date] ?: DailyVoidGap(date = date, averageGapMinutes = 0.0, voidCount = 0)
            base.copy(scheduleGoalMinutes = schedule?.intervalMinutes)
        }
    }
}
