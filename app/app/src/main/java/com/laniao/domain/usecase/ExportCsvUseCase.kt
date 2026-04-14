package com.laniao.domain.usecase

import com.laniao.domain.repository.DrinkEntryRepository
import com.laniao.domain.repository.ExerciseRepository
import com.laniao.domain.repository.PeeEntryRepository
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class ExportCsvUseCase @Inject constructor(
    private val peeEntryRepository: PeeEntryRepository,
    private val drinkEntryRepository: DrinkEntryRepository,
    private val exerciseRepository: ExerciseRepository
) {
    private val dateFormatter = com.laniao.util.DateFormatters.DATE_FILE
    private val timeFormatter = com.laniao.util.DateFormatters.TIME_CSV

    suspend operator fun invoke(): String {
        val zoneId = ZoneId.systemDefault()
        val sb = StringBuilder()

        // Header
        sb.appendLine("Date,Time,EntryType,Volume,LeakAmount,Color,Urgency,ScheduledTime,MinutesFromSchedule,ActivityContext,LiquidType,LiquidAmount,LiquidUnit,LiquidLiters,ExerciseType,Notes")

        // Pee entries
        val peeEntries = peeEntryRepository.getAll().first()
        for (entry in peeEntries.sortedBy { it.timestamp }) {
            val date = entry.timestamp.atZone(zoneId).toLocalDate().format(dateFormatter)
            val time = entry.timestamp.atZone(zoneId).toLocalTime().format(timeFormatter)
            val entryType = when {
                entry.isLeakOnly -> "Leak"
                entry.isUrgeOnly -> "Urge"
                entry.didVoid -> "Void"
                else -> "Unknown"
            }
            sb.appendLine(listOf(
                date,
                time,
                entryType,
                entry.volumeSize.name,
                entry.leakAmount.name,
                entry.color.name,
                entry.urgency.name,
                entry.scheduledTime?.format(timeFormatter) ?: "",
                entry.minutesFromSchedule()?.toString() ?: "",
                csvEscape(entry.activityContext ?: ""),
                "", "", "", "", "",
                csvEscape(entry.notes ?: "")
            ).joinToString(","))
        }

        // Drink entries
        val drinkEntries = drinkEntryRepository.getAll().first()
        for (entry in drinkEntries.sortedBy { it.timestamp }) {
            val date = entry.timestamp.atZone(zoneId).toLocalDate().format(dateFormatter)
            val time = entry.timestamp.atZone(zoneId).toLocalTime().format(timeFormatter)
            sb.appendLine(listOf(
                date,
                time,
                "Drink",
                "", "", "", "", "", "", "",
                entry.type.name,
                entry.amount.toString(),
                entry.unit.name,
                "%.3f".format(entry.liters),
                "",
                csvEscape(entry.customName ?: "")
            ).joinToString(","))
        }

        // Exercise completions (all time)
        val exerciseCompletions = exerciseRepository.getCompletionsByDateRange(
            LocalDate.of(2000, 1, 1), LocalDate.of(2099, 12, 31)
        ).first()
        for (entry in exerciseCompletions.sortedBy { it.completedAt }) {
            val date = entry.completedAt.atZone(zoneId).toLocalDate().format(dateFormatter)
            val time = entry.completedAt.atZone(zoneId).toLocalTime().format(timeFormatter)
            sb.appendLine(listOf(
                date,
                time,
                "Exercise",
                "", "", "", "", "", "", "",
                "", "", "", "",
                entry.exerciseType.name,
                ""
            ).joinToString(","))
        }

        return sb.toString()
    }

    private fun csvEscape(value: String): String {
        return if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            "\"${value.replace("\"", "\"\"")}\""
        } else {
            value
        }
    }
}
