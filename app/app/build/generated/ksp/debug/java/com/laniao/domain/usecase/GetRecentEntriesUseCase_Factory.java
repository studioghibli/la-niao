package com.laniao.domain.usecase;

import com.laniao.domain.repository.PeeEntryRepository;
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
public final class GetRecentEntriesUseCase_Factory implements Factory<GetRecentEntriesUseCase> {
  private final Provider<PeeEntryRepository> repositoryProvider;

  private final Provider<Clock> clockProvider;

  public GetRecentEntriesUseCase_Factory(Provider<PeeEntryRepository> repositoryProvider,
      Provider<Clock> clockProvider) {
    this.repositoryProvider = repositoryProvider;
    this.clockProvider = clockProvider;
  }

  @Override
  public GetRecentEntriesUseCase get() {
    return newInstance(repositoryProvider.get(), clockProvider.get());
  }

  public static GetRecentEntriesUseCase_Factory create(
      javax.inject.Provider<PeeEntryRepository> repositoryProvider,
      javax.inject.Provider<Clock> clockProvider) {
    return new GetRecentEntriesUseCase_Factory(Providers.asDaggerProvider(repositoryProvider), Providers.asDaggerProvider(clockProvider));
  }

  public static GetRecentEntriesUseCase_Factory create(
      Provider<PeeEntryRepository> repositoryProvider, Provider<Clock> clockProvider) {
    return new GetRecentEntriesUseCase_Factory(repositoryProvider, clockProvider);
  }

  public static GetRecentEntriesUseCase newInstance(PeeEntryRepository repository, Clock clock) {
    return new GetRecentEntriesUseCase(repository, clock);
  }
}
