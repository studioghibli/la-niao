package com.laniao.domain.usecase;

import com.laniao.domain.repository.PeeEntryRepository;
import com.laniao.domain.repository.VoidScheduleRepository;
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
public final class UpdateVoidScheduleExpiryUseCase_Factory implements Factory<UpdateVoidScheduleExpiryUseCase> {
  private final Provider<VoidScheduleRepository> repositoryProvider;

  private final Provider<PeeEntryRepository> entryRepositoryProvider;

  public UpdateVoidScheduleExpiryUseCase_Factory(
      Provider<VoidScheduleRepository> repositoryProvider,
      Provider<PeeEntryRepository> entryRepositoryProvider) {
    this.repositoryProvider = repositoryProvider;
    this.entryRepositoryProvider = entryRepositoryProvider;
  }

  @Override
  public UpdateVoidScheduleExpiryUseCase get() {
    return newInstance(repositoryProvider.get(), entryRepositoryProvider.get());
  }

  public static UpdateVoidScheduleExpiryUseCase_Factory create(
      javax.inject.Provider<VoidScheduleRepository> repositoryProvider,
      javax.inject.Provider<PeeEntryRepository> entryRepositoryProvider) {
    return new UpdateVoidScheduleExpiryUseCase_Factory(Providers.asDaggerProvider(repositoryProvider), Providers.asDaggerProvider(entryRepositoryProvider));
  }

  public static UpdateVoidScheduleExpiryUseCase_Factory create(
      Provider<VoidScheduleRepository> repositoryProvider,
      Provider<PeeEntryRepository> entryRepositoryProvider) {
    return new UpdateVoidScheduleExpiryUseCase_Factory(repositoryProvider, entryRepositoryProvider);
  }

  public static UpdateVoidScheduleExpiryUseCase newInstance(VoidScheduleRepository repository,
      PeeEntryRepository entryRepository) {
    return new UpdateVoidScheduleExpiryUseCase(repository, entryRepository);
  }
}
