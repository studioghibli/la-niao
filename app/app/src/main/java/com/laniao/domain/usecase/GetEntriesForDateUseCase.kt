package com.laniao.domain.usecase

import com.laniao.domain.model.PeeEntry
import com.laniao.domain.repository.PeeEntryRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

/**
 * Use case for retrieving entries for a specific date.
 */
class GetEntriesForDateUseCase @Inject constructor(
    private val repository: PeeEntryRepository
) {
    /**
     * Get all entries for a specific date.
     * Entries are ordered by timestamp descending (newest first).
     */
    operator fun invoke(date: LocalDate): Flow<List<PeeEntry>> {
        return repository.getByDate(date)
    }
}
