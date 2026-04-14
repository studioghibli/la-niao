package com.laniao.domain.model

import java.time.LocalDate

/**
 * Daily water consumption tracking.
 * One entry per day (upsert behavior - update if already exists).
 *
 * @property id Unique identifier (0 for new entries)
 * @property date The date this water intake was recorded for
 * @property amount The amount category (LITTLE, NORMAL, A_LOT)
 */
data class WaterIntake(
    val id: Long = 0,
    val date: LocalDate,
    val amount: WaterAmount
)
