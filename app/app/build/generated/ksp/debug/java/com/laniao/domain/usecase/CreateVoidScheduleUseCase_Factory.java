package com.laniao.domain.usecase;

import com.laniao.domain.repository.PeeEntryRepository;
import com.laniao.domain.repository.VoidScheduleRepository;
import com.laniao.util.Clock;
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
public final class CreateVoidScheduleUseCase_Factory implements Factory<CreateVoidScheduleUseCase> {
  private final Provider<VoidScheduleRepository> repositoryProvider;

  private final Provider<PeeEntryRepository> entryRepositoryProvider;

  private final Provider<Clock> clockProvider;

  public CreateVoidScheduleUseCase_Factory(Provider<VoidScheduleRepository> repositoryProvider,
      Provider<PeeEntryRepository> entryRepositoryProvider, Provider<Clock> clockProvider) {
    this.repositoryProvider = repositoryProvider;
    this.entryRepositoryProvider = entryRepositoryProvider;
    this.clockProvider = clockProvider;
  }

  @Override
  public CreateVoidScheduleUseCase get() {
    return newInstance(repositoryProvider.get(), entryRepositoryProvider.get(), clockProvider.get());
  }

  public static CreateVoidScheduleUseCase_Factory create(
      javax.inject.Provider<VoidScheduleRepository> repositoryProvider,
      javax.inject.Provider<PeeEntryRepository> entryRepositoryProvider,
      javax.inject.Provider<Clock> clockProvider) {
    return new CreateVoidScheduleUseCase_Factory(Providers.asDaggerProvider(repositoryProvider), Providers.asDaggerProvider(entryRepositoryProvider), Providers.asDaggerProvider(clockProvider));
  }

  public static CreateVoidScheduleUseCase_Factory create(
      Provider<VoidScheduleRepository> repositoryProvider,
      Provider<PeeEntryRepository> entryRepositoryProvider, Provider<Clock> clockProvider) {
    return new CreateVoidScheduleUseCase_Factory(repositoryProvider, entryRepositoryProvider, clockProvider);
  }

  public static CreateVoidScheduleUseCase newInstance(VoidScheduleRepository repository,
      PeeEntryRepository entryRepository, Clock clock) {
    return new CreateVoidScheduleUseCase(repository, entryRepository, clock);
  }
}
