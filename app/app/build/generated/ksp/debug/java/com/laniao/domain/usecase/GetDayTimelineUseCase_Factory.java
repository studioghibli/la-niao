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
public final class GetDayTimelineUseCase_Factory implements Factory<GetDayTimelineUseCase> {
  private final Provider<PeeEntryRepository> entryRepositoryProvider;

  private final Provider<DrinkEntryRepository> drinkRepositoryProvider;

  private final Provider<ExerciseRepository> exerciseRepositoryProvider;

  private final Provider<VoidScheduleRepository> scheduleRepositoryProvider;

  private final Provider<ManuallyMissedTimeRepository> manuallyMissedTimeRepositoryProvider;

  public GetDayTimelineUseCase_Factory(Provider<PeeEntryRepository> entryRepositoryProvider,
      Provider<DrinkEntryRepository> drinkRepositoryProvider,
      Provider<ExerciseRepository> exerciseRepositoryProvider,
      Provider<VoidScheduleRepository> scheduleRepositoryProvider,
      Provider<ManuallyMissedTimeRepository> manuallyMissedTimeRepositoryProvider) {
    this.entryRepositoryProvider = entryRepositoryProvider;
    this.drinkRepositoryProvider = drinkRepositoryProvider;
    this.exerciseRepositoryProvider = exerciseRepositoryProvider;
    this.scheduleRepositoryProvider = scheduleRepositoryProvider;
    this.manuallyMissedTimeRepositoryProvider = manuallyMissedTimeRepositoryProvider;
  }

  @Override
  public GetDayTimelineUseCase get() {
    return newInstance(entryRepositoryProvider.get(), drinkRepositoryProvider.get(), exerciseRepositoryProvider.get(), scheduleRepositoryProvider.get(), manuallyMissedTimeRepositoryProvider.get());
  }

  public static GetDayTimelineUseCase_Factory create(
      javax.inject.Provider<PeeEntryRepository> entryRepositoryProvider,
      javax.inject.Provider<DrinkEntryRepository> drinkRepositoryProvider,
      javax.inject.Provider<ExerciseRepository> exerciseRepositoryProvider,
      javax.inject.Provider<VoidScheduleRepository> scheduleRepositoryProvider,
      javax.inject.Provider<ManuallyMissedTimeRepository> manuallyMissedTimeRepositoryProvider) {
    return new GetDayTimelineUseCase_Factory(Providers.asDaggerProvider(entryRepositoryProvider), Providers.asDaggerProvider(drinkRepositoryProvider), Providers.asDaggerProvider(exerciseRepositoryProvider), Providers.asDaggerProvider(scheduleRepositoryProvider), Providers.asDaggerProvider(manuallyMissedTimeRepositoryProvider));
  }

  public static GetDayTimelineUseCase_Factory create(
      Provider<PeeEntryRepository> entryRepositoryProvider,
      Provider<DrinkEntryRepository> drinkRepositoryProvider,
      Provider<ExerciseRepository> exerciseRepositoryProvider,
      Provider<VoidScheduleRepository> scheduleRepositoryProvider,
      Provider<ManuallyMissedTimeRepository> manuallyMissedTimeRepositoryProvider) {
    return new GetDayTimelineUseCase_Factory(entryRepositoryProvider, drinkRepositoryProvider, exerciseRepositoryProvider, scheduleRepositoryProvider, manuallyMissedTimeRepositoryProvider);
  }

  public static GetDayTimelineUseCase newInstance(PeeEntryRepository entryRepository,
      DrinkEntryRepository drinkRepository, ExerciseRepository exerciseRepository,
      VoidScheduleRepository scheduleRepository,
      ManuallyMissedTimeRepository manuallyMissedTimeRepository) {
    return new GetDayTimelineUseCase(entryRepository, drinkRepository, exerciseRepository, scheduleRepository, manuallyMissedTimeRepository);
  }
}
