package com.laniao.domain.model

/**
 * All exercise types with category, display info, and default parameters.
 */
enum class ExerciseType(
    val category: ExerciseCategory,
    val displayName: String,
    val description: String,
    val emoji: String,
    val defaultSessionsPerDay: Int = 3,
    val defaultSets: Int = 1,
    val defaultReps: Int = 12,
    val defaultHoldSeconds: Int = 5
) {
    // Kegel exercises
    STANDARD_KEGEL(
        category = ExerciseCategory.KEGEL,
        displayName = "Standard Kegel",
        description = "Basic squeeze and hold",
        emoji = "\uD83D\uDCAA"
    ),
    QUICK_KEGEL(
        category = ExerciseCategory.KEGEL,
        displayName = "Quick Kegel",
        description = "Rapid contractions",
        emoji = "\u26A1",
        defaultHoldSeconds = 1
    ),
    HOLD_KEGEL(
        category = ExerciseCategory.KEGEL,
        displayName = "Hold Kegel",
        description = "Extended hold (10+ seconds)",
        emoji = "\u23F8\uFE0F",
        defaultHoldSeconds = 10,
        defaultReps = 5
    ),
    SIT_STAND_KEGEL(
        category = ExerciseCategory.KEGEL,
        displayName = "Sit/Stand Kegel",
        description = "Inhale sitting down, exhale + kegel standing up",
        emoji = "\uD83E\uDE91",
        defaultReps = 10,
        defaultHoldSeconds = 3
    ),

    // Relaxation exercises
    CHILDS_POSE(
        category = ExerciseCategory.RELAXATION,
        displayName = "Child's Pose",
        description = "Pelvic floor relaxation stretch",
        emoji = "\uD83D\uDE47",
        defaultSessionsPerDay = 2,
        defaultSets = 1,
        defaultReps = 1,
        defaultHoldSeconds = 30
    ),
    HAPPY_BABY(
        category = ExerciseCategory.RELAXATION,
        displayName = "Happy Baby",
        description = "Hip opening relaxation pose",
        emoji = "\uD83D\uDC23",
        defaultSessionsPerDay = 2,
        defaultSets = 1,
        defaultReps = 1,
        defaultHoldSeconds = 30
    ),
    DEEP_SQUAT(
        category = ExerciseCategory.RELAXATION,
        displayName = "Deep Squat",
        description = "Deep squat pelvic floor stretch",
        emoji = "\uD83E\uDDB5",
        defaultSessionsPerDay = 2,
        defaultSets = 1,
        defaultReps = 1,
        defaultHoldSeconds = 30
    )
}
