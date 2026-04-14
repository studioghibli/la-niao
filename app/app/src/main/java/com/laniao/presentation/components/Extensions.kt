package com.laniao.presentation.components

import com.laniao.domain.model.Urgency

/**
 * Extension property for user-friendly urgency names.
 */
val Urgency.displayName: String
    get() = when (this) {
        Urgency.UNKNOWN -> "?"
        Urgency.NONE -> "None"
        Urgency.LOW -> "Low"
        Urgency.MEDIUM -> "Med"
        Urgency.HIGH -> "High"
        Urgency.BURST -> "\uD83C\uDF88"
    }
