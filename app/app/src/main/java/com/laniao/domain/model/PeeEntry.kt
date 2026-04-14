package com.laniao.domain.model

import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

/**
 * Core data model representing a single urination event or urge.
 *
 * @property id Unique identifier (0 for new entries)
 * @property timestamp When the event occurred
 * @property didVoid True if voided, false if urge only (resisted without voiding)
 * @property leakAmount Leak amount (independent of didVoid; NONE means no leak)
 * @property volumeSize Size of void (ignored if didVoid=false)
 * @property color Urine color (ignored if didVoid=false)
 * @property urgency Level of urgency felt
 * @property activityContext Optional context text (what user was doing)
 * @property notes Optional notes (max 500 characters)
 * @property scheduledTime Associated scheduled time, null = explicitly unscheduled
 */
data class PeeEntry(
    val id: Long = 0,
    val timestamp: Instant,
    val didVoid: Boolean = true,
    val leakAmount: LeakAmount = LeakAmount.NONE,
    val volumeSize: VolumeSize = VolumeSize.UNKNOWN,
    val color: PeeColor = PeeColor.UNKNOWN,
    val urgency: Urgency = Urgency.UNKNOWN,
    val activityContext: String? = null,
    val notes: String? = null,
    val scheduledTime: LocalTime? = null
) {
    /** True when the entry is a standalone leak (no void). */
    val isLeakOnly: Boolean
        get() = !didVoid && leakAmount != LeakAmount.NONE

    /** True when the entry is an urge-only (no void, no leak). */
    val isUrgeOnly: Boolean
        get() = !didVoid && leakAmount == LeakAmount.NONE

    /** True when any leak was recorded, regardless of void status. */
    val hasLeak: Boolean
        get() = leakAmount != LeakAmount.NONE

    /**
     * Calculate minutes early (negative) or late (positive) compared to schedule.
     * Returns null if not associated with a scheduled time.
     */
    fun minutesFromSchedule(zoneId: ZoneId = ZoneId.systemDefault()): Int? {
        scheduledTime ?: return null
        val entryDate = timestamp.atZone(zoneId).toLocalDate()
        val scheduledInstant = scheduledTime.atDate(entryDate).atZone(zoneId).toInstant()
        return Duration.between(scheduledInstant, timestamp).toMinutes().toInt()
    }

    /**
     * Get the local date of this entry in the given timezone.
     */
    fun getLocalDate(zoneId: ZoneId = ZoneId.systemDefault()): LocalDate =
        timestamp.atZone(zoneId).toLocalDate()

    /**
     * Get the local time of this entry in the given timezone.
     */
    fun getLocalTime(zoneId: ZoneId = ZoneId.systemDefault()): LocalTime =
        timestamp.atZone(zoneId).toLocalTime()
}
