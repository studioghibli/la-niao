package com.laniao.domain.exception

/**
 * Thrown when attempting to create a schedule that overlaps with an existing active schedule.
 */
class ScheduleOverlapException(message: String) : Exception(message)
