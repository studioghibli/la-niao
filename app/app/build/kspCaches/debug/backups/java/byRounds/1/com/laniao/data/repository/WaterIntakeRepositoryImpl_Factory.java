package com.laniao.data.repository;

import com.laniao.data.local.dao.WaterIntakeDao;
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
public final class WaterIntakeRepositoryImpl_Factory implements Factory<WaterIntakeRepositoryImpl> {
  private final Provider<WaterIntakeDao> daoProvider;

  public WaterIntakeRepositoryImpl_Factory(Provider<WaterIntakeDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public WaterIntakeRepositoryImpl get() {
    return newInstance(daoProvider.get());
  }

  public static WaterIntakeRepositoryImpl_Factory create(
      javax.inject.Provider<WaterIntakeDao> daoProvider) {
    return new WaterIntakeRepositoryImpl_Factory(Providers.asDaggerProvider(daoProvider));
  }

  public static WaterIntakeRepositoryImpl_Factory create(Provider<WaterIntakeDao> daoProvider) {
    return new WaterIntakeRepositoryImpl_Factory(daoProvider);
  }

  public static WaterIntakeRepositoryImpl newInstance(WaterIntakeDao dao) {
    return new WaterIntakeRepositoryImpl(dao);
  }
}
