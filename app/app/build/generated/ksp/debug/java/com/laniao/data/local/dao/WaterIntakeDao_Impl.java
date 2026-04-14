package com.laniao.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.EntityUpsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.laniao.data.local.Converters;
import com.laniao.data.local.entity.WaterIntakeEntity;
import com.laniao.domain.model.WaterAmount;
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
public final class WaterIntakeDao_Impl implements WaterIntakeDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<WaterIntakeEntity> __insertionAdapterOfWaterIntakeEntity;

  private final Converters __converters = new Converters();

  private final EntityInsertionAdapter<WaterIntakeEntity> __insertionAdapterOfWaterIntakeEntity_1;

  private final EntityDeletionOrUpdateAdapter<WaterIntakeEntity> __deletionAdapterOfWaterIntakeEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  private final EntityUpsertionAdapter<WaterIntakeEntity> __upsertionAdapterOfWaterIntakeEntity;

  public WaterIntakeDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfWaterIntakeEntity = new EntityInsertionAdapter<WaterIntakeEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `water_intake` (`id`,`date`,`amount`) VALUES (nullif(?, 0),?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final WaterIntakeEntity entity) {
        statement.bindLong(1, entity.getId());
        final String _tmp = __converters.fromLocalDate(entity.getDate());
        if (_tmp == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, _tmp);
        }
        statement.bindString(3, __WaterAmount_enumToString(entity.getAmount()));
      }
    };
    this.__insertionAdapterOfWaterIntakeEntity_1 = new EntityInsertionAdapter<WaterIntakeEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `water_intake` (`id`,`date`,`amount`) VALUES (nullif(?, 0),?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final WaterIntakeEntity entity) {
        statement.bindLong(1, entity.getId());
        final String _tmp = __converters.fromLocalDate(entity.getDate());
        if (_tmp == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, _tmp);
        }
        statement.bindString(3, __WaterAmount_enumToString(entity.getAmount()));
      }
    };
    this.__deletionAdapterOfWaterIntakeEntity = new EntityDeletionOrUpdateAdapter<WaterIntakeEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `water_intake` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final WaterIntakeEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM water_intake";
        return _query;
      }
    };
    this.__upsertionAdapterOfWaterIntakeEntity = new EntityUpsertionAdapter<WaterIntakeEntity>(new EntityInsertionAdapter<WaterIntakeEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT INTO `water_intake` (`id`,`date`,`amount`) VALUES (nullif(?, 0),?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final WaterIntakeEntity entity) {
        statement.bindLong(1, entity.getId());
        final String _tmp = __converters.fromLocalDate(entity.getDate());
        if (_tmp == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, _tmp);
        }
        statement.bindString(3, __WaterAmount_enumToString(entity.getAmount()));
      }
    }, new EntityDeletionOrUpdateAdapter<WaterIntakeEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE `water_intake` SET `id` = ?,`date` = ?,`amount` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final WaterIntakeEntity entity) {
        statement.bindLong(1, entity.getId());
        final String _tmp = __converters.fromLocalDate(entity.getDate());
        if (_tmp == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, _tmp);
        }
        statement.bindString(3, __WaterAmount_enumToString(entity.getAmount()));
        statement.bindLong(4, entity.getId());
      }
    });
  }

  @Override
  public Object insert(final WaterIntakeEntity waterIntake,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfWaterIntakeEntity.insertAndReturnId(waterIntake);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAll(final List<WaterIntakeEntity> intakes,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfWaterIntakeEntity_1.insert(intakes);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final WaterIntakeEntity waterIntake,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfWaterIntakeEntity.handle(waterIntake);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
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
  public Object upsert(final WaterIntakeEntity waterIntake,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __upsertionAdapterOfWaterIntakeEntity.upsert(waterIntake);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object getById(final long id, final Continuation<? super WaterIntakeEntity> $completion) {
    final String _sql = "SELECT * FROM water_intake WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<WaterIntakeEntity>() {
      @Override
      @Nullable
      public WaterIntakeEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final WaterIntakeEntity _result;
          if (_cursor.moveToFirst()) {
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
            final WaterAmount _tmpAmount;
            _tmpAmount = __WaterAmount_stringToEnum(_cursor.getString(_cursorIndexOfAmount));
            _result = new WaterIntakeEntity(_tmpId,_tmpDate,_tmpAmount);
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
  public Object getByDate(final LocalDate date,
      final Continuation<? super WaterIntakeEntity> $completion) {
    final String _sql = "SELECT * FROM water_intake WHERE date = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    final String _tmp = __converters.fromLocalDate(date);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, _tmp);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<WaterIntakeEntity>() {
      @Override
      @Nullable
      public WaterIntakeEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final WaterIntakeEntity _result;
          if (_cursor.moveToFirst()) {
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
            final WaterAmount _tmpAmount;
            _tmpAmount = __WaterAmount_stringToEnum(_cursor.getString(_cursorIndexOfAmount));
            _result = new WaterIntakeEntity(_tmpId,_tmpDate,_tmpAmount);
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
  public Flow<WaterIntakeEntity> observeByDate(final LocalDate date) {
    final String _sql = "SELECT * FROM water_intake WHERE date = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    final String _tmp = __converters.fromLocalDate(date);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, _tmp);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"water_intake"}, new Callable<WaterIntakeEntity>() {
      @Override
      @Nullable
      public WaterIntakeEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final WaterIntakeEntity _result;
          if (_cursor.moveToFirst()) {
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
            final WaterAmount _tmpAmount;
            _tmpAmount = __WaterAmount_stringToEnum(_cursor.getString(_cursorIndexOfAmount));
            _result = new WaterIntakeEntity(_tmpId,_tmpDate,_tmpAmount);
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
  public Flow<List<WaterIntakeEntity>> getByDateRange(final LocalDate startDate,
      final LocalDate endDate) {
    final String _sql = "\n"
            + "        SELECT * FROM water_intake \n"
            + "        WHERE date >= ? AND date <= ? \n"
            + "        ORDER BY date DESC\n"
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
    return CoroutinesRoom.createFlow(__db, false, new String[] {"water_intake"}, new Callable<List<WaterIntakeEntity>>() {
      @Override
      @NonNull
      public List<WaterIntakeEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final List<WaterIntakeEntity> _result = new ArrayList<WaterIntakeEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final WaterIntakeEntity _item;
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
            final WaterAmount _tmpAmount;
            _tmpAmount = __WaterAmount_stringToEnum(_cursor.getString(_cursorIndexOfAmount));
            _item = new WaterIntakeEntity(_tmpId,_tmpDate,_tmpAmount);
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
  public Flow<List<WaterIntakeEntity>> getAll() {
    final String _sql = "SELECT * FROM water_intake ORDER BY date DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"water_intake"}, new Callable<List<WaterIntakeEntity>>() {
      @Override
      @NonNull
      public List<WaterIntakeEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final List<WaterIntakeEntity> _result = new ArrayList<WaterIntakeEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final WaterIntakeEntity _item;
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
            final WaterAmount _tmpAmount;
            _tmpAmount = __WaterAmount_stringToEnum(_cursor.getString(_cursorIndexOfAmount));
            _item = new WaterIntakeEntity(_tmpId,_tmpDate,_tmpAmount);
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

  private String __WaterAmount_enumToString(@NonNull final WaterAmount _value) {
    switch (_value) {
      case LITTLE: return "LITTLE";
      case NORMAL: return "NORMAL";
      case A_LOT: return "A_LOT";
      default: throw new IllegalArgumentException("Can't convert enum to string, unknown enum value: " + _value);
    }
  }

  private WaterAmount __WaterAmount_stringToEnum(@NonNull final String _value) {
    switch (_value) {
      case "LITTLE": return WaterAmount.LITTLE;
      case "NORMAL": return WaterAmount.NORMAL;
      case "A_LOT": return WaterAmount.A_LOT;
      default: throw new IllegalArgumentException("Can't convert value to enum, unknown value: " + _value);
    }
  }
}
