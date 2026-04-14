package com.laniao.data.repository;

import com.laniao.data.local.dao.VoidScheduleDao;
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
public final class VoidScheduleRepositoryImpl_Factory implements Factory<VoidScheduleRepositoryImpl> {
  private final Provider<VoidScheduleDao> daoProvider;

  public VoidScheduleRepositoryImpl_Factory(Provider<VoidScheduleDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public VoidScheduleRepositoryImpl get() {
    return newInstance(daoProvider.get());
  }

  public static VoidScheduleRepositoryImpl_Factory create(
      javax.inject.Provider<VoidScheduleDao> daoProvider) {
    return new VoidScheduleRepositoryImpl_Factory(Providers.asDaggerProvider(daoProvider));
  }

  public static VoidScheduleRepositoryImpl_Factory create(Provider<VoidScheduleDao> daoProvider) {
    return new VoidScheduleRepositoryImpl_Factory(daoProvider);
  }

  public static VoidScheduleRepositoryImpl newInstance(VoidScheduleDao dao) {
    return new VoidScheduleRepositoryImpl(dao);
  }
}
