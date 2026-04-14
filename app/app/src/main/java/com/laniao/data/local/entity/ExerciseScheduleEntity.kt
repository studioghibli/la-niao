package com.laniao.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.laniao.domain.model.ExerciseSchedule
import java.time.LocalDate

@Entity(tableName = "exercise_schedules")
data class ExerciseScheduleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val startDate: LocalDate,
    val endDate: LocalDate?,
    val enabled: Boolean,
    val createdAt: LocalDate
) {
    fun toDomainModel(): ExerciseSchedule = ExerciseSchedule(
        id = id,
        startDate = startDate,
        endDate = endDate,
        enabled = enabled,
        createdAt = createdAt
    )

    companion object {
        fun fromDomainModel(schedule: ExerciseSchedule): ExerciseScheduleEntity =
            ExerciseScheduleEntity(
                id = schedule.id,
                startDate = schedule.startDate,
                endDate = schedule.endDate,
                enabled = schedule.enabled,
                createdAt = schedule.createdAt
            )
    }
}
