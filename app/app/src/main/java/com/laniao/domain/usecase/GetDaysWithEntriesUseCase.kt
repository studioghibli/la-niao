package com.laniao.domain.usecase

import com.laniao.domain.repository.PeeEntryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import javax.inject.Inject

/**
 * Use case for getting which days in a month have entries.
 * Used for displaying dots on calendar days with data.
 */
class GetDaysWithEntriesUseCase @Inject constructor(
    private val repository: PeeEntryRepository
) {
    /**
     * Get list of dates that have entries within a given month.
     */
    operator fun invoke(yearMonth: YearMonth): Flow<List<LocalDate>> {
        val startDate = yearMonth.atDay(1)
        val endDate = yearMonth.atEndOfMonth()
        
        return repository.getByDateRange(startDate, endDate).map { entries ->
            entries.map { entry ->
                entry.timestamp.atZone(ZoneId.systemDefault()).toLocalDate()
            }.distinct()
        }
    }
}
