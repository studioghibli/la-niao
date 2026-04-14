package com.laniao.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.laniao.data.local.entity.ExerciseConfigEntity
import com.laniao.domain.model.KegelType
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for ExerciseConfig operations.
 */
@Dao
interface ExerciseConfigDao {

    /**
     * Insert a new exercise config.
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(config: ExerciseConfigEntity): Long

    /**
     * Update an existing config.
     */
    @Update
    suspend fun update(config: ExerciseConfigEntity)

    /**
     * Upsert config - useful for initializing defaults.
     */
    @Upsert
    suspend fun upsert(config: ExerciseConfigEntity)

    /**
     * Delete a config.
     */
    @Delete
    suspend fun delete(config: ExerciseConfigEntity)

    /**
     * Get config by ID.
     */
    @Query("SELECT * FROM exercise_configs WHERE id = :id")
    suspend fun getById(id: Long): ExerciseConfigEntity?

    /**
     * Get config by type.
     */
    @Query("SELECT * FROM exercise_configs WHERE type = :type")
    suspend fun getByType(type: KegelType): ExerciseConfigEntity?

    /**
     * Get all configs.
     */
    @Query("SELECT * FROM exercise_configs ORDER BY type")
    fun getAll(): Flow<List<ExerciseConfigEntity>>

    /**
     * Get only enabled configs.
     */
    @Query("SELECT * FROM exercise_configs WHERE enabled = 1 ORDER BY type")
    fun getEnabled(): Flow<List<ExerciseConfigEntity>>

    /**
     * Get enabled configs as list (non-reactive).
     */
    @Query("SELECT * FROM exercise_configs WHERE enabled = 1 ORDER BY type")
    suspend fun getEnabledList(): List<ExerciseConfigEntity>
}
