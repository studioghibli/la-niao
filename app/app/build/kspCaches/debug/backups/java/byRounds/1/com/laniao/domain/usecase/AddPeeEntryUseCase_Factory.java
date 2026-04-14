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
public final class AddPeeEntryUseCase_Factory implements Factory<AddPeeEntryUseCase> {
  private final Provider<PeeEntryRepository> repositoryProvider;

  private final Provider<Clock> clockProvider;

  public AddPeeEntryUseCase_Factory(Provider<PeeEntryRepository> repositoryProvider,
      Provider<Clock> clockProvider) {
    this.repositoryProvider = repositoryProvider;
    this.clockProvider = clockProvider;
  }

  @Override
  public AddPeeEntryUseCase get() {
    return newInstance(repositoryProvider.get(), clockProvider.get());
  }

  public static AddPeeEntryUseCase_Factory create(
      javax.inject.Provider<PeeEntryRepository> repositoryProvider,
      javax.inject.Provider<Clock> clockProvider) {
    return new AddPeeEntryUseCase_Factory(Providers.asDaggerProvider(repositoryProvider), Providers.asDaggerProvider(clockProvider));
  }

  public static AddPeeEntryUseCase_Factory create(Provider<PeeEntryRepository> repositoryProvider,
      Provider<Clock> clockProvider) {
    return new AddPeeEntryUseCase_Factory(repositoryProvider, clockProvider);
  }

  public static AddPeeEntryUseCase newInstance(PeeEntryRepository repository, Clock clock) {
    return new AddPeeEntryUseCase(repository, clock);
  }
}
