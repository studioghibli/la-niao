package com.laniao.presentation.viewmodel;

import com.laniao.domain.repository.DrinkEntryRepository;
import com.laniao.domain.repository.ExerciseRepository;
import com.laniao.domain.repository.ManuallyMissedTimeRepository;
import com.laniao.domain.repository.PeeEntryRepository;
import com.laniao.domain.repository.VoidScheduleRepository;
import com.laniao.domain.usecase.AddPeeEntryUseCase;
import com.laniao.domain.usecase.DeletePeeEntryUseCase;
import com.laniao.domain.usecase.GetDayTimelineUseCase;
import com.laniao.domain.usecase.GetDaysWithEntriesUseCase;
import com.laniao.domain.usecase.GetMonthDayStatsUseCase;
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
public final class CalendarViewModel_Factory implements Factory<CalendarViewModel> {
  private final Provider<GetDaysWithEntriesUseCase> getDaysWithEntriesUseCaseProvider;

  private final Provider<GetDayTimelineUseCase> getDayTimelineUseCaseProvider;

  private final Provider<GetMonthDayStatsUseCase> getMonthDayStatsUseCaseProvider;

  private final Provider<DeletePeeEntryUseCase> deleteEntryUseCaseProvider;

  private final Provider<AddPeeEntryUseCase> addPeeEntryUseCaseProvider;

  private final Provider<UpdatePeeEntryUseCase> updatePeeEntryUseCaseProvider;

  private final Provider<PeeEntryRepository> peeEntryRepositoryProvider;

  private final Provider<DrinkEntryRepository> drinkEntryRepositoryProvider;

  private final Provider<ExerciseRepository> exerciseRepositoryProvider;

  private final Provider<VoidScheduleRepository> voidScheduleRepositoryProvider;

  private final Provider<ManuallyMissedTimeRepository> manuallyMissedTimeRepositoryProvider;

  private final Provider<UndoRedoManager> undoRedoManagerProvider;

  private final Provider<Clock> clockProvider;

  private final Provider<DispatcherProvider> dispatchersProvider;

  public CalendarViewModel_Factory(
      Provider<GetDaysWithEntriesUseCase> getDaysWithEntriesUseCaseProvider,
      Provider<GetDayTimelineUseCase> getDayTimelineUseCaseProvider,
      Provider<GetMonthDayStatsUseCase> getMonthDayStatsUseCaseProvider,
      Provider<DeletePeeEntryUseCase> deleteEntryUseCaseProvider,
      Provider<AddPeeEntryUseCase> addPeeEntryUseCaseProvider,
      Provider<UpdatePeeEntryUseCase> updatePeeEntryUseCaseProvider,
      Provider<PeeEntryRepository> peeEntryRepositoryProvider,
      Provider<DrinkEntryRepository> drinkEntryRepositoryProvider,
      Provider<ExerciseRepository> exerciseRepositoryProvider,
      Provider<VoidScheduleRepository> voidScheduleRepositoryProvider,
      Provider<ManuallyMissedTimeRepository> manuallyMissedTimeRepositoryProvider,
      Provider<UndoRedoManager> undoRedoManagerProvider, Provider<Clock> clockProvider,
      Provider<DispatcherProvider> dispatchersProvider) {
    this.getDaysWithEntriesUseCaseProvider = getDaysWithEntriesUseCaseProvider;
    this.getDayTimelineUseCaseProvider = getDayTimelineUseCaseProvider;
    this.getMonthDayStatsUseCaseProvider = getMonthDayStatsUseCaseProvider;
    this.deleteEntryUseCaseProvider = deleteEntryUseCaseProvider;
    this.addPeeEntryUseCaseProvider = addPeeEntryUseCaseProvider;
    this.updatePeeEntryUseCaseProvider = updatePeeEntryUseCaseProvider;
    this.peeEntryRepositoryProvider = peeEntryRepositoryProvider;
    this.drinkEntryRepositoryProvider = drinkEntryRepositoryProvider;
    this.exerciseRepositoryProvider = exerciseRepositoryProvider;
    this.voidScheduleRepositoryProvider = voidScheduleRepositoryProvider;
    this.manuallyMissedTimeRepositoryProvider = manuallyMissedTimeRepositoryProvider;
    this.undoRedoManagerProvider = undoRedoManagerProvider;
    this.clockProvider = clockProvider;
    this.dispatchersProvider = dispatchersProvider;
  }

  @Override
  public CalendarViewModel get() {
    return newInstance(getDaysWithEntriesUseCaseProvider.get(), getDayTimelineUseCaseProvider.get(), getMonthDayStatsUseCaseProvider.get(), deleteEntryUseCaseProvider.get(), addPeeEntryUseCaseProvider.get(), updatePeeEntryUseCaseProvider.get(), peeEntryRepositoryProvider.get(), drinkEntryRepositoryProvider.get(), exerciseRepositoryProvider.get(), voidScheduleRepositoryProvider.get(), manuallyMissedTimeRepositoryProvider.get(), undoRedoManagerProvider.get(), clockProvider.get(), dispatchersProvider.get());
  }

  public static CalendarViewModel_Factory create(
      javax.inject.Provider<GetDaysWithEntriesUseCase> getDaysWithEntriesUseCaseProvider,
      javax.inject.Provider<GetDayTimelineUseCase> getDayTimelineUseCaseProvider,
      javax.inject.Provider<GetMonthDayStatsUseCase> getMonthDayStatsUseCaseProvider,
      javax.inject.Provider<DeletePeeEntryUseCase> deleteEntryUseCaseProvider,
      javax.inject.Provider<AddPeeEntryUseCase> addPeeEntryUseCaseProvider,
      javax.inject.Provider<UpdatePeeEntryUseCase> updatePeeEntryUseCaseProvider,
      javax.inject.Provider<PeeEntryRepository> peeEntryRepositoryProvider,
      javax.inject.Provider<DrinkEntryRepository> drinkEntryRepositoryProvider,
      javax.inject.Provider<ExerciseRepository> exerciseRepositoryProvider,
      javax.inject.Provider<VoidScheduleRepository> voidScheduleRepositoryProvider,
      javax.inject.Provider<ManuallyMissedTimeRepository> manuallyMissedTimeRepositoryProvider,
      javax.inject.Provider<UndoRedoManager> undoRedoManagerProvider,
      javax.inject.Provider<Clock> clockProvider,
      javax.inject.Provider<DispatcherProvider> dispatchersProvider) {
    return new CalendarViewModel_Factory(Providers.asDaggerProvider(getDaysWithEntriesUseCaseProvider), Providers.asDaggerProvider(getDayTimelineUseCaseProvider), Providers.asDaggerProvider(getMonthDayStatsUseCaseProvider), Providers.asDaggerProvider(deleteEntryUseCaseProvider), Providers.asDaggerProvider(addPeeEntryUseCaseProvider), Providers.asDaggerProvider(updatePeeEntryUseCaseProvider), Providers.asDaggerProvider(peeEntryRepositoryProvider), Providers.asDaggerProvider(drinkEntryRepositoryProvider), Providers.asDaggerProvider(exerciseRepositoryProvider), Providers.asDaggerProvider(voidScheduleRepositoryProvider), Providers.asDaggerProvider(manuallyMissedTimeRepositoryProvider), Providers.asDaggerProvider(undoRedoManagerProvider), Providers.asDaggerProvider(clockProvider), Providers.asDaggerProvider(dispatchersProvider));
  }

  public static CalendarViewModel_Factory create(
      Provider<GetDaysWithEntriesUseCase> getDaysWithEntriesUseCaseProvider,
      Provider<GetDayTimelineUseCase> getDayTimelineUseCaseProvider,
      Provider<GetMonthDayStatsUseCase> getMonthDayStatsUseCaseProvider,
      Provider<DeletePeeEntryUseCase> deleteEntryUseCaseProvider,
      Provider<AddPeeEntryUseCase> addPeeEntryUseCaseProvider,
      Provider<UpdatePeeEntryUseCase> updatePeeEntryUseCaseProvider,
      Provider<PeeEntryRepository> peeEntryRepositoryProvider,
      Provider<DrinkEntryRepository> drinkEntryRepositoryProvider,
      Provider<ExerciseRepository> exerciseRepositoryProvider,
      Provider<VoidScheduleRepository> voidScheduleRepositoryProvider,
      Provider<ManuallyMissedTimeRepository> manuallyMissedTimeRepositoryProvider,
      Provider<UndoRedoManager> undoRedoManagerProvider, Provider<Clock> clockProvider,
      Provider<DispatcherProvider> dispatchersProvider) {
    return new CalendarViewModel_Factory(getDaysWithEntriesUseCaseProvider, getDayTimelineUseCaseProvider, getMonthDayStatsUseCaseProvider, deleteEntryUseCaseProvider, addPeeEntryUseCaseProvider, updatePeeEntryUseCaseProvider, peeEntryRepositoryProvider, drinkEntryRepositoryProvider, exerciseRepositoryProvider, voidScheduleRepositoryProvider, manuallyMissedTimeRepositoryProvider, undoRedoManagerProvider, clockProvider, dispatchersProvider);
  }

  public static CalendarViewModel newInstance(GetDaysWithEntriesUseCase getDaysWithEntriesUseCase,
      GetDayTimelineUseCase getDayTimelineUseCase, GetMonthDayStatsUseCase getMonthDayStatsUseCase,
      DeletePeeEntryUseCase deleteEntryUseCase, AddPeeEntryUseCase addPeeEntryUseCase,
      UpdatePeeEntryUseCase updatePeeEntryUseCase, PeeEntryRepository peeEntryRepository,
      DrinkEntryRepository drinkEntryRepository, ExerciseRepository exerciseRepository,
      VoidScheduleRepository voidScheduleRepository,
      ManuallyMissedTimeRepository manuallyMissedTimeRepository, UndoRedoManager undoRedoManager,
      Clock clock, DispatcherProvider dispatchers) {
    return new CalendarViewModel(getDaysWithEntriesUseCase, getDayTimelineUseCase, getMonthDayStatsUseCase, deleteEntryUseCase, addPeeEntryUseCase, updatePeeEntryUseCase, peeEntryRepository, drinkEntryRepository, exerciseRepository, voidScheduleRepository, manuallyMissedTimeRepository, undoRedoManager, clock, dispatchers);
  }
}
