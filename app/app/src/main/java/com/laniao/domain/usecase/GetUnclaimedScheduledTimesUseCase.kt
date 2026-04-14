package com.laniao.domain.usecase

import com.laniao.domain.model.PeeEntry
import com.laniao.domain.repository.PeeEntryRepository
import com.laniao.domain.repository.VoidScheduleRepository
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject

/**
 * Use case for getting unclaimed scheduled times for a date.
 * 
 * Returns scheduled times that have not been associated with any entry.
 * Also provides the nearest unclaimed time for auto-suggestion.
 */
class GetUnclaimedScheduledTimesUseCase @Inject constructor(
    private val scheduleRepository: VoidScheduleRepository,
    private val entryRepository: PeeEntryRepository
) {
    /**
     * Get all unclaimed scheduled times for a date.
     * Returns empty list if no active schedule exists for the date.
     */
    suspend operator fun invoke(date: LocalDate): List<LocalTime> {
        val schedule = scheduleRepository.getActiveForDate(date) ?: return emptyList()
        
        // Get the first void of the day to determine anchor
        val firstVoid = entryRepository.getFirstVoidOfDay(date)
        val anchorTime = firstVoid?.getLocalTime() ?: schedule.startTime
        
        // Get all scheduled times for the day
        val scheduledTimes = schedule.getScheduledTimes(anchorTime)
        
        // Filter out claimed times
        val unclaimedTimes = mutableListOf<LocalTime>()
        for (time in scheduledTimes) {
            val entriesAtTime = entryRepository.getByScheduledTime(date, time)
            if (entriesAtTime.isEmpty()) {
                unclaimedTimes.add(time)
            }
        }
        
        return unclaimedTimes
    }

    /**
     * Get the nearest unclaimed scheduled time to the given time.
     * Uses absolute time difference (can be past or future).
     * Returns null if no unclaimed times available.
     */
    suspend fun getNearestTo(date: LocalDate, targetTime: LocalTime): LocalTime? {
        val unclaimedTimes = invoke(date)
        if (unclaimedTimes.isEmpty()) return null

        return unclaimedTimes.minByOrNull { time ->
            kotlin.math.abs(Duration.between(time, targetTime).toMinutes())
        }
    }
}
