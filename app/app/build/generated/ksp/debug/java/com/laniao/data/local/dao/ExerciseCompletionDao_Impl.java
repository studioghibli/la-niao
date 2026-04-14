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
import com.laniao.data.local.entity.ExerciseCompletionEntity;
import com.laniao.domain.model.ExerciseType;
import java.lang.Boolean;
import java.lang.Class;
import java.lang.Exception;
import java.lang.IllegalArgumentException;
import java.lang.IllegalStateException;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.time.Instant;
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
public final class ExerciseCompletionDao_Impl implements ExerciseCompletionDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ExerciseCompletionEntity> __insertionAdapterOfExerciseCompletionEntity;

  private final Converters __converters = new Converters();

  private final EntityInsertionAdapter<ExerciseCompletionEntity> __insertionAdapterOfExerciseCompletionEntity_1;

  private final EntityDeletionOrUpdateAdapter<ExerciseCompletionEntity> __deletionAdapterOfExerciseCompletionEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public ExerciseCompletionDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfExerciseCompletionEntity = new EntityInsertionAdapter<ExerciseCompletionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `exercise_completions` (`id`,`scheduleItemId`,`exerciseType`,`completedAt`,`scheduledDate`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ExerciseCompletionEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getScheduleItemId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindLong(2, entity.getScheduleItemId());
        }
        statement.bindString(3, __ExerciseType_enumToString(entity.getExerciseType()));
        final Long _tmp = __converters.fromInstant(entity.getCompletedAt());
        if (_tmp == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, _tmp);
        }
        final String _tmp_1 = __converters.fromLocalDate(entity.getScheduledDate());
        if (_tmp_1 == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, _tmp_1);
        }
      }
    };
    this.__insertionAdapterOfExerciseCompletionEntity_1 = new EntityInsertionAdapter<ExerciseCompletionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `exercise_completions` (`id`,`scheduleItemId`,`exerciseType`,`completedAt`,`scheduledDate`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ExerciseCompletionEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getScheduleItemId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindLong(2, entity.getScheduleItemId());
        }
        statement.bindString(3, __ExerciseType_enumToString(entity.getExerciseType()));
        final Long _tmp = __converters.fromInstant(entity.getCompletedAt());
        if (_tmp == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, _tmp);
        }
        final String _tmp_1 = __converters.fromLocalDate(entity.getScheduledDate());
        if (_tmp_1 == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, _tmp_1);
        }
      }
    };
    this.__deletionAdapterOfExerciseCompletionEntity = new EntityDeletionOrUpdateAdapter<ExerciseCompletionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `exercise_completions` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ExerciseCompletionEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM exercise_completions WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM exercise_completions";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final ExerciseCompletionEntity completion,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfExerciseCompletionEntity.insertAndReturnId(completion);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAll(final List<ExerciseCompletionEntity> completions,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfExerciseCompletionEntity_1.insert(completions);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final ExerciseCompletionEntity completion,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfExerciseCompletionEntity.handle(completion);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteById(final long id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteById.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
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
          __preparedStmtOfDeleteById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAll(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
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
          __preparedStmtOfDeleteAll.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getById(final long id,
      final Continuation<? super ExerciseCompletionEntity> $completion) {
    final String _sql = "SELECT * FROM exercise_completions WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ExerciseCompletionEntity>() {
      @Override
      @Nullable
      public ExerciseCompletionEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfScheduleItemId = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduleItemId");
          final int _cursorIndexOfExerciseType = CursorUtil.getColumnIndexOrThrow(_cursor, "exerciseType");
          final int _cursorIndexOfCompletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "completedAt");
          final int _cursorIndexOfScheduledDate = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduledDate");
          final ExerciseCompletionEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final Long _tmpScheduleItemId;
            if (_cursor.isNull(_cursorIndexOfScheduleItemId)) {
              _tmpScheduleItemId = null;
            } else {
              _tmpScheduleItemId = _cursor.getLong(_cursorIndexOfScheduleItemId);
            }
            final ExerciseType _tmpExerciseType;
            _tmpExerciseType = __ExerciseType_stringToEnum(_cursor.getString(_cursorIndexOfExerciseType));
            final Instant _tmpCompletedAt;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfCompletedAt)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfCompletedAt);
            }
            final Instant _tmp_1 = __converters.toInstant(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.Instant', but it was NULL.");
            } else {
              _tmpCompletedAt = _tmp_1;
            }
            final LocalDate _tmpScheduledDate;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfScheduledDate)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfScheduledDate);
            }
            final LocalDate _tmp_3 = __converters.toLocalDate(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDate', but it was NULL.");
            } else {
              _tmpScheduledDate = _tmp_3;
            }
            _result = new ExerciseCompletionEntity(_tmpId,_tmpScheduleItemId,_tmpExerciseType,_tmpCompletedAt,_tmpScheduledDate);
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
  public Flow<List<ExerciseCompletionEntity>> getByDate(final LocalDate date) {
    final String _sql = "SELECT * FROM exercise_completions WHERE scheduledDate = ? ORDER BY completedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    final String _tmp = __converters.fromLocalDate(date);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, _tmp);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"exercise_completions"}, new Callable<List<ExerciseCompletionEntity>>() {
      @Override
      @NonNull
      public List<ExerciseCompletionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfScheduleItemId = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduleItemId");
          final int _cursorIndexOfExerciseType = CursorUtil.getColumnIndexOrThrow(_cursor, "exerciseType");
          final int _cursorIndexOfCompletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "completedAt");
          final int _cursorIndexOfScheduledDate = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduledDate");
          final List<ExerciseCompletionEntity> _result = new ArrayList<ExerciseCompletionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ExerciseCompletionEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final Long _tmpScheduleItemId;
            if (_cursor.isNull(_cursorIndexOfScheduleItemId)) {
              _tmpScheduleItemId = null;
            } else {
              _tmpScheduleItemId = _cursor.getLong(_cursorIndexOfScheduleItemId);
            }
            final ExerciseType _tmpExerciseType;
            _tmpExerciseType = __ExerciseType_stringToEnum(_cursor.getString(_cursorIndexOfExerciseType));
            final Instant _tmpCompletedAt;
            final Long _tmp_1;
            if (_cursor.isNull(_cursorIndexOfCompletedAt)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getLong(_cursorIndexOfCompletedAt);
            }
            final Instant _tmp_2 = __converters.toInstant(_tmp_1);
            if (_tmp_2 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.Instant', but it was NULL.");
            } else {
              _tmpCompletedAt = _tmp_2;
            }
            final LocalDate _tmpScheduledDate;
            final String _tmp_3;
            if (_cursor.isNull(_cursorIndexOfScheduledDate)) {
              _tmp_3 = null;
            } else {
              _tmp_3 = _cursor.getString(_cursorIndexOfScheduledDate);
            }
            final LocalDate _tmp_4 = __converters.toLocalDate(_tmp_3);
            if (_tmp_4 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDate', but it was NULL.");
            } else {
              _tmpScheduledDate = _tmp_4;
            }
            _item = new ExerciseCompletionEntity(_tmpId,_tmpScheduleItemId,_tmpExerciseType,_tmpCompletedAt,_tmpScheduledDate);
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
  public Object getByDateList(final LocalDate date,
      final Continuation<? super List<ExerciseCompletionEntity>> $completion) {
    final String _sql = "SELECT * FROM exercise_completions WHERE scheduledDate = ? ORDER BY completedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    final String _tmp = __converters.fromLocalDate(date);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, _tmp);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ExerciseCompletionEntity>>() {
      @Override
      @NonNull
      public List<ExerciseCompletionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfScheduleItemId = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduleItemId");
          final int _cursorIndexOfExerciseType = CursorUtil.getColumnIndexOrThrow(_cursor, "exerciseType");
          final int _cursorIndexOfCompletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "completedAt");
          final int _cursorIndexOfScheduledDate = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduledDate");
          final List<ExerciseCompletionEntity> _result = new ArrayList<ExerciseCompletionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ExerciseCompletionEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final Long _tmpScheduleItemId;
            if (_cursor.isNull(_cursorIndexOfScheduleItemId)) {
              _tmpScheduleItemId = null;
            } else {
              _tmpScheduleItemId = _cursor.getLong(_cursorIndexOfScheduleItemId);
            }
            final ExerciseType _tmpExerciseType;
            _tmpExerciseType = __ExerciseType_stringToEnum(_cursor.getString(_cursorIndexOfExerciseType));
            final Instant _tmpCompletedAt;
            final Long _tmp_1;
            if (_cursor.isNull(_cursorIndexOfCompletedAt)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getLong(_cursorIndexOfCompletedAt);
            }
            final Instant _tmp_2 = __converters.toInstant(_tmp_1);
            if (_tmp_2 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.Instant', but it was NULL.");
            } else {
              _tmpCompletedAt = _tmp_2;
            }
            final LocalDate _tmpScheduledDate;
            final String _tmp_3;
            if (_cursor.isNull(_cursorIndexOfScheduledDate)) {
              _tmp_3 = null;
            } else {
              _tmp_3 = _cursor.getString(_cursorIndexOfScheduledDate);
            }
            final LocalDate _tmp_4 = __converters.toLocalDate(_tmp_3);
            if (_tmp_4 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDate', but it was NULL.");
            } else {
              _tmpScheduledDate = _tmp_4;
            }
            _item = new ExerciseCompletionEntity(_tmpId,_tmpScheduleItemId,_tmpExerciseType,_tmpCompletedAt,_tmpScheduledDate);
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
  public Flow<List<ExerciseCompletionEntity>> getByDateRange(final LocalDate startDate,
      final LocalDate endDate) {
    final String _sql = "\n"
            + "        SELECT * FROM exercise_completions \n"
            + "        WHERE scheduledDate >= ? AND scheduledDate <= ? \n"
            + "        ORDER BY completedAt DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    final String _tmp = __converters.fromLocalDate(startDate);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, _tmp);
    }
    _argIndex = 2;
    final String _tmp_1 = __converters.fromLocalDate(endDate);
    if (_tmp_1 == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, _tmp_1);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"exercise_completions"}, new Callable<List<ExerciseCompletionEntity>>() {
      @Override
      @NonNull
      public List<ExerciseCompletionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfScheduleItemId = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduleItemId");
          final int _cursorIndexOfExerciseType = CursorUtil.getColumnIndexOrThrow(_cursor, "exerciseType");
          final int _cursorIndexOfCompletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "completedAt");
          final int _cursorIndexOfScheduledDate = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduledDate");
          final List<ExerciseCompletionEntity> _result = new ArrayList<ExerciseCompletionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ExerciseCompletionEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final Long _tmpScheduleItemId;
            if (_cursor.isNull(_cursorIndexOfScheduleItemId)) {
              _tmpScheduleItemId = null;
            } else {
              _tmpScheduleItemId = _cursor.getLong(_cursorIndexOfScheduleItemId);
            }
            final ExerciseType _tmpExerciseType;
            _tmpExerciseType = __ExerciseType_stringToEnum(_cursor.getString(_cursorIndexOfExerciseType));
            final Instant _tmpCompletedAt;
            final Long _tmp_2;
            if (_cursor.isNull(_cursorIndexOfCompletedAt)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getLong(_cursorIndexOfCompletedAt);
            }
            final Instant _tmp_3 = __converters.toInstant(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.Instant', but it was NULL.");
            } else {
              _tmpCompletedAt = _tmp_3;
            }
            final LocalDate _tmpScheduledDate;
            final String _tmp_4;
            if (_cursor.isNull(_cursorIndexOfScheduledDate)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getString(_cursorIndexOfScheduledDate);
            }
            final LocalDate _tmp_5 = __converters.toLocalDate(_tmp_4);
            if (_tmp_5 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDate', but it was NULL.");
            } else {
              _tmpScheduledDate = _tmp_5;
            }
            _item = new ExerciseCompletionEntity(_tmpId,_tmpScheduleItemId,_tmpExerciseType,_tmpCompletedAt,_tmpScheduledDate);
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
  public Object getCountByTypeAndDate(final ExerciseType exerciseType, final LocalDate date,
      final Continuation<? super Integer> $completion) {
    final String _sql = "\n"
            + "        SELECT COUNT(*) FROM exercise_completions \n"
            + "        WHERE exerciseType = ? AND scheduledDate = ?\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, __ExerciseType_enumToString(exerciseType));
    _argIndex = 2;
    final String _tmp = __converters.fromLocalDate(date);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, _tmp);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(0);
            _result = _tmp_1;
          } else {
            _result = 0;
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
  public Object hasCompletionsForDate(final LocalDate date,
      final Continuation<? super Boolean> $completion) {
    final String _sql = "SELECT EXISTS(SELECT 1 FROM exercise_completions WHERE scheduledDate = ?)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    final String _tmp = __converters.fromLocalDate(date);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, _tmp);
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
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(0);
            _result = _tmp_1 != 0;
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
  public Flow<List<ExerciseCompletionEntity>> getAll() {
    final String _sql = "SELECT * FROM exercise_completions ORDER BY completedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"exercise_completions"}, new Callable<List<ExerciseCompletionEntity>>() {
      @Override
      @NonNull
      public List<ExerciseCompletionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfScheduleItemId = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduleItemId");
          final int _cursorIndexOfExerciseType = CursorUtil.getColumnIndexOrThrow(_cursor, "exerciseType");
          final int _cursorIndexOfCompletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "completedAt");
          final int _cursorIndexOfScheduledDate = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduledDate");
          final List<ExerciseCompletionEntity> _result = new ArrayList<ExerciseCompletionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ExerciseCompletionEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final Long _tmpScheduleItemId;
            if (_cursor.isNull(_cursorIndexOfScheduleItemId)) {
              _tmpScheduleItemId = null;
            } else {
              _tmpScheduleItemId = _cursor.getLong(_cursorIndexOfScheduleItemId);
            }
            final ExerciseType _tmpExerciseType;
            _tmpExerciseType = __ExerciseType_stringToEnum(_cursor.getString(_cursorIndexOfExerciseType));
            final Instant _tmpCompletedAt;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfCompletedAt)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfCompletedAt);
            }
            final Instant _tmp_1 = __converters.toInstant(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.Instant', but it was NULL.");
            } else {
              _tmpCompletedAt = _tmp_1;
            }
            final LocalDate _tmpScheduledDate;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfScheduledDate)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfScheduledDate);
            }
            final LocalDate _tmp_3 = __converters.toLocalDate(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDate', but it was NULL.");
            } else {
              _tmpScheduledDate = _tmp_3;
            }
            _item = new ExerciseCompletionEntity(_tmpId,_tmpScheduleItemId,_tmpExerciseType,_tmpCompletedAt,_tmpScheduledDate);
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
