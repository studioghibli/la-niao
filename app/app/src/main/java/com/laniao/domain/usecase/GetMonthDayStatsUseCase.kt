package com.laniao.domain.usecase

import com.laniao.domain.model.CalendarDayStats
import com.laniao.domain.repository.DrinkEntryRepository
import com.laniao.domain.repository.ExerciseRepository
import com.laniao.domain.repository.PeeEntryRepository
import com.laniao.domain.repository.VoidScheduleRepository
import com.laniao.data.local.dao.AppSettingsDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import javax.inject.Inject

/**
 * Builds per-day stats for every day in a month.
 * Used to render the rich calendar grid (void count, scheduled count, leak, kegel).
 */
class GetMonthDayStatsUseCase @Inject constructor(
    private val peeEntryRepository: PeeEntryRepository,
    private val drinkEntryRepository: DrinkEntryRepository,
    private val exerciseRepository: ExerciseRepository,
    private val voidScheduleRepository: VoidScheduleRepository,
    private val appSettingsDao: AppSettingsDao
) {
    operator fun invoke(yearMonth: YearMonth): Flow<Map<LocalDate, CalendarDayStats>> {
        val startDate = yearMonth.atDay(1)
        val endDate = yearMonth.atEndOfMonth()
        val zoneId = ZoneId.systemDefault()

        val entriesFlow = peeEntryRepository.getByDateRange(startDate, endDate).onStart { emit(emptyList()) }
        val exercisesFlow = exerciseRepository.getCompletionsByDateRange(startDate, endDate).onStart { emit(emptyList()) }
        val drinksFlow = drinkEntryRepository.getByDateRange(startDate, endDate).onStart { emit(emptyList()) }

        return entriesFlow.combine(exercisesFlow) { entries, exercises ->
            Pair(entries, exercises)
        }.combine(drinksFlow) { (entries, exercises), drinks ->
            val hydrationGoal = appSettingsDao.getOnce()?.hydrationGoalLiters ?: 2.7
            val statsMap = mutableMapOf<LocalDate, CalendarDayStats>()

            // Group entries by date
            val entriesByDate = entries.groupBy {
                it.timestamp.atZone(zoneId).toLocalDate()
            }

            for ((date, dayEntries) in entriesByDate) {
                val voidCount = dayEntries.count { it.didVoid }
                val hadBurstUrgency = dayEntries.any { it.urgency == com.laniao.domain.model.Urgency.BURST }
                val scheduledCount = calculateScheduledCount(date, dayEntries)

                // Perfect schedule: every scheduled time has a void, no extra voids, no leaks
                val perfectSchedule = if (scheduledCount > 0) {
                    voidCount == scheduledCount && !hadBurstUrgency &&
                        dayEntries.none { !it.didVoid } // no urge-only or leak-only
                } else {
                    false
                }

                statsMap[date] = CalendarDayStats(
                    date = date,
                    voidCount = voidCount,
                    scheduledCount = scheduledCount,
                    hadBurstUrgency = hadBurstUrgency,
                    perfectSchedule = perfectSchedule
                )
            }

            // Drinks: check hydration goal per day
            val drinksByDate = drinks.groupBy {
                it.timestamp.atZone(zoneId).toLocalDate()
            }
            for ((date, dayDrinks) in drinksByDate) {
                val totalLiters = dayDrinks.sumOf { it.liters }
                val metGoal = totalLiters >= hydrationGoal
                val existing = statsMap[date]
                if (existing != null) {
                    statsMap[date] = existing.copy(metHydrationGoal = metGoal)
                } else {
                    statsMap[date] = CalendarDayStats(
                        date = date,
                        metHydrationGoal = metGoal
                    )
                }
            }

            // Group exercises by date and check if all schedule items are completed
            val exercisesByDate = exercises.groupBy { it.scheduledDate }

            for ((date, dayExercises) in exercisesByDate) {
                // Get active schedule for this date to know what's required
                val schedule = exerciseRepository.getActiveForDate(date)
                val allComplete = if (schedule != null) {
                    val items = exerciseRepository.getItemsForSchedule(schedule.id)
                    items.isNotEmpty() && items.all { item ->
                        dayExercises.count { it.exerciseType == item.exerciseType } >= item.sessionsPerDay
                    }
                } else {
                    false
                }

                val existing = statsMap[date]
                if (existing != null) {
                    statsMap[date] = existing.copy(kegelsComplete = allComplete)
                } else {
                    statsMap[date] = CalendarDayStats(
                        date = date,
                        kegelsComplete = allComplete
                    )
                }
            }

            // Mark days with active void/exercise schedules
            var day = startDate
            while (!day.isAfter(endDate)) {
                val hasVoidSched = voidScheduleRepository.getActiveForDate(day) != null
                val hasExSched = exerciseRepository.getActiveForDate(day) != null
                if (hasVoidSched || hasExSched) {
                    val existing = statsMap[day]
                    if (existing != null) {
                        statsMap[day] = existing.copy(
                            hasVoidSchedule = hasVoidSched,
                            hasExerciseSchedule = hasExSched
                        )
                    } else {
                        statsMap[day] = CalendarDayStats(
                            date = day,
                            hasVoidSchedule = hasVoidSched,
                            hasExerciseSchedule = hasExSched
                        )
                    }
                }
                day = day.plusDays(1)
            }

            statsMap
        }
    }

    private suspend fun calculateScheduledCount(
        date: LocalDate,
        dayEntries: List<com.laniao.domain.model.PeeEntry>
    ): Int {
        val schedule = voidScheduleRepository.getActiveForDate(date) ?: return 0
        val zoneId = ZoneId.systemDefault()
        val firstVoid = dayEntries.filter { it.didVoid }.minByOrNull { it.timestamp }
        val anchorTime = firstVoid?.timestamp?.atZone(zoneId)?.toLocalTime()
        return schedule.getScheduledTimes(anchorTime).size
    }
}
