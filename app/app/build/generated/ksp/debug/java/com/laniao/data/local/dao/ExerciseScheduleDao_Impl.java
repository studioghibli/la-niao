package com.laniao.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.laniao.data.local.Converters;
import com.laniao.data.local.entity.ExerciseScheduleEntity;
import com.laniao.data.local.entity.ExerciseScheduleItemEntity;
import com.laniao.domain.model.ExerciseType;
import java.lang.Boolean;
import java.lang.Class;
import java.lang.Exception;
import java.lang.IllegalArgumentException;
import java.lang.IllegalStateException;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ExerciseScheduleDao_Impl implements ExerciseScheduleDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ExerciseScheduleEntity> __insertionAdapterOfExerciseScheduleEntity;

  private final Converters __converters = new Converters();

  private final EntityInsertionAdapter<ExerciseScheduleItemEntity> __insertionAdapterOfExerciseScheduleItemEntity;

  private final EntityDeletionOrUpdateAdapter<ExerciseScheduleEntity> __deletionAdapterOfExerciseScheduleEntity;

  private final EntityDeletionOrUpdateAdapter<ExerciseScheduleEntity> __updateAdapterOfExerciseScheduleEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteScheduleById;

  private final SharedSQLiteStatement __preparedStmtOfDeleteItemsForSchedule;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllSchedules;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllItems;

  public ExerciseScheduleDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfExerciseScheduleEntity = new EntityInsertionAdapter<ExerciseScheduleEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `exercise_schedules` (`id`,`startDate`,`endDate`,`enabled`,`createdAt`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ExerciseScheduleEntity entity) {
        statement.bindLong(1, entity.getId());
        final String _tmp = __converters.fromLocalDate(entity.getStartDate());
        if (_tmp == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, _tmp);
        }
        final String _tmp_1 = __converters.fromLocalDate(entity.getEndDate());
        if (_tmp_1 == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, _tmp_1);
        }
        final int _tmp_2 = entity.getEnabled() ? 1 : 0;
        statement.bindLong(4, _tmp_2);
        final String _tmp_3 = __converters.fromLocalDate(entity.getCreatedAt());
        if (_tmp_3 == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, _tmp_3);
        }
      }
    };
    this.__insertionAdapterOfExerciseScheduleItemEntity = new EntityInsertionAdapter<ExerciseScheduleItemEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `exercise_schedule_items` (`id`,`scheduleId`,`exerciseType`,`sessionsPerDay`,`sets`,`reps`,`holdSeconds`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ExerciseScheduleItemEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getScheduleId());
        statement.bindString(3, __ExerciseType_enumToString(entity.getExerciseType()));
        statement.bindLong(4, entity.getSessionsPerDay());
        statement.bindLong(5, entity.getSets());
        statement.bindLong(6, entity.getReps());
        statement.bindLong(7, entity.getHoldSeconds());
      }
    };
    this.__deletionAdapterOfExerciseScheduleEntity = new EntityDeletionOrUpdateAdapter<ExerciseScheduleEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `exercise_schedules` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ExerciseScheduleEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfExerciseScheduleEntity = new EntityDeletionOrUpdateAdapter<ExerciseScheduleEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `exercise_schedules` SET `id` = ?,`startDate` = ?,`endDate` = ?,`enabled` = ?,`createdAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ExerciseScheduleEntity entity) {
        statement.bindLong(1, entity.getId());
        final String _tmp = __converters.fromLocalDate(entity.getStartDate());
        if (_tmp == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, _tmp);
        }
        final String _tmp_1 = __converters.fromLocalDate(entity.getEndDate());
        if (_tmp_1 == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, _tmp_1);
        }
        final int _tmp_2 = entity.getEnabled() ? 1 : 0;
        statement.bindLong(4, _tmp_2);
        final String _tmp_3 = __converters.fromLocalDate(entity.getCreatedAt());
        if (_tmp_3 == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, _tmp_3);
        }
        statement.bindLong(6, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteScheduleById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM exercise_schedules WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteItemsForSchedule = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM exercise_schedule_items WHERE scheduleId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAllSchedules = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM exercise_schedules";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAllItems = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM exercise_schedule_items";
        return _query;
      }
    };
  }

  @Override
  public Object insertSchedule(final ExerciseScheduleEntity schedule,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfExerciseScheduleEntity.insertAndReturnId(schedule);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertItem(final ExerciseScheduleItemEntity item,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfExerciseScheduleItemEntity.insertAndReturnId(item);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertItems(final List<ExerciseScheduleItemEntity> items,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfExerciseScheduleItemEntity.insert(items);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteSchedule(final ExerciseScheduleEntity schedule,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfExerciseScheduleEntity.handle(schedule);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateSchedule(final ExerciseScheduleEntity schedule,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfExerciseScheduleEntity.handle(schedule);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteScheduleById(final long scheduleId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteScheduleById.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, scheduleId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteScheduleById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteItemsForSchedule(final long scheduleId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteItemsForSchedule.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, scheduleId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteItemsForSchedule.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAllSchedules(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllSchedules.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteAllSchedules.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAllItems(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllItems.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteAllItems.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getScheduleById(final long id,
      final Continuation<? super ExerciseScheduleEntity> $completion) {
    final String _sql = "SELECT * FROM exercise_schedules WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ExerciseScheduleEntity>() {
      @Override
      @Nullable
      public ExerciseScheduleEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
          final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "endDate");
          final int _cursorIndexOfEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "enabled");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final ExerciseScheduleEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final LocalDate _tmpStartDate;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfStartDate)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfStartDate);
            }
            final LocalDate _tmp_1 = __converters.toLocalDate(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDate', but it was NULL.");
            } else {
              _tmpStartDate = _tmp_1;
            }
            final LocalDate _tmpEndDate;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfEndDate)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfEndDate);
            }
            _tmpEndDate = __converters.toLocalDate(_tmp_2);
            final boolean _tmpEnabled;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfEnabled);
            _tmpEnabled = _tmp_3 != 0;
            final LocalDate _tmpCreatedAt;
            final String _tmp_4;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getString(_cursorIndexOfCreatedAt);
            }
            final LocalDate _tmp_5 = __converters.toLocalDate(_tmp_4);
            if (_tmp_5 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDate', but it was NULL.");
            } else {
              _tmpCreatedAt = _tmp_5;
            }
            _result = new ExerciseScheduleEntity(_tmpId,_tmpStartDate,_tmpEndDate,_tmpEnabled,_tmpCreatedAt);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<ExerciseScheduleEntity>> getAllSchedules() {
    final String _sql = "SELECT * FROM exercise_schedules ORDER BY startDate DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"exercise_schedules"}, new Callable<List<ExerciseScheduleEntity>>() {
      @Override
      @NonNull
      public List<ExerciseScheduleEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
          final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "endDate");
          final int _cursorIndexOfEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "enabled");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<ExerciseScheduleEntity> _result = new ArrayList<ExerciseScheduleEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ExerciseScheduleEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final LocalDate _tmpStartDate;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfStartDate)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfStartDate);
            }
            final LocalDate _tmp_1 = __converters.toLocalDate(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDate', but it was NULL.");
            } else {
              _tmpStartDate = _tmp_1;
            }
            final LocalDate _tmpEndDate;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfEndDate)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfEndDate);
            }
            _tmpEndDate = __converters.toLocalDate(_tmp_2);
            final boolean _tmpEnabled;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfEnabled);
            _tmpEnabled = _tmp_3 != 0;
            final LocalDate _tmpCreatedAt;
            final String _tmp_4;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getString(_cursorIndexOfCreatedAt);
            }
            final LocalDate _tmp_5 = __converters.toLocalDate(_tmp_4);
            if (_tmp_5 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDate', but it was NULL.");
            } else {
              _tmpCreatedAt = _tmp_5;
            }
            _item = new ExerciseScheduleEntity(_tmpId,_tmpStartDate,_tmpEndDate,_tmpEnabled,_tmpCreatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getActiveForDate(final LocalDate date,
      final Continuation<? super ExerciseScheduleEntity> $completion) {
    final String _sql = "\n"
            + "        SELECT * FROM exercise_schedules \n"
            + "        WHERE enabled = 1 AND startDate <= ? AND (endDate IS NULL OR endDate >= ?)\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    final String _tmp = __converters.fromLocalDate(date);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, _tmp);
    }
    _argIndex = 2;
    final String _tmp_1 = __converters.fromLocalDate(date);
    if (_tmp_1 == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, _tmp_1);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ExerciseScheduleEntity>() {
      @Override
      @Nullable
      public ExerciseScheduleEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
          final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "endDate");
          final int _cursorIndexOfEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "enabled");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final ExerciseScheduleEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final LocalDate _tmpStartDate;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfStartDate)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfStartDate);
            }
            final LocalDate _tmp_3 = __converters.toLocalDate(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDate', but it was NULL.");
            } else {
              _tmpStartDate = _tmp_3;
            }
            final LocalDate _tmpEndDate;
            final String _tmp_4;
            if (_cursor.isNull(_cursorIndexOfEndDate)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getString(_cursorIndexOfEndDate);
            }
            _tmpEndDate = __converters.toLocalDate(_tmp_4);
            final boolean _tmpEnabled;
            final int _tmp_5;
            _tmp_5 = _cursor.getInt(_cursorIndexOfEnabled);
            _tmpEnabled = _tmp_5 != 0;
            final LocalDate _tmpCreatedAt;
            final String _tmp_6;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmp_6 = null;
            } else {
              _tmp_6 = _cursor.getString(_cursorIndexOfCreatedAt);
            }
            final LocalDate _tmp_7 = __converters.toLocalDate(_tmp_6);
            if (_tmp_7 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDate', but it was NULL.");
            } else {
              _tmpCreatedAt = _tmp_7;
            }
            _result = new ExerciseScheduleEntity(_tmpId,_tmpStartDate,_tmpEndDate,_tmpEnabled,_tmpCreatedAt);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<ExerciseScheduleEntity> getActiveForDateFlow(final LocalDate date) {
    final String _sql = "\n"
            + "        SELECT * FROM exercise_schedules \n"
            + "        WHERE enabled = 1 AND startDate <= ? AND (endDate IS NULL OR endDate >= ?)\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    final String _tmp = __converters.fromLocalDate(date);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, _tmp);
    }
    _argIndex = 2;
    final String _tmp_1 = __converters.fromLocalDate(date);
    if (_tmp_1 == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, _tmp_1);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"exercise_schedules"}, new Callable<ExerciseScheduleEntity>() {
      @Override
      @Nullable
      public ExerciseScheduleEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
          final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "endDate");
          final int _cursorIndexOfEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "enabled");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final ExerciseScheduleEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final LocalDate _tmpStartDate;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfStartDate)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfStartDate);
            }
            final LocalDate _tmp_3 = __converters.toLocalDate(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDate', but it was NULL.");
            } else {
              _tmpStartDate = _tmp_3;
            }
            final LocalDate _tmpEndDate;
            final String _tmp_4;
            if (_cursor.isNull(_cursorIndexOfEndDate)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getString(_cursorIndexOfEndDate);
            }
            _tmpEndDate = __converters.toLocalDate(_tmp_4);
            final boolean _tmpEnabled;
            final int _tmp_5;
            _tmp_5 = _cursor.getInt(_cursorIndexOfEnabled);
            _tmpEnabled = _tmp_5 != 0;
            final LocalDate _tmpCreatedAt;
            final String _tmp_6;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmp_6 = null;
            } else {
              _tmp_6 = _cursor.getString(_cursorIndexOfCreatedAt);
            }
            final LocalDate _tmp_7 = __converters.toLocalDate(_tmp_6);
            if (_tmp_7 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDate', but it was NULL.");
            } else {
              _tmpCreatedAt = _tmp_7;
            }
            _result = new ExerciseScheduleEntity(_tmpId,_tmpStartDate,_tmpEndDate,_tmpEnabled,_tmpCreatedAt);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object hasOverlap(final LocalDate startDate, final LocalDate endDate, final long excludeId,
      final Continuation<? super Boolean> $completion) {
    final String _sql = "\n"
            + "        SELECT EXISTS(\n"
            + "            SELECT 1 FROM exercise_schedules \n"
            + "            WHERE enabled = 1 AND id != ? \n"
            + "            AND startDate <= ? AND (endDate IS NULL OR endDate >= ?)\n"
            + "        )\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, excludeId);
    _argIndex = 2;
    final String _tmp = __converters.fromLocalDate(endDate);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, _tmp);
    }
    _argIndex = 3;
    final String _tmp_1 = __converters.fromLocalDate(startDate);
    if (_tmp_1 == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, _tmp_1);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Boolean>() {
      @Override
      @NonNull
      public Boolean call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Boolean _result;
          if (_cursor.moveToFirst()) {
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(0);
            _result = _tmp_2 != 0;
          } else {
            _result = false;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getItemsForSchedule(final long scheduleId,
      final Continuation<? super List<ExerciseScheduleItemEntity>> $completion) {
    final String _sql = "SELECT * FROM exercise_schedule_items WHERE scheduleId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, scheduleId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ExerciseScheduleItemEntity>>() {
      @Override
      @NonNull
      public List<ExerciseScheduleItemEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfScheduleId = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduleId");
          final int _cursorIndexOfExerciseType = CursorUtil.getColumnIndexOrThrow(_cursor, "exerciseType");
          final int _cursorIndexOfSessionsPerDay = CursorUtil.getColumnIndexOrThrow(_cursor, "sessionsPerDay");
          final int _cursorIndexOfSets = CursorUtil.getColumnIndexOrThrow(_cursor, "sets");
          final int _cursorIndexOfReps = CursorUtil.getColumnIndexOrThrow(_cursor, "reps");
          final int _cursorIndexOfHoldSeconds = CursorUtil.getColumnIndexOrThrow(_cursor, "holdSeconds");
          final List<ExerciseScheduleItemEntity> _result = new ArrayList<ExerciseScheduleItemEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ExerciseScheduleItemEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpScheduleId;
            _tmpScheduleId = _cursor.getLong(_cursorIndexOfScheduleId);
            final ExerciseType _tmpExerciseType;
            _tmpExerciseType = __ExerciseType_stringToEnum(_cursor.getString(_cursorIndexOfExerciseType));
            final int _tmpSessionsPerDay;
            _tmpSessionsPerDay = _cursor.getInt(_cursorIndexOfSessionsPerDay);
            final int _tmpSets;
            _tmpSets = _cursor.getInt(_cursorIndexOfSets);
            final int _tmpReps;
            _tmpReps = _cursor.getInt(_cursorIndexOfReps);
            final int _tmpHoldSeconds;
            _tmpHoldSeconds = _cursor.getInt(_cursorIndexOfHoldSeconds);
            _item = new ExerciseScheduleItemEntity(_tmpId,_tmpScheduleId,_tmpExerciseType,_tmpSessionsPerDay,_tmpSets,_tmpReps,_tmpHoldSeconds);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<ExerciseScheduleItemEntity>> getItemsForScheduleFlow(final long scheduleId) {
    final String _sql = "SELECT * FROM exercise_schedule_items WHERE scheduleId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, scheduleId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"exercise_schedule_items"}, new Callable<List<ExerciseScheduleItemEntity>>() {
      @Override
      @NonNull
      public List<ExerciseScheduleItemEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfScheduleId = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduleId");
          final int _cursorIndexOfExerciseType = CursorUtil.getColumnIndexOrThrow(_cursor, "exerciseType");
          final int _cursorIndexOfSessionsPerDay = CursorUtil.getColumnIndexOrThrow(_cursor, "sessionsPerDay");
          final int _cursorIndexOfSets = CursorUtil.getColumnIndexOrThrow(_cursor, "sets");
          final int _cursorIndexOfReps = CursorUtil.getColumnIndexOrThrow(_cursor, "reps");
          final int _cursorIndexOfHoldSeconds = CursorUtil.getColumnIndexOrThrow(_cursor, "holdSeconds");
          final List<ExerciseScheduleItemEntity> _result = new ArrayList<ExerciseScheduleItemEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ExerciseScheduleItemEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpScheduleId;
            _tmpScheduleId = _cursor.getLong(_cursorIndexOfScheduleId);
            final ExerciseType _tmpExerciseType;
            _tmpExerciseType = __ExerciseType_stringToEnum(_cursor.getString(_cursorIndexOfExerciseType));
            final int _tmpSessionsPerDay;
            _tmpSessionsPerDay = _cursor.getInt(_cursorIndexOfSessionsPerDay);
            final int _tmpSets;
            _tmpSets = _cursor.getInt(_cursorIndexOfSets);
            final int _tmpReps;
            _tmpReps = _cursor.getInt(_cursorIndexOfReps);
            final int _tmpHoldSeconds;
            _tmpHoldSeconds = _cursor.getInt(_cursorIndexOfHoldSeconds);
            _item = new ExerciseScheduleItemEntity(_tmpId,_tmpScheduleId,_tmpExerciseType,_tmpSessionsPerDay,_tmpSets,_tmpReps,_tmpHoldSeconds);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }

  private String __ExerciseType_enumToString(@NonNull final ExerciseType _value) {
    switch (_value) {
      case STANDARD_KEGEL: return "STANDARD_KEGEL";
      case QUICK_KEGEL: return "QUICK_KEGEL";
      case HOLD_KEGEL: return "HOLD_KEGEL";
      case SIT_STAND_KEGEL: return "SIT_STAND_KEGEL";
      case CHILDS_POSE: return "CHILDS_POSE";
      case HAPPY_BABY: return "HAPPY_BABY";
      case DEEP_SQUAT: return "DEEP_SQUAT";
      default: throw new IllegalArgumentException("Can't convert enum to string, unknown enum value: " + _value);
    }
  }

  private ExerciseType __ExerciseType_stringToEnum(@NonNull final String _value) {
    switch (_value) {
      case "STANDARD_KEGEL": return ExerciseType.STANDARD_KEGEL;
      case "QUICK_KEGEL": return ExerciseType.QUICK_KEGEL;
      case "HOLD_KEGEL": return ExerciseType.HOLD_KEGEL;
      case "SIT_STAND_KEGEL": return ExerciseType.SIT_STAND_KEGEL;
      case "CHILDS_POSE": return ExerciseType.CHILDS_POSE;
      case "HAPPY_BABY": return ExerciseType.HAPPY_BABY;
      case "DEEP_SQUAT": return ExerciseType.DEEP_SQUAT;
      default: throw new IllegalArgumentException("Can't convert value to enum, unknown value: " + _value);
    }
  }
}
