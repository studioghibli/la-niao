package com.laniao.di;

import com.laniao.data.local.LaNiaoDatabase;
import com.laniao.data.local.dao.AppSettingsDao;
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
public final class DatabaseModule_ProvideAppSettingsDaoFactory implements Factory<AppSettingsDao> {
  private final Provider<LaNiaoDatabase> databaseProvider;

  public DatabaseModule_ProvideAppSettingsDaoFactory(Provider<LaNiaoDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public AppSettingsDao get() {
    return provideAppSettingsDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideAppSettingsDaoFactory create(
      javax.inject.Provider<LaNiaoDatabase> databaseProvider) {
    return new DatabaseModule_ProvideAppSettingsDaoFactory(Providers.asDaggerProvider(databaseProvider));
  }

  public static DatabaseModule_ProvideAppSettingsDaoFactory create(
      Provider<LaNiaoDatabase> databaseProvider) {
    return new DatabaseModule_ProvideAppSettingsDaoFactory(databaseProvider);
  }

  public static AppSettingsDao provideAppSettingsDao(LaNiaoDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideAppSettingsDao(database));
  }
}
