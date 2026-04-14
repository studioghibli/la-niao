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
import com.laniao.data.local.entity.VoidScheduleEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.IllegalStateException;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.time.LocalDate;
import java.time.LocalTime;
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
public final class VoidScheduleDao_Impl implements VoidScheduleDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<VoidScheduleEntity> __insertionAdapterOfVoidScheduleEntity;

  private final Converters __converters = new Converters();

  private final EntityInsertionAdapter<VoidScheduleEntity> __insertionAdapterOfVoidScheduleEntity_1;

  private final EntityDeletionOrUpdateAdapter<VoidScheduleEntity> __deletionAdapterOfVoidScheduleEntity;

  private final EntityDeletionOrUpdateAdapter<VoidScheduleEntity> __updateAdapterOfVoidScheduleEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  private final SharedSQLiteStatement __preparedStmtOfDisableAll;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public VoidScheduleDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfVoidScheduleEntity = new EntityInsertionAdapter<VoidScheduleEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `void_schedules` (`id`,`startTime`,`endTime`,`intervalMinutes`,`enabled`,`createdAt`,`expiresAt`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final VoidScheduleEntity entity) {
        statement.bindLong(1, entity.getId());
        final String _tmp = __converters.fromLocalTime(entity.getStartTime());
        if (_tmp == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, _tmp);
        }
        final String _tmp_1 = __converters.fromLocalTime(entity.getEndTime());
        if (_tmp_1 == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, _tmp_1);
        }
        statement.bindLong(4, entity.getIntervalMinutes());
        final int _tmp_2 = entity.getEnabled() ? 1 : 0;
        statement.bindLong(5, _tmp_2);
        final String _tmp_3 = __converters.fromLocalDate(entity.getCreatedAt());
        if (_tmp_3 == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, _tmp_3);
        }
        final String _tmp_4 = __converters.fromLocalDate(entity.getExpiresAt());
        if (_tmp_4 == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, _tmp_4);
        }
      }
    };
    this.__insertionAdapterOfVoidScheduleEntity_1 = new EntityInsertionAdapter<VoidScheduleEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `void_schedules` (`id`,`startTime`,`endTime`,`intervalMinutes`,`enabled`,`createdAt`,`expiresAt`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final VoidScheduleEntity entity) {
        statement.bindLong(1, entity.getId());
        final String _tmp = __converters.fromLocalTime(entity.getStartTime());
        if (_tmp == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, _tmp);
        }
        final String _tmp_1 = __converters.fromLocalTime(entity.getEndTime());
        if (_tmp_1 == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, _tmp_1);
        }
        statement.bindLong(4, entity.getIntervalMinutes());
        final int _tmp_2 = entity.getEnabled() ? 1 : 0;
        statement.bindLong(5, _tmp_2);
        final String _tmp_3 = __converters.fromLocalDate(entity.getCreatedAt());
        if (_tmp_3 == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, _tmp_3);
        }
        final String _tmp_4 = __converters.fromLocalDate(entity.getExpiresAt());
        if (_tmp_4 == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, _tmp_4);
        }
      }
    };
    this.__deletionAdapterOfVoidScheduleEntity = new EntityDeletionOrUpdateAdapter<VoidScheduleEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `void_schedules` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final VoidScheduleEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfVoidScheduleEntity = new EntityDeletionOrUpdateAdapter<VoidScheduleEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `void_schedules` SET `id` = ?,`startTime` = ?,`endTime` = ?,`intervalMinutes` = ?,`enabled` = ?,`createdAt` = ?,`expiresAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final VoidScheduleEntity entity) {
        statement.bindLong(1, entity.getId());
        final String _tmp = __converters.fromLocalTime(entity.getStartTime());
        if (_tmp == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, _tmp);
        }
        final String _tmp_1 = __converters.fromLocalTime(entity.getEndTime());
        if (_tmp_1 == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, _tmp_1);
        }
        statement.bindLong(4, entity.getIntervalMinutes());
        final int _tmp_2 = entity.getEnabled() ? 1 : 0;
        statement.bindLong(5, _tmp_2);
        final String _tmp_3 = __converters.fromLocalDate(entity.getCreatedAt());
        if (_tmp_3 == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, _tmp_3);
        }
        final String _tmp_4 = __converters.fromLocalDate(entity.getExpiresAt());
        if (_tmp_4 == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, _tmp_4);
        }
        statement.bindLong(8, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM void_schedules WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDisableAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE void_schedules SET enabled = 0 WHERE enabled = 1";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM void_schedules";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final VoidScheduleEntity schedule,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfVoidScheduleEntity.insertAndReturnId(schedule);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAll(final List<VoidScheduleEntity> schedules,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfVoidScheduleEntity_1.insert(schedules);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final VoidScheduleEntity schedule,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfVoidScheduleEntity.handle(schedule);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final VoidScheduleEntity schedule,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfVoidScheduleEntity.handle(schedule);
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
  public Object disableAll(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDisableAll.acquire();
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
          __preparedStmtOfDisableAll.release(_stmt);
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
  public Object getById(final long id, final Continuation<? super VoidScheduleEntity> $completion) {
    final String _sql = "SELECT * FROM void_schedules WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<VoidScheduleEntity>() {
      @Override
      @Nullable
      public VoidScheduleEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
          final int _cursorIndexOfIntervalMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "intervalMinutes");
          final int _cursorIndexOfEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "enabled");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfExpiresAt = CursorUtil.getColumnIndexOrThrow(_cursor, "expiresAt");
          final VoidScheduleEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final LocalTime _tmpStartTime;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfStartTime)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfStartTime);
            }
            final LocalTime _tmp_1 = __converters.toLocalTime(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalTime', but it was NULL.");
            } else {
              _tmpStartTime = _tmp_1;
            }
            final LocalTime _tmpEndTime;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfEndTime)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfEndTime);
            }
            final LocalTime _tmp_3 = __converters.toLocalTime(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalTime', but it was NULL.");
            } else {
              _tmpEndTime = _tmp_3;
            }
            final int _tmpIntervalMinutes;
            _tmpIntervalMinutes = _cursor.getInt(_cursorIndexOfIntervalMinutes);
            final boolean _tmpEnabled;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfEnabled);
            _tmpEnabled = _tmp_4 != 0;
            final LocalDate _tmpCreatedAt;
            final String _tmp_5;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmp_5 = null;
            } else {
              _tmp_5 = _cursor.getString(_cursorIndexOfCreatedAt);
            }
            final LocalDate _tmp_6 = __converters.toLocalDate(_tmp_5);
            if (_tmp_6 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDate', but it was NULL.");
            } else {
              _tmpCreatedAt = _tmp_6;
            }
            final LocalDate _tmpExpiresAt;
            final String _tmp_7;
            if (_cursor.isNull(_cursorIndexOfExpiresAt)) {
              _tmp_7 = null;
            } else {
              _tmp_7 = _cursor.getString(_cursorIndexOfExpiresAt);
            }
            final LocalDate _tmp_8 = __converters.toLocalDate(_tmp_7);
            if (_tmp_8 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDate', but it was NULL.");
            } else {
              _tmpExpiresAt = _tmp_8;
            }
            _result = new VoidScheduleEntity(_tmpId,_tmpStartTime,_tmpEndTime,_tmpIntervalMinutes,_tmpEnabled,_tmpCreatedAt,_tmpExpiresAt);
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
  public Flow<List<VoidScheduleEntity>> getActive(final LocalDate today) {
    final String _sql = "\n"
            + "        SELECT * FROM void_schedules \n"
            + "        WHERE enabled = 1 \n"
            + "        AND createdAt <= ?\n"
            + "        AND expiresAt >= ? \n"
            + "        ORDER BY createdAt DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    final String _tmp = __converters.fromLocalDate(today);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, _tmp);
    }
    _argIndex = 2;
    final String _tmp_1 = __converters.fromLocalDate(today);
    if (_tmp_1 == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, _tmp_1);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"void_schedules"}, new Callable<List<VoidScheduleEntity>>() {
      @Override
      @NonNull
      public List<VoidScheduleEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
          final int _cursorIndexOfIntervalMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "intervalMinutes");
          final int _cursorIndexOfEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "enabled");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfExpiresAt = CursorUtil.getColumnIndexOrThrow(_cursor, "expiresAt");
          final List<VoidScheduleEntity> _result = new ArrayList<VoidScheduleEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final VoidScheduleEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final LocalTime _tmpStartTime;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfStartTime)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfStartTime);
            }
            final LocalTime _tmp_3 = __converters.toLocalTime(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalTime', but it was NULL.");
            } else {
              _tmpStartTime = _tmp_3;
            }
            final LocalTime _tmpEndTime;
            final String _tmp_4;
            if (_cursor.isNull(_cursorIndexOfEndTime)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getString(_cursorIndexOfEndTime);
            }
            final LocalTime _tmp_5 = __converters.toLocalTime(_tmp_4);
            if (_tmp_5 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalTime', but it was NULL.");
            } else {
              _tmpEndTime = _tmp_5;
            }
            final int _tmpIntervalMinutes;
            _tmpIntervalMinutes = _cursor.getInt(_cursorIndexOfIntervalMinutes);
            final boolean _tmpEnabled;
            final int _tmp_6;
            _tmp_6 = _cursor.getInt(_cursorIndexOfEnabled);
            _tmpEnabled = _tmp_6 != 0;
            final LocalDate _tmpCreatedAt;
            final String _tmp_7;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmp_7 = null;
            } else {
              _tmp_7 = _cursor.getString(_cursorIndexOfCreatedAt);
            }
            final LocalDate _tmp_8 = __converters.toLocalDate(_tmp_7);
            if (_tmp_8 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDate', but it was NULL.");
            } else {
              _tmpCreatedAt = _tmp_8;
            }
            final LocalDate _tmpExpiresAt;
            final String _tmp_9;
            if (_cursor.isNull(_cursorIndexOfExpiresAt)) {
              _tmp_9 = null;
            } else {
              _tmp_9 = _cursor.getString(_cursorIndexOfExpiresAt);
            }
            final LocalDate _tmp_10 = __converters.toLocalDate(_tmp_9);
            if (_tmp_10 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDate', but it was NULL.");
            } else {
              _tmpExpiresAt = _tmp_10;
            }
            _item = new VoidScheduleEntity(_tmpId,_tmpStartTime,_tmpEndTime,_tmpIntervalMinutes,_tmpEnabled,_tmpCreatedAt,_tmpExpiresAt);
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
      final Continuation<? super VoidScheduleEntity> $completion) {
    final String _sql = "\n"
            + "        SELECT * FROM void_schedules \n"
            + "        WHERE enabled = 1 \n"
            + "        AND createdAt <= ? \n"
            + "        AND expiresAt >= ? \n"
            + "        ORDER BY createdAt DESC \n"
            + "        LIMIT 1\n"
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
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<VoidScheduleEntity>() {
      @Override
      @Nullable
      public VoidScheduleEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
          final int _cursorIndexOfIntervalMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "intervalMinutes");
          final int _cursorIndexOfEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "enabled");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfExpiresAt = CursorUtil.getColumnIndexOrThrow(_cursor, "expiresAt");
          final VoidScheduleEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final LocalTime _tmpStartTime;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfStartTime)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfStartTime);
            }
            final LocalTime _tmp_3 = __converters.toLocalTime(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalTime', but it was NULL.");
            } else {
              _tmpStartTime = _tmp_3;
            }
            final LocalTime _tmpEndTime;
            final String _tmp_4;
            if (_cursor.isNull(_cursorIndexOfEndTime)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getString(_cursorIndexOfEndTime);
            }
            final LocalTime _tmp_5 = __converters.toLocalTime(_tmp_4);
            if (_tmp_5 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalTime', but it was NULL.");
            } else {
              _tmpEndTime = _tmp_5;
            }
            final int _tmpIntervalMinutes;
            _tmpIntervalMinutes = _cursor.getInt(_cursorIndexOfIntervalMinutes);
            final boolean _tmpEnabled;
            final int _tmp_6;
            _tmp_6 = _cursor.getInt(_cursorIndexOfEnabled);
            _tmpEnabled = _tmp_6 != 0;
            final LocalDate _tmpCreatedAt;
            final String _tmp_7;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmp_7 = null;
            } else {
              _tmp_7 = _cursor.getString(_cursorIndexOfCreatedAt);
            }
            final LocalDate _tmp_8 = __converters.toLocalDate(_tmp_7);
            if (_tmp_8 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDate', but it was NULL.");
            } else {
              _tmpCreatedAt = _tmp_8;
            }
            final LocalDate _tmpExpiresAt;
            final String _tmp_9;
            if (_cursor.isNull(_cursorIndexOfExpiresAt)) {
              _tmp_9 = null;
            } else {
              _tmp_9 = _cursor.getString(_cursorIndexOfExpiresAt);
            }
            final LocalDate _tmp_10 = __converters.toLocalDate(_tmp_9);
            if (_tmp_10 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDate', but it was NULL.");
            } else {
              _tmpExpiresAt = _tmp_10;
            }
            _result = new VoidScheduleEntity(_tmpId,_tmpStartTime,_tmpEndTime,_tmpIntervalMinutes,_tmpEnabled,_tmpCreatedAt,_tmpExpiresAt);
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
  public Flow<List<VoidScheduleEntity>> getAll() {
    final String _sql = "SELECT * FROM void_schedules ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"void_schedules"}, new Callable<List<VoidScheduleEntity>>() {
      @Override
      @NonNull
      public List<VoidScheduleEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
          final int _cursorIndexOfIntervalMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "intervalMinutes");
          final int _cursorIndexOfEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "enabled");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfExpiresAt = CursorUtil.getColumnIndexOrThrow(_cursor, "expiresAt");
          final List<VoidScheduleEntity> _result = new ArrayList<VoidScheduleEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final VoidScheduleEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final LocalTime _tmpStartTime;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfStartTime)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfStartTime);
            }
            final LocalTime _tmp_1 = __converters.toLocalTime(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalTime', but it was NULL.");
            } else {
              _tmpStartTime = _tmp_1;
            }
            final LocalTime _tmpEndTime;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfEndTime)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfEndTime);
            }
            final LocalTime _tmp_3 = __converters.toLocalTime(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalTime', but it was NULL.");
            } else {
              _tmpEndTime = _tmp_3;
            }
            final int _tmpIntervalMinutes;
            _tmpIntervalMinutes = _cursor.getInt(_cursorIndexOfIntervalMinutes);
            final boolean _tmpEnabled;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfEnabled);
            _tmpEnabled = _tmp_4 != 0;
            final LocalDate _tmpCreatedAt;
            final String _tmp_5;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmp_5 = null;
            } else {
              _tmp_5 = _cursor.getString(_cursorIndexOfCreatedAt);
            }
            final LocalDate _tmp_6 = __converters.toLocalDate(_tmp_5);
            if (_tmp_6 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDate', but it was NULL.");
            } else {
              _tmpCreatedAt = _tmp_6;
            }
            final LocalDate _tmpExpiresAt;
            final String _tmp_7;
            if (_cursor.isNull(_cursorIndexOfExpiresAt)) {
              _tmp_7 = null;
            } else {
              _tmp_7 = _cursor.getString(_cursorIndexOfExpiresAt);
            }
            final LocalDate _tmp_8 = __converters.toLocalDate(_tmp_7);
            if (_tmp_8 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDate', but it was NULL.");
            } else {
              _tmpExpiresAt = _tmp_8;
            }
            _item = new VoidScheduleEntity(_tmpId,_tmpStartTime,_tmpEndTime,_tmpIntervalMinutes,_tmpEnabled,_tmpCreatedAt,_tmpExpiresAt);
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
  public Object getOverlapping(final LocalDate startDate, final LocalDate endDate,
      final Continuation<? super List<VoidScheduleEntity>> $completion) {
    final String _sql = "\n"
            + "        SELECT * FROM void_schedules \n"
            + "        WHERE enabled = 1 \n"
            + "        AND createdAt <= ? \n"
            + "        AND expiresAt >= ?\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    final String _tmp = __converters.fromLocalDate(endDate);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, _tmp);
    }
    _argIndex = 2;
    final String _tmp_1 = __converters.fromLocalDate(startDate);
    if (_tmp_1 == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, _tmp_1);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<VoidScheduleEntity>>() {
      @Override
      @NonNull
      public List<VoidScheduleEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
          final int _cursorIndexOfIntervalMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "intervalMinutes");
          final int _cursorIndexOfEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "enabled");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfExpiresAt = CursorUtil.getColumnIndexOrThrow(_cursor, "expiresAt");
          final List<VoidScheduleEntity> _result = new ArrayList<VoidScheduleEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final VoidScheduleEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final LocalTime _tmpStartTime;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfStartTime)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfStartTime);
            }
            final LocalTime _tmp_3 = __converters.toLocalTime(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalTime', but it was NULL.");
            } else {
              _tmpStartTime = _tmp_3;
            }
            final LocalTime _tmpEndTime;
            final String _tmp_4;
            if (_cursor.isNull(_cursorIndexOfEndTime)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getString(_cursorIndexOfEndTime);
            }
            final LocalTime _tmp_5 = __converters.toLocalTime(_tmp_4);
            if (_tmp_5 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalTime', but it was NULL.");
            } else {
              _tmpEndTime = _tmp_5;
            }
            final int _tmpIntervalMinutes;
            _tmpIntervalMinutes = _cursor.getInt(_cursorIndexOfIntervalMinutes);
            final boolean _tmpEnabled;
            final int _tmp_6;
            _tmp_6 = _cursor.getInt(_cursorIndexOfEnabled);
            _tmpEnabled = _tmp_6 != 0;
            final LocalDate _tmpCreatedAt;
            final String _tmp_7;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmp_7 = null;
            } else {
              _tmp_7 = _cursor.getString(_cursorIndexOfCreatedAt);
            }
            final LocalDate _tmp_8 = __converters.toLocalDate(_tmp_7);
            if (_tmp_8 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDate', but it was NULL.");
            } else {
              _tmpCreatedAt = _tmp_8;
            }
            final LocalDate _tmpExpiresAt;
            final String _tmp_9;
            if (_cursor.isNull(_cursorIndexOfExpiresAt)) {
              _tmp_9 = null;
            } else {
              _tmp_9 = _cursor.getString(_cursorIndexOfExpiresAt);
            }
            final LocalDate _tmp_10 = __converters.toLocalDate(_tmp_9);
            if (_tmp_10 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDate', but it was NULL.");
            } else {
              _tmpExpiresAt = _tmp_10;
            }
            _item = new VoidScheduleEntity(_tmpId,_tmpStartTime,_tmpEndTime,_tmpIntervalMinutes,_tmpEnabled,_tmpCreatedAt,_tmpExpiresAt);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
