package com.laniao.domain.usecase

import com.laniao.domain.repository.PeeEntryRepository
import com.laniao.domain.repository.VoidScheduleRepository
import javax.inject.Inject

/**
 * Use case for deleting a void schedule.
 * Clears scheduledTime on all entries in the schedule's date range.
 */
class DeleteVoidScheduleUseCase @Inject constructor(
    private val repository: VoidScheduleRepository,
    private val entryRepository: PeeEntryRepository
) {
    /**
     * Delete a schedule by ID and clear scheduled times for its date range.
     * 
     * @param scheduleId The ID of the schedule to delete
     */
    suspend operator fun invoke(scheduleId: Long) {
        // Get the schedule before deleting so we know the date range to clear
        val schedule = repository.getById(scheduleId)
        repository.deleteById(scheduleId)

        // Clear scheduledTime on entries that were associated with this schedule
        if (schedule != null) {
            entryRepository.clearScheduledTimesForDateRange(schedule.createdAt, schedule.expiresAt)
        }
    }
}
