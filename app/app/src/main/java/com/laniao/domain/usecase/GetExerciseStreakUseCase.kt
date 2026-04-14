package com.laniao.domain.usecase

import com.laniao.domain.repository.ExerciseRepository
import java.time.LocalDate
import javax.inject.Inject

/**
 * Calculates current exercise streak (consecutive complete days).
 * A day is complete when ALL exercise types in the active schedule have met their session count.
 */
class GetExerciseStreakUseCase @Inject constructor(
    private val repository: ExerciseRepository
) {
    suspend operator fun invoke(today: LocalDate): Int {
        val schedule = repository.getActiveForDate(today) ?: return 0
        val items = repository.getItemsForSchedule(schedule.id)
        if (items.isEmpty()) return 0

        var streak = 0
        var day = today
        val maxLookback = 365

        for (i in 0 until maxLookback) {
            // Only count days within schedule range
            if (day.isBefore(schedule.startDate)) break

            val allComplete = items.all { item ->
                val count = repository.getCompletionCountByTypeAndDate(item.exerciseType, day)
                count >= item.sessionsPerDay
            }

            if (allComplete) {
                streak++
                day = day.minusDays(1)
            } else {
                break
            }
        }

        return streak
    }
}
