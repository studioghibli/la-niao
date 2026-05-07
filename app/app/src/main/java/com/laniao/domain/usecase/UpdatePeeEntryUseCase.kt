package com.laniao.domain.usecase

import com.laniao.domain.exception.ValidationException
import com.laniao.domain.model.PeeEntry
import com.laniao.domain.repository.PeeEntryRepository
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject

/**
 * Use case for updating an existing pee entry.
 * 
 * Validates:
 * - Notes cannot exceed 500 characters
 * 
 * When editing causes the anchor (first void of day) to change, all entries'
 * scheduledTimes are cleared (Option A behavior) since the schedule times shift.
 */
class UpdatePeeEntryUseCase @Inject constructor(
    private val repository: PeeEntryRepository,
    private val recalculateScheduledTimesUseCase: RecalculateScheduledTimesUseCase
) {
    companion object {
        const val MAX_NOTES_LENGTH = 500
    }

    /**
     * Update an existing entry after validation.
     * Only recalculates scheduled times when the anchor (first void) might change,
     * i.e. when the timestamp or didVoid flag is modified.
     * @throws ValidationException if notes exceed 500 characters
     */
    suspend operator fun invoke(entry: PeeEntry) {
        validate(entry)
        
        // Get the old entry to check what changed
        val oldEntry = repository.getById(entry.id)
        
        // Get the date of the entry
        val zoneId = ZoneId.systemDefault()
        val entryDate = entry.timestamp.atZone(zoneId).toLocalDate()
        
        // Check if fields that affect the anchor actually changed
        val timestampChanged = oldEntry?.timestamp != entry.timestamp
        val didVoidChanged = oldEntry?.didVoid != entry.didVoid
        val anchorMightChange = timestampChanged || didVoidChanged
        
        // Capture the old anchor (first void) before updating
        val oldFirstVoid = if (anchorMightChange) {
            repository.getFirstVoidOfDay(entryDate)
        } else {
            null
        }
        val oldAnchorTime = oldFirstVoid?.timestamp?.atZone(zoneId)?.toLocalTime()
        
        // Also check old date if timestamp changed (entry might have moved dates)
        val oldDate: LocalDate? = oldEntry?.timestamp?.atZone(zoneId)?.toLocalDate()
        val dateChanged = oldDate != null && oldDate != entryDate
        
        // Update the entry
        repository.update(entry)
        
        // Only recalculate if the anchor actually changed
        if (anchorMightChange) {
            val newFirstVoid = repository.getFirstVoidOfDay(entryDate)
            val newAnchorTime = newFirstVoid?.timestamp?.atZone(zoneId)?.toLocalTime()
            
            // Only clear scheduled times if the anchor (first void) actually shifted
            if (oldAnchorTime != newAnchorTime || dateChanged) {
                recalculateScheduledTimesUseCase(entryDate, newAnchorTime)
            }
            
            // If entry moved to a different date, also recalculate the old date
            // dateChanged is only true when oldDate != null, so the null check is safe
            @Suppress("KotlinConstantConditions")
            if (dateChanged && oldDate != null) {
                val oldDateFirstVoid = repository.getFirstVoidOfDay(oldDate)
                val oldDateAnchorTime = oldDateFirstVoid?.let {
                    it.timestamp.atZone(zoneId).toLocalTime()
                }
                recalculateScheduledTimesUseCase(oldDate, oldDateAnchorTime)
            }
        }
    }

    private fun validate(entry: PeeEntry) {
        if (entry.notes != null && entry.notes.length > MAX_NOTES_LENGTH) {
            throw ValidationException("Notes cannot exceed $MAX_NOTES_LENGTH characters")
        }

        if (!entry.didVoid && entry.scheduledTime != null) {
            throw ValidationException("Urge/leak-only entries cannot be associated with scheduled times")
        }


    }
}
