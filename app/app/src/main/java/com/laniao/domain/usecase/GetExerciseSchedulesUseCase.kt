package com.laniao.domain.usecase

import com.laniao.domain.model.ExerciseSchedule
import com.laniao.domain.model.ExerciseScheduleItem
import com.laniao.domain.repository.ExerciseRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

/**
 * Use cases for exercise schedule management.
 */
class GetExerciseSchedulesUseCase @Inject constructor(
    private val repository: ExerciseRepository
) {
    fun all(): Flow<List<ExerciseSchedule>> = repository.getAllSchedules()
    suspend fun activeForDate(date: LocalDate): ExerciseSchedule? = repository.getActiveForDate(date)
    fun activeForDateFlow(date: LocalDate): Flow<ExerciseSchedule?> = repository.getActiveForDateFlow(date)
    suspend fun getItems(scheduleId: Long): List<ExerciseScheduleItem> = repository.getItemsForSchedule(scheduleId)
    fun getItemsFlow(scheduleId: Long): Flow<List<ExerciseScheduleItem>> = repository.getItemsForScheduleFlow(scheduleId)
}
