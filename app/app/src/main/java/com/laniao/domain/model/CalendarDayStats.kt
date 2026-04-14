package com.laniao.domain.model

import java.time.LocalDate

/**
 * Summary stats for a single day, used in the calendar month grid.
 */
data class CalendarDayStats(
    val date: LocalDate,
    val voidCount: Int = 0,
    val scheduledCount: Int = 0,
    val hadBurstUrgency: Boolean = false,
    val kegelsComplete: Boolean = false,
    val metHydrationGoal: Boolean = false,
    val perfectSchedule: Boolean = false,
    val hasVoidSchedule: Boolean = false,
    val hasExerciseSchedule: Boolean = false
)
