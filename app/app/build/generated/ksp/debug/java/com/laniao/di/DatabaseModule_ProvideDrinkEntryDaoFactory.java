package com.laniao.di;

import com.laniao.data.local.LaNiaoDatabase;
import com.laniao.data.local.dao.DrinkEntryDao;
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
public final class DatabaseModule_ProvideDrinkEntryDaoFactory implements Factory<DrinkEntryDao> {
  private final Provider<LaNiaoDatabase> databaseProvider;

  public DatabaseModule_ProvideDrinkEntryDaoFactory(Provider<LaNiaoDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public DrinkEntryDao get() {
    return provideDrinkEntryDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideDrinkEntryDaoFactory create(
      javax.inject.Provider<LaNiaoDatabase> databaseProvider) {
    return new DatabaseModule_ProvideDrinkEntryDaoFactory(Providers.asDaggerProvider(databaseProvider));
  }

  public static DatabaseModule_ProvideDrinkEntryDaoFactory create(
      Provider<LaNiaoDatabase> databaseProvider) {
    return new DatabaseModule_ProvideDrinkEntryDaoFactory(databaseProvider);
  }

  public static DrinkEntryDao provideDrinkEntryDao(LaNiaoDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideDrinkEntryDao(database));
  }
}
