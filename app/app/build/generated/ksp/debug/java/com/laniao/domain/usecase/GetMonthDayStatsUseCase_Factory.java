package com.laniao.domain.usecase;

import com.laniao.data.local.dao.AppSettingsDao;
import com.laniao.domain.repository.DrinkEntryRepository;
import com.laniao.domain.repository.ExerciseRepository;
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
public final class GetMonthDayStatsUseCase_Factory implements Factory<GetMonthDayStatsUseCase> {
  private final Provider<PeeEntryRepository> peeEntryRepositoryProvider;

  private final Provider<DrinkEntryRepository> drinkEntryRepositoryProvider;

  private final Provider<ExerciseRepository> exerciseRepositoryProvider;

  private final Provider<VoidScheduleRepository> voidScheduleRepositoryProvider;

  private final Provider<AppSettingsDao> appSettingsDaoProvider;

  public GetMonthDayStatsUseCase_Factory(Provider<PeeEntryRepository> peeEntryRepositoryProvider,
      Provider<DrinkEntryRepository> drinkEntryRepositoryProvider,
      Provider<ExerciseRepository> exerciseRepositoryProvider,
      Provider<VoidScheduleRepository> voidScheduleRepositoryProvider,
      Provider<AppSettingsDao> appSettingsDaoProvider) {
    this.peeEntryRepositoryProvider = peeEntryRepositoryProvider;
    this.drinkEntryRepositoryProvider = drinkEntryRepositoryProvider;
    this.exerciseRepositoryProvider = exerciseRepositoryProvider;
    this.voidScheduleRepositoryProvider = voidScheduleRepositoryProvider;
    this.appSettingsDaoProvider = appSettingsDaoProvider;
  }

  @Override
  public GetMonthDayStatsUseCase get() {
    return newInstance(peeEntryRepositoryProvider.get(), drinkEntryRepositoryProvider.get(), exerciseRepositoryProvider.get(), voidScheduleRepositoryProvider.get(), appSettingsDaoProvider.get());
  }

  public static GetMonthDayStatsUseCase_Factory create(
      javax.inject.Provider<PeeEntryRepository> peeEntryRepositoryProvider,
      javax.inject.Provider<DrinkEntryRepository> drinkEntryRepositoryProvider,
      javax.inject.Provider<ExerciseRepository> exerciseRepositoryProvider,
      javax.inject.Provider<VoidScheduleRepository> voidScheduleRepositoryProvider,
      javax.inject.Provider<AppSettingsDao> appSettingsDaoProvider) {
    return new GetMonthDayStatsUseCase_Factory(Providers.asDaggerProvider(peeEntryRepositoryProvider), Providers.asDaggerProvider(drinkEntryRepositoryProvider), Providers.asDaggerProvider(exerciseRepositoryProvider), Providers.asDaggerProvider(voidScheduleRepositoryProvider), Providers.asDaggerProvider(appSettingsDaoProvider));
  }

  public static GetMonthDayStatsUseCase_Factory create(
      Provider<PeeEntryRepository> peeEntryRepositoryProvider,
      Provider<DrinkEntryRepository> drinkEntryRepositoryProvider,
      Provider<ExerciseRepository> exerciseRepositoryProvider,
      Provider<VoidScheduleRepository> voidScheduleRepositoryProvider,
      Provider<AppSettingsDao> appSettingsDaoProvider) {
    return new GetMonthDayStatsUseCase_Factory(peeEntryRepositoryProvider, drinkEntryRepositoryProvider, exerciseRepositoryProvider, voidScheduleRepositoryProvider, appSettingsDaoProvider);
  }

  public static GetMonthDayStatsUseCase newInstance(PeeEntryRepository peeEntryRepository,
      DrinkEntryRepository drinkEntryRepository, ExerciseRepository exerciseRepository,
      VoidScheduleRepository voidScheduleRepository, AppSettingsDao appSettingsDao) {
    return new GetMonthDayStatsUseCase(peeEntryRepository, drinkEntryRepository, exerciseRepository, voidScheduleRepository, appSettingsDao);
  }
}
