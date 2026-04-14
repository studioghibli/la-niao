package com.laniao.domain.model

/**
 * Configuration for a Kegel exercise type.
 * User configures which types to do and parameters.
 *
 * @property id Unique identifier (0 for new configs)
 * @property type Type of Kegel exercise (STANDARD, QUICK, HOLD)
 * @property enabled Whether this exercise type is enabled
 * @property sessionsPerDay How many times per day (default: 3)
 * @property sets Number of sets per session (default: 1)
 * @property reps Number of reps per set (default: 12)
 * @property holdSeconds Hold duration in seconds (default: 5)
 */
data class ExerciseConfig(
    val id: Long = 0,
    val type: KegelType,
    val enabled: Boolean = true,
    val sessionsPerDay: Int = 3,
    val sets: Int = 1,
    val reps: Int = 12,
    val holdSeconds: Int = 5
)
