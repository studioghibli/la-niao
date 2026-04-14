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
public final class GetAllSchedulesUseCase_Factory implements Factory<GetAllSchedulesUseCase> {
  private final Provider<VoidScheduleRepository> repositoryProvider;

  public GetAllSchedulesUseCase_Factory(Provider<VoidScheduleRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public GetAllSchedulesUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static GetAllSchedulesUseCase_Factory create(
      javax.inject.Provider<VoidScheduleRepository> repositoryProvider) {
    return new GetAllSchedulesUseCase_Factory(Providers.asDaggerProvider(repositoryProvider));
  }

  public static GetAllSchedulesUseCase_Factory create(
      Provider<VoidScheduleRepository> repositoryProvider) {
    return new GetAllSchedulesUseCase_Factory(repositoryProvider);
  }

  public static GetAllSchedulesUseCase newInstance(VoidScheduleRepository repository) {
    return new GetAllSchedulesUseCase(repository);
  }
}
