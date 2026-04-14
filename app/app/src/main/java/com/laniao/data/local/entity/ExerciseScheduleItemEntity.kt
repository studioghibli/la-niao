package com.laniao.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.laniao.domain.model.ExerciseScheduleItem
import com.laniao.domain.model.ExerciseType

@Entity(
    tableName = "exercise_schedule_items",
    foreignKeys = [
        ForeignKey(
            entity = ExerciseScheduleEntity::class,
            parentColumns = ["id"],
            childColumns = ["scheduleId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["scheduleId"])]
)
data class ExerciseScheduleItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val scheduleId: Long,
    val exerciseType: ExerciseType,
    val sessionsPerDay: Int,
    val sets: Int,
    val reps: Int,
    val holdSeconds: Int
) {
    fun toDomainModel(): ExerciseScheduleItem = ExerciseScheduleItem(
        id = id,
        scheduleId = scheduleId,
        exerciseType = exerciseType,
        sessionsPerDay = sessionsPerDay,
        sets = sets,
        reps = reps,
        holdSeconds = holdSeconds
    )

    companion object {
        fun fromDomainModel(item: ExerciseScheduleItem): ExerciseScheduleItemEntity =
            ExerciseScheduleItemEntity(
                id = item.id,
                scheduleId = item.scheduleId,
                exerciseType = item.exerciseType,
                sessionsPerDay = item.sessionsPerDay,
                sets = item.sets,
                reps = item.reps,
                holdSeconds = item.holdSeconds
            )
    }
}
