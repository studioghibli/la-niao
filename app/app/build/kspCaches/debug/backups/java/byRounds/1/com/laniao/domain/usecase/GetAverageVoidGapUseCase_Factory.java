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
public final class GetAverageVoidGapUseCase_Factory implements Factory<GetAverageVoidGapUseCase> {
  private final Provider<PeeEntryRepository> repositoryProvider;

  private final Provider<VoidScheduleRepository> voidScheduleRepositoryProvider;

  public GetAverageVoidGapUseCase_Factory(Provider<PeeEntryRepository> repositoryProvider,
      Provider<VoidScheduleRepository> voidScheduleRepositoryProvider) {
    this.repositoryProvider = repositoryProvider;
    this.voidScheduleRepositoryProvider = voidScheduleRepositoryProvider;
  }

  @Override
  public GetAverageVoidGapUseCase get() {
    return newInstance(repositoryProvider.get(), voidScheduleRepositoryProvider.get());
  }

  public static GetAverageVoidGapUseCase_Factory create(
      javax.inject.Provider<PeeEntryRepository> repositoryProvider,
      javax.inject.Provider<VoidScheduleRepository> voidScheduleRepositoryProvider) {
    return new GetAverageVoidGapUseCase_Factory(Providers.asDaggerProvider(repositoryProvider), Providers.asDaggerProvider(voidScheduleRepositoryProvider));
  }

  public static GetAverageVoidGapUseCase_Factory create(
      Provider<PeeEntryRepository> repositoryProvider,
      Provider<VoidScheduleRepository> voidScheduleRepositoryProvider) {
    return new GetAverageVoidGapUseCase_Factory(repositoryProvider, voidScheduleRepositoryProvider);
  }

  public static GetAverageVoidGapUseCase newInstance(PeeEntryRepository repository,
      VoidScheduleRepository voidScheduleRepository) {
    return new GetAverageVoidGapUseCase(repository, voidScheduleRepository);
  }
}
