package com.laniao.domain.model

import java.time.Instant
import java.time.LocalDate

/**
 * Record of a completed exercise session.
 */
data class ExerciseCompletion(
    val id: Long = 0,
    val scheduleItemId: Long? = null,
    val exerciseType: ExerciseType,
    val completedAt: Instant,
    val scheduledDate: LocalDate
)
