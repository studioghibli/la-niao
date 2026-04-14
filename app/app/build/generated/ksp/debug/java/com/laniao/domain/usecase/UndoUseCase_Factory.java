package com.laniao.domain.usecase;

import com.laniao.domain.repository.DrinkEntryRepository;
import com.laniao.domain.repository.ExerciseRepository;
import com.laniao.domain.repository.ManuallyMissedTimeRepository;
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
public final class UndoUseCase_Factory implements Factory<UndoUseCase> {
  private final Provider<UndoRedoManager> undoRedoManagerProvider;

  private final Provider<PeeEntryRepository> peeEntryRepositoryProvider;

  private final Provider<DrinkEntryRepository> drinkEntryRepositoryProvider;

  private final Provider<ExerciseRepository> exerciseRepositoryProvider;

  private final Provider<VoidScheduleRepository> voidScheduleRepositoryProvider;

  private final Provider<ManuallyMissedTimeRepository> manuallyMissedTimeRepositoryProvider;

  public UndoUseCase_Factory(Provider<UndoRedoManager> undoRedoManagerProvider,
      Provider<PeeEntryRepository> peeEntryRepositoryProvider,
      Provider<DrinkEntryRepository> drinkEntryRepositoryProvider,
      Provider<ExerciseRepository> exerciseRepositoryProvider,
      Provider<VoidScheduleRepository> voidScheduleRepositoryProvider,
      Provider<ManuallyMissedTimeRepository> manuallyMissedTimeRepositoryProvider) {
    this.undoRedoManagerProvider = undoRedoManagerProvider;
    this.peeEntryRepositoryProvider = peeEntryRepositoryProvider;
    this.drinkEntryRepositoryProvider = drinkEntryRepositoryProvider;
    this.exerciseRepositoryProvider = exerciseRepositoryProvider;
    this.voidScheduleRepositoryProvider = voidScheduleRepositoryProvider;
    this.manuallyMissedTimeRepositoryProvider = manuallyMissedTimeRepositoryProvider;
  }

  @Override
  public UndoUseCase get() {
    return newInstance(undoRedoManagerProvider.get(), peeEntryRepositoryProvider.get(), drinkEntryRepositoryProvider.get(), exerciseRepositoryProvider.get(), voidScheduleRepositoryProvider.get(), manuallyMissedTimeRepositoryProvider.get());
  }

  public static UndoUseCase_Factory create(
      javax.inject.Provider<UndoRedoManager> undoRedoManagerProvider,
      javax.inject.Provider<PeeEntryRepository> peeEntryRepositoryProvider,
      javax.inject.Provider<DrinkEntryRepository> drinkEntryRepositoryProvider,
      javax.inject.Provider<ExerciseRepository> exerciseRepositoryProvider,
      javax.inject.Provider<VoidScheduleRepository> voidScheduleRepositoryProvider,
      javax.inject.Provider<ManuallyMissedTimeRepository> manuallyMissedTimeRepositoryProvider) {
    return new UndoUseCase_Factory(Providers.asDaggerProvider(undoRedoManagerProvider), Providers.asDaggerProvider(peeEntryRepositoryProvider), Providers.asDaggerProvider(drinkEntryRepositoryProvider), Providers.asDaggerProvider(exerciseRepositoryProvider), Providers.asDaggerProvider(voidScheduleRepositoryProvider), Providers.asDaggerProvider(manuallyMissedTimeRepositoryProvider));
  }

  public static UndoUseCase_Factory create(Provider<UndoRedoManager> undoRedoManagerProvider,
      Provider<PeeEntryRepository> peeEntryRepositoryProvider,
      Provider<DrinkEntryRepository> drinkEntryRepositoryProvider,
      Provider<ExerciseRepository> exerciseRepositoryProvider,
      Provider<VoidScheduleRepository> voidScheduleRepositoryProvider,
      Provider<ManuallyMissedTimeRepository> manuallyMissedTimeRepositoryProvider) {
    return new UndoUseCase_Factory(undoRedoManagerProvider, peeEntryRepositoryProvider, drinkEntryRepositoryProvider, exerciseRepositoryProvider, voidScheduleRepositoryProvider, manuallyMissedTimeRepositoryProvider);
  }

  public static UndoUseCase newInstance(UndoRedoManager undoRedoManager,
      PeeEntryRepository peeEntryRepository, DrinkEntryRepository drinkEntryRepository,
      ExerciseRepository exerciseRepository, VoidScheduleRepository voidScheduleRepository,
      ManuallyMissedTimeRepository manuallyMissedTimeRepository) {
    return new UndoUseCase(undoRedoManager, peeEntryRepository, drinkEntryRepository, exerciseRepository, voidScheduleRepository, manuallyMissedTimeRepository);
  }
}
