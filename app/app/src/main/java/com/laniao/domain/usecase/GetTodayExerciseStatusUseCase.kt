package com.laniao.domain.usecase

import com.laniao.domain.model.ExerciseStatus
import com.laniao.domain.repository.ExerciseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDate
import javax.inject.Inject

/**
 * Returns today's exercise status for all items in the active schedule.
 */
class GetTodayExerciseStatusUseCase @Inject constructor(
    private val repository: ExerciseRepository
) {
    operator fun invoke(today: LocalDate): Flow<List<ExerciseStatus>> {
        return repository.getActiveForDateFlow(today).combine(
            repository.getCompletionsByDate(today)
        ) { schedule, completions ->
            if (schedule == null) return@combine emptyList()

            val items = repository.getItemsForSchedule(schedule.id)
            items.map { item ->
                val completed = completions.count { it.exerciseType == item.exerciseType }
                ExerciseStatus(
                    exerciseType = item.exerciseType,
                    scheduleItemId = item.id,
                    sessionsRequired = item.sessionsPerDay,
                    completedToday = completed,
                    sets = item.sets,
                    reps = item.reps,
                    holdSeconds = item.holdSeconds
                )
            }
        }
    }
}
