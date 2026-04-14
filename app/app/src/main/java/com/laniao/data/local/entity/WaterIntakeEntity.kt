package com.laniao.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.laniao.domain.model.WaterAmount
import com.laniao.domain.model.WaterIntake
import java.time.LocalDate

/**
 * Room entity for WaterIntake.
 * One entry per day (unique date constraint).
 */
@Entity(
    tableName = "water_intake",
    indices = [Index(value = ["date"], unique = true)]
)
data class WaterIntakeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: LocalDate,
    val amount: WaterAmount
) {
    /**
     * Convert entity to domain model.
     */
    fun toDomainModel(): WaterIntake = WaterIntake(
        id = id,
        date = date,
        amount = amount
    )

    companion object {
        /**
         * Convert domain model to entity.
         */
        fun fromDomainModel(waterIntake: WaterIntake): WaterIntakeEntity = WaterIntakeEntity(
            id = waterIntake.id,
            date = waterIntake.date,
            amount = waterIntake.amount
        )
    }
}
