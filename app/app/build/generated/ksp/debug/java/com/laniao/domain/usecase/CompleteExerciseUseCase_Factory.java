package com.laniao.domain.usecase;

import com.laniao.domain.repository.ExerciseRepository;
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
public final class CompleteExerciseUseCase_Factory implements Factory<CompleteExerciseUseCase> {
  private final Provider<ExerciseRepository> repositoryProvider;

  private final Provider<Clock> clockProvider;

  public CompleteExerciseUseCase_Factory(Provider<ExerciseRepository> repositoryProvider,
      Provider<Clock> clockProvider) {
    this.repositoryProvider = repositoryProvider;
    this.clockProvider = clockProvider;
  }

  @Override
  public CompleteExerciseUseCase get() {
    return newInstance(repositoryProvider.get(), clockProvider.get());
  }

  public static CompleteExerciseUseCase_Factory create(
      javax.inject.Provider<ExerciseRepository> repositoryProvider,
      javax.inject.Provider<Clock> clockProvider) {
    return new CompleteExerciseUseCase_Factory(Providers.asDaggerProvider(repositoryProvider), Providers.asDaggerProvider(clockProvider));
  }

  public static CompleteExerciseUseCase_Factory create(
      Provider<ExerciseRepository> repositoryProvider, Provider<Clock> clockProvider) {
    return new CompleteExerciseUseCase_Factory(repositoryProvider, clockProvider);
  }

  public static CompleteExerciseUseCase newInstance(ExerciseRepository repository, Clock clock) {
    return new CompleteExerciseUseCase(repository, clock);
  }
}
