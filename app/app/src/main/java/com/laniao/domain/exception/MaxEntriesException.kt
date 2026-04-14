package com.laniao.domain.exception

/**
 * Thrown when attempting to add more than 50 entries per day.
 */
class MaxEntriesException(message: String) : Exception(message)
