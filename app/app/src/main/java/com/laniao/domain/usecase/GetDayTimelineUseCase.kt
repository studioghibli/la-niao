package com.laniao.domain.usecase

import com.laniao.domain.model.DrinkEntry
import com.laniao.domain.model.ExerciseCompletion
import com.laniao.domain.model.PeeEntry
import com.laniao.domain.model.VoidSchedule
import com.laniao.domain.repository.DrinkEntryRepository
import com.laniao.domain.repository.ExerciseRepository
import com.laniao.domain.repository.PeeEntryRepository
import com.laniao.domain.repository.VoidScheduleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject

/**
 * Item in the day timeline, containing either an entry, a scheduled time, or both.
 */
data class TimelineItem(
    val time: LocalTime,
    val entry: PeeEntry? = null,
    val drinkEntry: DrinkEntry? = null,
    val exerciseCompletion: ExerciseCompletion? = null,
    val scheduledTime: LocalTime? = null,
    val isScheduled: Boolean = false,
    val isClaimed: Boolean = false,
    val isManuallyMissed: Boolean = false
) {
    val isMissed: Boolean
        get() = isScheduled && !isClaimed
    
    val isCompleted: Boolean
        get() = isScheduled && isClaimed

    val isDrink: Boolean
        get() = drinkEntry != null

    val isExercise: Boolean
        get() = exerciseCompletion != null
}

/**
 * Use case for building a day timeline with entries and scheduled times.
 */
class GetDayTimelineUseCase @Inject constructor(
    private val entryRepository: PeeEntryRepository,
    private val drinkRepository: DrinkEntryRepository,
    private val exerciseRepository: ExerciseRepository,
    private val scheduleRepository: VoidScheduleRepository,
    private val manuallyMissedTimeRepository: com.laniao.domain.repository.ManuallyMissedTimeRepository
) {
    /**
     * Get timeline items for a specific date.
     * Combines entries with scheduled times to show what was scheduled vs what happened.
     */
    operator fun invoke(date: LocalDate): Flow<List<TimelineItem>> {
        val entriesFlow = entryRepository.getByDate(date)
        val drinksFlow = drinkRepository.getByDate(date)
        val exercisesFlow = exerciseRepository.getCompletionsByDate(date)
        
        return entriesFlow.combine(drinksFlow) { entries, drinks ->
            Pair(entries, drinks)
        }.combine(exercisesFlow) { (entries, drinks), exercises ->
            val schedule = scheduleRepository.getActiveForDate(date)
            val manuallyMissed = manuallyMissedTimeRepository.getByDateOnce(date).toSet()
            buildTimeline(date, entries, drinks, exercises, schedule, manuallyMissed)
        }
    }
    
    private fun buildTimeline(
        date: LocalDate,
        entries: List<PeeEntry>,
        drinks: List<DrinkEntry>,
        exercises: List<ExerciseCompletion>,
        schedule: VoidSchedule?,
        manuallyMissed: Set<LocalTime>
    ): List<TimelineItem> {
        val items = mutableListOf<TimelineItem>()
        val zoneId = ZoneId.systemDefault()
        
        // Get scheduled times if there's an active schedule
        val scheduledTimes = if (schedule != null) {
            // Find anchor (first void of day)
            val firstVoid = entries.filter { it.didVoid }
                .minByOrNull { it.timestamp }
            val anchorTime = firstVoid?.timestamp?.atZone(zoneId)?.toLocalTime()
            schedule.getScheduledTimes(anchorTime)
        } else {
            emptyList()
        }
        
        // Add entries — each is its own item, never marked as isScheduled
        for (entry in entries) {
            val entryTime = entry.timestamp.atZone(zoneId).toLocalTime()
            items.add(
                TimelineItem(
                    time = entryTime,
                    entry = entry,
                    scheduledTime = entry.scheduledTime,
                    isScheduled = false
                )
            )
        }
        
        // Add schedule markers — mark ones that have a logged entry
        val claimedScheduledTimes = entries.mapNotNull { it.scheduledTime }.toSet()
        for (scheduledTime in scheduledTimes) {
            items.add(
                TimelineItem(
                    time = scheduledTime,
                    entry = null,
                    scheduledTime = scheduledTime,
                    isScheduled = true,
                    isClaimed = scheduledTime in claimedScheduledTimes,
                    isManuallyMissed = scheduledTime in manuallyMissed
                )
            )
        }
        
        // Add drink entries
        for (drink in drinks) {
            val drinkTime = drink.timestamp.atZone(zoneId).toLocalTime()
            items.add(
                TimelineItem(
                    time = drinkTime,
                    drinkEntry = drink
                )
            )
        }
        
        // Add exercise completions
        for (exercise in exercises) {
            val exerciseTime = exercise.completedAt.atZone(zoneId).toLocalTime()
            items.add(
                TimelineItem(
                    time = exerciseTime,
                    exerciseCompletion = exercise
                )
            )
        }
        
        // Sort by time
        return items.sortedBy { it.time }
    }
}
