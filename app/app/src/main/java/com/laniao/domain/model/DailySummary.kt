package com.laniao.domain.model

/**
 * Summary data for a single day.
 * Used to display daily statistics on the Home screen.
 */
data class DailySummary(
    /** Total number of void entries for the day */
    val voidCount: Int = 0,
    /** Number of urge-only entries (resisted without voiding) */
    val urgeOnlyCount: Int = 0,
    /** Number of leak-only entries */
    val leakOnlyCount: Int = 0,
    /** Total entries with any leak (standalone + void-with-leak) */
    val totalLeakCount: Int = 0,
    /** Number of scheduled voids completed */
    val scheduledCount: Int = 0,
    /** Number of unscheduled voids */
    val unscheduledCount: Int = 0,
    /** Number of missed scheduled times (past, unclaimed, after grace period) */
    val missedCount: Int = 0
)
