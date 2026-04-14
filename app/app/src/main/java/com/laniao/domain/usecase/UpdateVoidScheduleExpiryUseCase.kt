package com.laniao.domain.usecase

import com.laniao.domain.model.VoidSchedule
import com.laniao.domain.repository.PeeEntryRepository
import com.laniao.domain.repository.VoidScheduleRepository
import java.time.LocalDate
import javax.inject.Inject

/**
 * Use case for updating the expiry date of a void schedule.
 * Clears scheduledTime on affected entries to force manual re-association.
 */
class UpdateVoidScheduleExpiryUseCase @Inject constructor(
    private val repository: VoidScheduleRepository,
    private val entryRepository: PeeEntryRepository
) {
    /**
     * Update the expiry date of an existing schedule.
     * Clears scheduledTime on all entries in the schedule's full date range.
     * 
     * @param scheduleId The ID of the schedule to update
     * @param newExpiryDate The new expiry date
     * @throws IllegalArgumentException if the schedule doesn't exist or expiry date is invalid
     */
    suspend operator fun invoke(scheduleId: Long, newExpiryDate: LocalDate) {
        val schedule = repository.getById(scheduleId)
            ?: throw IllegalArgumentException("Schedule not found")
        
        require(newExpiryDate >= schedule.createdAt) { 
            "Expiry date cannot be before creation date" 
        }

        // Determine the widest affected range (old expiry vs new expiry)
        val clearEnd = maxOf(schedule.expiresAt, newExpiryDate)
        entryRepository.clearScheduledTimesForDateRange(schedule.createdAt, clearEnd)

        repository.update(schedule.copy(expiresAt = newExpiryDate))
    }
}
