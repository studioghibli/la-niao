package com.laniao.di;

import com.laniao.data.local.LaNiaoDatabase;
import com.laniao.data.local.dao.VoidScheduleDao;
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
public final class DatabaseModule_ProvideVoidScheduleDaoFactory implements Factory<VoidScheduleDao> {
  private final Provider<LaNiaoDatabase> databaseProvider;

  public DatabaseModule_ProvideVoidScheduleDaoFactory(Provider<LaNiaoDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public VoidScheduleDao get() {
    return provideVoidScheduleDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideVoidScheduleDaoFactory create(
      javax.inject.Provider<LaNiaoDatabase> databaseProvider) {
    return new DatabaseModule_ProvideVoidScheduleDaoFactory(Providers.asDaggerProvider(databaseProvider));
  }

  public static DatabaseModule_ProvideVoidScheduleDaoFactory create(
      Provider<LaNiaoDatabase> databaseProvider) {
    return new DatabaseModule_ProvideVoidScheduleDaoFactory(databaseProvider);
  }

  public static VoidScheduleDao provideVoidScheduleDao(LaNiaoDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideVoidScheduleDao(database));
  }
}
