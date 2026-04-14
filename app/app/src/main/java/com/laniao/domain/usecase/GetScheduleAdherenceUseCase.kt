package com.laniao.domain.usecase

import com.laniao.domain.repository.PeeEntryRepository
import com.laniao.domain.repository.VoidScheduleRepository
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject

data class DayScheduleAdherence(
    val date: LocalDate,
    val totalScheduled: Int,
    val completed: Int,
    val missed: Int
) {
    val adherenceRate: Float
        get() = if (totalScheduled > 0) completed.toFloat() / totalScheduled else 0f
}

class GetScheduleAdherenceUseCase @Inject constructor(
    private val scheduleRepository: VoidScheduleRepository,
    private val entryRepository: PeeEntryRepository
) {
    suspend operator fun invoke(startDate: LocalDate, endDate: LocalDate): List<DayScheduleAdherence> {
        val days = mutableListOf<DayScheduleAdherence>()
        var current = startDate

        while (!current.isAfter(endDate)) {
            val schedule = scheduleRepository.getActiveForDate(current)
            if (schedule != null) {
                val firstVoid = entryRepository.getFirstVoidOfDay(current)
                val firstVoidTime = firstVoid?.let {
                    it.timestamp.atZone(ZoneId.systemDefault()).toLocalTime()
                }
                val scheduledTimes = schedule.getScheduledTimes(anchorTime = firstVoidTime)

                var completed = 0
                for (time in scheduledTimes) {
                    val matches = entryRepository.getByScheduledTime(current, time)
                    if (matches.isNotEmpty()) completed++
                }

                days.add(DayScheduleAdherence(
                    date = current,
                    totalScheduled = scheduledTimes.size,
                    completed = completed,
                    missed = scheduledTimes.size - completed
                ))
            } else {
                // No active schedule — show as zero
                days.add(DayScheduleAdherence(
                    date = current,
                    totalScheduled = 0,
                    completed = 0,
                    missed = 0
                ))
            }
            current = current.plusDays(1)
        }
        return days
    }
}
