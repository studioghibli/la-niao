package com.laniao.data.repository

import com.laniao.data.local.dao.ExerciseCompletionDao
import com.laniao.data.local.dao.ExerciseScheduleDao
import com.laniao.data.local.entity.ExerciseCompletionEntity
import com.laniao.data.local.entity.ExerciseScheduleEntity
import com.laniao.data.local.entity.ExerciseScheduleItemEntity
import com.laniao.domain.model.ExerciseCompletion
import com.laniao.domain.model.ExerciseSchedule
import com.laniao.domain.model.ExerciseScheduleItem
import com.laniao.domain.model.ExerciseType
import com.laniao.domain.repository.ExerciseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExerciseRepositoryImpl @Inject constructor(
    private val scheduleDao: ExerciseScheduleDao,
    private val completionDao: ExerciseCompletionDao
) : ExerciseRepository {

    // Schedule operations
    override suspend fun insertSchedule(schedule: ExerciseSchedule): Long =
        scheduleDao.insertSchedule(ExerciseScheduleEntity.fromDomainModel(schedule))

    override suspend fun updateSchedule(schedule: ExerciseSchedule) =
        scheduleDao.updateSchedule(ExerciseScheduleEntity.fromDomainModel(schedule))

    override suspend fun deleteSchedule(scheduleId: Long) =
        scheduleDao.deleteScheduleById(scheduleId)

    override suspend fun getScheduleById(id: Long): ExerciseSchedule? =
        scheduleDao.getScheduleById(id)?.toDomainModel()

    override fun getAllSchedules(): Flow<List<ExerciseSchedule>> =
        scheduleDao.getAllSchedules().map { list -> list.map { it.toDomainModel() } }

    override suspend fun getActiveForDate(date: LocalDate): ExerciseSchedule? =
        scheduleDao.getActiveForDate(date)?.toDomainModel()

    override fun getActiveForDateFlow(date: LocalDate): Flow<ExerciseSchedule?> =
        scheduleDao.getActiveForDateFlow(date).map { it?.toDomainModel() }

    override suspend fun hasOverlap(startDate: LocalDate, endDate: LocalDate, excludeId: Long): Boolean =
        scheduleDao.hasOverlap(startDate, endDate, excludeId)

    // Schedule item operations
    override suspend fun insertItems(items: List<ExerciseScheduleItem>) =
        scheduleDao.insertItems(items.map { ExerciseScheduleItemEntity.fromDomainModel(it) })

    override suspend fun getItemsForSchedule(scheduleId: Long): List<ExerciseScheduleItem> =
        scheduleDao.getItemsForSchedule(scheduleId).map { it.toDomainModel() }

    override fun getItemsForScheduleFlow(scheduleId: Long): Flow<List<ExerciseScheduleItem>> =
        scheduleDao.getItemsForScheduleFlow(scheduleId).map { list -> list.map { it.toDomainModel() } }

    override suspend fun deleteItemsForSchedule(scheduleId: Long) =
        scheduleDao.deleteItemsForSchedule(scheduleId)

    // Completion operations
    override suspend fun insertCompletion(completion: ExerciseCompletion): Long =
        completionDao.insert(ExerciseCompletionEntity.fromDomainModel(completion))

    override suspend fun deleteCompletion(id: Long) =
        completionDao.deleteById(id)

    override suspend fun getCompletionById(id: Long): ExerciseCompletion? =
        completionDao.getById(id)?.toDomainModel()

    override fun getCompletionsByDate(date: LocalDate): Flow<List<ExerciseCompletion>> =
        completionDao.getByDate(date).map { list -> list.map { it.toDomainModel() } }

    override suspend fun getCompletionsByDateList(date: LocalDate): List<ExerciseCompletion> =
        completionDao.getByDateList(date).map { it.toDomainModel() }

    override fun getCompletionsByDateRange(startDate: LocalDate, endDate: LocalDate): Flow<List<ExerciseCompletion>> =
        completionDao.getByDateRange(startDate, endDate).map { list -> list.map { it.toDomainModel() } }

    override suspend fun getCompletionCountByTypeAndDate(exerciseType: ExerciseType, date: LocalDate): Int =
        completionDao.getCountByTypeAndDate(exerciseType, date)

    override suspend fun hasCompletionsForDate(date: LocalDate): Boolean =
        completionDao.hasCompletionsForDate(date)
}
