package com.laniao.domain.usecase;

import com.laniao.domain.repository.PeeEntryRepository;
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
public final class GetEntriesForDateUseCase_Factory implements Factory<GetEntriesForDateUseCase> {
  private final Provider<PeeEntryRepository> repositoryProvider;

  public GetEntriesForDateUseCase_Factory(Provider<PeeEntryRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public GetEntriesForDateUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static GetEntriesForDateUseCase_Factory create(
      javax.inject.Provider<PeeEntryRepository> repositoryProvider) {
    return new GetEntriesForDateUseCase_Factory(Providers.asDaggerProvider(repositoryProvider));
  }

  public static GetEntriesForDateUseCase_Factory create(
      Provider<PeeEntryRepository> repositoryProvider) {
    return new GetEntriesForDateUseCase_Factory(repositoryProvider);
  }

  public static GetEntriesForDateUseCase newInstance(PeeEntryRepository repository) {
    return new GetEntriesForDateUseCase(repository);
  }
}
