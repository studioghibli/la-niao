package com.laniao.domain.usecase;

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
public final class GetActiveSchedulesUseCase_Factory implements Factory<GetActiveSchedulesUseCase> {
  private final Provider<VoidScheduleRepository> repositoryProvider;

  public GetActiveSchedulesUseCase_Factory(Provider<VoidScheduleRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public GetActiveSchedulesUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static GetActiveSchedulesUseCase_Factory create(
      javax.inject.Provider<VoidScheduleRepository> repositoryProvider) {
    return new GetActiveSchedulesUseCase_Factory(Providers.asDaggerProvider(repositoryProvider));
  }

  public static GetActiveSchedulesUseCase_Factory create(
      Provider<VoidScheduleRepository> repositoryProvider) {
    return new GetActiveSchedulesUseCase_Factory(repositoryProvider);
  }

  public static GetActiveSchedulesUseCase newInstance(VoidScheduleRepository repository) {
    return new GetActiveSchedulesUseCase(repository);
  }
}
