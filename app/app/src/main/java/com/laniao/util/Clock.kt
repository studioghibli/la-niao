package com.laniao.util

import java.time.Instant

/**
 * Abstraction for time operations to enable testability.
 * Use this instead of Instant.now() directly in production code.
 */
interface Clock {
    fun now(): Instant
}

/**
 * Production implementation that uses the system clock.
 */
class SystemClock : Clock {
    override fun now(): Instant = Instant.now()
}
