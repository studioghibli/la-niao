package com.laniao.data.repository;

import com.laniao.data.local.dao.DrinkEntryDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.Providers;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
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
public final class DrinkEntryRepositoryImpl_Factory implements Factory<DrinkEntryRepositoryImpl> {
  private final Provider<DrinkEntryDao> daoProvider;

  public DrinkEntryRepositoryImpl_Factory(Provider<DrinkEntryDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public DrinkEntryRepositoryImpl get() {
    return newInstance(daoProvider.get());
  }

  public static DrinkEntryRepositoryImpl_Factory create(
      javax.inject.Provider<DrinkEntryDao> daoProvider) {
    return new DrinkEntryRepositoryImpl_Factory(Providers.asDaggerProvider(daoProvider));
  }

  public static DrinkEntryRepositoryImpl_Factory create(Provider<DrinkEntryDao> daoProvider) {
    return new DrinkEntryRepositoryImpl_Factory(daoProvider);
  }

  public static DrinkEntryRepositoryImpl newInstance(DrinkEntryDao dao) {
    return new DrinkEntryRepositoryImpl(dao);
  }
}
