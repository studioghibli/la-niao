package com.laniao.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.laniao.domain.model.LeakAmount
import com.laniao.domain.model.PeeColor
import com.laniao.domain.model.PeeEntry
import com.laniao.domain.model.Urgency
import com.laniao.domain.model.VolumeSize
import java.time.Instant
import java.time.LocalTime

/**
 * Room entity for PeeEntry.
 * Includes index on timestamp for efficient date range queries.
 */
@Entity(
    tableName = "pee_entries",
    indices = [Index(value = ["timestamp"])]
)
data class PeeEntryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestamp: Instant,
    val didVoid: Boolean,
    val leakAmount: LeakAmount,
    val volumeSize: VolumeSize,
    val color: PeeColor,
    val urgency: Urgency,
    val activityContext: String?,
    val notes: String?,
    val scheduledTime: LocalTime?
) {
    /**
     * Convert entity to domain model.
     */
    fun toDomainModel(): PeeEntry = PeeEntry(
        id = id,
        timestamp = timestamp,
        didVoid = didVoid,
        leakAmount = leakAmount,
        volumeSize = volumeSize,
        color = color,
        urgency = urgency,
        activityContext = activityContext,
        notes = notes,
        scheduledTime = scheduledTime
    )

    companion object {
        /**
         * Convert domain model to entity.
         */
        fun fromDomainModel(entry: PeeEntry): PeeEntryEntity = PeeEntryEntity(
            id = entry.id,
            timestamp = entry.timestamp,
            didVoid = entry.didVoid,
            leakAmount = entry.leakAmount,
            volumeSize = entry.volumeSize,
            color = entry.color,
            urgency = entry.urgency,
            activityContext = entry.activityContext,
            notes = entry.notes,
            scheduledTime = entry.scheduledTime
        )
    }
}
