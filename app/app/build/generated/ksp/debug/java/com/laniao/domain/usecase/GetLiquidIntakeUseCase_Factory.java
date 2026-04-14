package com.laniao.domain.usecase;

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
public final class GetLiquidIntakeUseCase_Factory implements Factory<GetLiquidIntakeUseCase> {
  private final Provider<DrinkEntryRepository> drinkEntryRepositoryProvider;

  public GetLiquidIntakeUseCase_Factory(
      Provider<DrinkEntryRepository> drinkEntryRepositoryProvider) {
    this.drinkEntryRepositoryProvider = drinkEntryRepositoryProvider;
  }

  @Override
  public GetLiquidIntakeUseCase get() {
    return newInstance(drinkEntryRepositoryProvider.get());
  }

  public static GetLiquidIntakeUseCase_Factory create(
      javax.inject.Provider<DrinkEntryRepository> drinkEntryRepositoryProvider) {
    return new GetLiquidIntakeUseCase_Factory(Providers.asDaggerProvider(drinkEntryRepositoryProvider));
  }

  public static GetLiquidIntakeUseCase_Factory create(
      Provider<DrinkEntryRepository> drinkEntryRepositoryProvider) {
    return new GetLiquidIntakeUseCase_Factory(drinkEntryRepositoryProvider);
  }

  public static GetLiquidIntakeUseCase newInstance(DrinkEntryRepository drinkEntryRepository) {
    return new GetLiquidIntakeUseCase(drinkEntryRepository);
  }
}
