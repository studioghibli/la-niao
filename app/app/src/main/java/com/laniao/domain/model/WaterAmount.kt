package com.laniao.domain.model

/**
 * Daily water consumption level.
 * Used to correlate hydration with urine color in statistics.
 */
enum class WaterAmount(
    /** Human-readable description */
    val description: String,
    /** Approximate liters per day */
    val litersApprox: Double
) {
    /** Less than typical daily intake (~1.8 liters) */
    LITTLE("A little", 1.8),
    /** Recommended daily intake (~2.7 liters) */
    NORMAL("Normal", 2.7),
    /** More than typical daily intake (~3.6 liters) */
    A_LOT("A lot", 3.6)
}
