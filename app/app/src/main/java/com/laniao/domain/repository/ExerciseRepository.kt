package com.laniao.domain.repository

import com.laniao.domain.model.ExerciseCompletion
import com.laniao.domain.model.ExerciseSchedule
import com.laniao.domain.model.ExerciseScheduleItem
import com.laniao.domain.model.ExerciseType
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface ExerciseRepository {

    // Schedule operations
    suspend fun insertSchedule(schedule: ExerciseSchedule): Long
    suspend fun updateSchedule(schedule: ExerciseSchedule)
    suspend fun deleteSchedule(scheduleId: Long)
    suspend fun getScheduleById(id: Long): ExerciseSchedule?
    fun getAllSchedules(): Flow<List<ExerciseSchedule>>
    suspend fun getActiveForDate(date: LocalDate): ExerciseSchedule?
    fun getActiveForDateFlow(date: LocalDate): Flow<ExerciseSchedule?>
    suspend fun hasOverlap(startDate: LocalDate, endDate: LocalDate, excludeId: Long = 0): Boolean

    // Schedule item operations
    suspend fun insertItems(items: List<ExerciseScheduleItem>)
    suspend fun getItemsForSchedule(scheduleId: Long): List<ExerciseScheduleItem>
    fun getItemsForScheduleFlow(scheduleId: Long): Flow<List<ExerciseScheduleItem>>
    suspend fun deleteItemsForSchedule(scheduleId: Long)

    // Completion operations
    suspend fun insertCompletion(completion: ExerciseCompletion): Long
    suspend fun deleteCompletion(id: Long)
    suspend fun getCompletionById(id: Long): ExerciseCompletion?
    fun getCompletionsByDate(date: LocalDate): Flow<List<ExerciseCompletion>>
    suspend fun getCompletionsByDateList(date: LocalDate): List<ExerciseCompletion>
    fun getCompletionsByDateRange(startDate: LocalDate, endDate: LocalDate): Flow<List<ExerciseCompletion>>
    suspend fun getCompletionCountByTypeAndDate(exerciseType: ExerciseType, date: LocalDate): Int
    suspend fun hasCompletionsForDate(date: LocalDate): Boolean
}
