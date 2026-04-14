package com.laniao.domain.usecase

import com.laniao.domain.model.ExerciseCompletion
import com.laniao.domain.model.ExerciseType
import com.laniao.domain.repository.ExerciseRepository
import com.laniao.util.Clock
import java.time.ZoneId
import javax.inject.Inject

/**
 * Marks one exercise session as completed for today.
 */
class CompleteExerciseUseCase @Inject constructor(
    private val repository: ExerciseRepository,
    private val clock: Clock
) {
    suspend operator fun invoke(exerciseType: ExerciseType, scheduleItemId: Long? = null): ExerciseCompletion {
        val now = clock.now()
        val today = now.atZone(ZoneId.systemDefault()).toLocalDate()

        val completion = ExerciseCompletion(
            scheduleItemId = scheduleItemId,
            exerciseType = exerciseType,
            completedAt = now,
            scheduledDate = today
        )
        val id = repository.insertCompletion(completion)
        return completion.copy(id = id)
    }
}
