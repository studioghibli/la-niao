package com.laniao.domain.usecase

import com.laniao.domain.model.PeeEntry
import com.laniao.domain.repository.PeeEntryRepository
import com.laniao.util.Clock
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

/**
 * Use case for getting recent entries for today.
 * Returns the most recent entries (up to 3) for display on home screen.
 */
class GetRecentEntriesUseCase @Inject constructor(
    private val repository: PeeEntryRepository,
    private val clock: Clock
) {
    companion object {
        const val MAX_RECENT_ENTRIES = 3
    }

    /**
     * Get recent entries for today as a Flow.
     */
    operator fun invoke(): Flow<List<PeeEntry>> {
        val today = clock.now().atZone(ZoneId.systemDefault()).toLocalDate()
        return invoke(today)
    }

    /**
     * Get recent entries for a specific date.
     */
    fun invoke(date: LocalDate): Flow<List<PeeEntry>> {
        return repository.getByDate(date).map { entries ->
            entries.take(MAX_RECENT_ENTRIES)
        }
    }
}
