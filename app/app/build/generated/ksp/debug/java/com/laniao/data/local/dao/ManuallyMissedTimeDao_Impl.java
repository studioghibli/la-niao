package com.laniao.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.laniao.data.local.Converters;
import com.laniao.data.local.entity.ManuallyMissedTimeEntity;
import java.lang.Boolean;
import java.lang.Class;
import java.lang.Exception;
import java.lang.IllegalStateException;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.time.Instant;
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
public final class ManuallyMissedTimeDao_Impl implements ManuallyMissedTimeDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ManuallyMissedTimeEntity> __insertionAdapterOfManuallyMissedTimeEntity;

  private final Converters __converters = new Converters();

  private final SharedSQLiteStatement __preparedStmtOfDelete;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public ManuallyMissedTimeDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfManuallyMissedTimeEntity = new EntityInsertionAdapter<ManuallyMissedTimeEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `manually_missed_times` (`id`,`date`,`scheduledTime`,`createdAt`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ManuallyMissedTimeEntity entity) {
        statement.bindLong(1, entity.getId());
        final String _tmp = __converters.fromLocalDate(entity.getDate());
        if (_tmp == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, _tmp);
        }
        final String _tmp_1 = __converters.fromLocalTime(entity.getScheduledTime());
        if (_tmp_1 == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, _tmp_1);
        }
        final Long _tmp_2 = __converters.fromInstant(entity.getCreatedAt());
        if (_tmp_2 == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, _tmp_2);
        }
      }
    };
    this.__preparedStmtOfDelete = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM manually_missed_times WHERE date = ? AND scheduledTime = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM manually_missed_times";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final ManuallyMissedTimeEntity entity,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfManuallyMissedTimeEntity.insertAndReturnId(entity);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAll(final List<ManuallyMissedTimeEntity> entities,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfManuallyMissedTimeEntity.insert(entities);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final LocalDate date, final LocalTime scheduledTime,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDelete.acquire();
        int _argIndex = 1;
        final String _tmp = __converters.fromLocalDate(date);
        if (_tmp == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, _tmp);
        }
        _argIndex = 2;
        final String _tmp_1 = __converters.fromLocalTime(scheduledTime);
        if (_tmp_1 == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, _tmp_1);
        }
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
          __preparedStmtOfDelete.release(_stmt);
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
  public Flow<List<ManuallyMissedTimeEntity>> getByDate(final LocalDate date) {
    final String _sql = "SELECT * FROM manually_missed_times WHERE date = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    final String _tmp = __converters.fromLocalDate(date);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, _tmp);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"manually_missed_times"}, new Callable<List<ManuallyMissedTimeEntity>>() {
      @Override
      @NonNull
      public List<ManuallyMissedTimeEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfScheduledTime = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduledTime");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<ManuallyMissedTimeEntity> _result = new ArrayList<ManuallyMissedTimeEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ManuallyMissedTimeEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final LocalDate _tmpDate;
            final String _tmp_1;
            if (_cursor.isNull(_cursorIndexOfDate)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getString(_cursorIndexOfDate);
            }
            final LocalDate _tmp_2 = __converters.toLocalDate(_tmp_1);
            if (_tmp_2 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDate', but it was NULL.");
            } else {
              _tmpDate = _tmp_2;
            }
            final LocalTime _tmpScheduledTime;
            final String _tmp_3;
            if (_cursor.isNull(_cursorIndexOfScheduledTime)) {
              _tmp_3 = null;
            } else {
              _tmp_3 = _cursor.getString(_cursorIndexOfScheduledTime);
            }
            final LocalTime _tmp_4 = __converters.toLocalTime(_tmp_3);
            if (_tmp_4 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalTime', but it was NULL.");
            } else {
              _tmpScheduledTime = _tmp_4;
            }
            final Instant _tmpCreatedAt;
            final Long _tmp_5;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmp_5 = null;
            } else {
              _tmp_5 = _cursor.getLong(_cursorIndexOfCreatedAt);
            }
            final Instant _tmp_6 = __converters.toInstant(_tmp_5);
            if (_tmp_6 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.Instant', but it was NULL.");
            } else {
              _tmpCreatedAt = _tmp_6;
            }
            _item = new ManuallyMissedTimeEntity(_tmpId,_tmpDate,_tmpScheduledTime,_tmpCreatedAt);
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
  public Object getByDateOnce(final LocalDate date,
      final Continuation<? super List<ManuallyMissedTimeEntity>> $completion) {
    final String _sql = "SELECT * FROM manually_missed_times WHERE date = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    final String _tmp = __converters.fromLocalDate(date);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, _tmp);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ManuallyMissedTimeEntity>>() {
      @Override
      @NonNull
      public List<ManuallyMissedTimeEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfScheduledTime = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduledTime");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<ManuallyMissedTimeEntity> _result = new ArrayList<ManuallyMissedTimeEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ManuallyMissedTimeEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final LocalDate _tmpDate;
            final String _tmp_1;
            if (_cursor.isNull(_cursorIndexOfDate)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getString(_cursorIndexOfDate);
            }
            final LocalDate _tmp_2 = __converters.toLocalDate(_tmp_1);
            if (_tmp_2 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDate', but it was NULL.");
            } else {
              _tmpDate = _tmp_2;
            }
            final LocalTime _tmpScheduledTime;
            final String _tmp_3;
            if (_cursor.isNull(_cursorIndexOfScheduledTime)) {
              _tmp_3 = null;
            } else {
              _tmp_3 = _cursor.getString(_cursorIndexOfScheduledTime);
            }
            final LocalTime _tmp_4 = __converters.toLocalTime(_tmp_3);
            if (_tmp_4 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalTime', but it was NULL.");
            } else {
              _tmpScheduledTime = _tmp_4;
            }
            final Instant _tmpCreatedAt;
            final Long _tmp_5;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmp_5 = null;
            } else {
              _tmp_5 = _cursor.getLong(_cursorIndexOfCreatedAt);
            }
            final Instant _tmp_6 = __converters.toInstant(_tmp_5);
            if (_tmp_6 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.Instant', but it was NULL.");
            } else {
              _tmpCreatedAt = _tmp_6;
            }
            _item = new ManuallyMissedTimeEntity(_tmpId,_tmpDate,_tmpScheduledTime,_tmpCreatedAt);
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
  public Flow<List<ManuallyMissedTimeEntity>> getByDateRange(final LocalDate startDate,
      final LocalDate endDate) {
    final String _sql = "SELECT * FROM manually_missed_times WHERE date BETWEEN ? AND ?";
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
    return CoroutinesRoom.createFlow(__db, false, new String[] {"manually_missed_times"}, new Callable<List<ManuallyMissedTimeEntity>>() {
      @Override
      @NonNull
      public List<ManuallyMissedTimeEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfScheduledTime = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduledTime");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<ManuallyMissedTimeEntity> _result = new ArrayList<ManuallyMissedTimeEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ManuallyMissedTimeEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final LocalDate _tmpDate;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfDate)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfDate);
            }
            final LocalDate _tmp_3 = __converters.toLocalDate(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDate', but it was NULL.");
            } else {
              _tmpDate = _tmp_3;
            }
            final LocalTime _tmpScheduledTime;
            final String _tmp_4;
            if (_cursor.isNull(_cursorIndexOfScheduledTime)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getString(_cursorIndexOfScheduledTime);
            }
            final LocalTime _tmp_5 = __converters.toLocalTime(_tmp_4);
            if (_tmp_5 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalTime', but it was NULL.");
            } else {
              _tmpScheduledTime = _tmp_5;
            }
            final Instant _tmpCreatedAt;
            final Long _tmp_6;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmp_6 = null;
            } else {
              _tmp_6 = _cursor.getLong(_cursorIndexOfCreatedAt);
            }
            final Instant _tmp_7 = __converters.toInstant(_tmp_6);
            if (_tmp_7 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.Instant', but it was NULL.");
            } else {
              _tmpCreatedAt = _tmp_7;
            }
            _item = new ManuallyMissedTimeEntity(_tmpId,_tmpDate,_tmpScheduledTime,_tmpCreatedAt);
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
  public Object exists(final LocalDate date, final LocalTime scheduledTime,
      final Continuation<? super Boolean> $completion) {
    final String _sql = "SELECT EXISTS(SELECT 1 FROM manually_missed_times WHERE date = ? AND scheduledTime = ?)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    final String _tmp = __converters.fromLocalDate(date);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, _tmp);
    }
    _argIndex = 2;
    final String _tmp_1 = __converters.fromLocalTime(scheduledTime);
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
  public Object getAll(final Continuation<? super List<ManuallyMissedTimeEntity>> $completion) {
    final String _sql = "SELECT * FROM manually_missed_times";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ManuallyMissedTimeEntity>>() {
      @Override
      @NonNull
      public List<ManuallyMissedTimeEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfScheduledTime = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduledTime");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<ManuallyMissedTimeEntity> _result = new ArrayList<ManuallyMissedTimeEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ManuallyMissedTimeEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final LocalDate _tmpDate;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfDate)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfDate);
            }
            final LocalDate _tmp_1 = __converters.toLocalDate(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDate', but it was NULL.");
            } else {
              _tmpDate = _tmp_1;
            }
            final LocalTime _tmpScheduledTime;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfScheduledTime)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfScheduledTime);
            }
            final LocalTime _tmp_3 = __converters.toLocalTime(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalTime', but it was NULL.");
            } else {
              _tmpScheduledTime = _tmp_3;
            }
            final Instant _tmpCreatedAt;
            final Long _tmp_4;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getLong(_cursorIndexOfCreatedAt);
            }
            final Instant _tmp_5 = __converters.toInstant(_tmp_4);
            if (_tmp_5 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.Instant', but it was NULL.");
            } else {
              _tmpCreatedAt = _tmp_5;
            }
            _item = new ManuallyMissedTimeEntity(_tmpId,_tmpDate,_tmpScheduledTime,_tmpCreatedAt);
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
