package com.laniao.domain.usecase

import com.laniao.domain.model.ExerciseSchedule
import com.laniao.domain.model.ExerciseScheduleItem
import com.laniao.domain.repository.ExerciseRepository
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import javax.inject.Inject

/**
 * Creates or updates exercise schedules.
 */
class ManageExerciseScheduleUseCase @Inject constructor(
    private val repository: ExerciseRepository
) {
    /**
     * Create a new exercise schedule with items.
     * @param replaceExisting If true, deletes any overlapping schedule first.
     */
    suspend fun create(
        startDate: LocalDate,
        endDate: LocalDate?,
        items: List<ExerciseScheduleItem>,
        replaceExisting: Boolean = false
    ): Long {
        if (items.isEmpty()) throw IllegalArgumentException("Schedule must have at least one exercise")

        val checkEndDate = endDate ?: LocalDate.of(9999, 12, 31)

        if (!replaceExisting && repository.hasOverlap(startDate, checkEndDate)) {
            throw IllegalStateException("Overlapping exercise schedule exists")
        }

        if (replaceExisting) {
            val allSchedules = repository.getAllSchedules().first()
            allSchedules.filter { it.enabled }.forEach { schedule ->
                val schedEnd = schedule.endDate ?: LocalDate.of(9999, 12, 31)
                if (startDate <= schedEnd && checkEndDate >= schedule.startDate) {
                    repository.deleteSchedule(schedule.id)
                }
            }
        }

        val schedule = ExerciseSchedule(
            startDate = startDate,
            endDate = endDate,
            enabled = true,
            createdAt = LocalDate.now()
        )
        val scheduleId = repository.insertSchedule(schedule)

        val itemsWithScheduleId = items.map { it.copy(scheduleId = scheduleId) }
        repository.insertItems(itemsWithScheduleId)

        return scheduleId
    }

    suspend fun delete(scheduleId: Long) {
        repository.deleteSchedule(scheduleId)
    }

    suspend fun update(
        scheduleId: Long,
        startDate: LocalDate,
        endDate: LocalDate?,
        items: List<ExerciseScheduleItem>
    ) {
        if (items.isEmpty()) throw IllegalArgumentException("Schedule must have at least one exercise")

        val existing = repository.getScheduleById(scheduleId)
            ?: throw IllegalArgumentException("Schedule not found")

        // Update the schedule dates
        repository.updateSchedule(existing.copy(startDate = startDate, endDate = endDate))

        // Replace items: delete old, insert new
        repository.deleteItemsForSchedule(scheduleId)
        repository.insertItems(items.map { it.copy(scheduleId = scheduleId) })
    }

    suspend fun hasOverlap(startDate: LocalDate, endDate: LocalDate?, excludeId: Long = 0): Boolean {
        val checkEndDate = endDate ?: LocalDate.of(9999, 12, 31)
        return repository.hasOverlap(startDate, checkEndDate, excludeId)
    }
}
