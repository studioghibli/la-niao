package com.laniao.domain.usecase

import com.laniao.domain.model.PeeEntry
import com.laniao.domain.model.Urgency
import com.laniao.domain.repository.ManuallyMissedTimeRepository
import com.laniao.domain.repository.PeeEntryRepository
import com.laniao.domain.repository.VoidScheduleRepository
import com.laniao.util.Clock
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject

/**
 * Status of a scheduled void time.
 */
enum class ScheduledTimeStatus {
    /** Completed - void entry exists with this scheduled time */
    COMPLETED,
    /** Completed with burst urgency */
    COMPLETED_BURST,
    /** Overdue - time has passed (with grace period) and no entry, not manually marked */
    OVERDUE,
    /** Missed - user explicitly marked this scheduled time as missed */
    MISSED,
    /** Upcoming - time has not passed yet */
    UPCOMING
}

/**
 * Progress item for a scheduled void time.
 */
data class ScheduleProgressItem(
    val scheduledTime: LocalTime,
    val status: ScheduledTimeStatus,
    /** Entry associated with this time (if completed) */
    val entry: PeeEntry? = null
)

/**
 * Use case for getting schedule progress for a day.
 * Returns the status of each scheduled time (completed, missed, upcoming).
 */
class GetScheduleProgressUseCase @Inject constructor(
    private val scheduleRepository: VoidScheduleRepository,
    private val entryRepository: PeeEntryRepository,
    private val manuallyMissedTimeRepository: ManuallyMissedTimeRepository,
    private val clock: Clock
) {
    companion object {
        /** Grace period in minutes before a scheduled time is considered "missed" */
        const val GRACE_PERIOD_MINUTES = 15
    }

    /**
     * Get progress for today as a Flow.
     */
    operator fun invoke(): Flow<List<ScheduleProgressItem>> {
        val today = clock.now().atZone(ZoneId.systemDefault()).toLocalDate()
        return invoke(today)
    }

    /**
     * Get progress for a specific date as a Flow.
     */
    fun invoke(date: LocalDate): Flow<List<ScheduleProgressItem>> {
        val entriesFlow = entryRepository.getByDate(date)
        val schedulesFlow = scheduleRepository.getActive()
        val missedFlow = manuallyMissedTimeRepository.getByDate(date)
        // Tick every 60s so UPCOMING→OVERDUE transitions happen in real-time
        val tickerFlow = flow {
            while (true) {
                emit(Unit)
                delay(60_000L)
            }
        }
        
        return entriesFlow.combine(schedulesFlow) { entries, schedules ->
            Pair(entries, schedules)
        }.combine(missedFlow) { (entries, schedules), manuallyMissedTimes ->
            Triple(entries, schedules, manuallyMissedTimes)
        }.combine(tickerFlow) { (entries, schedules, manuallyMissedTimes), _ ->
            val schedule = schedules.firstOrNull { it.isActive(date) }
                ?: return@combine emptyList()

            // Find first void for anchor
            val voidEntries = entries
                .filter { it.didVoid }
                .sortedBy { it.timestamp }

            val firstVoidTime = voidEntries.firstOrNull()?.let {
                it.timestamp.atZone(ZoneId.systemDefault()).toLocalTime()
            }

            val scheduledTimes = schedule.getScheduledTimes(anchorTime = firstVoidTime)
            val now = clock.now().atZone(ZoneId.systemDefault()).toLocalTime()
            val isToday = date == clock.now().atZone(ZoneId.systemDefault()).toLocalDate()

            scheduledTimes.map { time ->
                val matchingEntry = entries.find { it.scheduledTime == time }
                
                val status = when {
                    matchingEntry != null -> {
                        if (matchingEntry.urgency == Urgency.BURST) {
                            ScheduledTimeStatus.COMPLETED_BURST
                        } else {
                            ScheduledTimeStatus.COMPLETED
                        }
                    }
                    time in manuallyMissedTimes -> ScheduledTimeStatus.MISSED
                    isToday && now < time.plusMinutes(GRACE_PERIOD_MINUTES.toLong()) -> {
                        ScheduledTimeStatus.UPCOMING
                    }
                    else -> ScheduledTimeStatus.OVERDUE
                }

                ScheduleProgressItem(
                    scheduledTime = time,
                    status = status,
                    entry = matchingEntry
                )
            }
        }
    }

    /**
     * Get just the overdue scheduled times for today (excludes manually missed).
     * Used for the Home screen overdue alert.
     */
    suspend fun getMissedTimes(): List<LocalTime> {
        val today = clock.now().atZone(ZoneId.systemDefault()).toLocalDate()
        val now = clock.now().atZone(ZoneId.systemDefault()).toLocalTime()
        
        val schedule = scheduleRepository.getActiveForDate(today)
            ?: return emptyList()

        val firstVoidEntry = entryRepository.getFirstVoidOfDay(today)
        val firstVoidTime = firstVoidEntry?.let {
            it.timestamp.atZone(ZoneId.systemDefault()).toLocalTime()
        }

        val scheduledTimes = schedule.getScheduledTimes(anchorTime = firstVoidTime)
        val manuallyMissed = manuallyMissedTimeRepository.getByDateOnce(today).toSet()

        return scheduledTimes.filter { time ->
            // Exclude manually missed times — those are intentional
            if (time in manuallyMissed) return@filter false

            // Time has passed (with grace period) and no entry with this scheduled time
            val pastGracePeriod = now >= time.plusMinutes(GRACE_PERIOD_MINUTES.toLong())
            if (!pastGracePeriod) return@filter false

            val entries = entryRepository.getByScheduledTime(today, time)
            entries.isEmpty()
        }
    }
}
