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
public final class GetTodaySummaryUseCase_Factory implements Factory<GetTodaySummaryUseCase> {
  private final Provider<PeeEntryRepository> entryRepositoryProvider;

  private final Provider<VoidScheduleRepository> scheduleRepositoryProvider;

  private final Provider<Clock> clockProvider;

  public GetTodaySummaryUseCase_Factory(Provider<PeeEntryRepository> entryRepositoryProvider,
      Provider<VoidScheduleRepository> scheduleRepositoryProvider, Provider<Clock> clockProvider) {
    this.entryRepositoryProvider = entryRepositoryProvider;
    this.scheduleRepositoryProvider = scheduleRepositoryProvider;
    this.clockProvider = clockProvider;
  }

  @Override
  public GetTodaySummaryUseCase get() {
    return newInstance(entryRepositoryProvider.get(), scheduleRepositoryProvider.get(), clockProvider.get());
  }

  public static GetTodaySummaryUseCase_Factory create(
      javax.inject.Provider<PeeEntryRepository> entryRepositoryProvider,
      javax.inject.Provider<VoidScheduleRepository> scheduleRepositoryProvider,
      javax.inject.Provider<Clock> clockProvider) {
    return new GetTodaySummaryUseCase_Factory(Providers.asDaggerProvider(entryRepositoryProvider), Providers.asDaggerProvider(scheduleRepositoryProvider), Providers.asDaggerProvider(clockProvider));
  }

  public static GetTodaySummaryUseCase_Factory create(
      Provider<PeeEntryRepository> entryRepositoryProvider,
      Provider<VoidScheduleRepository> scheduleRepositoryProvider, Provider<Clock> clockProvider) {
    return new GetTodaySummaryUseCase_Factory(entryRepositoryProvider, scheduleRepositoryProvider, clockProvider);
  }

  public static GetTodaySummaryUseCase newInstance(PeeEntryRepository entryRepository,
      VoidScheduleRepository scheduleRepository, Clock clock) {
    return new GetTodaySummaryUseCase(entryRepository, scheduleRepository, clock);
  }
}
