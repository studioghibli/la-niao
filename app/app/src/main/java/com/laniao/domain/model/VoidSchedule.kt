package com.laniao.domain.model

import java.time.LocalDate
import java.time.LocalTime

/**
 * Custom scheduled voiding times for bladder training.
 *
 * @property id Unique identifier (0 for new schedules)
 * @property startTime Typical start time (e.g., 09:00)
 * @property endTime Fixed end time (e.g., 21:00)
 * @property intervalMinutes Minutes between scheduled voids (e.g., 120 for every 2 hours)
 * @property enabled Whether the schedule is currently active
 * @property createdAt Date when schedule was created
 * @property expiresAt Schedule active until this date (inclusive, through 11:59 PM)
 */
data class VoidSchedule(
    val id: Long = 0,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val intervalMinutes: Int,
    val enabled: Boolean = true,
    val createdAt: LocalDate,
    val expiresAt: LocalDate
) {
    /**
     * Days remaining until schedule expires.
     * Returns 0 if already expired.
     */
    fun daysRemaining(today: LocalDate = LocalDate.now()): Int =
        maxOf(0, (expiresAt.toEpochDay() - today.toEpochDay()).toInt())

    /**
     * Check if schedule is active on the given date.
     */
    fun isActive(today: LocalDate = LocalDate.now()): Boolean =
        enabled && today <= expiresAt && today >= createdAt

    /**
     * Generate all scheduled times for the day based on start time.
     * Only includes intervals that fit completely before end time.
     *
     * @param anchorTime Optional anchor time (e.g., first void of day). If null, uses startTime.
     */
    fun getScheduledTimes(anchorTime: LocalTime? = null): List<LocalTime> {
        val times = mutableListOf<LocalTime>()
        var current = anchorTime ?: startTime
        
        // If anchor is after end time, no scheduled times for the day
        if (current > endTime) return emptyList()
        
        while (current <= endTime) {
            times.add(current)
            val next = current.plusMinutes(intervalMinutes.toLong())
            // Check for midnight wraparound (next < current) or past end time
            if (next < current || next > endTime) break
            current = next
        }
        return times
    }

}
