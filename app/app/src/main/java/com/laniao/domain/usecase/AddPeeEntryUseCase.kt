package com.laniao.domain.usecase

import com.laniao.domain.exception.DuplicateMinuteException
import com.laniao.domain.exception.MaxEntriesException
import com.laniao.domain.exception.ValidationException
import com.laniao.domain.model.PeeEntry
import com.laniao.domain.repository.PeeEntryRepository
import com.laniao.util.Clock
import java.time.ZoneId
import javax.inject.Inject

/**
 * Use case for adding a new pee entry with validation.
 * 
 * Validates:
 * - Maximum 50 entries per day
 * - One entry per minute maximum
 * - Notes cannot exceed 500 characters
 */
class AddPeeEntryUseCase @Inject constructor(
    private val repository: PeeEntryRepository,
    private val clock: Clock
) {
    companion object {
        const val MAX_ENTRIES_PER_DAY = 50
        const val MAX_NOTES_LENGTH = 500
    }

    /**
     * Add a new entry after validation.
     * @throws MaxEntriesException if 50 entries exist for the day
     * @throws DuplicateMinuteException if an entry exists in the same minute
     * @throws ValidationException if notes exceed 500 characters
     * @return The ID of the created entry
     */
    suspend operator fun invoke(entry: PeeEntry): Long {
        validate(entry)
        return repository.insert(entry)
    }

    private suspend fun validate(entry: PeeEntry) {
        // Validate notes length
        if (entry.notes != null && entry.notes.length > MAX_NOTES_LENGTH) {
            throw ValidationException("Notes cannot exceed $MAX_NOTES_LENGTH characters")
        }

        // Urge/leak-only entries cannot be associated with scheduled times
        if (!entry.didVoid && entry.scheduledTime != null) {
            throw ValidationException("Urge/leak-only entries cannot be associated with scheduled times")
        }



        // Validate max entries per day
        val date = entry.getLocalDate()
        val count = repository.getCountForDate(date)
        if (count >= MAX_ENTRIES_PER_DAY) {
            throw MaxEntriesException("Maximum of $MAX_ENTRIES_PER_DAY entries per day reached")
        }

        // Validate one entry per minute
        if (repository.existsInSameMinute(entry.timestamp)) {
            throw DuplicateMinuteException("An entry already exists for this minute")
        }
    }
}
