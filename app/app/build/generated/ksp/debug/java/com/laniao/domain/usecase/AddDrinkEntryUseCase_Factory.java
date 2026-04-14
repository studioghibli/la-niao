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
public final class AddDrinkEntryUseCase_Factory implements Factory<AddDrinkEntryUseCase> {
  private final Provider<DrinkEntryRepository> repositoryProvider;

  public AddDrinkEntryUseCase_Factory(Provider<DrinkEntryRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public AddDrinkEntryUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static AddDrinkEntryUseCase_Factory create(
      javax.inject.Provider<DrinkEntryRepository> repositoryProvider) {
    return new AddDrinkEntryUseCase_Factory(Providers.asDaggerProvider(repositoryProvider));
  }

  public static AddDrinkEntryUseCase_Factory create(
      Provider<DrinkEntryRepository> repositoryProvider) {
    return new AddDrinkEntryUseCase_Factory(repositoryProvider);
  }

  public static AddDrinkEntryUseCase newInstance(DrinkEntryRepository repository) {
    return new AddDrinkEntryUseCase(repository);
  }
}
