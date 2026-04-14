package com.laniao.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime

/**
 * Room entity for manually missed scheduled void times.
 * When a user explicitly marks a scheduled time as "missed", a row is inserted here.
 */
@Entity(
    tableName = "manually_missed_times",
    indices = [Index(value = ["date", "scheduledTime"], unique = true)]
)
data class ManuallyMissedTimeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: LocalDate,
    val scheduledTime: LocalTime,
    val createdAt: Instant
)
