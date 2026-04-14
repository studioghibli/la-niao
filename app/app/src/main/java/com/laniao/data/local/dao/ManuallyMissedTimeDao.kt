package com.laniao.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.laniao.data.local.entity.ManuallyMissedTimeEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalTime

/**
 * Data Access Object for manually missed scheduled times.
 */
@Dao
interface ManuallyMissedTimeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: ManuallyMissedTimeEntity): Long

    @Query("DELETE FROM manually_missed_times WHERE date = :date AND scheduledTime = :scheduledTime")
    suspend fun delete(date: LocalDate, scheduledTime: LocalTime)

    @Query("SELECT * FROM manually_missed_times WHERE date = :date")
    fun getByDate(date: LocalDate): Flow<List<ManuallyMissedTimeEntity>>

    @Query("SELECT * FROM manually_missed_times WHERE date = :date")
    suspend fun getByDateOnce(date: LocalDate): List<ManuallyMissedTimeEntity>

    @Query("SELECT * FROM manually_missed_times WHERE date BETWEEN :startDate AND :endDate")
    fun getByDateRange(startDate: LocalDate, endDate: LocalDate): Flow<List<ManuallyMissedTimeEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM manually_missed_times WHERE date = :date AND scheduledTime = :scheduledTime)")
    suspend fun exists(date: LocalDate, scheduledTime: LocalTime): Boolean

    @Query("DELETE FROM manually_missed_times")
    suspend fun deleteAll()

    @Query("SELECT * FROM manually_missed_times")
    suspend fun getAll(): List<ManuallyMissedTimeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<ManuallyMissedTimeEntity>)
}
