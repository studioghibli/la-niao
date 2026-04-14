package com.laniao.domain.model

import androidx.compose.ui.graphics.Color

/**
 * Standardized color scale for urine color tracking.
 * Used for hydration monitoring.
 */
enum class PeeColor(
    /** Hydration level score (5 = best hydration, 0 = worst, null = unknown) */
    val hydrationLevel: Int?
) {
    /** Not recorded - displayed as gray in UI */
    UNKNOWN(null),
    /** Very well hydrated */
    CLEAR(5),
    /** Normal, healthy */
    LIGHT_YELLOW(4),
    /** Normal */
    YELLOW(3),
    /** Drink more water */
    DARK_YELLOW(2),
    /** Dehydrated */
    AMBER(1);

    /**
     * Returns the display color for UI rendering.
     */
    fun toDisplayColor(): Color = when (this) {
        UNKNOWN -> Color(0xFF9E9E9E)      // Gray
        CLEAR -> Color(0xFFE3F2FD)         // Very light blue
        LIGHT_YELLOW -> Color(0xFFFFF9C4) // Light yellow
        YELLOW -> Color(0xFFFFEE58)       // Yellow
        DARK_YELLOW -> Color(0xFFFBC02D)  // Dark yellow
        AMBER -> Color(0xFFFF8F00)        // Amber/orange
    }
}
