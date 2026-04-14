package com.laniao.domain.usecase;

import com.laniao.domain.repository.DrinkEntryRepository;
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
public final class GetTodayDrinksUseCase_Factory implements Factory<GetTodayDrinksUseCase> {
  private final Provider<DrinkEntryRepository> repositoryProvider;

  private final Provider<Clock> clockProvider;

  public GetTodayDrinksUseCase_Factory(Provider<DrinkEntryRepository> repositoryProvider,
      Provider<Clock> clockProvider) {
    this.repositoryProvider = repositoryProvider;
    this.clockProvider = clockProvider;
  }

  @Override
  public GetTodayDrinksUseCase get() {
    return newInstance(repositoryProvider.get(), clockProvider.get());
  }

  public static GetTodayDrinksUseCase_Factory create(
      javax.inject.Provider<DrinkEntryRepository> repositoryProvider,
      javax.inject.Provider<Clock> clockProvider) {
    return new GetTodayDrinksUseCase_Factory(Providers.asDaggerProvider(repositoryProvider), Providers.asDaggerProvider(clockProvider));
  }

  public static GetTodayDrinksUseCase_Factory create(
      Provider<DrinkEntryRepository> repositoryProvider, Provider<Clock> clockProvider) {
    return new GetTodayDrinksUseCase_Factory(repositoryProvider, clockProvider);
  }

  public static GetTodayDrinksUseCase newInstance(DrinkEntryRepository repository, Clock clock) {
    return new GetTodayDrinksUseCase(repository, clock);
  }
}
