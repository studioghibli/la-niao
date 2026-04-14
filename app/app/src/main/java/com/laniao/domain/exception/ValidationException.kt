package com.laniao.domain.exception

/**
 * Thrown when validation fails (e.g., notes exceed 500 characters).
 */
class ValidationException(message: String) : Exception(message)
