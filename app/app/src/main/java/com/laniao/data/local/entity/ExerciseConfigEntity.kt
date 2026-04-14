package com.laniao.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.laniao.domain.model.ExerciseConfig
import com.laniao.domain.model.KegelType

/**
 * Room entity for ExerciseConfig.
 * Unique constraint on type ensures one config per Kegel type.
 */
@Entity(
    tableName = "exercise_configs",
    indices = [Index(value = ["type"], unique = true)]
)
data class ExerciseConfigEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: KegelType,
    val enabled: Boolean,
    val sessionsPerDay: Int,
    val sets: Int,
    val reps: Int,
    val holdSeconds: Int
) {
    /**
     * Convert entity to domain model.
     */
    fun toDomainModel(): ExerciseConfig = ExerciseConfig(
        id = id,
        type = type,
        enabled = enabled,
        sessionsPerDay = sessionsPerDay,
        sets = sets,
        reps = reps,
        holdSeconds = holdSeconds
    )

    companion object {
        /**
         * Convert domain model to entity.
         */
        fun fromDomainModel(config: ExerciseConfig): ExerciseConfigEntity = ExerciseConfigEntity(
            id = config.id,
            type = config.type,
            enabled = config.enabled,
            sessionsPerDay = config.sessionsPerDay,
            sets = config.sets,
            reps = config.reps,
            holdSeconds = config.holdSeconds
        )
    }
}
