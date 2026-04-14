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
public final class GetExerciseCompletionUseCase_Factory implements Factory<GetExerciseCompletionUseCase> {
  private final Provider<ExerciseRepository> repositoryProvider;

  public GetExerciseCompletionUseCase_Factory(Provider<ExerciseRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public GetExerciseCompletionUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static GetExerciseCompletionUseCase_Factory create(
      javax.inject.Provider<ExerciseRepository> repositoryProvider) {
    return new GetExerciseCompletionUseCase_Factory(Providers.asDaggerProvider(repositoryProvider));
  }

  public static GetExerciseCompletionUseCase_Factory create(
      Provider<ExerciseRepository> repositoryProvider) {
    return new GetExerciseCompletionUseCase_Factory(repositoryProvider);
  }

  public static GetExerciseCompletionUseCase newInstance(ExerciseRepository repository) {
    return new GetExerciseCompletionUseCase(repository);
  }
}
