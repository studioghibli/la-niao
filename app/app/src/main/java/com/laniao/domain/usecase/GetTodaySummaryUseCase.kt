package com.laniao.domain.usecase

import com.laniao.domain.model.DailySummary
import com.laniao.domain.repository.PeeEntryRepository
import com.laniao.domain.repository.VoidScheduleRepository
import com.laniao.util.Clock
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject

/**
 * Use case for getting today's summary statistics.
 * Calculates void count, urge-only count, scheduled/unscheduled counts, and missed times.
 */
class GetTodaySummaryUseCase @Inject constructor(
    private val entryRepository: PeeEntryRepository,
    private val scheduleRepository: VoidScheduleRepository,
    private val clock: Clock
) {
    companion object {
        /** Grace period in minutes before a scheduled time is considered "missed" */
        const val MISSED_GRACE_PERIOD_MINUTES = 15
    }

    /**
     * Get summary for today as a Flow (auto-updates on data change).
     */
    operator fun invoke(): Flow<DailySummary> {
        val today = clock.now().atZone(ZoneId.systemDefault()).toLocalDate()
        return invoke(today)
    }

    /**
     * Get summary for a specific date as a Flow.
     */
    fun invoke(date: LocalDate): Flow<DailySummary> {
        val entriesFlow = entryRepository.getByDate(date)
        
        return entriesFlow.map { entries ->
            val voidEntries = entries.filter { it.didVoid }
            val urgeOnlyEntries = entries.filter { it.isUrgeOnly }
            val leakOnlyEntries = entries.filter { it.isLeakOnly }
            val totalLeakEntries = entries.filter { it.hasLeak }
            
            val scheduledEntries = voidEntries.filter { it.scheduledTime != null }
            val unscheduledEntries = voidEntries.filter { it.scheduledTime == null }

            DailySummary(
                voidCount = voidEntries.size,
                urgeOnlyCount = urgeOnlyEntries.size,
                leakOnlyCount = leakOnlyEntries.size,
                totalLeakCount = totalLeakEntries.size,
                scheduledCount = scheduledEntries.size,
                unscheduledCount = unscheduledEntries.size,
                missedCount = 0
            )
        }
    }
}
