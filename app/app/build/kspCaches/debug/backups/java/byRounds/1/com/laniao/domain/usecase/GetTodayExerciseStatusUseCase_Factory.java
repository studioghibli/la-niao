package com.laniao.domain.usecase;

import com.laniao.domain.repository.ExerciseRepository;
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
public final class GetTodayExerciseStatusUseCase_Factory implements Factory<GetTodayExerciseStatusUseCase> {
  private final Provider<ExerciseRepository> repositoryProvider;

  public GetTodayExerciseStatusUseCase_Factory(Provider<ExerciseRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public GetTodayExerciseStatusUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static GetTodayExerciseStatusUseCase_Factory create(
      javax.inject.Provider<ExerciseRepository> repositoryProvider) {
    return new GetTodayExerciseStatusUseCase_Factory(Providers.asDaggerProvider(repositoryProvider));
  }

  public static GetTodayExerciseStatusUseCase_Factory create(
      Provider<ExerciseRepository> repositoryProvider) {
    return new GetTodayExerciseStatusUseCase_Factory(repositoryProvider);
  }

  public static GetTodayExerciseStatusUseCase newInstance(ExerciseRepository repository) {
    return new GetTodayExerciseStatusUseCase(repository);
  }
}
