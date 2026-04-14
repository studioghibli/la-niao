package com.laniao.domain.usecase;

import com.laniao.domain.repository.PeeEntryRepository;
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
public final class DeletePeeEntryUseCase_Factory implements Factory<DeletePeeEntryUseCase> {
  private final Provider<PeeEntryRepository> repositoryProvider;

  private final Provider<RecalculateScheduledTimesUseCase> recalculateScheduledTimesUseCaseProvider;

  public DeletePeeEntryUseCase_Factory(Provider<PeeEntryRepository> repositoryProvider,
      Provider<RecalculateScheduledTimesUseCase> recalculateScheduledTimesUseCaseProvider) {
    this.repositoryProvider = repositoryProvider;
    this.recalculateScheduledTimesUseCaseProvider = recalculateScheduledTimesUseCaseProvider;
  }

  @Override
  public DeletePeeEntryUseCase get() {
    return newInstance(repositoryProvider.get(), recalculateScheduledTimesUseCaseProvider.get());
  }

  public static DeletePeeEntryUseCase_Factory create(
      javax.inject.Provider<PeeEntryRepository> repositoryProvider,
      javax.inject.Provider<RecalculateScheduledTimesUseCase> recalculateScheduledTimesUseCaseProvider) {
    return new DeletePeeEntryUseCase_Factory(Providers.asDaggerProvider(repositoryProvider), Providers.asDaggerProvider(recalculateScheduledTimesUseCaseProvider));
  }

  public static DeletePeeEntryUseCase_Factory create(
      Provider<PeeEntryRepository> repositoryProvider,
      Provider<RecalculateScheduledTimesUseCase> recalculateScheduledTimesUseCaseProvider) {
    return new DeletePeeEntryUseCase_Factory(repositoryProvider, recalculateScheduledTimesUseCaseProvider);
  }

  public static DeletePeeEntryUseCase newInstance(PeeEntryRepository repository,
      RecalculateScheduledTimesUseCase recalculateScheduledTimesUseCase) {
    return new DeletePeeEntryUseCase(repository, recalculateScheduledTimesUseCase);
  }
}
