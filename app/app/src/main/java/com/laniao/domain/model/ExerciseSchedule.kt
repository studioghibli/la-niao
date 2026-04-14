package com.laniao.domain.model

import java.time.LocalDate

/**
 * An exercise schedule defining which exercises to do and for how long.
 * Similar to VoidSchedule but for exercises.
 *
 * @property id Unique identifier
 * @property startDate When the schedule starts
 * @property endDate When the schedule ends (null = forever)
 * @property enabled Whether the schedule is active
 * @property createdAt When the schedule was created
 */
data class ExerciseSchedule(
    val id: Long = 0,
    val startDate: LocalDate,
    val endDate: LocalDate?,
    val enabled: Boolean = true,
    val createdAt: LocalDate = LocalDate.now()
) {
    val isActive: Boolean
        get() {
            val today = LocalDate.now()
            return enabled &&
                !today.isBefore(startDate) &&
                (endDate == null || !today.isAfter(endDate))
        }

    val daysRemaining: Int?
        get() = endDate?.let {
            java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), it).toInt().coerceAtLeast(0)
        }
}
