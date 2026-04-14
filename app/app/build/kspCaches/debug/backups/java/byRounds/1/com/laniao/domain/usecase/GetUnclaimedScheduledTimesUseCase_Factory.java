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
public final class GetUnclaimedScheduledTimesUseCase_Factory implements Factory<GetUnclaimedScheduledTimesUseCase> {
  private final Provider<VoidScheduleRepository> scheduleRepositoryProvider;

  private final Provider<PeeEntryRepository> entryRepositoryProvider;

  public GetUnclaimedScheduledTimesUseCase_Factory(
      Provider<VoidScheduleRepository> scheduleRepositoryProvider,
      Provider<PeeEntryRepository> entryRepositoryProvider) {
    this.scheduleRepositoryProvider = scheduleRepositoryProvider;
    this.entryRepositoryProvider = entryRepositoryProvider;
  }

  @Override
  public GetUnclaimedScheduledTimesUseCase get() {
    return newInstance(scheduleRepositoryProvider.get(), entryRepositoryProvider.get());
  }

  public static GetUnclaimedScheduledTimesUseCase_Factory create(
      javax.inject.Provider<VoidScheduleRepository> scheduleRepositoryProvider,
      javax.inject.Provider<PeeEntryRepository> entryRepositoryProvider) {
    return new GetUnclaimedScheduledTimesUseCase_Factory(Providers.asDaggerProvider(scheduleRepositoryProvider), Providers.asDaggerProvider(entryRepositoryProvider));
  }

  public static GetUnclaimedScheduledTimesUseCase_Factory create(
      Provider<VoidScheduleRepository> scheduleRepositoryProvider,
      Provider<PeeEntryRepository> entryRepositoryProvider) {
    return new GetUnclaimedScheduledTimesUseCase_Factory(scheduleRepositoryProvider, entryRepositoryProvider);
  }

  public static GetUnclaimedScheduledTimesUseCase newInstance(
      VoidScheduleRepository scheduleRepository, PeeEntryRepository entryRepository) {
    return new GetUnclaimedScheduledTimesUseCase(scheduleRepository, entryRepository);
  }
}
