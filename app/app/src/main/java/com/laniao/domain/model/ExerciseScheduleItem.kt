package com.laniao.domain.model

/**
 * A single exercise type configured within a schedule.
 * Each schedule can have multiple items (one per exercise type).
 */
data class ExerciseScheduleItem(
    val id: Long = 0,
    val scheduleId: Long,
    val exerciseType: ExerciseType,
    val sessionsPerDay: Int = 3,
    val sets: Int = 1,
    val reps: Int = 12,
    val holdSeconds: Int = 5
)
