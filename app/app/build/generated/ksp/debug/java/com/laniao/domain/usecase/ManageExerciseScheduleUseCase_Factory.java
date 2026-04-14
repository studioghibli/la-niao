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
public final class ManageExerciseScheduleUseCase_Factory implements Factory<ManageExerciseScheduleUseCase> {
  private final Provider<ExerciseRepository> repositoryProvider;

  public ManageExerciseScheduleUseCase_Factory(Provider<ExerciseRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public ManageExerciseScheduleUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static ManageExerciseScheduleUseCase_Factory create(
      javax.inject.Provider<ExerciseRepository> repositoryProvider) {
    return new ManageExerciseScheduleUseCase_Factory(Providers.asDaggerProvider(repositoryProvider));
  }

  public static ManageExerciseScheduleUseCase_Factory create(
      Provider<ExerciseRepository> repositoryProvider) {
    return new ManageExerciseScheduleUseCase_Factory(repositoryProvider);
  }

  public static ManageExerciseScheduleUseCase newInstance(ExerciseRepository repository) {
    return new ManageExerciseScheduleUseCase(repository);
  }
}
