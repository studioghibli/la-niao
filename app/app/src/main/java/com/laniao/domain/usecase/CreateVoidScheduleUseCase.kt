package com.laniao.domain.usecase

import com.laniao.domain.exception.ScheduleOverlapException
import com.laniao.domain.model.VoidSchedule
import com.laniao.domain.repository.PeeEntryRepository
import com.laniao.domain.repository.VoidScheduleRepository
import com.laniao.util.Clock
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject

/**
 * Use case for creating a new void schedule.
 * Validates the schedule and checks for overlaps.
 */
class CreateVoidScheduleUseCase @Inject constructor(
    private val repository: VoidScheduleRepository,
    private val entryRepository: PeeEntryRepository,
    private val clock: Clock
) {
    /**
     * Create a new void schedule.
     * 
     * @param startTime Typical start time for the schedule
     * @param endTime End time for the schedule
     * @param intervalMinutes Minutes between scheduled voids
     * @param durationDays Number of days the schedule should be active (used when startDate/endDate not specified)
     * @param replaceExisting If true, deletes overlapping schedules before creating
     * @param startDate Optional explicit start date (defaults to today)
     * @param endDate Optional explicit end date (overrides durationDays when provided)
     * @return The created schedule with generated ID
     * @throws IllegalArgumentException if validation fails
     * @throws ScheduleOverlapException if overlapping schedule exists and replaceExisting is false
     */
    suspend operator fun invoke(
        startTime: LocalTime,
        endTime: LocalTime,
        intervalMinutes: Int,
        durationDays: Int = 7,
        replaceExisting: Boolean = false,
        startDate: LocalDate? = null,
        endDate: LocalDate? = null
    ): VoidSchedule {
        // Validation
        require(startTime < endTime) { "Start time must be before end time" }
        require(intervalMinutes in 30..480) { "Interval must be between 30 minutes and 8 hours" }

        val today = clock.now().atZone(ZoneId.systemDefault()).toLocalDate()
        val scheduleStart = startDate ?: today
        val scheduleEnd = endDate ?: scheduleStart.plusDays(durationDays.toLong() - 1)

        require(scheduleEnd >= scheduleStart) { "End date must be on or after start date" }
        val totalDays = (scheduleEnd.toEpochDay() - scheduleStart.toEpochDay()).toInt() + 1
        require(totalDays in 1..365) { "Schedule duration must be between 1 and 365 days" }

        // Check for overlaps
        val hasOverlap = repository.hasOverlapping(scheduleStart, scheduleEnd)
        if (hasOverlap) {
            if (replaceExisting) {
                // Delete overlapping schedules and clear their entries' scheduled times
                val overlapping = repository.getOverlapping(scheduleStart, scheduleEnd)
                overlapping.forEach { old ->
                    entryRepository.clearScheduledTimesForDateRange(old.createdAt, old.expiresAt)
                    repository.delete(old)
                }
            } else {
                throw ScheduleOverlapException("An active schedule already exists for this date range")
            }
        }

        val schedule = VoidSchedule(
            startTime = startTime,
            endTime = endTime,
            intervalMinutes = intervalMinutes,
            enabled = true,
            createdAt = scheduleStart,
            expiresAt = scheduleEnd
        )

        // Clear scheduledTime on all entries in the affected date range.
        // This forces users to manually re-associate entries with the new schedule.
        entryRepository.clearScheduledTimesForDateRange(scheduleStart, scheduleEnd)

        val id = repository.insert(schedule)
        return schedule.copy(id = id)
    }
}
