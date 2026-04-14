package com.laniao.domain.usecase

import com.laniao.data.local.LaNiaoDatabase
import com.laniao.data.local.dao.AppSettingsDao
import com.laniao.data.local.dao.DrinkEntryDao
import com.laniao.data.local.dao.ExerciseCompletionDao
import com.laniao.data.local.dao.ExerciseScheduleDao
import com.laniao.data.local.dao.PeeEntryDao
import com.laniao.data.local.dao.VoidScheduleDao
import com.laniao.data.local.dao.WaterIntakeDao
import com.laniao.data.local.entity.AppSettingsEntity
import com.laniao.data.local.entity.DrinkEntryEntity
import com.laniao.data.local.entity.ExerciseCompletionEntity
import com.laniao.data.local.entity.ExerciseScheduleEntity
import com.laniao.data.local.entity.ExerciseScheduleItemEntity
import com.laniao.data.local.entity.PeeEntryEntity
import com.laniao.data.local.entity.VoidScheduleEntity
import com.laniao.data.local.entity.WaterIntakeEntity
import com.laniao.domain.model.DrinkType
import com.laniao.domain.model.DrinkUnit
import com.laniao.domain.model.ExerciseType
import com.laniao.domain.model.LeakAmount
import com.laniao.domain.model.PeeColor
import com.laniao.domain.model.Urgency
import com.laniao.domain.model.VolumeSize
import com.laniao.domain.model.WaterAmount
import kotlinx.serialization.json.Json
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

private const val SUPPORTED_SCHEMA_VERSION = 1

data class RestoreResult(
    val peeEntryCount: Int,
    val drinkEntryCount: Int,
    val scheduleCount: Int,
    val exerciseCompletionCount: Int
)

class RestoreDataUseCase @Inject constructor(
    private val database: LaNiaoDatabase,
    private val peeEntryDao: PeeEntryDao,
    private val voidScheduleDao: VoidScheduleDao,
    private val waterIntakeDao: WaterIntakeDao,
    private val drinkEntryDao: DrinkEntryDao,
    private val exerciseScheduleDao: ExerciseScheduleDao,
    private val exerciseCompletionDao: ExerciseCompletionDao,
    private val appSettingsDao: AppSettingsDao
) {
    private val json = Json { ignoreUnknownKeys = true }

    suspend operator fun invoke(jsonString: String): RestoreResult {
        val backupData = json.decodeFromString<BackupData>(jsonString)

        if (backupData.schemaVersion > SUPPORTED_SCHEMA_VERSION) {
            throw UnsupportedSchemaVersionException(backupData.schemaVersion, SUPPORTED_SCHEMA_VERSION)
        }

        database.runInTransaction {
            kotlinx.coroutines.runBlocking {
                // Clear all tables
                exerciseCompletionDao.deleteAll()
                exerciseScheduleDao.deleteAllItems()
                exerciseScheduleDao.deleteAllSchedules()
                drinkEntryDao.deleteAll()
                waterIntakeDao.deleteAll()
                voidScheduleDao.deleteAll()
                peeEntryDao.deleteAll()
                appSettingsDao.deleteAll()

                // Restore pee entries
                peeEntryDao.insertAll(backupData.peeEntries.map { e ->
                    PeeEntryEntity(
                        id = e.id,
                        timestamp = Instant.ofEpochMilli(e.timestampEpochMilli),
                        didVoid = e.didVoid,
                        leakAmount = LeakAmount.valueOf(e.leakAmount),
                        volumeSize = VolumeSize.valueOf(e.volumeSize),
                        color = PeeColor.valueOf(e.color),
                        urgency = Urgency.valueOf(e.urgency),
                        activityContext = e.activityContext,
                        notes = e.notes,
                        scheduledTime = e.scheduledTime?.let { LocalTime.parse(it) }
                    )
                })

                // Restore void schedules
                voidScheduleDao.insertAll(backupData.voidSchedules.map { s ->
                    VoidScheduleEntity(
                        id = s.id,
                        startTime = LocalTime.parse(s.startTime),
                        endTime = LocalTime.parse(s.endTime),
                        intervalMinutes = s.intervalMinutes,
                        enabled = s.enabled,
                        createdAt = LocalDate.parse(s.createdAt),
                        expiresAt = LocalDate.parse(s.expiresAt)
                    )
                })

                // Restore water intakes
                waterIntakeDao.insertAll(backupData.waterIntakes.map { w ->
                    WaterIntakeEntity(
                        id = w.id,
                        date = LocalDate.parse(w.date),
                        amount = WaterAmount.valueOf(w.amount)
                    )
                })

                // Restore drink entries
                drinkEntryDao.insertAll(backupData.drinkEntries.map { d ->
                    DrinkEntryEntity(
                        id = d.id,
                        timestamp = Instant.ofEpochMilli(d.timestampEpochMilli),
                        type = DrinkType.valueOf(d.type),
                        amount = d.amount,
                        unit = DrinkUnit.valueOf(d.unit),
                        customName = d.customName
                    )
                })

                // Restore exercise schedules (must insert before items due to FK)
                for (schedule in backupData.exerciseSchedules) {
                    exerciseScheduleDao.insertSchedule(
                        ExerciseScheduleEntity(
                            id = schedule.id,
                            startDate = LocalDate.parse(schedule.startDate),
                            endDate = schedule.endDate?.let { LocalDate.parse(it) },
                            enabled = schedule.enabled,
                            createdAt = LocalDate.parse(schedule.createdAt)
                        )
                    )
                }

                // Restore exercise schedule items
                exerciseScheduleDao.insertItems(backupData.exerciseScheduleItems.map { item ->
                    ExerciseScheduleItemEntity(
                        id = item.id,
                        scheduleId = item.scheduleId,
                        exerciseType = ExerciseType.valueOf(item.exerciseType),
                        sessionsPerDay = item.sessionsPerDay,
                        sets = item.sets,
                        reps = item.reps,
                        holdSeconds = item.holdSeconds
                    )
                })

                // Restore exercise completions
                exerciseCompletionDao.insertAll(backupData.exerciseCompletions.map { c ->
                    ExerciseCompletionEntity(
                        id = c.id,
                        scheduleItemId = c.scheduleItemId,
                        exerciseType = ExerciseType.valueOf(c.exerciseType),
                        completedAt = Instant.ofEpochMilli(c.completedAtEpochMilli),
                        scheduledDate = LocalDate.parse(c.scheduledDate)
                    )
                })

                // Restore settings
                backupData.hydrationGoalLiters?.let { goal ->
                    appSettingsDao.upsert(AppSettingsEntity(hydrationGoalLiters = goal))
                }
            }
        }

        return RestoreResult(
            peeEntryCount = backupData.peeEntries.size,
            drinkEntryCount = backupData.drinkEntries.size,
            scheduleCount = backupData.voidSchedules.size + backupData.exerciseSchedules.size,
            exerciseCompletionCount = backupData.exerciseCompletions.size
        )
    }
}

class UnsupportedSchemaVersionException(
    val fileVersion: Int,
    val supportedVersion: Int
) : Exception("Backup schema version $fileVersion is newer than supported version $supportedVersion")
