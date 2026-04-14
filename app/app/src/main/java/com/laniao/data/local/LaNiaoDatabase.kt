package com.laniao.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.laniao.data.local.dao.AppSettingsDao
import com.laniao.data.local.dao.DrinkEntryDao
import com.laniao.data.local.dao.ExerciseCompletionDao
import com.laniao.data.local.dao.ExerciseScheduleDao
import com.laniao.data.local.dao.ManuallyMissedTimeDao
import com.laniao.data.local.dao.PeeEntryDao
import com.laniao.data.local.dao.VoidScheduleDao
import com.laniao.data.local.dao.WaterIntakeDao
import com.laniao.data.local.entity.AppSettingsEntity
import com.laniao.data.local.entity.DrinkEntryEntity
import com.laniao.data.local.entity.ExerciseCompletionEntity
import com.laniao.data.local.entity.ExerciseScheduleEntity
import com.laniao.data.local.entity.ExerciseScheduleItemEntity
import com.laniao.data.local.entity.ManuallyMissedTimeEntity
import com.laniao.data.local.entity.PeeEntryEntity
import com.laniao.data.local.entity.VoidScheduleEntity
import com.laniao.data.local.entity.WaterIntakeEntity

/**
 * Room database for LaNiao app.
 */
@Database(
    entities = [
        PeeEntryEntity::class,
        VoidScheduleEntity::class,
        WaterIntakeEntity::class,
        DrinkEntryEntity::class,
        ExerciseScheduleEntity::class,
        ExerciseScheduleItemEntity::class,
        ExerciseCompletionEntity::class,
        AppSettingsEntity::class,
        ManuallyMissedTimeEntity::class
    ],
    version = 8,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class LaNiaoDatabase : RoomDatabase() {
    companion object {
        const val DATABASE_NAME = "laniao_database"
    }

    abstract fun peeEntryDao(): PeeEntryDao
    abstract fun voidScheduleDao(): VoidScheduleDao
    abstract fun waterIntakeDao(): WaterIntakeDao
    abstract fun drinkEntryDao(): DrinkEntryDao
    abstract fun exerciseScheduleDao(): ExerciseScheduleDao
    abstract fun exerciseCompletionDao(): ExerciseCompletionDao
    abstract fun appSettingsDao(): AppSettingsDao
    abstract fun manuallyMissedTimeDao(): ManuallyMissedTimeDao
}
