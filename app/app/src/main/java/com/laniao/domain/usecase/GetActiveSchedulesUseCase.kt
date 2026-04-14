package com.laniao.domain.usecase

import com.laniao.domain.model.VoidSchedule
import com.laniao.domain.repository.VoidScheduleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for getting all active (non-expired) schedules.
 */
class GetActiveSchedulesUseCase @Inject constructor(
    private val repository: VoidScheduleRepository
) {
    /**
     * Get all active schedules as a Flow.
     * Active = enabled and not expired.
     */
    operator fun invoke(): Flow<List<VoidSchedule>> = repository.getActive()
}
