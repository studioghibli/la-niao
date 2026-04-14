package com.laniao.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.laniao.data.local.dao.AppSettingsDao;
import com.laniao.data.local.dao.AppSettingsDao_Impl;
import com.laniao.data.local.dao.DrinkEntryDao;
import com.laniao.data.local.dao.DrinkEntryDao_Impl;
import com.laniao.data.local.dao.ExerciseCompletionDao;
import com.laniao.data.local.dao.ExerciseCompletionDao_Impl;
import com.laniao.data.local.dao.ExerciseScheduleDao;
import com.laniao.data.local.dao.ExerciseScheduleDao_Impl;
import com.laniao.data.local.dao.ManuallyMissedTimeDao;
import com.laniao.data.local.dao.ManuallyMissedTimeDao_Impl;
import com.laniao.data.local.dao.PeeEntryDao;
import com.laniao.data.local.dao.PeeEntryDao_Impl;
import com.laniao.data.local.dao.VoidScheduleDao;
import com.laniao.data.local.dao.VoidScheduleDao_Impl;
import com.laniao.data.local.dao.WaterIntakeDao;
import com.laniao.data.local.dao.WaterIntakeDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class LaNiaoDatabase_Impl extends LaNiaoDatabase {
  private volatile PeeEntryDao _peeEntryDao;

  private volatile VoidScheduleDao _voidScheduleDao;

  private volatile WaterIntakeDao _waterIntakeDao;

  private volatile DrinkEntryDao _drinkEntryDao;

  private volatile ExerciseScheduleDao _exerciseScheduleDao;

  private volatile ExerciseCompletionDao _exerciseCompletionDao;

  private volatile AppSettingsDao _appSettingsDao;

  private volatile ManuallyMissedTimeDao _manuallyMissedTimeDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(8) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `pee_entries` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `timestamp` INTEGER NOT NULL, `didVoid` INTEGER NOT NULL, `leakAmount` TEXT NOT NULL, `volumeSize` TEXT NOT NULL, `color` TEXT NOT NULL, `urgency` TEXT NOT NULL, `activityContext` TEXT, `notes` TEXT, `scheduledTime` TEXT)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_pee_entries_timestamp` ON `pee_entries` (`timestamp`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `void_schedules` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `startTime` TEXT NOT NULL, `endTime` TEXT NOT NULL, `intervalMinutes` INTEGER NOT NULL, `enabled` INTEGER NOT NULL, `createdAt` TEXT NOT NULL, `expiresAt` TEXT NOT NULL)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_void_schedules_enabled` ON `void_schedules` (`enabled`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_void_schedules_expiresAt` ON `void_schedules` (`expiresAt`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `water_intake` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `date` TEXT NOT NULL, `amount` TEXT NOT NULL)");
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_water_intake_date` ON `water_intake` (`date`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `drink_entries` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `timestamp` INTEGER NOT NULL, `type` TEXT NOT NULL, `amount` REAL NOT NULL, `unit` TEXT NOT NULL, `customName` TEXT)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_drink_entries_timestamp` ON `drink_entries` (`timestamp`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `exercise_schedules` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `startDate` TEXT NOT NULL, `endDate` TEXT, `enabled` INTEGER NOT NULL, `createdAt` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `exercise_schedule_items` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `scheduleId` INTEGER NOT NULL, `exerciseType` TEXT NOT NULL, `sessionsPerDay` INTEGER NOT NULL, `sets` INTEGER NOT NULL, `reps` INTEGER NOT NULL, `holdSeconds` INTEGER NOT NULL, FOREIGN KEY(`scheduleId`) REFERENCES `exercise_schedules`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_exercise_schedule_items_scheduleId` ON `exercise_schedule_items` (`scheduleId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `exercise_completions` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `scheduleItemId` INTEGER, `exerciseType` TEXT NOT NULL, `completedAt` INTEGER NOT NULL, `scheduledDate` TEXT NOT NULL)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_exercise_completions_scheduleItemId` ON `exercise_completions` (`scheduleItemId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_exercise_completions_scheduledDate` ON `exercise_completions` (`scheduledDate`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_exercise_completions_completedAt` ON `exercise_completions` (`completedAt`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `app_settings` (`id` INTEGER NOT NULL, `hydrationGoalLiters` REAL NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `manually_missed_times` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `date` TEXT NOT NULL, `scheduledTime` TEXT NOT NULL, `createdAt` INTEGER NOT NULL)");
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_manually_missed_times_date_scheduledTime` ON `manually_missed_times` (`date`, `scheduledTime`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '3a3b1e619b7b91be4b891f5426871fde')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `pee_entries`");
        db.execSQL("DROP TABLE IF EXISTS `void_schedules`");
        db.execSQL("DROP TABLE IF EXISTS `water_intake`");
        db.execSQL("DROP TABLE IF EXISTS `drink_entries`");
        db.execSQL("DROP TABLE IF EXISTS `exercise_schedules`");
        db.execSQL("DROP TABLE IF EXISTS `exercise_schedule_items`");
        db.execSQL("DROP TABLE IF EXISTS `exercise_completions`");
        db.execSQL("DROP TABLE IF EXISTS `app_settings`");
        db.execSQL("DROP TABLE IF EXISTS `manually_missed_times`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsPeeEntries = new HashMap<String, TableInfo.Column>(10);
        _columnsPeeEntries.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPeeEntries.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPeeEntries.put("didVoid", new TableInfo.Column("didVoid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPeeEntries.put("leakAmount", new TableInfo.Column("leakAmount", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPeeEntries.put("volumeSize", new TableInfo.Column("volumeSize", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPeeEntries.put("color", new TableInfo.Column("color", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPeeEntries.put("urgency", new TableInfo.Column("urgency", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPeeEntries.put("activityContext", new TableInfo.Column("activityContext", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPeeEntries.put("notes", new TableInfo.Column("notes", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPeeEntries.put("scheduledTime", new TableInfo.Column("scheduledTime", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPeeEntries = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPeeEntries = new HashSet<TableInfo.Index>(1);
        _indicesPeeEntries.add(new TableInfo.Index("index_pee_entries_timestamp", false, Arrays.asList("timestamp"), Arrays.asList("ASC")));
        final TableInfo _infoPeeEntries = new TableInfo("pee_entries", _columnsPeeEntries, _foreignKeysPeeEntries, _indicesPeeEntries);
        final TableInfo _existingPeeEntries = TableInfo.read(db, "pee_entries");
        if (!_infoPeeEntries.equals(_existingPeeEntries)) {
          return new RoomOpenHelper.ValidationResult(false, "pee_entries(com.laniao.data.local.entity.PeeEntryEntity).\n"
                  + " Expected:\n" + _infoPeeEntries + "\n"
                  + " Found:\n" + _existingPeeEntries);
        }
        final HashMap<String, TableInfo.Column> _columnsVoidSchedules = new HashMap<String, TableInfo.Column>(7);
        _columnsVoidSchedules.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVoidSchedules.put("startTime", new TableInfo.Column("startTime", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVoidSchedules.put("endTime", new TableInfo.Column("endTime", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVoidSchedules.put("intervalMinutes", new TableInfo.Column("intervalMinutes", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVoidSchedules.put("enabled", new TableInfo.Column("enabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVoidSchedules.put("createdAt", new TableInfo.Column("createdAt", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVoidSchedules.put("expiresAt", new TableInfo.Column("expiresAt", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysVoidSchedules = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesVoidSchedules = new HashSet<TableInfo.Index>(2);
        _indicesVoidSchedules.add(new TableInfo.Index("index_void_schedules_enabled", false, Arrays.asList("enabled"), Arrays.asList("ASC")));
        _indicesVoidSchedules.add(new TableInfo.Index("index_void_schedules_expiresAt", false, Arrays.asList("expiresAt"), Arrays.asList("ASC")));
        final TableInfo _infoVoidSchedules = new TableInfo("void_schedules", _columnsVoidSchedules, _foreignKeysVoidSchedules, _indicesVoidSchedules);
        final TableInfo _existingVoidSchedules = TableInfo.read(db, "void_schedules");
        if (!_infoVoidSchedules.equals(_existingVoidSchedules)) {
          return new RoomOpenHelper.ValidationResult(false, "void_schedules(com.laniao.data.local.entity.VoidScheduleEntity).\n"
                  + " Expected:\n" + _infoVoidSchedules + "\n"
                  + " Found:\n" + _existingVoidSchedules);
        }
        final HashMap<String, TableInfo.Column> _columnsWaterIntake = new HashMap<String, TableInfo.Column>(3);
        _columnsWaterIntake.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWaterIntake.put("date", new TableInfo.Column("date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWaterIntake.put("amount", new TableInfo.Column("amount", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysWaterIntake = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesWaterIntake = new HashSet<TableInfo.Index>(1);
        _indicesWaterIntake.add(new TableInfo.Index("index_water_intake_date", true, Arrays.asList("date"), Arrays.asList("ASC")));
        final TableInfo _infoWaterIntake = new TableInfo("water_intake", _columnsWaterIntake, _foreignKeysWaterIntake, _indicesWaterIntake);
        final TableInfo _existingWaterIntake = TableInfo.read(db, "water_intake");
        if (!_infoWaterIntake.equals(_existingWaterIntake)) {
          return new RoomOpenHelper.ValidationResult(false, "water_intake(com.laniao.data.local.entity.WaterIntakeEntity).\n"
                  + " Expected:\n" + _infoWaterIntake + "\n"
                  + " Found:\n" + _existingWaterIntake);
        }
        final HashMap<String, TableInfo.Column> _columnsDrinkEntries = new HashMap<String, TableInfo.Column>(6);
        _columnsDrinkEntries.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDrinkEntries.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDrinkEntries.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDrinkEntries.put("amount", new TableInfo.Column("amount", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDrinkEntries.put("unit", new TableInfo.Column("unit", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDrinkEntries.put("customName", new TableInfo.Column("customName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysDrinkEntries = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesDrinkEntries = new HashSet<TableInfo.Index>(1);
        _indicesDrinkEntries.add(new TableInfo.Index("index_drink_entries_timestamp", false, Arrays.asList("timestamp"), Arrays.asList("ASC")));
        final TableInfo _infoDrinkEntries = new TableInfo("drink_entries", _columnsDrinkEntries, _foreignKeysDrinkEntries, _indicesDrinkEntries);
        final TableInfo _existingDrinkEntries = TableInfo.read(db, "drink_entries");
        if (!_infoDrinkEntries.equals(_existingDrinkEntries)) {
          return new RoomOpenHelper.ValidationResult(false, "drink_entries(com.laniao.data.local.entity.DrinkEntryEntity).\n"
                  + " Expected:\n" + _infoDrinkEntries + "\n"
                  + " Found:\n" + _existingDrinkEntries);
        }
        final HashMap<String, TableInfo.Column> _columnsExerciseSchedules = new HashMap<String, TableInfo.Column>(5);
        _columnsExerciseSchedules.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExerciseSchedules.put("startDate", new TableInfo.Column("startDate", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExerciseSchedules.put("endDate", new TableInfo.Column("endDate", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExerciseSchedules.put("enabled", new TableInfo.Column("enabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExerciseSchedules.put("createdAt", new TableInfo.Column("createdAt", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysExerciseSchedules = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesExerciseSchedules = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoExerciseSchedules = new TableInfo("exercise_schedules", _columnsExerciseSchedules, _foreignKeysExerciseSchedules, _indicesExerciseSchedules);
        final TableInfo _existingExerciseSchedules = TableInfo.read(db, "exercise_schedules");
        if (!_infoExerciseSchedules.equals(_existingExerciseSchedules)) {
          return new RoomOpenHelper.ValidationResult(false, "exercise_schedules(com.laniao.data.local.entity.ExerciseScheduleEntity).\n"
                  + " Expected:\n" + _infoExerciseSchedules + "\n"
                  + " Found:\n" + _existingExerciseSchedules);
        }
        final HashMap<String, TableInfo.Column> _columnsExerciseScheduleItems = new HashMap<String, TableInfo.Column>(7);
        _columnsExerciseScheduleItems.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExerciseScheduleItems.put("scheduleId", new TableInfo.Column("scheduleId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExerciseScheduleItems.put("exerciseType", new TableInfo.Column("exerciseType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExerciseScheduleItems.put("sessionsPerDay", new TableInfo.Column("sessionsPerDay", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExerciseScheduleItems.put("sets", new TableInfo.Column("sets", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExerciseScheduleItems.put("reps", new TableInfo.Column("reps", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExerciseScheduleItems.put("holdSeconds", new TableInfo.Column("holdSeconds", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysExerciseScheduleItems = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysExerciseScheduleItems.add(new TableInfo.ForeignKey("exercise_schedules", "CASCADE", "NO ACTION", Arrays.asList("scheduleId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesExerciseScheduleItems = new HashSet<TableInfo.Index>(1);
        _indicesExerciseScheduleItems.add(new TableInfo.Index("index_exercise_schedule_items_scheduleId", false, Arrays.asList("scheduleId"), Arrays.asList("ASC")));
        final TableInfo _infoExerciseScheduleItems = new TableInfo("exercise_schedule_items", _columnsExerciseScheduleItems, _foreignKeysExerciseScheduleItems, _indicesExerciseScheduleItems);
        final TableInfo _existingExerciseScheduleItems = TableInfo.read(db, "exercise_schedule_items");
        if (!_infoExerciseScheduleItems.equals(_existingExerciseScheduleItems)) {
          return new RoomOpenHelper.ValidationResult(false, "exercise_schedule_items(com.laniao.data.local.entity.ExerciseScheduleItemEntity).\n"
                  + " Expected:\n" + _infoExerciseScheduleItems + "\n"
                  + " Found:\n" + _existingExerciseScheduleItems);
        }
        final HashMap<String, TableInfo.Column> _columnsExerciseCompletions = new HashMap<String, TableInfo.Column>(5);
        _columnsExerciseCompletions.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExerciseCompletions.put("scheduleItemId", new TableInfo.Column("scheduleItemId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExerciseCompletions.put("exerciseType", new TableInfo.Column("exerciseType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExerciseCompletions.put("completedAt", new TableInfo.Column("completedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExerciseCompletions.put("scheduledDate", new TableInfo.Column("scheduledDate", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysExerciseCompletions = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesExerciseCompletions = new HashSet<TableInfo.Index>(3);
        _indicesExerciseCompletions.add(new TableInfo.Index("index_exercise_completions_scheduleItemId", false, Arrays.asList("scheduleItemId"), Arrays.asList("ASC")));
        _indicesExerciseCompletions.add(new TableInfo.Index("index_exercise_completions_scheduledDate", false, Arrays.asList("scheduledDate"), Arrays.asList("ASC")));
        _indicesExerciseCompletions.add(new TableInfo.Index("index_exercise_completions_completedAt", false, Arrays.asList("completedAt"), Arrays.asList("ASC")));
        final TableInfo _infoExerciseCompletions = new TableInfo("exercise_completions", _columnsExerciseCompletions, _foreignKeysExerciseCompletions, _indicesExerciseCompletions);
        final TableInfo _existingExerciseCompletions = TableInfo.read(db, "exercise_completions");
        if (!_infoExerciseCompletions.equals(_existingExerciseCompletions)) {
          return new RoomOpenHelper.ValidationResult(false, "exercise_completions(com.laniao.data.local.entity.ExerciseCompletionEntity).\n"
                  + " Expected:\n" + _infoExerciseCompletions + "\n"
                  + " Found:\n" + _existingExerciseCompletions);
        }
        final HashMap<String, TableInfo.Column> _columnsAppSettings = new HashMap<String, TableInfo.Column>(2);
        _columnsAppSettings.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppSettings.put("hydrationGoalLiters", new TableInfo.Column("hydrationGoalLiters", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAppSettings = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesAppSettings = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoAppSettings = new TableInfo("app_settings", _columnsAppSettings, _foreignKeysAppSettings, _indicesAppSettings);
        final TableInfo _existingAppSettings = TableInfo.read(db, "app_settings");
        if (!_infoAppSettings.equals(_existingAppSettings)) {
          return new RoomOpenHelper.ValidationResult(false, "app_settings(com.laniao.data.local.entity.AppSettingsEntity).\n"
                  + " Expected:\n" + _infoAppSettings + "\n"
                  + " Found:\n" + _existingAppSettings);
        }
        final HashMap<String, TableInfo.Column> _columnsManuallyMissedTimes = new HashMap<String, TableInfo.Column>(4);
        _columnsManuallyMissedTimes.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsManuallyMissedTimes.put("date", new TableInfo.Column("date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsManuallyMissedTimes.put("scheduledTime", new TableInfo.Column("scheduledTime", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsManuallyMissedTimes.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysManuallyMissedTimes = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesManuallyMissedTimes = new HashSet<TableInfo.Index>(1);
        _indicesManuallyMissedTimes.add(new TableInfo.Index("index_manually_missed_times_date_scheduledTime", true, Arrays.asList("date", "scheduledTime"), Arrays.asList("ASC", "ASC")));
        final TableInfo _infoManuallyMissedTimes = new TableInfo("manually_missed_times", _columnsManuallyMissedTimes, _foreignKeysManuallyMissedTimes, _indicesManuallyMissedTimes);
        final TableInfo _existingManuallyMissedTimes = TableInfo.read(db, "manually_missed_times");
        if (!_infoManuallyMissedTimes.equals(_existingManuallyMissedTimes)) {
          return new RoomOpenHelper.ValidationResult(false, "manually_missed_times(com.laniao.data.local.entity.ManuallyMissedTimeEntity).\n"
                  + " Expected:\n" + _infoManuallyMissedTimes + "\n"
                  + " Found:\n" + _existingManuallyMissedTimes);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "3a3b1e619b7b91be4b891f5426871fde", "8ae1247626384218339292fb99416170");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "pee_entries","void_schedules","water_intake","drink_entries","exercise_schedules","exercise_schedule_items","exercise_completions","app_settings","manually_missed_times");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `pee_entries`");
      _db.execSQL("DELETE FROM `void_schedules`");
      _db.execSQL("DELETE FROM `water_intake`");
      _db.execSQL("DELETE FROM `drink_entries`");
      _db.execSQL("DELETE FROM `exercise_schedules`");
      _db.execSQL("DELETE FROM `exercise_schedule_items`");
      _db.execSQL("DELETE FROM `exercise_completions`");
      _db.execSQL("DELETE FROM `app_settings`");
      _db.execSQL("DELETE FROM `manually_missed_times`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(PeeEntryDao.class, PeeEntryDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(VoidScheduleDao.class, VoidScheduleDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(WaterIntakeDao.class, WaterIntakeDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(DrinkEntryDao.class, DrinkEntryDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ExerciseScheduleDao.class, ExerciseScheduleDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ExerciseCompletionDao.class, ExerciseCompletionDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(AppSettingsDao.class, AppSettingsDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ManuallyMissedTimeDao.class, ManuallyMissedTimeDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public PeeEntryDao peeEntryDao() {
    if (_peeEntryDao != null) {
      return _peeEntryDao;
    } else {
      synchronized(this) {
        if(_peeEntryDao == null) {
          _peeEntryDao = new PeeEntryDao_Impl(this);
        }
        return _peeEntryDao;
      }
    }
  }

  @Override
  public VoidScheduleDao voidScheduleDao() {
    if (_voidScheduleDao != null) {
      return _voidScheduleDao;
    } else {
      synchronized(this) {
        if(_voidScheduleDao == null) {
          _voidScheduleDao = new VoidScheduleDao_Impl(this);
        }
        return _voidScheduleDao;
      }
    }
  }

  @Override
  public WaterIntakeDao waterIntakeDao() {
    if (_waterIntakeDao != null) {
      return _waterIntakeDao;
    } else {
      synchronized(this) {
        if(_waterIntakeDao == null) {
          _waterIntakeDao = new WaterIntakeDao_Impl(this);
        }
        return _waterIntakeDao;
      }
    }
  }

  @Override
  public DrinkEntryDao drinkEntryDao() {
    if (_drinkEntryDao != null) {
      return _drinkEntryDao;
    } else {
      synchronized(this) {
        if(_drinkEntryDao == null) {
          _drinkEntryDao = new DrinkEntryDao_Impl(this);
        }
        return _drinkEntryDao;
      }
    }
  }

  @Override
  public ExerciseScheduleDao exerciseScheduleDao() {
    if (_exerciseScheduleDao != null) {
      return _exerciseScheduleDao;
    } else {
      synchronized(this) {
        if(_exerciseScheduleDao == null) {
          _exerciseScheduleDao = new ExerciseScheduleDao_Impl(this);
        }
        return _exerciseScheduleDao;
      }
    }
  }

  @Override
  public ExerciseCompletionDao exerciseCompletionDao() {
    if (_exerciseCompletionDao != null) {
      return _exerciseCompletionDao;
    } else {
      synchronized(this) {
        if(_exerciseCompletionDao == null) {
          _exerciseCompletionDao = new ExerciseCompletionDao_Impl(this);
        }
        return _exerciseCompletionDao;
      }
    }
  }

  @Override
  public AppSettingsDao appSettingsDao() {
    if (_appSettingsDao != null) {
      return _appSettingsDao;
    } else {
      synchronized(this) {
        if(_appSettingsDao == null) {
          _appSettingsDao = new AppSettingsDao_Impl(this);
        }
        return _appSettingsDao;
      }
    }
  }

  @Override
  public ManuallyMissedTimeDao manuallyMissedTimeDao() {
    if (_manuallyMissedTimeDao != null) {
      return _manuallyMissedTimeDao;
    } else {
      synchronized(this) {
        if(_manuallyMissedTimeDao == null) {
          _manuallyMissedTimeDao = new ManuallyMissedTimeDao_Impl(this);
        }
        return _manuallyMissedTimeDao;
      }
    }
  }
}
