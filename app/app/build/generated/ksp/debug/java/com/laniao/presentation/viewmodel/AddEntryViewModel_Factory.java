package com.laniao.presentation.viewmodel;

import androidx.lifecycle.SavedStateHandle;
import com.laniao.domain.usecase.AddPeeEntryUseCase;
import com.laniao.domain.usecase.DeletePeeEntryUseCase;
import com.laniao.domain.usecase.GetPeeEntryByIdUseCase;
import com.laniao.domain.usecase.GetUnclaimedScheduledTimesUseCase;
import com.laniao.domain.usecase.UpdatePeeEntryUseCase;
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
public final class AddEntryViewModel_Factory implements Factory<AddEntryViewModel> {
  private final Provider<AddPeeEntryUseCase> addPeeEntryUseCaseProvider;

  private final Provider<UpdatePeeEntryUseCase> updatePeeEntryUseCaseProvider;

  private final Provider<DeletePeeEntryUseCase> deletePeeEntryUseCaseProvider;

  private final Provider<GetPeeEntryByIdUseCase> getPeeEntryByIdUseCaseProvider;

  private final Provider<GetUnclaimedScheduledTimesUseCase> getUnclaimedScheduledTimesUseCaseProvider;

  private final Provider<Clock> clockProvider;

  private final Provider<DispatcherProvider> dispatcherProvider;

  private final Provider<SavedStateHandle> savedStateHandleProvider;

  public AddEntryViewModel_Factory(Provider<AddPeeEntryUseCase> addPeeEntryUseCaseProvider,
      Provider<UpdatePeeEntryUseCase> updatePeeEntryUseCaseProvider,
      Provider<DeletePeeEntryUseCase> deletePeeEntryUseCaseProvider,
      Provider<GetPeeEntryByIdUseCase> getPeeEntryByIdUseCaseProvider,
      Provider<GetUnclaimedScheduledTimesUseCase> getUnclaimedScheduledTimesUseCaseProvider,
      Provider<Clock> clockProvider, Provider<DispatcherProvider> dispatcherProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    this.addPeeEntryUseCaseProvider = addPeeEntryUseCaseProvider;
    this.updatePeeEntryUseCaseProvider = updatePeeEntryUseCaseProvider;
    this.deletePeeEntryUseCaseProvider = deletePeeEntryUseCaseProvider;
    this.getPeeEntryByIdUseCaseProvider = getPeeEntryByIdUseCaseProvider;
    this.getUnclaimedScheduledTimesUseCaseProvider = getUnclaimedScheduledTimesUseCaseProvider;
    this.clockProvider = clockProvider;
    this.dispatcherProvider = dispatcherProvider;
    this.savedStateHandleProvider = savedStateHandleProvider;
  }

  @Override
  public AddEntryViewModel get() {
    return newInstance(addPeeEntryUseCaseProvider.get(), updatePeeEntryUseCaseProvider.get(), deletePeeEntryUseCaseProvider.get(), getPeeEntryByIdUseCaseProvider.get(), getUnclaimedScheduledTimesUseCaseProvider.get(), clockProvider.get(), dispatcherProvider.get(), savedStateHandleProvider.get());
  }

  public static AddEntryViewModel_Factory create(
      javax.inject.Provider<AddPeeEntryUseCase> addPeeEntryUseCaseProvider,
      javax.inject.Provider<UpdatePeeEntryUseCase> updatePeeEntryUseCaseProvider,
      javax.inject.Provider<DeletePeeEntryUseCase> deletePeeEntryUseCaseProvider,
      javax.inject.Provider<GetPeeEntryByIdUseCase> getPeeEntryByIdUseCaseProvider,
      javax.inject.Provider<GetUnclaimedScheduledTimesUseCase> getUnclaimedScheduledTimesUseCaseProvider,
      javax.inject.Provider<Clock> clockProvider,
      javax.inject.Provider<DispatcherProvider> dispatcherProvider,
      javax.inject.Provider<SavedStateHandle> savedStateHandleProvider) {
    return new AddEntryViewModel_Factory(Providers.asDaggerProvider(addPeeEntryUseCaseProvider), Providers.asDaggerProvider(updatePeeEntryUseCaseProvider), Providers.asDaggerProvider(deletePeeEntryUseCaseProvider), Providers.asDaggerProvider(getPeeEntryByIdUseCaseProvider), Providers.asDaggerProvider(getUnclaimedScheduledTimesUseCaseProvider), Providers.asDaggerProvider(clockProvider), Providers.asDaggerProvider(dispatcherProvider), Providers.asDaggerProvider(savedStateHandleProvider));
  }

  public static AddEntryViewModel_Factory create(
      Provider<AddPeeEntryUseCase> addPeeEntryUseCaseProvider,
      Provider<UpdatePeeEntryUseCase> updatePeeEntryUseCaseProvider,
      Provider<DeletePeeEntryUseCase> deletePeeEntryUseCaseProvider,
      Provider<GetPeeEntryByIdUseCase> getPeeEntryByIdUseCaseProvider,
      Provider<GetUnclaimedScheduledTimesUseCase> getUnclaimedScheduledTimesUseCaseProvider,
      Provider<Clock> clockProvider, Provider<DispatcherProvider> dispatcherProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    return new AddEntryViewModel_Factory(addPeeEntryUseCaseProvider, updatePeeEntryUseCaseProvider, deletePeeEntryUseCaseProvider, getPeeEntryByIdUseCaseProvider, getUnclaimedScheduledTimesUseCaseProvider, clockProvider, dispatcherProvider, savedStateHandleProvider);
  }

  public static AddEntryViewModel newInstance(AddPeeEntryUseCase addPeeEntryUseCase,
      UpdatePeeEntryUseCase updatePeeEntryUseCase, DeletePeeEntryUseCase deletePeeEntryUseCase,
      GetPeeEntryByIdUseCase getPeeEntryByIdUseCase,
      GetUnclaimedScheduledTimesUseCase getUnclaimedScheduledTimesUseCase, Clock clock,
      DispatcherProvider dispatcherProvider, SavedStateHandle savedStateHandle) {
    return new AddEntryViewModel(addPeeEntryUseCase, updatePeeEntryUseCase, deletePeeEntryUseCase, getPeeEntryByIdUseCase, getUnclaimedScheduledTimesUseCase, clock, dispatcherProvider, savedStateHandle);
  }
}
