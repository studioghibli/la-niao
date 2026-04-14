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
import com.laniao.data.local.entity.AppSettingsEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppSettingsDao_Impl implements AppSettingsDao {
  private final RoomDatabase __db;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  private final EntityUpsertionAdapter<AppSettingsEntity> __upsertionAdapterOfAppSettingsEntity;

  public AppSettingsDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM app_settings";
        return _query;
      }
    };
    this.__upsertionAdapterOfAppSettingsEntity = new EntityUpsertionAdapter<AppSettingsEntity>(new EntityInsertionAdapter<AppSettingsEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT INTO `app_settings` (`id`,`hydrationGoalLiters`) VALUES (?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AppSettingsEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindDouble(2, entity.getHydrationGoalLiters());
      }
    }, new EntityDeletionOrUpdateAdapter<AppSettingsEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE `app_settings` SET `id` = ?,`hydrationGoalLiters` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AppSettingsEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindDouble(2, entity.getHydrationGoalLiters());
        statement.bindLong(3, entity.getId());
      }
    });
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
  public Object upsert(final AppSettingsEntity settings,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __upsertionAdapterOfAppSettingsEntity.upsert(settings);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<AppSettingsEntity> get() {
    final String _sql = "SELECT * FROM app_settings WHERE id = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"app_settings"}, new Callable<AppSettingsEntity>() {
      @Override
      @Nullable
      public AppSettingsEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfHydrationGoalLiters = CursorUtil.getColumnIndexOrThrow(_cursor, "hydrationGoalLiters");
          final AppSettingsEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final double _tmpHydrationGoalLiters;
            _tmpHydrationGoalLiters = _cursor.getDouble(_cursorIndexOfHydrationGoalLiters);
            _result = new AppSettingsEntity(_tmpId,_tmpHydrationGoalLiters);
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
  public Object getOnce(final Continuation<? super AppSettingsEntity> $completion) {
    final String _sql = "SELECT * FROM app_settings WHERE id = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<AppSettingsEntity>() {
      @Override
      @Nullable
      public AppSettingsEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfHydrationGoalLiters = CursorUtil.getColumnIndexOrThrow(_cursor, "hydrationGoalLiters");
          final AppSettingsEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final double _tmpHydrationGoalLiters;
            _tmpHydrationGoalLiters = _cursor.getDouble(_cursorIndexOfHydrationGoalLiters);
            _result = new AppSettingsEntity(_tmpId,_tmpHydrationGoalLiters);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
