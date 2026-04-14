package com.laniao.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.laniao.data.local.LaNiaoDatabase
import com.laniao.data.local.dao.AppSettingsDao
import com.laniao.data.local.dao.DrinkEntryDao
import com.laniao.data.local.dao.ExerciseCompletionDao
import com.laniao.data.local.dao.ExerciseScheduleDao
import com.laniao.data.local.dao.ManuallyMissedTimeDao
import com.laniao.data.local.dao.PeeEntryDao
import com.laniao.data.local.dao.VoidScheduleDao
import com.laniao.data.local.dao.WaterIntakeDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): LaNiaoDatabase {
        return Room.databaseBuilder(
            context,
            LaNiaoDatabase::class.java,
            LaNiaoDatabase.DATABASE_NAME
        )
            .addMigrations(MIGRATION_6_7, MIGRATION_7_8)
            .build()
    }

    private val MIGRATION_6_7 = object : Migration(6, 7) {
        override fun migrate(db: SupportSQLiteDatabase) {
            // Drop old exercise tables (user confirmed starting fresh)
            db.execSQL("DROP TABLE IF EXISTS exercise_completions")
            db.execSQL("DROP TABLE IF EXISTS exercise_configs")

            // Create new exercise schedule tables
            db.execSQL(
                "CREATE TABLE IF NOT EXISTS `exercise_schedules` (" +
                    "`id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                    "`startDate` TEXT NOT NULL, " +
                    "`endDate` TEXT, " +
                    "`enabled` INTEGER NOT NULL, " +
                    "`createdAt` TEXT NOT NULL)"
            )
            db.execSQL(
                "CREATE TABLE IF NOT EXISTS `exercise_schedule_items` (" +
                    "`id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                    "`scheduleId` INTEGER NOT NULL, " +
                    "`exerciseType` TEXT NOT NULL, " +
                    "`sessionsPerDay` INTEGER NOT NULL, " +
                    "`sets` INTEGER NOT NULL, " +
                    "`reps` INTEGER NOT NULL, " +
                    "`holdSeconds` INTEGER NOT NULL, " +
                    "FOREIGN KEY(`scheduleId`) REFERENCES `exercise_schedules`(`id`) ON DELETE CASCADE)"
            )
            db.execSQL("CREATE INDEX IF NOT EXISTS `index_exercise_schedule_items_scheduleId` ON `exercise_schedule_items` (`scheduleId`)")

            // Create new exercise completions table
            db.execSQL(
                "CREATE TABLE IF NOT EXISTS `exercise_completions` (" +
                    "`id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                    "`scheduleItemId` INTEGER, " +
                    "`exerciseType` TEXT NOT NULL, " +
                    "`completedAt` INTEGER NOT NULL, " +
                    "`scheduledDate` TEXT NOT NULL)"
            )
            db.execSQL("CREATE INDEX IF NOT EXISTS `index_exercise_completions_scheduleItemId` ON `exercise_completions` (`scheduleItemId`)")
            db.execSQL("CREATE INDEX IF NOT EXISTS `index_exercise_completions_scheduledDate` ON `exercise_completions` (`scheduledDate`)")
            db.execSQL("CREATE INDEX IF NOT EXISTS `index_exercise_completions_completedAt` ON `exercise_completions` (`completedAt`)")
        }
    }

    private val MIGRATION_7_8 = object : Migration(7, 8) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                "CREATE TABLE IF NOT EXISTS `manually_missed_times` (" +
                    "`id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                    "`date` TEXT NOT NULL, " +
                    "`scheduledTime` TEXT NOT NULL, " +
                    "`createdAt` INTEGER NOT NULL)"
            )
            db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_manually_missed_times_date_scheduledTime` ON `manually_missed_times` (`date`, `scheduledTime`)")
        }
    }

    @Provides
    fun providePeeEntryDao(database: LaNiaoDatabase): PeeEntryDao {
        return database.peeEntryDao()
    }

    @Provides
    fun provideVoidScheduleDao(database: LaNiaoDatabase): VoidScheduleDao {
        return database.voidScheduleDao()
    }

    @Provides
    fun provideWaterIntakeDao(database: LaNiaoDatabase): WaterIntakeDao {
        return database.waterIntakeDao()
    }

    @Provides
    fun provideDrinkEntryDao(database: LaNiaoDatabase): DrinkEntryDao {
        return database.drinkEntryDao()
    }

    @Provides
    fun provideExerciseScheduleDao(database: LaNiaoDatabase): ExerciseScheduleDao {
        return database.exerciseScheduleDao()
    }

    @Provides
    fun provideExerciseCompletionDao(database: LaNiaoDatabase): ExerciseCompletionDao {
        return database.exerciseCompletionDao()
    }

    @Provides
    fun provideAppSettingsDao(database: LaNiaoDatabase): AppSettingsDao {
        return database.appSettingsDao()
    }

    @Provides
    fun provideManuallyMissedTimeDao(database: LaNiaoDatabase): ManuallyMissedTimeDao {
        return database.manuallyMissedTimeDao()
    }
}
