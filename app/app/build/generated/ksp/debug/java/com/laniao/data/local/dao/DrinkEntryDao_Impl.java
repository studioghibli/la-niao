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
import com.laniao.data.local.entity.DrinkEntryEntity;
import com.laniao.domain.model.DrinkType;
import com.laniao.domain.model.DrinkUnit;
import java.lang.Class;
import java.lang.Exception;
import java.lang.IllegalArgumentException;
import java.lang.IllegalStateException;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.time.Instant;
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
public final class DrinkEntryDao_Impl implements DrinkEntryDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<DrinkEntryEntity> __insertionAdapterOfDrinkEntryEntity;

  private final Converters __converters = new Converters();

  private final EntityInsertionAdapter<DrinkEntryEntity> __insertionAdapterOfDrinkEntryEntity_1;

  private final EntityDeletionOrUpdateAdapter<DrinkEntryEntity> __deletionAdapterOfDrinkEntryEntity;

  private final EntityDeletionOrUpdateAdapter<DrinkEntryEntity> __updateAdapterOfDrinkEntryEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public DrinkEntryDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfDrinkEntryEntity = new EntityInsertionAdapter<DrinkEntryEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `drink_entries` (`id`,`timestamp`,`type`,`amount`,`unit`,`customName`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final DrinkEntryEntity entity) {
        statement.bindLong(1, entity.getId());
        final Long _tmp = __converters.fromInstant(entity.getTimestamp());
        if (_tmp == null) {
          statement.bindNull(2);
        } else {
          statement.bindLong(2, _tmp);
        }
        statement.bindString(3, __DrinkType_enumToString(entity.getType()));
        statement.bindDouble(4, entity.getAmount());
        statement.bindString(5, __DrinkUnit_enumToString(entity.getUnit()));
        if (entity.getCustomName() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getCustomName());
        }
      }
    };
    this.__insertionAdapterOfDrinkEntryEntity_1 = new EntityInsertionAdapter<DrinkEntryEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `drink_entries` (`id`,`timestamp`,`type`,`amount`,`unit`,`customName`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final DrinkEntryEntity entity) {
        statement.bindLong(1, entity.getId());
        final Long _tmp = __converters.fromInstant(entity.getTimestamp());
        if (_tmp == null) {
          statement.bindNull(2);
        } else {
          statement.bindLong(2, _tmp);
        }
        statement.bindString(3, __DrinkType_enumToString(entity.getType()));
        statement.bindDouble(4, entity.getAmount());
        statement.bindString(5, __DrinkUnit_enumToString(entity.getUnit()));
        if (entity.getCustomName() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getCustomName());
        }
      }
    };
    this.__deletionAdapterOfDrinkEntryEntity = new EntityDeletionOrUpdateAdapter<DrinkEntryEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `drink_entries` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final DrinkEntryEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfDrinkEntryEntity = new EntityDeletionOrUpdateAdapter<DrinkEntryEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `drink_entries` SET `id` = ?,`timestamp` = ?,`type` = ?,`amount` = ?,`unit` = ?,`customName` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final DrinkEntryEntity entity) {
        statement.bindLong(1, entity.getId());
        final Long _tmp = __converters.fromInstant(entity.getTimestamp());
        if (_tmp == null) {
          statement.bindNull(2);
        } else {
          statement.bindLong(2, _tmp);
        }
        statement.bindString(3, __DrinkType_enumToString(entity.getType()));
        statement.bindDouble(4, entity.getAmount());
        statement.bindString(5, __DrinkUnit_enumToString(entity.getUnit()));
        if (entity.getCustomName() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getCustomName());
        }
        statement.bindLong(7, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM drink_entries";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final DrinkEntryEntity entry, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfDrinkEntryEntity.insertAndReturnId(entry);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAll(final List<DrinkEntryEntity> entries,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfDrinkEntryEntity_1.insert(entries);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final DrinkEntryEntity entry, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfDrinkEntryEntity.handle(entry);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final DrinkEntryEntity entry, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfDrinkEntryEntity.handle(entry);
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
  public Object getById(final long id, final Continuation<? super DrinkEntryEntity> $completion) {
    final String _sql = "SELECT * FROM drink_entries WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<DrinkEntryEntity>() {
      @Override
      @Nullable
      public DrinkEntryEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "unit");
          final int _cursorIndexOfCustomName = CursorUtil.getColumnIndexOrThrow(_cursor, "customName");
          final DrinkEntryEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final Instant _tmpTimestamp;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfTimestamp)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfTimestamp);
            }
            final Instant _tmp_1 = __converters.toInstant(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.Instant', but it was NULL.");
            } else {
              _tmpTimestamp = _tmp_1;
            }
            final DrinkType _tmpType;
            _tmpType = __DrinkType_stringToEnum(_cursor.getString(_cursorIndexOfType));
            final double _tmpAmount;
            _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
            final DrinkUnit _tmpUnit;
            _tmpUnit = __DrinkUnit_stringToEnum(_cursor.getString(_cursorIndexOfUnit));
            final String _tmpCustomName;
            if (_cursor.isNull(_cursorIndexOfCustomName)) {
              _tmpCustomName = null;
            } else {
              _tmpCustomName = _cursor.getString(_cursorIndexOfCustomName);
            }
            _result = new DrinkEntryEntity(_tmpId,_tmpTimestamp,_tmpType,_tmpAmount,_tmpUnit,_tmpCustomName);
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
  public Flow<List<DrinkEntryEntity>> getByDateRange(final Instant startOfDay,
      final Instant endOfDay) {
    final String _sql = "\n"
            + "        SELECT * FROM drink_entries\n"
            + "        WHERE timestamp >= ? AND timestamp < ?\n"
            + "        ORDER BY timestamp DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    final Long _tmp = __converters.fromInstant(startOfDay);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, _tmp);
    }
    _argIndex = 2;
    final Long _tmp_1 = __converters.fromInstant(endOfDay);
    if (_tmp_1 == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, _tmp_1);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"drink_entries"}, new Callable<List<DrinkEntryEntity>>() {
      @Override
      @NonNull
      public List<DrinkEntryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "unit");
          final int _cursorIndexOfCustomName = CursorUtil.getColumnIndexOrThrow(_cursor, "customName");
          final List<DrinkEntryEntity> _result = new ArrayList<DrinkEntryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final DrinkEntryEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final Instant _tmpTimestamp;
            final Long _tmp_2;
            if (_cursor.isNull(_cursorIndexOfTimestamp)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getLong(_cursorIndexOfTimestamp);
            }
            final Instant _tmp_3 = __converters.toInstant(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.Instant', but it was NULL.");
            } else {
              _tmpTimestamp = _tmp_3;
            }
            final DrinkType _tmpType;
            _tmpType = __DrinkType_stringToEnum(_cursor.getString(_cursorIndexOfType));
            final double _tmpAmount;
            _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
            final DrinkUnit _tmpUnit;
            _tmpUnit = __DrinkUnit_stringToEnum(_cursor.getString(_cursorIndexOfUnit));
            final String _tmpCustomName;
            if (_cursor.isNull(_cursorIndexOfCustomName)) {
              _tmpCustomName = null;
            } else {
              _tmpCustomName = _cursor.getString(_cursorIndexOfCustomName);
            }
            _item = new DrinkEntryEntity(_tmpId,_tmpTimestamp,_tmpType,_tmpAmount,_tmpUnit,_tmpCustomName);
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
  public Flow<List<DrinkEntryEntity>> getAll() {
    final String _sql = "SELECT * FROM drink_entries ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"drink_entries"}, new Callable<List<DrinkEntryEntity>>() {
      @Override
      @NonNull
      public List<DrinkEntryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "unit");
          final int _cursorIndexOfCustomName = CursorUtil.getColumnIndexOrThrow(_cursor, "customName");
          final List<DrinkEntryEntity> _result = new ArrayList<DrinkEntryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final DrinkEntryEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final Instant _tmpTimestamp;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfTimestamp)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfTimestamp);
            }
            final Instant _tmp_1 = __converters.toInstant(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.Instant', but it was NULL.");
            } else {
              _tmpTimestamp = _tmp_1;
            }
            final DrinkType _tmpType;
            _tmpType = __DrinkType_stringToEnum(_cursor.getString(_cursorIndexOfType));
            final double _tmpAmount;
            _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
            final DrinkUnit _tmpUnit;
            _tmpUnit = __DrinkUnit_stringToEnum(_cursor.getString(_cursorIndexOfUnit));
            final String _tmpCustomName;
            if (_cursor.isNull(_cursorIndexOfCustomName)) {
              _tmpCustomName = null;
            } else {
              _tmpCustomName = _cursor.getString(_cursorIndexOfCustomName);
            }
            _item = new DrinkEntryEntity(_tmpId,_tmpTimestamp,_tmpType,_tmpAmount,_tmpUnit,_tmpCustomName);
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

  private String __DrinkType_enumToString(@NonNull final DrinkType _value) {
    switch (_value) {
      case WATER: return "WATER";
      case SPARKLING_WATER: return "SPARKLING_WATER";
      case TEA: return "TEA";
      case COFFEE: return "COFFEE";
      case MILK: return "MILK";
      case JUICE: return "JUICE";
      case CUSTOM: return "CUSTOM";
      default: throw new IllegalArgumentException("Can't convert enum to string, unknown enum value: " + _value);
    }
  }

  private String __DrinkUnit_enumToString(@NonNull final DrinkUnit _value) {
    switch (_value) {
      case OZ: return "OZ";
      case ML: return "ML";
      case CUPS: return "CUPS";
      default: throw new IllegalArgumentException("Can't convert enum to string, unknown enum value: " + _value);
    }
  }

  private DrinkType __DrinkType_stringToEnum(@NonNull final String _value) {
    switch (_value) {
      case "WATER": return DrinkType.WATER;
      case "SPARKLING_WATER": return DrinkType.SPARKLING_WATER;
      case "TEA": return DrinkType.TEA;
      case "COFFEE": return DrinkType.COFFEE;
      case "MILK": return DrinkType.MILK;
      case "JUICE": return DrinkType.JUICE;
      case "CUSTOM": return DrinkType.CUSTOM;
      default: throw new IllegalArgumentException("Can't convert value to enum, unknown value: " + _value);
    }
  }

  private DrinkUnit __DrinkUnit_stringToEnum(@NonNull final String _value) {
    switch (_value) {
      case "OZ": return DrinkUnit.OZ;
      case "ML": return DrinkUnit.ML;
      case "CUPS": return DrinkUnit.CUPS;
      default: throw new IllegalArgumentException("Can't convert value to enum, unknown value: " + _value);
    }
  }
}
