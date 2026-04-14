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
import com.laniao.data.local.entity.PeeEntryEntity;
import com.laniao.domain.model.LeakAmount;
import com.laniao.domain.model.PeeColor;
import com.laniao.domain.model.Urgency;
import com.laniao.domain.model.VolumeSize;
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
public final class PeeEntryDao_Impl implements PeeEntryDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<PeeEntryEntity> __insertionAdapterOfPeeEntryEntity;

  private final Converters __converters = new Converters();

  private final EntityInsertionAdapter<PeeEntryEntity> __insertionAdapterOfPeeEntryEntity_1;

  private final EntityDeletionOrUpdateAdapter<PeeEntryEntity> __deletionAdapterOfPeeEntryEntity;

  private final EntityDeletionOrUpdateAdapter<PeeEntryEntity> __updateAdapterOfPeeEntryEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  private final SharedSQLiteStatement __preparedStmtOfClearScheduledTimesInRange;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public PeeEntryDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPeeEntryEntity = new EntityInsertionAdapter<PeeEntryEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `pee_entries` (`id`,`timestamp`,`didVoid`,`leakAmount`,`volumeSize`,`color`,`urgency`,`activityContext`,`notes`,`scheduledTime`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PeeEntryEntity entity) {
        statement.bindLong(1, entity.getId());
        final Long _tmp = __converters.fromInstant(entity.getTimestamp());
        if (_tmp == null) {
          statement.bindNull(2);
        } else {
          statement.bindLong(2, _tmp);
        }
        final int _tmp_1 = entity.getDidVoid() ? 1 : 0;
        statement.bindLong(3, _tmp_1);
        statement.bindString(4, __LeakAmount_enumToString(entity.getLeakAmount()));
        statement.bindString(5, __VolumeSize_enumToString(entity.getVolumeSize()));
        statement.bindString(6, __PeeColor_enumToString(entity.getColor()));
        statement.bindString(7, __Urgency_enumToString(entity.getUrgency()));
        if (entity.getActivityContext() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getActivityContext());
        }
        if (entity.getNotes() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getNotes());
        }
        final String _tmp_2 = __converters.fromLocalTime(entity.getScheduledTime());
        if (_tmp_2 == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, _tmp_2);
        }
      }
    };
    this.__insertionAdapterOfPeeEntryEntity_1 = new EntityInsertionAdapter<PeeEntryEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `pee_entries` (`id`,`timestamp`,`didVoid`,`leakAmount`,`volumeSize`,`color`,`urgency`,`activityContext`,`notes`,`scheduledTime`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PeeEntryEntity entity) {
        statement.bindLong(1, entity.getId());
        final Long _tmp = __converters.fromInstant(entity.getTimestamp());
        if (_tmp == null) {
          statement.bindNull(2);
        } else {
          statement.bindLong(2, _tmp);
        }
        final int _tmp_1 = entity.getDidVoid() ? 1 : 0;
        statement.bindLong(3, _tmp_1);
        statement.bindString(4, __LeakAmount_enumToString(entity.getLeakAmount()));
        statement.bindString(5, __VolumeSize_enumToString(entity.getVolumeSize()));
        statement.bindString(6, __PeeColor_enumToString(entity.getColor()));
        statement.bindString(7, __Urgency_enumToString(entity.getUrgency()));
        if (entity.getActivityContext() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getActivityContext());
        }
        if (entity.getNotes() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getNotes());
        }
        final String _tmp_2 = __converters.fromLocalTime(entity.getScheduledTime());
        if (_tmp_2 == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, _tmp_2);
        }
      }
    };
    this.__deletionAdapterOfPeeEntryEntity = new EntityDeletionOrUpdateAdapter<PeeEntryEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `pee_entries` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PeeEntryEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfPeeEntryEntity = new EntityDeletionOrUpdateAdapter<PeeEntryEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `pee_entries` SET `id` = ?,`timestamp` = ?,`didVoid` = ?,`leakAmount` = ?,`volumeSize` = ?,`color` = ?,`urgency` = ?,`activityContext` = ?,`notes` = ?,`scheduledTime` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PeeEntryEntity entity) {
        statement.bindLong(1, entity.getId());
        final Long _tmp = __converters.fromInstant(entity.getTimestamp());
        if (_tmp == null) {
          statement.bindNull(2);
        } else {
          statement.bindLong(2, _tmp);
        }
        final int _tmp_1 = entity.getDidVoid() ? 1 : 0;
        statement.bindLong(3, _tmp_1);
        statement.bindString(4, __LeakAmount_enumToString(entity.getLeakAmount()));
        statement.bindString(5, __VolumeSize_enumToString(entity.getVolumeSize()));
        statement.bindString(6, __PeeColor_enumToString(entity.getColor()));
        statement.bindString(7, __Urgency_enumToString(entity.getUrgency()));
        if (entity.getActivityContext() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getActivityContext());
        }
        if (entity.getNotes() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getNotes());
        }
        final String _tmp_2 = __converters.fromLocalTime(entity.getScheduledTime());
        if (_tmp_2 == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, _tmp_2);
        }
        statement.bindLong(11, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM pee_entries WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfClearScheduledTimesInRange = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "\n"
                + "        UPDATE pee_entries \n"
                + "        SET scheduledTime = NULL \n"
                + "        WHERE timestamp >= ? AND timestamp < ? \n"
                + "        AND scheduledTime IS NOT NULL\n"
                + "    ";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM pee_entries";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final PeeEntryEntity entry, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfPeeEntryEntity.insertAndReturnId(entry);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAll(final List<PeeEntryEntity> entries,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfPeeEntryEntity_1.insert(entries);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final PeeEntryEntity entry, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfPeeEntryEntity.handle(entry);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final PeeEntryEntity entry, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfPeeEntryEntity.handle(entry);
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
  public Object clearScheduledTimesInRange(final Instant rangeStart, final Instant rangeEnd,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearScheduledTimesInRange.acquire();
        int _argIndex = 1;
        final Long _tmp = __converters.fromInstant(rangeStart);
        if (_tmp == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindLong(_argIndex, _tmp);
        }
        _argIndex = 2;
        final Long _tmp_1 = __converters.fromInstant(rangeEnd);
        if (_tmp_1 == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindLong(_argIndex, _tmp_1);
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
          __preparedStmtOfClearScheduledTimesInRange.release(_stmt);
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
  public Object getById(final long id, final Continuation<? super PeeEntryEntity> $completion) {
    final String _sql = "SELECT * FROM pee_entries WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<PeeEntryEntity>() {
      @Override
      @Nullable
      public PeeEntryEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfDidVoid = CursorUtil.getColumnIndexOrThrow(_cursor, "didVoid");
          final int _cursorIndexOfLeakAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "leakAmount");
          final int _cursorIndexOfVolumeSize = CursorUtil.getColumnIndexOrThrow(_cursor, "volumeSize");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfUrgency = CursorUtil.getColumnIndexOrThrow(_cursor, "urgency");
          final int _cursorIndexOfActivityContext = CursorUtil.getColumnIndexOrThrow(_cursor, "activityContext");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfScheduledTime = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduledTime");
          final PeeEntryEntity _result;
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
            final boolean _tmpDidVoid;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfDidVoid);
            _tmpDidVoid = _tmp_2 != 0;
            final LeakAmount _tmpLeakAmount;
            _tmpLeakAmount = __LeakAmount_stringToEnum(_cursor.getString(_cursorIndexOfLeakAmount));
            final VolumeSize _tmpVolumeSize;
            _tmpVolumeSize = __VolumeSize_stringToEnum(_cursor.getString(_cursorIndexOfVolumeSize));
            final PeeColor _tmpColor;
            _tmpColor = __PeeColor_stringToEnum(_cursor.getString(_cursorIndexOfColor));
            final Urgency _tmpUrgency;
            _tmpUrgency = __Urgency_stringToEnum(_cursor.getString(_cursorIndexOfUrgency));
            final String _tmpActivityContext;
            if (_cursor.isNull(_cursorIndexOfActivityContext)) {
              _tmpActivityContext = null;
            } else {
              _tmpActivityContext = _cursor.getString(_cursorIndexOfActivityContext);
            }
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final LocalTime _tmpScheduledTime;
            final String _tmp_3;
            if (_cursor.isNull(_cursorIndexOfScheduledTime)) {
              _tmp_3 = null;
            } else {
              _tmp_3 = _cursor.getString(_cursorIndexOfScheduledTime);
            }
            _tmpScheduledTime = __converters.toLocalTime(_tmp_3);
            _result = new PeeEntryEntity(_tmpId,_tmpTimestamp,_tmpDidVoid,_tmpLeakAmount,_tmpVolumeSize,_tmpColor,_tmpUrgency,_tmpActivityContext,_tmpNotes,_tmpScheduledTime);
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
  public Flow<List<PeeEntryEntity>> getByDateRange(final Instant startOfDay,
      final Instant endOfDay) {
    final String _sql = "\n"
            + "        SELECT * FROM pee_entries \n"
            + "        WHERE timestamp >= ? AND timestamp < ? \n"
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
    return CoroutinesRoom.createFlow(__db, false, new String[] {"pee_entries"}, new Callable<List<PeeEntryEntity>>() {
      @Override
      @NonNull
      public List<PeeEntryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfDidVoid = CursorUtil.getColumnIndexOrThrow(_cursor, "didVoid");
          final int _cursorIndexOfLeakAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "leakAmount");
          final int _cursorIndexOfVolumeSize = CursorUtil.getColumnIndexOrThrow(_cursor, "volumeSize");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfUrgency = CursorUtil.getColumnIndexOrThrow(_cursor, "urgency");
          final int _cursorIndexOfActivityContext = CursorUtil.getColumnIndexOrThrow(_cursor, "activityContext");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfScheduledTime = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduledTime");
          final List<PeeEntryEntity> _result = new ArrayList<PeeEntryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PeeEntryEntity _item;
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
            final boolean _tmpDidVoid;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfDidVoid);
            _tmpDidVoid = _tmp_4 != 0;
            final LeakAmount _tmpLeakAmount;
            _tmpLeakAmount = __LeakAmount_stringToEnum(_cursor.getString(_cursorIndexOfLeakAmount));
            final VolumeSize _tmpVolumeSize;
            _tmpVolumeSize = __VolumeSize_stringToEnum(_cursor.getString(_cursorIndexOfVolumeSize));
            final PeeColor _tmpColor;
            _tmpColor = __PeeColor_stringToEnum(_cursor.getString(_cursorIndexOfColor));
            final Urgency _tmpUrgency;
            _tmpUrgency = __Urgency_stringToEnum(_cursor.getString(_cursorIndexOfUrgency));
            final String _tmpActivityContext;
            if (_cursor.isNull(_cursorIndexOfActivityContext)) {
              _tmpActivityContext = null;
            } else {
              _tmpActivityContext = _cursor.getString(_cursorIndexOfActivityContext);
            }
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final LocalTime _tmpScheduledTime;
            final String _tmp_5;
            if (_cursor.isNull(_cursorIndexOfScheduledTime)) {
              _tmp_5 = null;
            } else {
              _tmp_5 = _cursor.getString(_cursorIndexOfScheduledTime);
            }
            _tmpScheduledTime = __converters.toLocalTime(_tmp_5);
            _item = new PeeEntryEntity(_tmpId,_tmpTimestamp,_tmpDidVoid,_tmpLeakAmount,_tmpVolumeSize,_tmpColor,_tmpUrgency,_tmpActivityContext,_tmpNotes,_tmpScheduledTime);
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
  public Flow<List<PeeEntryEntity>> getAll() {
    final String _sql = "SELECT * FROM pee_entries ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"pee_entries"}, new Callable<List<PeeEntryEntity>>() {
      @Override
      @NonNull
      public List<PeeEntryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfDidVoid = CursorUtil.getColumnIndexOrThrow(_cursor, "didVoid");
          final int _cursorIndexOfLeakAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "leakAmount");
          final int _cursorIndexOfVolumeSize = CursorUtil.getColumnIndexOrThrow(_cursor, "volumeSize");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfUrgency = CursorUtil.getColumnIndexOrThrow(_cursor, "urgency");
          final int _cursorIndexOfActivityContext = CursorUtil.getColumnIndexOrThrow(_cursor, "activityContext");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfScheduledTime = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduledTime");
          final List<PeeEntryEntity> _result = new ArrayList<PeeEntryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PeeEntryEntity _item;
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
            final boolean _tmpDidVoid;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfDidVoid);
            _tmpDidVoid = _tmp_2 != 0;
            final LeakAmount _tmpLeakAmount;
            _tmpLeakAmount = __LeakAmount_stringToEnum(_cursor.getString(_cursorIndexOfLeakAmount));
            final VolumeSize _tmpVolumeSize;
            _tmpVolumeSize = __VolumeSize_stringToEnum(_cursor.getString(_cursorIndexOfVolumeSize));
            final PeeColor _tmpColor;
            _tmpColor = __PeeColor_stringToEnum(_cursor.getString(_cursorIndexOfColor));
            final Urgency _tmpUrgency;
            _tmpUrgency = __Urgency_stringToEnum(_cursor.getString(_cursorIndexOfUrgency));
            final String _tmpActivityContext;
            if (_cursor.isNull(_cursorIndexOfActivityContext)) {
              _tmpActivityContext = null;
            } else {
              _tmpActivityContext = _cursor.getString(_cursorIndexOfActivityContext);
            }
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final LocalTime _tmpScheduledTime;
            final String _tmp_3;
            if (_cursor.isNull(_cursorIndexOfScheduledTime)) {
              _tmp_3 = null;
            } else {
              _tmp_3 = _cursor.getString(_cursorIndexOfScheduledTime);
            }
            _tmpScheduledTime = __converters.toLocalTime(_tmp_3);
            _item = new PeeEntryEntity(_tmpId,_tmpTimestamp,_tmpDidVoid,_tmpLeakAmount,_tmpVolumeSize,_tmpColor,_tmpUrgency,_tmpActivityContext,_tmpNotes,_tmpScheduledTime);
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
  public Object getCountByDateRange(final Instant startOfDay, final Instant endOfDay,
      final Continuation<? super Integer> $completion) {
    final String _sql = "\n"
            + "        SELECT COUNT(*) FROM pee_entries \n"
            + "        WHERE timestamp >= ? AND timestamp < ?\n"
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
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(0);
            _result = _tmp_2;
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
  public Object getByMinuteRange(final Instant startOfMinute, final Instant endOfMinute,
      final Continuation<? super Integer> $completion) {
    final String _sql = "\n"
            + "        SELECT COUNT(*) FROM pee_entries \n"
            + "        WHERE timestamp >= ? AND timestamp < ?\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    final Long _tmp = __converters.fromInstant(startOfMinute);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, _tmp);
    }
    _argIndex = 2;
    final Long _tmp_1 = __converters.fromInstant(endOfMinute);
    if (_tmp_1 == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, _tmp_1);
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
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(0);
            _result = _tmp_2;
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
  public Object getFirstVoidOfDay(final Instant startOfDay, final Instant endOfDay,
      final Continuation<? super PeeEntryEntity> $completion) {
    final String _sql = "\n"
            + "        SELECT * FROM pee_entries \n"
            + "        WHERE timestamp >= ? AND timestamp < ? \n"
            + "        AND didVoid = 1 \n"
            + "        ORDER BY timestamp ASC \n"
            + "        LIMIT 1\n"
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
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<PeeEntryEntity>() {
      @Override
      @Nullable
      public PeeEntryEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfDidVoid = CursorUtil.getColumnIndexOrThrow(_cursor, "didVoid");
          final int _cursorIndexOfLeakAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "leakAmount");
          final int _cursorIndexOfVolumeSize = CursorUtil.getColumnIndexOrThrow(_cursor, "volumeSize");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfUrgency = CursorUtil.getColumnIndexOrThrow(_cursor, "urgency");
          final int _cursorIndexOfActivityContext = CursorUtil.getColumnIndexOrThrow(_cursor, "activityContext");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfScheduledTime = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduledTime");
          final PeeEntryEntity _result;
          if (_cursor.moveToFirst()) {
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
            final boolean _tmpDidVoid;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfDidVoid);
            _tmpDidVoid = _tmp_4 != 0;
            final LeakAmount _tmpLeakAmount;
            _tmpLeakAmount = __LeakAmount_stringToEnum(_cursor.getString(_cursorIndexOfLeakAmount));
            final VolumeSize _tmpVolumeSize;
            _tmpVolumeSize = __VolumeSize_stringToEnum(_cursor.getString(_cursorIndexOfVolumeSize));
            final PeeColor _tmpColor;
            _tmpColor = __PeeColor_stringToEnum(_cursor.getString(_cursorIndexOfColor));
            final Urgency _tmpUrgency;
            _tmpUrgency = __Urgency_stringToEnum(_cursor.getString(_cursorIndexOfUrgency));
            final String _tmpActivityContext;
            if (_cursor.isNull(_cursorIndexOfActivityContext)) {
              _tmpActivityContext = null;
            } else {
              _tmpActivityContext = _cursor.getString(_cursorIndexOfActivityContext);
            }
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final LocalTime _tmpScheduledTime;
            final String _tmp_5;
            if (_cursor.isNull(_cursorIndexOfScheduledTime)) {
              _tmp_5 = null;
            } else {
              _tmp_5 = _cursor.getString(_cursorIndexOfScheduledTime);
            }
            _tmpScheduledTime = __converters.toLocalTime(_tmp_5);
            _result = new PeeEntryEntity(_tmpId,_tmpTimestamp,_tmpDidVoid,_tmpLeakAmount,_tmpVolumeSize,_tmpColor,_tmpUrgency,_tmpActivityContext,_tmpNotes,_tmpScheduledTime);
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
  public Object getByScheduledTime(final Instant startOfDay, final Instant endOfDay,
      final LocalTime scheduledTime, final Continuation<? super List<PeeEntryEntity>> $completion) {
    final String _sql = "\n"
            + "        SELECT * FROM pee_entries \n"
            + "        WHERE timestamp >= ? AND timestamp < ? \n"
            + "        AND scheduledTime = ?\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
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
    _argIndex = 3;
    final String _tmp_2 = __converters.fromLocalTime(scheduledTime);
    if (_tmp_2 == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, _tmp_2);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<PeeEntryEntity>>() {
      @Override
      @NonNull
      public List<PeeEntryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfDidVoid = CursorUtil.getColumnIndexOrThrow(_cursor, "didVoid");
          final int _cursorIndexOfLeakAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "leakAmount");
          final int _cursorIndexOfVolumeSize = CursorUtil.getColumnIndexOrThrow(_cursor, "volumeSize");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfUrgency = CursorUtil.getColumnIndexOrThrow(_cursor, "urgency");
          final int _cursorIndexOfActivityContext = CursorUtil.getColumnIndexOrThrow(_cursor, "activityContext");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfScheduledTime = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduledTime");
          final List<PeeEntryEntity> _result = new ArrayList<PeeEntryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PeeEntryEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final Instant _tmpTimestamp;
            final Long _tmp_3;
            if (_cursor.isNull(_cursorIndexOfTimestamp)) {
              _tmp_3 = null;
            } else {
              _tmp_3 = _cursor.getLong(_cursorIndexOfTimestamp);
            }
            final Instant _tmp_4 = __converters.toInstant(_tmp_3);
            if (_tmp_4 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.Instant', but it was NULL.");
            } else {
              _tmpTimestamp = _tmp_4;
            }
            final boolean _tmpDidVoid;
            final int _tmp_5;
            _tmp_5 = _cursor.getInt(_cursorIndexOfDidVoid);
            _tmpDidVoid = _tmp_5 != 0;
            final LeakAmount _tmpLeakAmount;
            _tmpLeakAmount = __LeakAmount_stringToEnum(_cursor.getString(_cursorIndexOfLeakAmount));
            final VolumeSize _tmpVolumeSize;
            _tmpVolumeSize = __VolumeSize_stringToEnum(_cursor.getString(_cursorIndexOfVolumeSize));
            final PeeColor _tmpColor;
            _tmpColor = __PeeColor_stringToEnum(_cursor.getString(_cursorIndexOfColor));
            final Urgency _tmpUrgency;
            _tmpUrgency = __Urgency_stringToEnum(_cursor.getString(_cursorIndexOfUrgency));
            final String _tmpActivityContext;
            if (_cursor.isNull(_cursorIndexOfActivityContext)) {
              _tmpActivityContext = null;
            } else {
              _tmpActivityContext = _cursor.getString(_cursorIndexOfActivityContext);
            }
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final LocalTime _tmpScheduledTime;
            final String _tmp_6;
            if (_cursor.isNull(_cursorIndexOfScheduledTime)) {
              _tmp_6 = null;
            } else {
              _tmp_6 = _cursor.getString(_cursorIndexOfScheduledTime);
            }
            _tmpScheduledTime = __converters.toLocalTime(_tmp_6);
            _item = new PeeEntryEntity(_tmpId,_tmpTimestamp,_tmpDidVoid,_tmpLeakAmount,_tmpVolumeSize,_tmpColor,_tmpUrgency,_tmpActivityContext,_tmpNotes,_tmpScheduledTime);
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
  public Object getEntriesWithScheduledTime(final Instant startOfDay, final Instant endOfDay,
      final Continuation<? super List<PeeEntryEntity>> $completion) {
    final String _sql = "\n"
            + "        SELECT * FROM pee_entries \n"
            + "        WHERE timestamp >= ? AND timestamp < ? \n"
            + "        AND scheduledTime IS NOT NULL\n"
            + "        ORDER BY timestamp ASC\n"
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
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<PeeEntryEntity>>() {
      @Override
      @NonNull
      public List<PeeEntryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfDidVoid = CursorUtil.getColumnIndexOrThrow(_cursor, "didVoid");
          final int _cursorIndexOfLeakAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "leakAmount");
          final int _cursorIndexOfVolumeSize = CursorUtil.getColumnIndexOrThrow(_cursor, "volumeSize");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfUrgency = CursorUtil.getColumnIndexOrThrow(_cursor, "urgency");
          final int _cursorIndexOfActivityContext = CursorUtil.getColumnIndexOrThrow(_cursor, "activityContext");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfScheduledTime = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduledTime");
          final List<PeeEntryEntity> _result = new ArrayList<PeeEntryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PeeEntryEntity _item;
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
            final boolean _tmpDidVoid;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfDidVoid);
            _tmpDidVoid = _tmp_4 != 0;
            final LeakAmount _tmpLeakAmount;
            _tmpLeakAmount = __LeakAmount_stringToEnum(_cursor.getString(_cursorIndexOfLeakAmount));
            final VolumeSize _tmpVolumeSize;
            _tmpVolumeSize = __VolumeSize_stringToEnum(_cursor.getString(_cursorIndexOfVolumeSize));
            final PeeColor _tmpColor;
            _tmpColor = __PeeColor_stringToEnum(_cursor.getString(_cursorIndexOfColor));
            final Urgency _tmpUrgency;
            _tmpUrgency = __Urgency_stringToEnum(_cursor.getString(_cursorIndexOfUrgency));
            final String _tmpActivityContext;
            if (_cursor.isNull(_cursorIndexOfActivityContext)) {
              _tmpActivityContext = null;
            } else {
              _tmpActivityContext = _cursor.getString(_cursorIndexOfActivityContext);
            }
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final LocalTime _tmpScheduledTime;
            final String _tmp_5;
            if (_cursor.isNull(_cursorIndexOfScheduledTime)) {
              _tmp_5 = null;
            } else {
              _tmp_5 = _cursor.getString(_cursorIndexOfScheduledTime);
            }
            _tmpScheduledTime = __converters.toLocalTime(_tmp_5);
            _item = new PeeEntryEntity(_tmpId,_tmpTimestamp,_tmpDidVoid,_tmpLeakAmount,_tmpVolumeSize,_tmpColor,_tmpUrgency,_tmpActivityContext,_tmpNotes,_tmpScheduledTime);
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
  public Flow<List<PeeEntryEntity>> getVoidsOnlyByDateRange(final Instant startOfDay,
      final Instant endOfDay) {
    final String _sql = "\n"
            + "        SELECT * FROM pee_entries \n"
            + "        WHERE timestamp >= ? AND timestamp < ? \n"
            + "        AND didVoid = 1 \n"
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
    return CoroutinesRoom.createFlow(__db, false, new String[] {"pee_entries"}, new Callable<List<PeeEntryEntity>>() {
      @Override
      @NonNull
      public List<PeeEntryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfDidVoid = CursorUtil.getColumnIndexOrThrow(_cursor, "didVoid");
          final int _cursorIndexOfLeakAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "leakAmount");
          final int _cursorIndexOfVolumeSize = CursorUtil.getColumnIndexOrThrow(_cursor, "volumeSize");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfUrgency = CursorUtil.getColumnIndexOrThrow(_cursor, "urgency");
          final int _cursorIndexOfActivityContext = CursorUtil.getColumnIndexOrThrow(_cursor, "activityContext");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfScheduledTime = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduledTime");
          final List<PeeEntryEntity> _result = new ArrayList<PeeEntryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PeeEntryEntity _item;
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
            final boolean _tmpDidVoid;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfDidVoid);
            _tmpDidVoid = _tmp_4 != 0;
            final LeakAmount _tmpLeakAmount;
            _tmpLeakAmount = __LeakAmount_stringToEnum(_cursor.getString(_cursorIndexOfLeakAmount));
            final VolumeSize _tmpVolumeSize;
            _tmpVolumeSize = __VolumeSize_stringToEnum(_cursor.getString(_cursorIndexOfVolumeSize));
            final PeeColor _tmpColor;
            _tmpColor = __PeeColor_stringToEnum(_cursor.getString(_cursorIndexOfColor));
            final Urgency _tmpUrgency;
            _tmpUrgency = __Urgency_stringToEnum(_cursor.getString(_cursorIndexOfUrgency));
            final String _tmpActivityContext;
            if (_cursor.isNull(_cursorIndexOfActivityContext)) {
              _tmpActivityContext = null;
            } else {
              _tmpActivityContext = _cursor.getString(_cursorIndexOfActivityContext);
            }
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final LocalTime _tmpScheduledTime;
            final String _tmp_5;
            if (_cursor.isNull(_cursorIndexOfScheduledTime)) {
              _tmp_5 = null;
            } else {
              _tmp_5 = _cursor.getString(_cursorIndexOfScheduledTime);
            }
            _tmpScheduledTime = __converters.toLocalTime(_tmp_5);
            _item = new PeeEntryEntity(_tmpId,_tmpTimestamp,_tmpDidVoid,_tmpLeakAmount,_tmpVolumeSize,_tmpColor,_tmpUrgency,_tmpActivityContext,_tmpNotes,_tmpScheduledTime);
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
  public Flow<List<PeeEntryEntity>> getUrgesOnlyByDateRange(final Instant startOfDay,
      final Instant endOfDay) {
    final String _sql = "\n"
            + "        SELECT * FROM pee_entries \n"
            + "        WHERE timestamp >= ? AND timestamp < ? \n"
            + "        AND didVoid = 0 \n"
            + "        AND leakAmount = 'NONE'\n"
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
    return CoroutinesRoom.createFlow(__db, false, new String[] {"pee_entries"}, new Callable<List<PeeEntryEntity>>() {
      @Override
      @NonNull
      public List<PeeEntryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfDidVoid = CursorUtil.getColumnIndexOrThrow(_cursor, "didVoid");
          final int _cursorIndexOfLeakAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "leakAmount");
          final int _cursorIndexOfVolumeSize = CursorUtil.getColumnIndexOrThrow(_cursor, "volumeSize");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfUrgency = CursorUtil.getColumnIndexOrThrow(_cursor, "urgency");
          final int _cursorIndexOfActivityContext = CursorUtil.getColumnIndexOrThrow(_cursor, "activityContext");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfScheduledTime = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduledTime");
          final List<PeeEntryEntity> _result = new ArrayList<PeeEntryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PeeEntryEntity _item;
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
            final boolean _tmpDidVoid;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfDidVoid);
            _tmpDidVoid = _tmp_4 != 0;
            final LeakAmount _tmpLeakAmount;
            _tmpLeakAmount = __LeakAmount_stringToEnum(_cursor.getString(_cursorIndexOfLeakAmount));
            final VolumeSize _tmpVolumeSize;
            _tmpVolumeSize = __VolumeSize_stringToEnum(_cursor.getString(_cursorIndexOfVolumeSize));
            final PeeColor _tmpColor;
            _tmpColor = __PeeColor_stringToEnum(_cursor.getString(_cursorIndexOfColor));
            final Urgency _tmpUrgency;
            _tmpUrgency = __Urgency_stringToEnum(_cursor.getString(_cursorIndexOfUrgency));
            final String _tmpActivityContext;
            if (_cursor.isNull(_cursorIndexOfActivityContext)) {
              _tmpActivityContext = null;
            } else {
              _tmpActivityContext = _cursor.getString(_cursorIndexOfActivityContext);
            }
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final LocalTime _tmpScheduledTime;
            final String _tmp_5;
            if (_cursor.isNull(_cursorIndexOfScheduledTime)) {
              _tmp_5 = null;
            } else {
              _tmp_5 = _cursor.getString(_cursorIndexOfScheduledTime);
            }
            _tmpScheduledTime = __converters.toLocalTime(_tmp_5);
            _item = new PeeEntryEntity(_tmpId,_tmpTimestamp,_tmpDidVoid,_tmpLeakAmount,_tmpVolumeSize,_tmpColor,_tmpUrgency,_tmpActivityContext,_tmpNotes,_tmpScheduledTime);
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
  public Flow<List<PeeEntryEntity>> getLeaksOnlyByDateRange(final Instant startOfDay,
      final Instant endOfDay) {
    final String _sql = "\n"
            + "        SELECT * FROM pee_entries\n"
            + "        WHERE timestamp >= ? AND timestamp < ?\n"
            + "        AND didVoid = 0\n"
            + "        AND leakAmount != 'NONE'\n"
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
    return CoroutinesRoom.createFlow(__db, false, new String[] {"pee_entries"}, new Callable<List<PeeEntryEntity>>() {
      @Override
      @NonNull
      public List<PeeEntryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfDidVoid = CursorUtil.getColumnIndexOrThrow(_cursor, "didVoid");
          final int _cursorIndexOfLeakAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "leakAmount");
          final int _cursorIndexOfVolumeSize = CursorUtil.getColumnIndexOrThrow(_cursor, "volumeSize");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfUrgency = CursorUtil.getColumnIndexOrThrow(_cursor, "urgency");
          final int _cursorIndexOfActivityContext = CursorUtil.getColumnIndexOrThrow(_cursor, "activityContext");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfScheduledTime = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduledTime");
          final List<PeeEntryEntity> _result = new ArrayList<PeeEntryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PeeEntryEntity _item;
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
            final boolean _tmpDidVoid;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfDidVoid);
            _tmpDidVoid = _tmp_4 != 0;
            final LeakAmount _tmpLeakAmount;
            _tmpLeakAmount = __LeakAmount_stringToEnum(_cursor.getString(_cursorIndexOfLeakAmount));
            final VolumeSize _tmpVolumeSize;
            _tmpVolumeSize = __VolumeSize_stringToEnum(_cursor.getString(_cursorIndexOfVolumeSize));
            final PeeColor _tmpColor;
            _tmpColor = __PeeColor_stringToEnum(_cursor.getString(_cursorIndexOfColor));
            final Urgency _tmpUrgency;
            _tmpUrgency = __Urgency_stringToEnum(_cursor.getString(_cursorIndexOfUrgency));
            final String _tmpActivityContext;
            if (_cursor.isNull(_cursorIndexOfActivityContext)) {
              _tmpActivityContext = null;
            } else {
              _tmpActivityContext = _cursor.getString(_cursorIndexOfActivityContext);
            }
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final LocalTime _tmpScheduledTime;
            final String _tmp_5;
            if (_cursor.isNull(_cursorIndexOfScheduledTime)) {
              _tmp_5 = null;
            } else {
              _tmp_5 = _cursor.getString(_cursorIndexOfScheduledTime);
            }
            _tmpScheduledTime = __converters.toLocalTime(_tmp_5);
            _item = new PeeEntryEntity(_tmpId,_tmpTimestamp,_tmpDidVoid,_tmpLeakAmount,_tmpVolumeSize,_tmpColor,_tmpUrgency,_tmpActivityContext,_tmpNotes,_tmpScheduledTime);
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

  private String __LeakAmount_enumToString(@NonNull final LeakAmount _value) {
    switch (_value) {
      case NONE: return "NONE";
      case SMALL: return "SMALL";
      case MEDIUM: return "MEDIUM";
      case LARGE: return "LARGE";
      default: throw new IllegalArgumentException("Can't convert enum to string, unknown enum value: " + _value);
    }
  }

  private String __VolumeSize_enumToString(@NonNull final VolumeSize _value) {
    switch (_value) {
      case UNKNOWN: return "UNKNOWN";
      case SMALL: return "SMALL";
      case MEDIUM: return "MEDIUM";
      case LARGE: return "LARGE";
      default: throw new IllegalArgumentException("Can't convert enum to string, unknown enum value: " + _value);
    }
  }

  private String __PeeColor_enumToString(@NonNull final PeeColor _value) {
    switch (_value) {
      case UNKNOWN: return "UNKNOWN";
      case CLEAR: return "CLEAR";
      case LIGHT_YELLOW: return "LIGHT_YELLOW";
      case YELLOW: return "YELLOW";
      case DARK_YELLOW: return "DARK_YELLOW";
      case AMBER: return "AMBER";
      default: throw new IllegalArgumentException("Can't convert enum to string, unknown enum value: " + _value);
    }
  }

  private String __Urgency_enumToString(@NonNull final Urgency _value) {
    switch (_value) {
      case UNKNOWN: return "UNKNOWN";
      case NONE: return "NONE";
      case LOW: return "LOW";
      case MEDIUM: return "MEDIUM";
      case HIGH: return "HIGH";
      case BURST: return "BURST";
      default: throw new IllegalArgumentException("Can't convert enum to string, unknown enum value: " + _value);
    }
  }

  private LeakAmount __LeakAmount_stringToEnum(@NonNull final String _value) {
    switch (_value) {
      case "NONE": return LeakAmount.NONE;
      case "SMALL": return LeakAmount.SMALL;
      case "MEDIUM": return LeakAmount.MEDIUM;
      case "LARGE": return LeakAmount.LARGE;
      default: throw new IllegalArgumentException("Can't convert value to enum, unknown value: " + _value);
    }
  }

  private VolumeSize __VolumeSize_stringToEnum(@NonNull final String _value) {
    switch (_value) {
      case "UNKNOWN": return VolumeSize.UNKNOWN;
      case "SMALL": return VolumeSize.SMALL;
      case "MEDIUM": return VolumeSize.MEDIUM;
      case "LARGE": return VolumeSize.LARGE;
      default: throw new IllegalArgumentException("Can't convert value to enum, unknown value: " + _value);
    }
  }

  private PeeColor __PeeColor_stringToEnum(@NonNull final String _value) {
    switch (_value) {
      case "UNKNOWN": return PeeColor.UNKNOWN;
      case "CLEAR": return PeeColor.CLEAR;
      case "LIGHT_YELLOW": return PeeColor.LIGHT_YELLOW;
      case "YELLOW": return PeeColor.YELLOW;
      case "DARK_YELLOW": return PeeColor.DARK_YELLOW;
      case "AMBER": return PeeColor.AMBER;
      default: throw new IllegalArgumentException("Can't convert value to enum, unknown value: " + _value);
    }
  }

  private Urgency __Urgency_stringToEnum(@NonNull final String _value) {
    switch (_value) {
      case "UNKNOWN": return Urgency.UNKNOWN;
      case "NONE": return Urgency.NONE;
      case "LOW": return Urgency.LOW;
      case "MEDIUM": return Urgency.MEDIUM;
      case "HIGH": return Urgency.HIGH;
      case "BURST": return Urgency.BURST;
      default: throw new IllegalArgumentException("Can't convert value to enum, unknown value: " + _value);
    }
  }
}
