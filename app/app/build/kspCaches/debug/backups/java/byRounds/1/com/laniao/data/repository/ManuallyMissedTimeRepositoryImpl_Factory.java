package com.laniao.data.repository;

import com.laniao.data.local.dao.ManuallyMissedTimeDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class ManuallyMissedTimeRepositoryImpl_Factory implements Factory<ManuallyMissedTimeRepositoryImpl> {
  private final Provider<ManuallyMissedTimeDao> daoProvider;

  public ManuallyMissedTimeRepositoryImpl_Factory(Provider<ManuallyMissedTimeDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public ManuallyMissedTimeRepositoryImpl get() {
    return newInstance(daoProvider.get());
  }

  public static ManuallyMissedTimeRepositoryImpl_Factory create(
      javax.inject.Provider<ManuallyMissedTimeDao> daoProvider) {
    return new ManuallyMissedTimeRepositoryImpl_Factory(Providers.asDaggerProvider(daoProvider));
  }

  public static ManuallyMissedTimeRepositoryImpl_Factory create(
      Provider<ManuallyMissedTimeDao> daoProvider) {
    return new ManuallyMissedTimeRepositoryImpl_Factory(daoProvider);
  }

  public static ManuallyMissedTimeRepositoryImpl newInstance(ManuallyMissedTimeDao dao) {
    return new ManuallyMissedTimeRepositoryImpl(dao);
  }
}
