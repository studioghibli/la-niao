package com.laniao.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.laniao.data.local.entity.ExerciseCompletionEntity
import com.laniao.domain.model.ExerciseType
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface ExerciseCompletionDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(completion: ExerciseCompletionEntity): Long

    @Delete
    suspend fun delete(completion: ExerciseCompletionEntity)

    @Query("DELETE FROM exercise_completions WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT * FROM exercise_completions WHERE id = :id")
    suspend fun getById(id: Long): ExerciseCompletionEntity?

    @Query("SELECT * FROM exercise_completions WHERE scheduledDate = :date ORDER BY completedAt DESC")
    fun getByDate(date: LocalDate): Flow<List<ExerciseCompletionEntity>>

    @Query("SELECT * FROM exercise_completions WHERE scheduledDate = :date ORDER BY completedAt DESC")
    suspend fun getByDateList(date: LocalDate): List<ExerciseCompletionEntity>

    @Query("""
        SELECT * FROM exercise_completions 
        WHERE scheduledDate >= :startDate AND scheduledDate <= :endDate 
        ORDER BY completedAt DESC
    """)
    fun getByDateRange(startDate: LocalDate, endDate: LocalDate): Flow<List<ExerciseCompletionEntity>>

    @Query("""
        SELECT COUNT(*) FROM exercise_completions 
        WHERE exerciseType = :exerciseType AND scheduledDate = :date
    """)
    suspend fun getCountByTypeAndDate(exerciseType: ExerciseType, date: LocalDate): Int

    @Query("SELECT EXISTS(SELECT 1 FROM exercise_completions WHERE scheduledDate = :date)")
    suspend fun hasCompletionsForDate(date: LocalDate): Boolean

    @Query("SELECT * FROM exercise_completions ORDER BY completedAt DESC")
    fun getAll(): Flow<List<ExerciseCompletionEntity>>

    @Query("DELETE FROM exercise_completions")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(completions: List<ExerciseCompletionEntity>)
}
