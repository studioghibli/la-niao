package com.laniao.domain.usecase

import com.laniao.domain.repository.PeeEntryRepository
import com.laniao.domain.repository.VoidScheduleRepository
import com.laniao.util.Clock
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject

/**
 * Use case for getting today's scheduled void times.
 * Uses dynamic anchor based on first void of the day.
 */
class GetDailyScheduledTimesUseCase @Inject constructor(
    private val scheduleRepository: VoidScheduleRepository,
    private val entryRepository: PeeEntryRepository,
    private val clock: Clock
) {
    /**
     * Get scheduled times for today as a Flow.
     * Automatically recalculates when entries change (anchor changes).
     */
    operator fun invoke(): Flow<List<LocalTime>> {
        val today = clock.now().atZone(ZoneId.systemDefault()).toLocalDate()
        return invoke(today)
    }

    /**
     * Get scheduled times for a specific date as a Flow.
     */
    fun invoke(date: LocalDate): Flow<List<LocalTime>> {
        val entriesFlow = entryRepository.getByDate(date)
        
        return entriesFlow.map { entries ->
            // Get active schedule for the date
            val schedule = scheduleRepository.getActiveForDate(date)
                ?: return@map emptyList()

            // Find first void of the day (by timestamp)
            val voidEntries = entries
                .filter { it.didVoid }
                .sortedBy { it.timestamp }
            
            val firstVoidTime = voidEntries.firstOrNull()?.let {
                it.timestamp.atZone(ZoneId.systemDefault()).toLocalTime()
            }

            // Calculate scheduled times with anchor
            schedule.getScheduledTimes(anchorTime = firstVoidTime)
        }
    }

    /**
     * Get scheduled times for a specific date synchronously.
     * For use in places where Flow is not needed.
     */
    suspend fun getForDate(date: LocalDate): List<LocalTime> {
        val schedule = scheduleRepository.getActiveForDate(date)
            ?: return emptyList()

        val firstVoidEntry = entryRepository.getFirstVoidOfDay(date)
        val firstVoidTime = firstVoidEntry?.let {
            it.timestamp.atZone(ZoneId.systemDefault()).toLocalTime()
        }

        return schedule.getScheduledTimes(anchorTime = firstVoidTime)
    }
}
