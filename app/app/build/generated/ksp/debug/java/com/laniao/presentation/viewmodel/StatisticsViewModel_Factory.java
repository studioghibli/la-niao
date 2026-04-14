package com.laniao.presentation.viewmodel;

import com.laniao.data.local.dao.AppSettingsDao;
import com.laniao.domain.usecase.GetAverageVoidGapUseCase;
import com.laniao.domain.usecase.GetExerciseCompletionUseCase;
import com.laniao.domain.usecase.GetLiquidIntakeUseCase;
import com.laniao.domain.usecase.GetScheduleAdherenceUseCase;
import com.laniao.domain.usecase.GetVoidFrequencyUseCase;
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
public final class StatisticsViewModel_Factory implements Factory<StatisticsViewModel> {
  private final Provider<GetVoidFrequencyUseCase> getVoidFrequencyUseCaseProvider;

  private final Provider<GetScheduleAdherenceUseCase> getScheduleAdherenceUseCaseProvider;

  private final Provider<GetLiquidIntakeUseCase> getLiquidIntakeUseCaseProvider;

  private final Provider<GetExerciseCompletionUseCase> getExerciseCompletionUseCaseProvider;

  private final Provider<GetAverageVoidGapUseCase> getAverageVoidGapUseCaseProvider;

  private final Provider<AppSettingsDao> appSettingsDaoProvider;

  private final Provider<Clock> clockProvider;

  private final Provider<DispatcherProvider> dispatchersProvider;

  public StatisticsViewModel_Factory(
      Provider<GetVoidFrequencyUseCase> getVoidFrequencyUseCaseProvider,
      Provider<GetScheduleAdherenceUseCase> getScheduleAdherenceUseCaseProvider,
      Provider<GetLiquidIntakeUseCase> getLiquidIntakeUseCaseProvider,
      Provider<GetExerciseCompletionUseCase> getExerciseCompletionUseCaseProvider,
      Provider<GetAverageVoidGapUseCase> getAverageVoidGapUseCaseProvider,
      Provider<AppSettingsDao> appSettingsDaoProvider, Provider<Clock> clockProvider,
      Provider<DispatcherProvider> dispatchersProvider) {
    this.getVoidFrequencyUseCaseProvider = getVoidFrequencyUseCaseProvider;
    this.getScheduleAdherenceUseCaseProvider = getScheduleAdherenceUseCaseProvider;
    this.getLiquidIntakeUseCaseProvider = getLiquidIntakeUseCaseProvider;
    this.getExerciseCompletionUseCaseProvider = getExerciseCompletionUseCaseProvider;
    this.getAverageVoidGapUseCaseProvider = getAverageVoidGapUseCaseProvider;
    this.appSettingsDaoProvider = appSettingsDaoProvider;
    this.clockProvider = clockProvider;
    this.dispatchersProvider = dispatchersProvider;
  }

  @Override
  public StatisticsViewModel get() {
    return newInstance(getVoidFrequencyUseCaseProvider.get(), getScheduleAdherenceUseCaseProvider.get(), getLiquidIntakeUseCaseProvider.get(), getExerciseCompletionUseCaseProvider.get(), getAverageVoidGapUseCaseProvider.get(), appSettingsDaoProvider.get(), clockProvider.get(), dispatchersProvider.get());
  }

  public static StatisticsViewModel_Factory create(
      javax.inject.Provider<GetVoidFrequencyUseCase> getVoidFrequencyUseCaseProvider,
      javax.inject.Provider<GetScheduleAdherenceUseCase> getScheduleAdherenceUseCaseProvider,
      javax.inject.Provider<GetLiquidIntakeUseCase> getLiquidIntakeUseCaseProvider,
      javax.inject.Provider<GetExerciseCompletionUseCase> getExerciseCompletionUseCaseProvider,
      javax.inject.Provider<GetAverageVoidGapUseCase> getAverageVoidGapUseCaseProvider,
      javax.inject.Provider<AppSettingsDao> appSettingsDaoProvider,
      javax.inject.Provider<Clock> clockProvider,
      javax.inject.Provider<DispatcherProvider> dispatchersProvider) {
    return new StatisticsViewModel_Factory(Providers.asDaggerProvider(getVoidFrequencyUseCaseProvider), Providers.asDaggerProvider(getScheduleAdherenceUseCaseProvider), Providers.asDaggerProvider(getLiquidIntakeUseCaseProvider), Providers.asDaggerProvider(getExerciseCompletionUseCaseProvider), Providers.asDaggerProvider(getAverageVoidGapUseCaseProvider), Providers.asDaggerProvider(appSettingsDaoProvider), Providers.asDaggerProvider(clockProvider), Providers.asDaggerProvider(dispatchersProvider));
  }

  public static StatisticsViewModel_Factory create(
      Provider<GetVoidFrequencyUseCase> getVoidFrequencyUseCaseProvider,
      Provider<GetScheduleAdherenceUseCase> getScheduleAdherenceUseCaseProvider,
      Provider<GetLiquidIntakeUseCase> getLiquidIntakeUseCaseProvider,
      Provider<GetExerciseCompletionUseCase> getExerciseCompletionUseCaseProvider,
      Provider<GetAverageVoidGapUseCase> getAverageVoidGapUseCaseProvider,
      Provider<AppSettingsDao> appSettingsDaoProvider, Provider<Clock> clockProvider,
      Provider<DispatcherProvider> dispatchersProvider) {
    return new StatisticsViewModel_Factory(getVoidFrequencyUseCaseProvider, getScheduleAdherenceUseCaseProvider, getLiquidIntakeUseCaseProvider, getExerciseCompletionUseCaseProvider, getAverageVoidGapUseCaseProvider, appSettingsDaoProvider, clockProvider, dispatchersProvider);
  }

  public static StatisticsViewModel newInstance(GetVoidFrequencyUseCase getVoidFrequencyUseCase,
      GetScheduleAdherenceUseCase getScheduleAdherenceUseCase,
      GetLiquidIntakeUseCase getLiquidIntakeUseCase,
      GetExerciseCompletionUseCase getExerciseCompletionUseCase,
      GetAverageVoidGapUseCase getAverageVoidGapUseCase, AppSettingsDao appSettingsDao, Clock clock,
      DispatcherProvider dispatchers) {
    return new StatisticsViewModel(getVoidFrequencyUseCase, getScheduleAdherenceUseCase, getLiquidIntakeUseCase, getExerciseCompletionUseCase, getAverageVoidGapUseCase, appSettingsDao, clock, dispatchers);
  }
}
