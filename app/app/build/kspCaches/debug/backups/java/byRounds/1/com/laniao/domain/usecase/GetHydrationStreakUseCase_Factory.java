package com.laniao.domain.usecase;

import com.laniao.data.local.dao.AppSettingsDao;
import com.laniao.domain.repository.DrinkEntryRepository;
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
public final class GetHydrationStreakUseCase_Factory implements Factory<GetHydrationStreakUseCase> {
  private final Provider<DrinkEntryRepository> drinkRepositoryProvider;

  private final Provider<AppSettingsDao> appSettingsDaoProvider;

  public GetHydrationStreakUseCase_Factory(Provider<DrinkEntryRepository> drinkRepositoryProvider,
      Provider<AppSettingsDao> appSettingsDaoProvider) {
    this.drinkRepositoryProvider = drinkRepositoryProvider;
    this.appSettingsDaoProvider = appSettingsDaoProvider;
  }

  @Override
  public GetHydrationStreakUseCase get() {
    return newInstance(drinkRepositoryProvider.get(), appSettingsDaoProvider.get());
  }

  public static GetHydrationStreakUseCase_Factory create(
      javax.inject.Provider<DrinkEntryRepository> drinkRepositoryProvider,
      javax.inject.Provider<AppSettingsDao> appSettingsDaoProvider) {
    return new GetHydrationStreakUseCase_Factory(Providers.asDaggerProvider(drinkRepositoryProvider), Providers.asDaggerProvider(appSettingsDaoProvider));
  }

  public static GetHydrationStreakUseCase_Factory create(
      Provider<DrinkEntryRepository> drinkRepositoryProvider,
      Provider<AppSettingsDao> appSettingsDaoProvider) {
    return new GetHydrationStreakUseCase_Factory(drinkRepositoryProvider, appSettingsDaoProvider);
  }

  public static GetHydrationStreakUseCase newInstance(DrinkEntryRepository drinkRepository,
      AppSettingsDao appSettingsDao) {
    return new GetHydrationStreakUseCase(drinkRepository, appSettingsDao);
  }
}
