package com.laniao.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.laniao.domain.model.DrinkType
import com.laniao.domain.model.DrinkUnit
import java.time.Instant

/**
 * Room entity for drink entries.
 */
@Entity(
    tableName = "drink_entries",
    indices = [Index(value = ["timestamp"])]
)
data class DrinkEntryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestamp: Instant,
    val type: DrinkType,
    val amount: Double,
    val unit: DrinkUnit,
    val customName: String?
)
