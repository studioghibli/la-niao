package com.laniao.presentation.viewmodel;

import android.content.Context;
import com.laniao.data.local.dao.AppSettingsDao;
import com.laniao.domain.usecase.BackupDataUseCase;
import com.laniao.domain.usecase.ClearAllDataUseCase;
import com.laniao.domain.usecase.ExportCsvUseCase;
import com.laniao.domain.usecase.RedoUseCase;
import com.laniao.domain.usecase.RestoreDataUseCase;
import com.laniao.domain.usecase.UndoRedoManager;
import com.laniao.domain.usecase.UndoUseCase;
import com.laniao.util.DispatcherProvider;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.Providers;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class SettingsViewModel_Factory implements Factory<SettingsViewModel> {
  private final Provider<Context> appContextProvider;

  private final Provider<AppSettingsDao> appSettingsDaoProvider;

  private final Provider<ExportCsvUseCase> exportCsvUseCaseProvider;

  private final Provider<BackupDataUseCase> backupDataUseCaseProvider;

  private final Provider<RestoreDataUseCase> restoreDataUseCaseProvider;

  private final Provider<ClearAllDataUseCase> clearAllDataUseCaseProvider;

  private final Provider<UndoRedoManager> undoRedoManagerProvider;

  private final Provider<UndoUseCase> undoUseCaseProvider;

  private final Provider<RedoUseCase> redoUseCaseProvider;

  private final Provider<DispatcherProvider> dispatcherProvider;

  public SettingsViewModel_Factory(Provider<Context> appContextProvider,
      Provider<AppSettingsDao> appSettingsDaoProvider,
      Provider<ExportCsvUseCase> exportCsvUseCaseProvider,
      Provider<BackupDataUseCase> backupDataUseCaseProvider,
      Provider<RestoreDataUseCase> restoreDataUseCaseProvider,
      Provider<ClearAllDataUseCase> clearAllDataUseCaseProvider,
      Provider<UndoRedoManager> undoRedoManagerProvider, Provider<UndoUseCase> undoUseCaseProvider,
      Provider<RedoUseCase> redoUseCaseProvider, Provider<DispatcherProvider> dispatcherProvider) {
    this.appContextProvider = appContextProvider;
    this.appSettingsDaoProvider = appSettingsDaoProvider;
    this.exportCsvUseCaseProvider = exportCsvUseCaseProvider;
    this.backupDataUseCaseProvider = backupDataUseCaseProvider;
    this.restoreDataUseCaseProvider = restoreDataUseCaseProvider;
    this.clearAllDataUseCaseProvider = clearAllDataUseCaseProvider;
    this.undoRedoManagerProvider = undoRedoManagerProvider;
    this.undoUseCaseProvider = undoUseCaseProvider;
    this.redoUseCaseProvider = redoUseCaseProvider;
    this.dispatcherProvider = dispatcherProvider;
  }

  @Override
  public SettingsViewModel get() {
    return newInstance(appContextProvider.get(), appSettingsDaoProvider.get(), exportCsvUseCaseProvider.get(), backupDataUseCaseProvider.get(), restoreDataUseCaseProvider.get(), clearAllDataUseCaseProvider.get(), undoRedoManagerProvider.get(), undoUseCaseProvider.get(), redoUseCaseProvider.get(), dispatcherProvider.get());
  }

  public static SettingsViewModel_Factory create(javax.inject.Provider<Context> appContextProvider,
      javax.inject.Provider<AppSettingsDao> appSettingsDaoProvider,
      javax.inject.Provider<ExportCsvUseCase> exportCsvUseCaseProvider,
      javax.inject.Provider<BackupDataUseCase> backupDataUseCaseProvider,
      javax.inject.Provider<RestoreDataUseCase> restoreDataUseCaseProvider,
      javax.inject.Provider<ClearAllDataUseCase> clearAllDataUseCaseProvider,
      javax.inject.Provider<UndoRedoManager> undoRedoManagerProvider,
      javax.inject.Provider<UndoUseCase> undoUseCaseProvider,
      javax.inject.Provider<RedoUseCase> redoUseCaseProvider,
      javax.inject.Provider<DispatcherProvider> dispatcherProvider) {
    return new SettingsViewModel_Factory(Providers.asDaggerProvider(appContextProvider), Providers.asDaggerProvider(appSettingsDaoProvider), Providers.asDaggerProvider(exportCsvUseCaseProvider), Providers.asDaggerProvider(backupDataUseCaseProvider), Providers.asDaggerProvider(restoreDataUseCaseProvider), Providers.asDaggerProvider(clearAllDataUseCaseProvider), Providers.asDaggerProvider(undoRedoManagerProvider), Providers.asDaggerProvider(undoUseCaseProvider), Providers.asDaggerProvider(redoUseCaseProvider), Providers.asDaggerProvider(dispatcherProvider));
  }

  public static SettingsViewModel_Factory create(Provider<Context> appContextProvider,
      Provider<AppSettingsDao> appSettingsDaoProvider,
      Provider<ExportCsvUseCase> exportCsvUseCaseProvider,
      Provider<BackupDataUseCase> backupDataUseCaseProvider,
      Provider<RestoreDataUseCase> restoreDataUseCaseProvider,
      Provider<ClearAllDataUseCase> clearAllDataUseCaseProvider,
      Provider<UndoRedoManager> undoRedoManagerProvider, Provider<UndoUseCase> undoUseCaseProvider,
      Provider<RedoUseCase> redoUseCaseProvider, Provider<DispatcherProvider> dispatcherProvider) {
    return new SettingsViewModel_Factory(appContextProvider, appSettingsDaoProvider, exportCsvUseCaseProvider, backupDataUseCaseProvider, restoreDataUseCaseProvider, clearAllDataUseCaseProvider, undoRedoManagerProvider, undoUseCaseProvider, redoUseCaseProvider, dispatcherProvider);
  }

  public static SettingsViewModel newInstance(Context appContext, AppSettingsDao appSettingsDao,
      ExportCsvUseCase exportCsvUseCase, BackupDataUseCase backupDataUseCase,
      RestoreDataUseCase restoreDataUseCase, ClearAllDataUseCase clearAllDataUseCase,
      UndoRedoManager undoRedoManager, UndoUseCase undoUseCase, RedoUseCase redoUseCase,
      DispatcherProvider dispatcherProvider) {
    return new SettingsViewModel(appContext, appSettingsDao, exportCsvUseCase, backupDataUseCase, restoreDataUseCase, clearAllDataUseCase, undoRedoManager, undoUseCase, redoUseCase, dispatcherProvider);
  }
}
