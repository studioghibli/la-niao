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
public final class DeleteDrinkEntryUseCase_Factory implements Factory<DeleteDrinkEntryUseCase> {
  private final Provider<DrinkEntryRepository> repositoryProvider;

  public DeleteDrinkEntryUseCase_Factory(Provider<DrinkEntryRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public DeleteDrinkEntryUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static DeleteDrinkEntryUseCase_Factory create(
      javax.inject.Provider<DrinkEntryRepository> repositoryProvider) {
    return new DeleteDrinkEntryUseCase_Factory(Providers.asDaggerProvider(repositoryProvider));
  }

  public static DeleteDrinkEntryUseCase_Factory create(
      Provider<DrinkEntryRepository> repositoryProvider) {
    return new DeleteDrinkEntryUseCase_Factory(repositoryProvider);
  }

  public static DeleteDrinkEntryUseCase newInstance(DrinkEntryRepository repository) {
    return new DeleteDrinkEntryUseCase(repository);
  }
}
