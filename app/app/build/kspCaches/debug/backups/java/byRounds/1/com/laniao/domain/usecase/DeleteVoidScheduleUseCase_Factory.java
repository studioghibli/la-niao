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
public final class DeleteVoidScheduleUseCase_Factory implements Factory<DeleteVoidScheduleUseCase> {
  private final Provider<VoidScheduleRepository> repositoryProvider;

  private final Provider<PeeEntryRepository> entryRepositoryProvider;

  public DeleteVoidScheduleUseCase_Factory(Provider<VoidScheduleRepository> repositoryProvider,
      Provider<PeeEntryRepository> entryRepositoryProvider) {
    this.repositoryProvider = repositoryProvider;
    this.entryRepositoryProvider = entryRepositoryProvider;
  }

  @Override
  public DeleteVoidScheduleUseCase get() {
    return newInstance(repositoryProvider.get(), entryRepositoryProvider.get());
  }

  public static DeleteVoidScheduleUseCase_Factory create(
      javax.inject.Provider<VoidScheduleRepository> repositoryProvider,
      javax.inject.Provider<PeeEntryRepository> entryRepositoryProvider) {
    return new DeleteVoidScheduleUseCase_Factory(Providers.asDaggerProvider(repositoryProvider), Providers.asDaggerProvider(entryRepositoryProvider));
  }

  public static DeleteVoidScheduleUseCase_Factory create(
      Provider<VoidScheduleRepository> repositoryProvider,
      Provider<PeeEntryRepository> entryRepositoryProvider) {
    return new DeleteVoidScheduleUseCase_Factory(repositoryProvider, entryRepositoryProvider);
  }

  public static DeleteVoidScheduleUseCase newInstance(VoidScheduleRepository repository,
      PeeEntryRepository entryRepository) {
    return new DeleteVoidScheduleUseCase(repository, entryRepository);
  }
}
