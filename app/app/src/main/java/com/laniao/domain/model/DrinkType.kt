package com.laniao.domain.model

/**
 * Types of drinks that can be logged.
 */
enum class DrinkType(val displayName: String) {
    WATER("Water"),
    SPARKLING_WATER("Sparkling water"),
    TEA("Tea"),
    COFFEE("Coffee"),
    MILK("Milk"),
    JUICE("Juice"),
    CUSTOM("Custom")
}
