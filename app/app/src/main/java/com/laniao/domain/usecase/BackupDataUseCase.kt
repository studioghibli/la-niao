package com.laniao.domain.usecase

import com.laniao.data.local.LaNiaoDatabase
import com.laniao.data.local.dao.AppSettingsDao
import com.laniao.data.local.dao.DrinkEntryDao
import com.laniao.data.local.dao.ExerciseCompletionDao
import com.laniao.data.local.dao.ExerciseScheduleDao
import com.laniao.data.local.dao.PeeEntryDao
import com.laniao.data.local.dao.VoidScheduleDao
import com.laniao.data.local.dao.WaterIntakeDao
import kotlinx.coroutines.flow.first
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

private const val SCHEMA_VERSION = 1

@Serializable
data class BackupData(
    val schemaVersion: Int = SCHEMA_VERSION,
    val createdAt: Long = System.currentTimeMillis(),
    val peeEntries: List<BackupPeeEntry> = emptyList(),
    val voidSchedules: List<BackupVoidSchedule> = emptyList(),
    val waterIntakes: List<BackupWaterIntake> = emptyList(),
    val drinkEntries: List<BackupDrinkEntry> = emptyList(),
    val exerciseSchedules: List<BackupExerciseSchedule> = emptyList(),
    val exerciseScheduleItems: List<BackupExerciseScheduleItem> = emptyList(),
    val exerciseCompletions: List<BackupExerciseCompletion> = emptyList(),
    val hydrationGoalLiters: Double? = null
)

@Serializable
data class BackupPeeEntry(
    val id: Long,
    val timestampEpochMilli: Long,
    val didVoid: Boolean,
    val leakAmount: String,
    val volumeSize: String,
    val color: String,
    val urgency: String,
    val activityContext: String?,
    val notes: String?,
    val scheduledTime: String?
)

@Serializable
data class BackupVoidSchedule(
    val id: Long,
    val startTime: String,
    val endTime: String,
    val intervalMinutes: Int,
    val enabled: Boolean,
    val createdAt: String,
    val expiresAt: String
)

@Serializable
data class BackupWaterIntake(
    val id: Long,
    val date: String,
    val amount: String
)

@Serializable
data class BackupDrinkEntry(
    val id: Long,
    val timestampEpochMilli: Long,
    val type: String,
    val amount: Double,
    val unit: String,
    val customName: String?
)

@Serializable
data class BackupExerciseSchedule(
    val id: Long,
    val startDate: String,
    val endDate: String?,
    val enabled: Boolean,
    val createdAt: String
)

@Serializable
data class BackupExerciseScheduleItem(
    val id: Long,
    val scheduleId: Long,
    val exerciseType: String,
    val sessionsPerDay: Int,
    val sets: Int,
    val reps: Int,
    val holdSeconds: Int
)

@Serializable
data class BackupExerciseCompletion(
    val id: Long,
    val scheduleItemId: Long?,
    val exerciseType: String,
    val completedAtEpochMilli: Long,
    val scheduledDate: String
)

class BackupDataUseCase @Inject constructor(
    private val peeEntryDao: PeeEntryDao,
    private val voidScheduleDao: VoidScheduleDao,
    private val waterIntakeDao: WaterIntakeDao,
    private val drinkEntryDao: DrinkEntryDao,
    private val exerciseScheduleDao: ExerciseScheduleDao,
    private val exerciseCompletionDao: ExerciseCompletionDao,
    private val appSettingsDao: AppSettingsDao
) {
    private val json = Json { prettyPrint = true }

    suspend operator fun invoke(): String {
        val peeEntries = peeEntryDao.getAll().first().map { e ->
            BackupPeeEntry(
                id = e.id,
                timestampEpochMilli = e.timestamp.toEpochMilli(),
                didVoid = e.didVoid,
                leakAmount = e.leakAmount.name,
                volumeSize = e.volumeSize.name,
                color = e.color.name,
                urgency = e.urgency.name,
                activityContext = e.activityContext,
                notes = e.notes,
                scheduledTime = e.scheduledTime?.toString()
            )
        }

        val voidSchedules = voidScheduleDao.getAll().first().map { s ->
            BackupVoidSchedule(
                id = s.id,
                startTime = s.startTime.toString(),
                endTime = s.endTime.toString(),
                intervalMinutes = s.intervalMinutes,
                enabled = s.enabled,
                createdAt = s.createdAt.toString(),
                expiresAt = s.expiresAt.toString()
            )
        }

        val waterIntakes = waterIntakeDao.getAll().first().map { w ->
            BackupWaterIntake(
                id = w.id,
                date = w.date.toString(),
                amount = w.amount.name
            )
        }

        val drinkEntries = drinkEntryDao.getAll().first().map { d ->
            BackupDrinkEntry(
                id = d.id,
                timestampEpochMilli = d.timestamp.toEpochMilli(),
                type = d.type.name,
                amount = d.amount,
                unit = d.unit.name,
                customName = d.customName
            )
        }

        val exerciseSchedules = exerciseScheduleDao.getAllSchedules().first().map { s ->
            BackupExerciseSchedule(
                id = s.id,
                startDate = s.startDate.toString(),
                endDate = s.endDate?.toString(),
                enabled = s.enabled,
                createdAt = s.createdAt.toString()
            )
        }

        val exerciseScheduleItems = exerciseSchedules.flatMap { schedule ->
            exerciseScheduleDao.getItemsForSchedule(schedule.id).map { item ->
                BackupExerciseScheduleItem(
                    id = item.id,
                    scheduleId = item.scheduleId,
                    exerciseType = item.exerciseType.name,
                    sessionsPerDay = item.sessionsPerDay,
                    sets = item.sets,
                    reps = item.reps,
                    holdSeconds = item.holdSeconds
                )
            }
        }

        val exerciseCompletions = exerciseCompletionDao.getAll().first().map { c ->
            BackupExerciseCompletion(
                id = c.id,
                scheduleItemId = c.scheduleItemId,
                exerciseType = c.exerciseType.name,
                completedAtEpochMilli = c.completedAt.toEpochMilli(),
                scheduledDate = c.scheduledDate.toString()
            )
        }

        val settings = appSettingsDao.getOnce()

        val backupData = BackupData(
            peeEntries = peeEntries,
            voidSchedules = voidSchedules,
            waterIntakes = waterIntakes,
            drinkEntries = drinkEntries,
            exerciseSchedules = exerciseSchedules,
            exerciseScheduleItems = exerciseScheduleItems,
            exerciseCompletions = exerciseCompletions,
            hydrationGoalLiters = settings?.hydrationGoalLiters
        )

        return json.encodeToString(backupData)
    }
}
