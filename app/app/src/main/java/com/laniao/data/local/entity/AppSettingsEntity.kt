package com.laniao.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Single-row table for app settings.
 * Always uses id=1.
 */
@Entity(tableName = "app_settings")
data class AppSettingsEntity(
    @PrimaryKey
    val id: Int = 1,
    val hydrationGoalLiters: Double = 2.7
)
