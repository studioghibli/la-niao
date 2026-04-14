package com.laniao.domain.usecase

import com.laniao.domain.repository.PeeEntryRepository
import com.laniao.domain.repository.VoidScheduleRepository
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

/**
 * Use case for clearing scheduledTime for all entries on a given day when the anchor changes.
 * 
 * When the first void of the day (anchor) changes, all scheduled times shift. Rather than
 * trying to match entries to new times, we simply clear their scheduledTime, making them
 * "unscheduled". This is Option A behavior - entries logged against the old schedule
 * become unassigned when the anchor shifts.
 * 
 * This is simpler and more predictable than trying to reassign entries to shifted times.
 */
class RecalculateScheduledTimesUseCase @Inject constructor(
    private val entryRepository: PeeEntryRepository,
    private val scheduleRepository: VoidScheduleRepository
) {
    /**
     * Clear scheduledTime for all entries on the given date (except the first void).
     * 
     * Called when the anchor (first void) changes, invalidating the old scheduled times.
     * 
     * @param date The date to clear
     * @param newAnchorTime The new anchor time (unused in Option A, kept for API consistency)
     */
    suspend operator fun invoke(date: LocalDate, newAnchorTime: LocalTime?) {
        // Get active schedule for the date - if no schedule, nothing to clear
        scheduleRepository.getActiveForDate(date)
            ?: return

        // Get all entries for the date that have a scheduledTime and clear them
        val entries = entryRepository.getEntriesWithScheduledTime(date)
        for (entry in entries) {
            entryRepository.update(entry.copy(scheduledTime = null))
        }
    }
}
