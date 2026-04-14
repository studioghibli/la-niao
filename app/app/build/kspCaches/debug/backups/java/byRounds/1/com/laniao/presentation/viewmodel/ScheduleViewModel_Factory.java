package com.laniao.presentation.viewmodel;

import com.laniao.domain.repository.ExerciseRepository;
import com.laniao.domain.usecase.CheckScheduleOverlapUseCase;
import com.laniao.domain.usecase.CreateVoidScheduleUseCase;
import com.laniao.domain.usecase.DeleteVoidScheduleUseCase;
import com.laniao.domain.usecase.GetActiveSchedulesUseCase;
import com.laniao.domain.usecase.GetAllSchedulesUseCase;
import com.laniao.domain.usecase.GetScheduleProgressUseCase;
import com.laniao.domain.usecase.GetTodayExerciseStatusUseCase;
import com.laniao.domain.usecase.ManageExerciseScheduleUseCase;
import com.laniao.domain.usecase.UndoRedoManager;
import com.laniao.domain.usecase.UpdateVoidScheduleExpiryUseCase;
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
public final class ScheduleViewModel_Factory implements Factory<ScheduleViewModel> {
  private final Provider<GetActiveSchedulesUseCase> getActiveSchedulesUseCaseProvider;

  private final Provider<GetAllSchedulesUseCase> getAllSchedulesUseCaseProvider;

  private final Provider<GetScheduleProgressUseCase> getScheduleProgressUseCaseProvider;

  private final Provider<CreateVoidScheduleUseCase> createVoidScheduleUseCaseProvider;

  private final Provider<UpdateVoidScheduleExpiryUseCase> updateVoidScheduleExpiryUseCaseProvider;

  private final Provider<DeleteVoidScheduleUseCase> deleteVoidScheduleUseCaseProvider;

  private final Provider<CheckScheduleOverlapUseCase> checkScheduleOverlapUseCaseProvider;

  private final Provider<ManageExerciseScheduleUseCase> manageExerciseScheduleUseCaseProvider;

  private final Provider<GetTodayExerciseStatusUseCase> getTodayExerciseStatusUseCaseProvider;

  private final Provider<ExerciseRepository> exerciseRepositoryProvider;

  private final Provider<UndoRedoManager> undoRedoManagerProvider;

  private final Provider<DispatcherProvider> dispatcherProvider;

  public ScheduleViewModel_Factory(
      Provider<GetActiveSchedulesUseCase> getActiveSchedulesUseCaseProvider,
      Provider<GetAllSchedulesUseCase> getAllSchedulesUseCaseProvider,
      Provider<GetScheduleProgressUseCase> getScheduleProgressUseCaseProvider,
      Provider<CreateVoidScheduleUseCase> createVoidScheduleUseCaseProvider,
      Provider<UpdateVoidScheduleExpiryUseCase> updateVoidScheduleExpiryUseCaseProvider,
      Provider<DeleteVoidScheduleUseCase> deleteVoidScheduleUseCaseProvider,
      Provider<CheckScheduleOverlapUseCase> checkScheduleOverlapUseCaseProvider,
      Provider<ManageExerciseScheduleUseCase> manageExerciseScheduleUseCaseProvider,
      Provider<GetTodayExerciseStatusUseCase> getTodayExerciseStatusUseCaseProvider,
      Provider<ExerciseRepository> exerciseRepositoryProvider,
      Provider<UndoRedoManager> undoRedoManagerProvider,
      Provider<DispatcherProvider> dispatcherProvider) {
    this.getActiveSchedulesUseCaseProvider = getActiveSchedulesUseCaseProvider;
    this.getAllSchedulesUseCaseProvider = getAllSchedulesUseCaseProvider;
    this.getScheduleProgressUseCaseProvider = getScheduleProgressUseCaseProvider;
    this.createVoidScheduleUseCaseProvider = createVoidScheduleUseCaseProvider;
    this.updateVoidScheduleExpiryUseCaseProvider = updateVoidScheduleExpiryUseCaseProvider;
    this.deleteVoidScheduleUseCaseProvider = deleteVoidScheduleUseCaseProvider;
    this.checkScheduleOverlapUseCaseProvider = checkScheduleOverlapUseCaseProvider;
    this.manageExerciseScheduleUseCaseProvider = manageExerciseScheduleUseCaseProvider;
    this.getTodayExerciseStatusUseCaseProvider = getTodayExerciseStatusUseCaseProvider;
    this.exerciseRepositoryProvider = exerciseRepositoryProvider;
    this.undoRedoManagerProvider = undoRedoManagerProvider;
    this.dispatcherProvider = dispatcherProvider;
  }

  @Override
  public ScheduleViewModel get() {
    return newInstance(getActiveSchedulesUseCaseProvider.get(), getAllSchedulesUseCaseProvider.get(), getScheduleProgressUseCaseProvider.get(), createVoidScheduleUseCaseProvider.get(), updateVoidScheduleExpiryUseCaseProvider.get(), deleteVoidScheduleUseCaseProvider.get(), checkScheduleOverlapUseCaseProvider.get(), manageExerciseScheduleUseCaseProvider.get(), getTodayExerciseStatusUseCaseProvider.get(), exerciseRepositoryProvider.get(), undoRedoManagerProvider.get(), dispatcherProvider.get());
  }

  public static ScheduleViewModel_Factory create(
      javax.inject.Provider<GetActiveSchedulesUseCase> getActiveSchedulesUseCaseProvider,
      javax.inject.Provider<GetAllSchedulesUseCase> getAllSchedulesUseCaseProvider,
      javax.inject.Provider<GetScheduleProgressUseCase> getScheduleProgressUseCaseProvider,
      javax.inject.Provider<CreateVoidScheduleUseCase> createVoidScheduleUseCaseProvider,
      javax.inject.Provider<UpdateVoidScheduleExpiryUseCase> updateVoidScheduleExpiryUseCaseProvider,
      javax.inject.Provider<DeleteVoidScheduleUseCase> deleteVoidScheduleUseCaseProvider,
      javax.inject.Provider<CheckScheduleOverlapUseCase> checkScheduleOverlapUseCaseProvider,
      javax.inject.Provider<ManageExerciseScheduleUseCase> manageExerciseScheduleUseCaseProvider,
      javax.inject.Provider<GetTodayExerciseStatusUseCase> getTodayExerciseStatusUseCaseProvider,
      javax.inject.Provider<ExerciseRepository> exerciseRepositoryProvider,
      javax.inject.Provider<UndoRedoManager> undoRedoManagerProvider,
      javax.inject.Provider<DispatcherProvider> dispatcherProvider) {
    return new ScheduleViewModel_Factory(Providers.asDaggerProvider(getActiveSchedulesUseCaseProvider), Providers.asDaggerProvider(getAllSchedulesUseCaseProvider), Providers.asDaggerProvider(getScheduleProgressUseCaseProvider), Providers.asDaggerProvider(createVoidScheduleUseCaseProvider), Providers.asDaggerProvider(updateVoidScheduleExpiryUseCaseProvider), Providers.asDaggerProvider(deleteVoidScheduleUseCaseProvider), Providers.asDaggerProvider(checkScheduleOverlapUseCaseProvider), Providers.asDaggerProvider(manageExerciseScheduleUseCaseProvider), Providers.asDaggerProvider(getTodayExerciseStatusUseCaseProvider), Providers.asDaggerProvider(exerciseRepositoryProvider), Providers.asDaggerProvider(undoRedoManagerProvider), Providers.asDaggerProvider(dispatcherProvider));
  }

  public static ScheduleViewModel_Factory create(
      Provider<GetActiveSchedulesUseCase> getActiveSchedulesUseCaseProvider,
      Provider<GetAllSchedulesUseCase> getAllSchedulesUseCaseProvider,
      Provider<GetScheduleProgressUseCase> getScheduleProgressUseCaseProvider,
      Provider<CreateVoidScheduleUseCase> createVoidScheduleUseCaseProvider,
      Provider<UpdateVoidScheduleExpiryUseCase> updateVoidScheduleExpiryUseCaseProvider,
      Provider<DeleteVoidScheduleUseCase> deleteVoidScheduleUseCaseProvider,
      Provider<CheckScheduleOverlapUseCase> checkScheduleOverlapUseCaseProvider,
      Provider<ManageExerciseScheduleUseCase> manageExerciseScheduleUseCaseProvider,
      Provider<GetTodayExerciseStatusUseCase> getTodayExerciseStatusUseCaseProvider,
      Provider<ExerciseRepository> exerciseRepositoryProvider,
      Provider<UndoRedoManager> undoRedoManagerProvider,
      Provider<DispatcherProvider> dispatcherProvider) {
    return new ScheduleViewModel_Factory(getActiveSchedulesUseCaseProvider, getAllSchedulesUseCaseProvider, getScheduleProgressUseCaseProvider, createVoidScheduleUseCaseProvider, updateVoidScheduleExpiryUseCaseProvider, deleteVoidScheduleUseCaseProvider, checkScheduleOverlapUseCaseProvider, manageExerciseScheduleUseCaseProvider, getTodayExerciseStatusUseCaseProvider, exerciseRepositoryProvider, undoRedoManagerProvider, dispatcherProvider);
  }

  public static ScheduleViewModel newInstance(GetActiveSchedulesUseCase getActiveSchedulesUseCase,
      GetAllSchedulesUseCase getAllSchedulesUseCase,
      GetScheduleProgressUseCase getScheduleProgressUseCase,
      CreateVoidScheduleUseCase createVoidScheduleUseCase,
      UpdateVoidScheduleExpiryUseCase updateVoidScheduleExpiryUseCase,
      DeleteVoidScheduleUseCase deleteVoidScheduleUseCase,
      CheckScheduleOverlapUseCase checkScheduleOverlapUseCase,
      ManageExerciseScheduleUseCase manageExerciseScheduleUseCase,
      GetTodayExerciseStatusUseCase getTodayExerciseStatusUseCase,
      ExerciseRepository exerciseRepository, UndoRedoManager undoRedoManager,
      DispatcherProvider dispatcherProvider) {
    return new ScheduleViewModel(getActiveSchedulesUseCase, getAllSchedulesUseCase, getScheduleProgressUseCase, createVoidScheduleUseCase, updateVoidScheduleExpiryUseCase, deleteVoidScheduleUseCase, checkScheduleOverlapUseCase, manageExerciseScheduleUseCase, getTodayExerciseStatusUseCase, exerciseRepository, undoRedoManager, dispatcherProvider);
  }
}
