package com.laniao.domain.usecase

import com.laniao.domain.model.PeeEntry
import com.laniao.domain.repository.PeeEntryRepository
import java.time.ZoneId
import javax.inject.Inject

/**
 * Use case for deleting a pee entry.
 * 
 * When deleting the first void of the day (anchor), all entries' scheduledTimes
 * are cleared (Option A behavior) since the schedule times shift to the new anchor.
 */
class DeletePeeEntryUseCase @Inject constructor(
    private val repository: PeeEntryRepository,
    private val recalculateScheduledTimesUseCase: RecalculateScheduledTimesUseCase
) {
    /**
     * Delete an entry by ID.
     * If this was the first void of the day, recalculate all scheduled times.
     */
    suspend operator fun invoke(entryId: Long) {
        val entry = repository.getById(entryId) ?: return
        deleteAndRecalculateIfNeeded(entry)
    }

    /**
     * Delete an entry.
     * If this was the first void of the day, recalculate all scheduled times.
     */
    suspend operator fun invoke(entry: PeeEntry) {
        deleteAndRecalculateIfNeeded(entry)
    }

    private suspend fun deleteAndRecalculateIfNeeded(entry: PeeEntry) {
        val zoneId = ZoneId.systemDefault()
        val entryDate = entry.timestamp.atZone(zoneId).toLocalDate()

        // Check if this was the first void of the day
        val firstVoid = repository.getFirstVoidOfDay(entryDate)
        val wasFirstVoid = firstVoid?.id == entry.id && entry.didVoid

        // Delete the entry
        repository.delete(entry)

        // If this was the first void, recalculate schedule times
        if (wasFirstVoid) {
            val newFirstVoid = repository.getFirstVoidOfDay(entryDate)
            val newAnchorTime = newFirstVoid?.let {
                it.timestamp.atZone(zoneId).toLocalTime()
            }
            recalculateScheduledTimesUseCase(entryDate, newAnchorTime)
        }
    }
}
