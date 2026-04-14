package com.laniao.domain.usecase

import com.laniao.domain.repository.ExerciseRepository
import java.time.LocalDate
import javax.inject.Inject

data class DayExerciseCompletion(
    val date: LocalDate,
    val totalRequired: Int,
    val completed: Int
) {
    val completionRate: Float
        get() = if (totalRequired > 0) completed.toFloat() / totalRequired else 0f
}

class GetExerciseCompletionUseCase @Inject constructor(
    private val repository: ExerciseRepository
) {
    suspend operator fun invoke(startDate: LocalDate, endDate: LocalDate): List<DayExerciseCompletion> {
        val days = mutableListOf<DayExerciseCompletion>()
        var current = startDate

        while (!current.isAfter(endDate)) {
            val schedule = repository.getActiveForDate(current)
            if (schedule != null) {
                val items = repository.getItemsForSchedule(schedule.id)
                val totalRequired = items.sumOf { it.sessionsPerDay }
                var completed = 0
                for (item in items) {
                    val count = repository.getCompletionCountByTypeAndDate(item.exerciseType, current)
                    completed += count.coerceAtMost(item.sessionsPerDay)
                }
                days.add(DayExerciseCompletion(
                    date = current,
                    totalRequired = totalRequired,
                    completed = completed
                ))
            } else {
                days.add(DayExerciseCompletion(
                    date = current,
                    totalRequired = 0,
                    completed = 0
                ))
            }
            current = current.plusDays(1)
        }
        return days
    }
}
