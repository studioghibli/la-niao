package com.laniao.domain.usecase;

import com.laniao.domain.repository.ManuallyMissedTimeRepository;
import com.laniao.domain.repository.PeeEntryRepository;
import com.laniao.domain.repository.VoidScheduleRepository;
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
public final class GetScheduleProgressUseCase_Factory implements Factory<GetScheduleProgressUseCase> {
  private final Provider<VoidScheduleRepository> scheduleRepositoryProvider;

  private final Provider<PeeEntryRepository> entryRepositoryProvider;

  private final Provider<ManuallyMissedTimeRepository> manuallyMissedTimeRepositoryProvider;

  private final Provider<Clock> clockProvider;

  public GetScheduleProgressUseCase_Factory(
      Provider<VoidScheduleRepository> scheduleRepositoryProvider,
      Provider<PeeEntryRepository> entryRepositoryProvider,
      Provider<ManuallyMissedTimeRepository> manuallyMissedTimeRepositoryProvider,
      Provider<Clock> clockProvider) {
    this.scheduleRepositoryProvider = scheduleRepositoryProvider;
    this.entryRepositoryProvider = entryRepositoryProvider;
    this.manuallyMissedTimeRepositoryProvider = manuallyMissedTimeRepositoryProvider;
    this.clockProvider = clockProvider;
  }

  @Override
  public GetScheduleProgressUseCase get() {
    return newInstance(scheduleRepositoryProvider.get(), entryRepositoryProvider.get(), manuallyMissedTimeRepositoryProvider.get(), clockProvider.get());
  }

  public static GetScheduleProgressUseCase_Factory create(
      javax.inject.Provider<VoidScheduleRepository> scheduleRepositoryProvider,
      javax.inject.Provider<PeeEntryRepository> entryRepositoryProvider,
      javax.inject.Provider<ManuallyMissedTimeRepository> manuallyMissedTimeRepositoryProvider,
      javax.inject.Provider<Clock> clockProvider) {
    return new GetScheduleProgressUseCase_Factory(Providers.asDaggerProvider(scheduleRepositoryProvider), Providers.asDaggerProvider(entryRepositoryProvider), Providers.asDaggerProvider(manuallyMissedTimeRepositoryProvider), Providers.asDaggerProvider(clockProvider));
  }

  public static GetScheduleProgressUseCase_Factory create(
      Provider<VoidScheduleRepository> scheduleRepositoryProvider,
      Provider<PeeEntryRepository> entryRepositoryProvider,
      Provider<ManuallyMissedTimeRepository> manuallyMissedTimeRepositoryProvider,
      Provider<Clock> clockProvider) {
    return new GetScheduleProgressUseCase_Factory(scheduleRepositoryProvider, entryRepositoryProvider, manuallyMissedTimeRepositoryProvider, clockProvider);
  }

  public static GetScheduleProgressUseCase newInstance(VoidScheduleRepository scheduleRepository,
      PeeEntryRepository entryRepository, ManuallyMissedTimeRepository manuallyMissedTimeRepository,
      Clock clock) {
    return new GetScheduleProgressUseCase(scheduleRepository, entryRepository, manuallyMissedTimeRepository, clock);
  }
}
