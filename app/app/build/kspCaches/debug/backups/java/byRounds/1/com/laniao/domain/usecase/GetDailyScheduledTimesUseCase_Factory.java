package com.laniao.domain.usecase;

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
public final class GetDailyScheduledTimesUseCase_Factory implements Factory<GetDailyScheduledTimesUseCase> {
  private final Provider<VoidScheduleRepository> scheduleRepositoryProvider;

  private final Provider<PeeEntryRepository> entryRepositoryProvider;

  private final Provider<Clock> clockProvider;

  public GetDailyScheduledTimesUseCase_Factory(
      Provider<VoidScheduleRepository> scheduleRepositoryProvider,
      Provider<PeeEntryRepository> entryRepositoryProvider, Provider<Clock> clockProvider) {
    this.scheduleRepositoryProvider = scheduleRepositoryProvider;
    this.entryRepositoryProvider = entryRepositoryProvider;
    this.clockProvider = clockProvider;
  }

  @Override
  public GetDailyScheduledTimesUseCase get() {
    return newInstance(scheduleRepositoryProvider.get(), entryRepositoryProvider.get(), clockProvider.get());
  }

  public static GetDailyScheduledTimesUseCase_Factory create(
      javax.inject.Provider<VoidScheduleRepository> scheduleRepositoryProvider,
      javax.inject.Provider<PeeEntryRepository> entryRepositoryProvider,
      javax.inject.Provider<Clock> clockProvider) {
    return new GetDailyScheduledTimesUseCase_Factory(Providers.asDaggerProvider(scheduleRepositoryProvider), Providers.asDaggerProvider(entryRepositoryProvider), Providers.asDaggerProvider(clockProvider));
  }

  public static GetDailyScheduledTimesUseCase_Factory create(
      Provider<VoidScheduleRepository> scheduleRepositoryProvider,
      Provider<PeeEntryRepository> entryRepositoryProvider, Provider<Clock> clockProvider) {
    return new GetDailyScheduledTimesUseCase_Factory(scheduleRepositoryProvider, entryRepositoryProvider, clockProvider);
  }

  public static GetDailyScheduledTimesUseCase newInstance(VoidScheduleRepository scheduleRepository,
      PeeEntryRepository entryRepository, Clock clock) {
    return new GetDailyScheduledTimesUseCase(scheduleRepository, entryRepository, clock);
  }
}
