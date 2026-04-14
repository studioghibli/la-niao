package com.laniao.domain.usecase

import com.laniao.domain.model.VoidSchedule
import com.laniao.domain.repository.VoidScheduleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for getting all schedules (active, upcoming, and historical).
 */
class GetAllSchedulesUseCase @Inject constructor(
    private val repository: VoidScheduleRepository
) {
    operator fun invoke(): Flow<List<VoidSchedule>> = repository.getAll()
}
