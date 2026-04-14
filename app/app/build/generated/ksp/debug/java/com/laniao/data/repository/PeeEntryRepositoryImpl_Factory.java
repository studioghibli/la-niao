package com.laniao.data.repository;

import com.laniao.data.local.dao.PeeEntryDao;
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
public final class PeeEntryRepositoryImpl_Factory implements Factory<PeeEntryRepositoryImpl> {
  private final Provider<PeeEntryDao> daoProvider;

  public PeeEntryRepositoryImpl_Factory(Provider<PeeEntryDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public PeeEntryRepositoryImpl get() {
    return newInstance(daoProvider.get());
  }

  public static PeeEntryRepositoryImpl_Factory create(
      javax.inject.Provider<PeeEntryDao> daoProvider) {
    return new PeeEntryRepositoryImpl_Factory(Providers.asDaggerProvider(daoProvider));
  }

  public static PeeEntryRepositoryImpl_Factory create(Provider<PeeEntryDao> daoProvider) {
    return new PeeEntryRepositoryImpl_Factory(daoProvider);
  }

  public static PeeEntryRepositoryImpl newInstance(PeeEntryDao dao) {
    return new PeeEntryRepositoryImpl(dao);
  }
}
