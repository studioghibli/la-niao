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
public final class GetVoidFrequencyUseCase_Factory implements Factory<GetVoidFrequencyUseCase> {
  private final Provider<PeeEntryRepository> peeEntryRepositoryProvider;

  public GetVoidFrequencyUseCase_Factory(Provider<PeeEntryRepository> peeEntryRepositoryProvider) {
    this.peeEntryRepositoryProvider = peeEntryRepositoryProvider;
  }

  @Override
  public GetVoidFrequencyUseCase get() {
    return newInstance(peeEntryRepositoryProvider.get());
  }

  public static GetVoidFrequencyUseCase_Factory create(
      javax.inject.Provider<PeeEntryRepository> peeEntryRepositoryProvider) {
    return new GetVoidFrequencyUseCase_Factory(Providers.asDaggerProvider(peeEntryRepositoryProvider));
  }

  public static GetVoidFrequencyUseCase_Factory create(
      Provider<PeeEntryRepository> peeEntryRepositoryProvider) {
    return new GetVoidFrequencyUseCase_Factory(peeEntryRepositoryProvider);
  }

  public static GetVoidFrequencyUseCase newInstance(PeeEntryRepository peeEntryRepository) {
    return new GetVoidFrequencyUseCase(peeEntryRepository);
  }
}
