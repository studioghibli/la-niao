package com.laniao.domain.model

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

/**
 * A single drink entry recording liquid consumption.
 *
 * @property id Unique identifier (0 for new entries)
 * @property timestamp When the drink was consumed
 * @property type Type of drink (water, coffee, etc.)
 * @property amount Numeric amount in the given unit
 * @property unit Measurement unit (oz, ml, cups)
 * @property customName Name for CUSTOM drink type, null otherwise
 */
data class DrinkEntry(
    val id: Long = 0,
    val timestamp: Instant,
    val type: DrinkType = DrinkType.WATER,
    val amount: Double,
    val unit: DrinkUnit = DrinkUnit.ML,
    val customName: String? = null
) {
    /** Convert this entry's amount to liters. */
    val liters: Double
        get() = amount * unit.litersPerUnit

    fun getLocalDate(zoneId: ZoneId = ZoneId.systemDefault()): LocalDate =
        timestamp.atZone(zoneId).toLocalDate()
}
