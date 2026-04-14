package com.laniao.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.laniao.data.local.entity.AppSettingsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AppSettingsDao {
    @Query("SELECT * FROM app_settings WHERE id = 1")
    fun get(): Flow<AppSettingsEntity?>

    @Query("SELECT * FROM app_settings WHERE id = 1")
    suspend fun getOnce(): AppSettingsEntity?

    @Upsert
    suspend fun upsert(settings: AppSettingsEntity)

    @Query("DELETE FROM app_settings")
    suspend fun deleteAll()
}
