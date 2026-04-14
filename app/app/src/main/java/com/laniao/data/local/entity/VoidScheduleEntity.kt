package com.laniao.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.laniao.domain.model.VoidSchedule
import java.time.LocalDate
import java.time.LocalTime

/**
 * Room entity for VoidSchedule.
 * Includes indices for efficient active schedule queries.
 */
@Entity(
    tableName = "void_schedules",
    indices = [
        Index(value = ["enabled"]),
        Index(value = ["expiresAt"])
    ]
)
data class VoidScheduleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val intervalMinutes: Int,
    val enabled: Boolean,
    val createdAt: LocalDate,
    val expiresAt: LocalDate
) {
    /**
     * Convert entity to domain model.
     */
    fun toDomainModel(): VoidSchedule = VoidSchedule(
        id = id,
        startTime = startTime,
        endTime = endTime,
        intervalMinutes = intervalMinutes,
        enabled = enabled,
        createdAt = createdAt,
        expiresAt = expiresAt
    )

    companion object {
        /**
         * Convert domain model to entity.
         */
        fun fromDomainModel(schedule: VoidSchedule): VoidScheduleEntity = VoidScheduleEntity(
            id = schedule.id,
            startTime = schedule.startTime,
            endTime = schedule.endTime,
            intervalMinutes = schedule.intervalMinutes,
            enabled = schedule.enabled,
            createdAt = schedule.createdAt,
            expiresAt = schedule.expiresAt
        )
    }
}
