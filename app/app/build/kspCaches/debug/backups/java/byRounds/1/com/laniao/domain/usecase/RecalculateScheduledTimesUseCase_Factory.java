package com.laniao.domain.usecase;

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
public final class RecalculateScheduledTimesUseCase_Factory implements Factory<RecalculateScheduledTimesUseCase> {
  private final Provider<PeeEntryRepository> entryRepositoryProvider;

  private final Provider<VoidScheduleRepository> scheduleRepositoryProvider;

  public RecalculateScheduledTimesUseCase_Factory(
      Provider<PeeEntryRepository> entryRepositoryProvider,
      Provider<VoidScheduleRepository> scheduleRepositoryProvider) {
    this.entryRepositoryProvider = entryRepositoryProvider;
    this.scheduleRepositoryProvider = scheduleRepositoryProvider;
  }

  @Override
  public RecalculateScheduledTimesUseCase get() {
    return newInstance(entryRepositoryProvider.get(), scheduleRepositoryProvider.get());
  }

  public static RecalculateScheduledTimesUseCase_Factory create(
      javax.inject.Provider<PeeEntryRepository> entryRepositoryProvider,
      javax.inject.Provider<VoidScheduleRepository> scheduleRepositoryProvider) {
    return new RecalculateScheduledTimesUseCase_Factory(Providers.asDaggerProvider(entryRepositoryProvider), Providers.asDaggerProvider(scheduleRepositoryProvider));
  }

  public static RecalculateScheduledTimesUseCase_Factory create(
      Provider<PeeEntryRepository> entryRepositoryProvider,
      Provider<VoidScheduleRepository> scheduleRepositoryProvider) {
    return new RecalculateScheduledTimesUseCase_Factory(entryRepositoryProvider, scheduleRepositoryProvider);
  }

  public static RecalculateScheduledTimesUseCase newInstance(PeeEntryRepository entryRepository,
      VoidScheduleRepository scheduleRepository) {
    return new RecalculateScheduledTimesUseCase(entryRepository, scheduleRepository);
  }
}
