package com.laniao.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.laniao.domain.model.ExerciseCompletion
import com.laniao.domain.model.ExerciseType
import java.time.Instant
import java.time.LocalDate

@Entity(
    tableName = "exercise_completions",
    indices = [
        Index(value = ["scheduleItemId"]),
        Index(value = ["scheduledDate"]),
        Index(value = ["completedAt"])
    ]
)
data class ExerciseCompletionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val scheduleItemId: Long? = null,
    val exerciseType: ExerciseType,
    val completedAt: Instant,
    val scheduledDate: LocalDate
) {
    fun toDomainModel(): ExerciseCompletion = ExerciseCompletion(
        id = id,
        scheduleItemId = scheduleItemId,
        exerciseType = exerciseType,
        completedAt = completedAt,
        scheduledDate = scheduledDate
    )

    companion object {
        fun fromDomainModel(completion: ExerciseCompletion): ExerciseCompletionEntity =
            ExerciseCompletionEntity(
                id = completion.id,
                scheduleItemId = completion.scheduleItemId,
                exerciseType = completion.exerciseType,
                completedAt = completion.completedAt,
                scheduledDate = completion.scheduledDate
            )
    }
}
