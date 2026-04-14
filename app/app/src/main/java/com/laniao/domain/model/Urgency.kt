package com.laniao.domain.model

/**
 * Urgency level for urination events.
 * BURST (🎈) indicates maximum urgency on a void entry.
 */
enum class Urgency {
    /** Not recorded */
    UNKNOWN,
    /** No urgency felt */
    NONE,
    /** Mild urgency */
    LOW,
    /** Moderate urgency */
    MEDIUM,
    /** Strong urgency */
    HIGH,
    /** Maximum urgency - bursting (🎈) */
    BURST
}
