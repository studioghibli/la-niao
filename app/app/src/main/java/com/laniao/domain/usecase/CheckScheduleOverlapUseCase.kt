package com.laniao.domain.usecase

import com.laniao.domain.repository.VoidScheduleRepository
import java.time.LocalDate
import javax.inject.Inject

/**
 * Use case for checking if a date range overlaps with existing schedules.
 */
class CheckScheduleOverlapUseCase @Inject constructor(
    private val repository: VoidScheduleRepository
) {
    /**
     * Check if the given date range overlaps with any existing active schedule.
     * 
     * @param startDate Start of the date range
     * @param endDate End of the date range
     * @return true if there's an overlap, false otherwise
     */
    suspend operator fun invoke(startDate: LocalDate, endDate: LocalDate): Boolean {
        return repository.hasOverlapping(startDate, endDate)
    }
}
