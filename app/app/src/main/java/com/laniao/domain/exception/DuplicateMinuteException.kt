package com.laniao.domain.exception

/**
 * Thrown when attempting to add an entry in the same minute as an existing entry.
 */
class DuplicateMinuteException(message: String) : Exception(message)
