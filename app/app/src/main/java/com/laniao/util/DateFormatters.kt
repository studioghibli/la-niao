package com.laniao.util

import java.time.format.DateTimeFormatter

/**
 * Shared date/time formatters used throughout the app.
 * Centralizes format patterns for consistency.
 */
object DateFormatters {
    /** "MMM d, yyyy" — e.g. "Mar 14, 2026". Most common date display. */
    val DATE_SHORT: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy")

    /** "EEEE, MMMM d, yyyy" — e.g. "Saturday, March 14, 2026". Full date headers. */
    val DATE_LONG: DateTimeFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy")

    /** "EEEE, MMM d, yyyy" — e.g. "Saturday, Mar 14, 2026". Timeline date headers. */
    val DATE_MEDIUM: DateTimeFormatter = DateTimeFormatter.ofPattern("EEEE, MMM d, yyyy")

    /** "EEE, MMM d, yyyy" — e.g. "Sat, Mar 14, 2026". Compact with weekday. */
    val DATE_COMPACT: DateTimeFormatter = DateTimeFormatter.ofPattern("EEE, MMM d, yyyy")

    /** "MMM d" — e.g. "Mar 14". Short date without year. */
    val DATE_SHORT_NO_YEAR: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM d")

    /** "EEE, MMM d" — e.g. "Sat, Mar 14". Feed date headers. */
    val DATE_FEED: DateTimeFormatter = DateTimeFormatter.ofPattern("EEE, MMM d")

    /** "M/d" — e.g. "3/14". Chart axis labels. */
    val DATE_CHART: DateTimeFormatter = DateTimeFormatter.ofPattern("M/d")

    /** "yyyy-MM-dd" — e.g. "2026-03-14". File names and exports. */
    val DATE_FILE: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    /** "HH:mm" — e.g. "14:30". All UI time display uses 24-hour format. */
    val TIME_SHORT: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    /** "HH:mm:ss" — e.g. "14:30:00". CSV export time. */
    val TIME_CSV: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
}
