package com.laniao.domain.model

/**
 * Leak amount. Can appear on any entry type (void, urge-only, or leak-only).
 * NONE means no leak occurred.
 */
enum class LeakAmount {
    NONE,
    SMALL,
    MEDIUM,
    LARGE
}
