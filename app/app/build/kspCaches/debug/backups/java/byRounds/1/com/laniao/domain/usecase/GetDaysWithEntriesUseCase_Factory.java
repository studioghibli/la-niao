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
public final class GetDaysWithEntriesUseCase_Factory implements Factory<GetDaysWithEntriesUseCase> {
  private final Provider<PeeEntryRepository> repositoryProvider;

  public GetDaysWithEntriesUseCase_Factory(Provider<PeeEntryRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public GetDaysWithEntriesUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static GetDaysWithEntriesUseCase_Factory create(
      javax.inject.Provider<PeeEntryRepository> repositoryProvider) {
    return new GetDaysWithEntriesUseCase_Factory(Providers.asDaggerProvider(repositoryProvider));
  }

  public static GetDaysWithEntriesUseCase_Factory create(
      Provider<PeeEntryRepository> repositoryProvider) {
    return new GetDaysWithEntriesUseCase_Factory(repositoryProvider);
  }

  public static GetDaysWithEntriesUseCase newInstance(PeeEntryRepository repository) {
    return new GetDaysWithEntriesUseCase(repository);
  }
}
