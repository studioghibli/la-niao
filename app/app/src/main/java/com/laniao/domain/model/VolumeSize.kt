package com.laniao.domain.model

/**
 * Simplified volume tracking for urination events.
 */
enum class VolumeSize {
    /** Not recorded (for retroactive entries) */
    UNKNOWN,
    /** Brief, minimal output */
    SMALL,
    /** Normal, typical void */
    MEDIUM,
    /** Full bladder, extended void */
    LARGE
}
