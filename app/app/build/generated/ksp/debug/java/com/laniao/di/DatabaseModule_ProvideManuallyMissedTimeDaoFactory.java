package com.laniao.di;

import com.laniao.data.local.LaNiaoDatabase;
import com.laniao.data.local.dao.ManuallyMissedTimeDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.Providers;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class DatabaseModule_ProvideManuallyMissedTimeDaoFactory implements Factory<ManuallyMissedTimeDao> {
  private final Provider<LaNiaoDatabase> databaseProvider;

  public DatabaseModule_ProvideManuallyMissedTimeDaoFactory(
      Provider<LaNiaoDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public ManuallyMissedTimeDao get() {
    return provideManuallyMissedTimeDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideManuallyMissedTimeDaoFactory create(
      javax.inject.Provider<LaNiaoDatabase> databaseProvider) {
    return new DatabaseModule_ProvideManuallyMissedTimeDaoFactory(Providers.asDaggerProvider(databaseProvider));
  }

  public static DatabaseModule_ProvideManuallyMissedTimeDaoFactory create(
      Provider<LaNiaoDatabase> databaseProvider) {
    return new DatabaseModule_ProvideManuallyMissedTimeDaoFactory(databaseProvider);
  }

  public static ManuallyMissedTimeDao provideManuallyMissedTimeDao(LaNiaoDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideManuallyMissedTimeDao(database));
  }
}
