package com.laniao.domain.model

/**
 * Units for measuring drink amounts.
 */
enum class DrinkUnit(val displayName: String, val litersPerUnit: Double) {
    /** Fluid ounces → liters (1 oz ≈ 0.02957 L) */
    OZ("oz", 0.02957),
    /** Milliliters → liters (1 ml = 0.001 L) */
    ML("ml", 0.001),
    /** Cups → liters (1 cup ≈ 0.23659 L) */
    CUPS("cups", 0.23659)
}
