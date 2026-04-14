package com.laniao.domain.usecase;

import com.laniao.domain.repository.DrinkEntryRepository;
import com.laniao.domain.repository.ExerciseRepository;
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
public final class ExportCsvUseCase_Factory implements Factory<ExportCsvUseCase> {
  private final Provider<PeeEntryRepository> peeEntryRepositoryProvider;

  private final Provider<DrinkEntryRepository> drinkEntryRepositoryProvider;

  private final Provider<ExerciseRepository> exerciseRepositoryProvider;

  public ExportCsvUseCase_Factory(Provider<PeeEntryRepository> peeEntryRepositoryProvider,
      Provider<DrinkEntryRepository> drinkEntryRepositoryProvider,
      Provider<ExerciseRepository> exerciseRepositoryProvider) {
    this.peeEntryRepositoryProvider = peeEntryRepositoryProvider;
    this.drinkEntryRepositoryProvider = drinkEntryRepositoryProvider;
    this.exerciseRepositoryProvider = exerciseRepositoryProvider;
  }

  @Override
  public ExportCsvUseCase get() {
    return newInstance(peeEntryRepositoryProvider.get(), drinkEntryRepositoryProvider.get(), exerciseRepositoryProvider.get());
  }

  public static ExportCsvUseCase_Factory create(
      javax.inject.Provider<PeeEntryRepository> peeEntryRepositoryProvider,
      javax.inject.Provider<DrinkEntryRepository> drinkEntryRepositoryProvider,
      javax.inject.Provider<ExerciseRepository> exerciseRepositoryProvider) {
    return new ExportCsvUseCase_Factory(Providers.asDaggerProvider(peeEntryRepositoryProvider), Providers.asDaggerProvider(drinkEntryRepositoryProvider), Providers.asDaggerProvider(exerciseRepositoryProvider));
  }

  public static ExportCsvUseCase_Factory create(
      Provider<PeeEntryRepository> peeEntryRepositoryProvider,
      Provider<DrinkEntryRepository> drinkEntryRepositoryProvider,
      Provider<ExerciseRepository> exerciseRepositoryProvider) {
    return new ExportCsvUseCase_Factory(peeEntryRepositoryProvider, drinkEntryRepositoryProvider, exerciseRepositoryProvider);
  }

  public static ExportCsvUseCase newInstance(PeeEntryRepository peeEntryRepository,
      DrinkEntryRepository drinkEntryRepository, ExerciseRepository exerciseRepository) {
    return new ExportCsvUseCase(peeEntryRepository, drinkEntryRepository, exerciseRepository);
  }
}
