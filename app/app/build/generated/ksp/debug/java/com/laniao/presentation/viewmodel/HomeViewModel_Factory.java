package com.laniao.presentation.viewmodel;

import com.laniao.domain.repository.DrinkEntryRepository;
import com.laniao.domain.repository.ExerciseRepository;
import com.laniao.domain.repository.PeeEntryRepository;
import com.laniao.domain.usecase.AddDrinkEntryUseCase;
import com.laniao.domain.usecase.AddPeeEntryUseCase;
import com.laniao.domain.usecase.CompleteExerciseUseCase;
import com.laniao.domain.usecase.DeleteDrinkEntryUseCase;
import com.laniao.domain.usecase.DeletePeeEntryUseCase;
import com.laniao.domain.usecase.GetExerciseStreakUseCase;
import com.laniao.domain.usecase.GetHydrationStreakUseCase;
import com.laniao.domain.usecase.GetRecentEntriesUseCase;
import com.laniao.domain.usecase.GetScheduleProgressUseCase;
import com.laniao.domain.usecase.GetTodayDrinksUseCase;
import com.laniao.domain.usecase.GetTodayExerciseStatusUseCase;
import com.laniao.domain.usecase.GetTodaySummaryUseCase;
import com.laniao.domain.usecase.UndoRedoManager;
import com.laniao.domain.usecase.UpdatePeeEntryUseCase;
import com.laniao.util.Clock;
import com.laniao.util.DispatcherProvider;
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
public final class HomeViewModel_Factory implements Factory<HomeViewModel> {
  private final Provider<AddPeeEntryUseCase> addPeeEntryUseCaseProvider;

  private final Provider<UpdatePeeEntryUseCase> updatePeeEntryUseCaseProvider;

  private final Provider<DeletePeeEntryUseCase> deletePeeEntryUseCaseProvider;

  private final Provider<GetTodaySummaryUseCase> getTodaySummaryUseCaseProvider;

  private final Provider<GetRecentEntriesUseCase> getRecentEntriesUseCaseProvider;

  private final Provider<AddDrinkEntryUseCase> addDrinkEntryUseCaseProvider;

  private final Provider<DeleteDrinkEntryUseCase> deleteDrinkEntryUseCaseProvider;

  private final Provider<GetTodayDrinksUseCase> getTodayDrinksUseCaseProvider;

  private final Provider<DrinkEntryRepository> drinkEntryRepositoryProvider;

  private final Provider<PeeEntryRepository> peeEntryRepositoryProvider;

  private final Provider<ExerciseRepository> exerciseRepositoryProvider;

  private final Provider<GetTodayExerciseStatusUseCase> getTodayExerciseStatusUseCaseProvider;

  private final Provider<CompleteExerciseUseCase> completeExerciseUseCaseProvider;

  private final Provider<GetExerciseStreakUseCase> getExerciseStreakUseCaseProvider;

  private final Provider<GetHydrationStreakUseCase> getHydrationStreakUseCaseProvider;

  private final Provider<GetScheduleProgressUseCase> getScheduleProgressUseCaseProvider;

  private final Provider<UndoRedoManager> undoRedoManagerProvider;

  private final Provider<Clock> clockProvider;

  private final Provider<DispatcherProvider> dispatcherProvider;

  public HomeViewModel_Factory(Provider<AddPeeEntryUseCase> addPeeEntryUseCaseProvider,
      Provider<UpdatePeeEntryUseCase> updatePeeEntryUseCaseProvider,
      Provider<DeletePeeEntryUseCase> deletePeeEntryUseCaseProvider,
      Provider<GetTodaySummaryUseCase> getTodaySummaryUseCaseProvider,
      Provider<GetRecentEntriesUseCase> getRecentEntriesUseCaseProvider,
      Provider<AddDrinkEntryUseCase> addDrinkEntryUseCaseProvider,
      Provider<DeleteDrinkEntryUseCase> deleteDrinkEntryUseCaseProvider,
      Provider<GetTodayDrinksUseCase> getTodayDrinksUseCaseProvider,
      Provider<DrinkEntryRepository> drinkEntryRepositoryProvider,
      Provider<PeeEntryRepository> peeEntryRepositoryProvider,
      Provider<ExerciseRepository> exerciseRepositoryProvider,
      Provider<GetTodayExerciseStatusUseCase> getTodayExerciseStatusUseCaseProvider,
      Provider<CompleteExerciseUseCase> completeExerciseUseCaseProvider,
      Provider<GetExerciseStreakUseCase> getExerciseStreakUseCaseProvider,
      Provider<GetHydrationStreakUseCase> getHydrationStreakUseCaseProvider,
      Provider<GetScheduleProgressUseCase> getScheduleProgressUseCaseProvider,
      Provider<UndoRedoManager> undoRedoManagerProvider, Provider<Clock> clockProvider,
      Provider<DispatcherProvider> dispatcherProvider) {
    this.addPeeEntryUseCaseProvider = addPeeEntryUseCaseProvider;
    this.updatePeeEntryUseCaseProvider = updatePeeEntryUseCaseProvider;
    this.deletePeeEntryUseCaseProvider = deletePeeEntryUseCaseProvider;
    this.getTodaySummaryUseCaseProvider = getTodaySummaryUseCaseProvider;
    this.getRecentEntriesUseCaseProvider = getRecentEntriesUseCaseProvider;
    this.addDrinkEntryUseCaseProvider = addDrinkEntryUseCaseProvider;
    this.deleteDrinkEntryUseCaseProvider = deleteDrinkEntryUseCaseProvider;
    this.getTodayDrinksUseCaseProvider = getTodayDrinksUseCaseProvider;
    this.drinkEntryRepositoryProvider = drinkEntryRepositoryProvider;
    this.peeEntryRepositoryProvider = peeEntryRepositoryProvider;
    this.exerciseRepositoryProvider = exerciseRepositoryProvider;
    this.getTodayExerciseStatusUseCaseProvider = getTodayExerciseStatusUseCaseProvider;
    this.completeExerciseUseCaseProvider = completeExerciseUseCaseProvider;
    this.getExerciseStreakUseCaseProvider = getExerciseStreakUseCaseProvider;
    this.getHydrationStreakUseCaseProvider = getHydrationStreakUseCaseProvider;
    this.getScheduleProgressUseCaseProvider = getScheduleProgressUseCaseProvider;
    this.undoRedoManagerProvider = undoRedoManagerProvider;
    this.clockProvider = clockProvider;
    this.dispatcherProvider = dispatcherProvider;
  }

  @Override
  public HomeViewModel get() {
    return newInstance(addPeeEntryUseCaseProvider.get(), updatePeeEntryUseCaseProvider.get(), deletePeeEntryUseCaseProvider.get(), getTodaySummaryUseCaseProvider.get(), getRecentEntriesUseCaseProvider.get(), addDrinkEntryUseCaseProvider.get(), deleteDrinkEntryUseCaseProvider.get(), getTodayDrinksUseCaseProvider.get(), drinkEntryRepositoryProvider.get(), peeEntryRepositoryProvider.get(), exerciseRepositoryProvider.get(), getTodayExerciseStatusUseCaseProvider.get(), completeExerciseUseCaseProvider.get(), getExerciseStreakUseCaseProvider.get(), getHydrationStreakUseCaseProvider.get(), getScheduleProgressUseCaseProvider.get(), undoRedoManagerProvider.get(), clockProvider.get(), dispatcherProvider.get());
  }

  public static HomeViewModel_Factory create(
      javax.inject.Provider<AddPeeEntryUseCase> addPeeEntryUseCaseProvider,
      javax.inject.Provider<UpdatePeeEntryUseCase> updatePeeEntryUseCaseProvider,
      javax.inject.Provider<DeletePeeEntryUseCase> deletePeeEntryUseCaseProvider,
      javax.inject.Provider<GetTodaySummaryUseCase> getTodaySummaryUseCaseProvider,
      javax.inject.Provider<GetRecentEntriesUseCase> getRecentEntriesUseCaseProvider,
      javax.inject.Provider<AddDrinkEntryUseCase> addDrinkEntryUseCaseProvider,
      javax.inject.Provider<DeleteDrinkEntryUseCase> deleteDrinkEntryUseCaseProvider,
      javax.inject.Provider<GetTodayDrinksUseCase> getTodayDrinksUseCaseProvider,
      javax.inject.Provider<DrinkEntryRepository> drinkEntryRepositoryProvider,
      javax.inject.Provider<PeeEntryRepository> peeEntryRepositoryProvider,
      javax.inject.Provider<ExerciseRepository> exerciseRepositoryProvider,
      javax.inject.Provider<GetTodayExerciseStatusUseCase> getTodayExerciseStatusUseCaseProvider,
      javax.inject.Provider<CompleteExerciseUseCase> completeExerciseUseCaseProvider,
      javax.inject.Provider<GetExerciseStreakUseCase> getExerciseStreakUseCaseProvider,
      javax.inject.Provider<GetHydrationStreakUseCase> getHydrationStreakUseCaseProvider,
      javax.inject.Provider<GetScheduleProgressUseCase> getScheduleProgressUseCaseProvider,
      javax.inject.Provider<UndoRedoManager> undoRedoManagerProvider,
      javax.inject.Provider<Clock> clockProvider,
      javax.inject.Provider<DispatcherProvider> dispatcherProvider) {
    return new HomeViewModel_Factory(Providers.asDaggerProvider(addPeeEntryUseCaseProvider), Providers.asDaggerProvider(updatePeeEntryUseCaseProvider), Providers.asDaggerProvider(deletePeeEntryUseCaseProvider), Providers.asDaggerProvider(getTodaySummaryUseCaseProvider), Providers.asDaggerProvider(getRecentEntriesUseCaseProvider), Providers.asDaggerProvider(addDrinkEntryUseCaseProvider), Providers.asDaggerProvider(deleteDrinkEntryUseCaseProvider), Providers.asDaggerProvider(getTodayDrinksUseCaseProvider), Providers.asDaggerProvider(drinkEntryRepositoryProvider), Providers.asDaggerProvider(peeEntryRepositoryProvider), Providers.asDaggerProvider(exerciseRepositoryProvider), Providers.asDaggerProvider(getTodayExerciseStatusUseCaseProvider), Providers.asDaggerProvider(completeExerciseUseCaseProvider), Providers.asDaggerProvider(getExerciseStreakUseCaseProvider), Providers.asDaggerProvider(getHydrationStreakUseCaseProvider), Providers.asDaggerProvider(getScheduleProgressUseCaseProvider), Providers.asDaggerProvider(undoRedoManagerProvider), Providers.asDaggerProvider(clockProvider), Providers.asDaggerProvider(dispatcherProvider));
  }

  public static HomeViewModel_Factory create(
      Provider<AddPeeEntryUseCase> addPeeEntryUseCaseProvider,
      Provider<UpdatePeeEntryUseCase> updatePeeEntryUseCaseProvider,
      Provider<DeletePeeEntryUseCase> deletePeeEntryUseCaseProvider,
      Provider<GetTodaySummaryUseCase> getTodaySummaryUseCaseProvider,
      Provider<GetRecentEntriesUseCase> getRecentEntriesUseCaseProvider,
      Provider<AddDrinkEntryUseCase> addDrinkEntryUseCaseProvider,
      Provider<DeleteDrinkEntryUseCase> deleteDrinkEntryUseCaseProvider,
      Provider<GetTodayDrinksUseCase> getTodayDrinksUseCaseProvider,
      Provider<DrinkEntryRepository> drinkEntryRepositoryProvider,
      Provider<PeeEntryRepository> peeEntryRepositoryProvider,
      Provider<ExerciseRepository> exerciseRepositoryProvider,
      Provider<GetTodayExerciseStatusUseCase> getTodayExerciseStatusUseCaseProvider,
      Provider<CompleteExerciseUseCase> completeExerciseUseCaseProvider,
      Provider<GetExerciseStreakUseCase> getExerciseStreakUseCaseProvider,
      Provider<GetHydrationStreakUseCase> getHydrationStreakUseCaseProvider,
      Provider<GetScheduleProgressUseCase> getScheduleProgressUseCaseProvider,
      Provider<UndoRedoManager> undoRedoManagerProvider, Provider<Clock> clockProvider,
      Provider<DispatcherProvider> dispatcherProvider) {
    return new HomeViewModel_Factory(addPeeEntryUseCaseProvider, updatePeeEntryUseCaseProvider, deletePeeEntryUseCaseProvider, getTodaySummaryUseCaseProvider, getRecentEntriesUseCaseProvider, addDrinkEntryUseCaseProvider, deleteDrinkEntryUseCaseProvider, getTodayDrinksUseCaseProvider, drinkEntryRepositoryProvider, peeEntryRepositoryProvider, exerciseRepositoryProvider, getTodayExerciseStatusUseCaseProvider, completeExerciseUseCaseProvider, getExerciseStreakUseCaseProvider, getHydrationStreakUseCaseProvider, getScheduleProgressUseCaseProvider, undoRedoManagerProvider, clockProvider, dispatcherProvider);
  }

  public static HomeViewModel newInstance(AddPeeEntryUseCase addPeeEntryUseCase,
      UpdatePeeEntryUseCase updatePeeEntryUseCase, DeletePeeEntryUseCase deletePeeEntryUseCase,
      GetTodaySummaryUseCase getTodaySummaryUseCase,
      GetRecentEntriesUseCase getRecentEntriesUseCase, AddDrinkEntryUseCase addDrinkEntryUseCase,
      DeleteDrinkEntryUseCase deleteDrinkEntryUseCase, GetTodayDrinksUseCase getTodayDrinksUseCase,
      DrinkEntryRepository drinkEntryRepository, PeeEntryRepository peeEntryRepository,
      ExerciseRepository exerciseRepository,
      GetTodayExerciseStatusUseCase getTodayExerciseStatusUseCase,
      CompleteExerciseUseCase completeExerciseUseCase,
      GetExerciseStreakUseCase getExerciseStreakUseCase,
      GetHydrationStreakUseCase getHydrationStreakUseCase,
      GetScheduleProgressUseCase getScheduleProgressUseCase, UndoRedoManager undoRedoManager,
      Clock clock, DispatcherProvider dispatcherProvider) {
    return new HomeViewModel(addPeeEntryUseCase, updatePeeEntryUseCase, deletePeeEntryUseCase, getTodaySummaryUseCase, getRecentEntriesUseCase, addDrinkEntryUseCase, deleteDrinkEntryUseCase, getTodayDrinksUseCase, drinkEntryRepository, peeEntryRepository, exerciseRepository, getTodayExerciseStatusUseCase, completeExerciseUseCase, getExerciseStreakUseCase, getHydrationStreakUseCase, getScheduleProgressUseCase, undoRedoManager, clock, dispatcherProvider);
  }
}
