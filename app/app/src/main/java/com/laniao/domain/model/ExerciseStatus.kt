package com.laniao.domain.model

/**
 * Today's status for a single exercise type.
 * Used on the Home screen checklist.
 */
data class ExerciseStatus(
    val exerciseType: ExerciseType,
    val scheduleItemId: Long,
    val sessionsRequired: Int,
    val completedToday: Int,
    val sets: Int,
    val reps: Int,
    val holdSeconds: Int
) {
    val isComplete: Boolean get() = completedToday >= sessionsRequired
    val remaining: Int get() = (sessionsRequired - completedToday).coerceAtLeast(0)
}
