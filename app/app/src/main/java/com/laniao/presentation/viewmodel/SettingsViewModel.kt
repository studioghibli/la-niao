package com.laniao.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.laniao.data.local.dao.AppSettingsDao
import com.laniao.data.local.entity.AppSettingsEntity
import com.laniao.domain.usecase.BackupDataUseCase
import com.laniao.domain.usecase.ClearAllDataUseCase
import com.laniao.domain.usecase.ExportCsvUseCase
import com.laniao.domain.usecase.RestoreDataUseCase
import com.laniao.domain.usecase.RestoreResult
import com.laniao.domain.usecase.UndoRedoManager
import com.laniao.domain.usecase.UndoUseCase
import com.laniao.domain.usecase.RedoUseCase
import com.laniao.domain.usecase.UnsupportedSchemaVersionException
import com.laniao.util.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val hydrationGoalLiters: Double = 2.7,
    val isLoading: Boolean = true,
    val isExporting: Boolean = false,
    val isBackingUp: Boolean = false,
    val isRestoring: Boolean = false,
    val availableBackups: List<BackupFileInfo> = emptyList(),
    val canUndo: Boolean = false,
    val canRedo: Boolean = false,
    val lastUndoDescription: String? = null,
    val lastRedoDescription: String? = null
)

data class BackupFileInfo(
    val fileName: String,
    val sizeBytes: Long,
    val lastModified: Long
)

sealed interface SettingsEvent {
    data class CsvReady(val content: String) : SettingsEvent
    data class BackupReady(val fileName: String) : SettingsEvent
    data class RestoreComplete(val result: RestoreResult) : SettingsEvent
    data object DataCleared : SettingsEvent
    data class UndoComplete(val message: String) : SettingsEvent
    data class RedoComplete(val message: String) : SettingsEvent
    data class Error(val message: String) : SettingsEvent
}

@HiltViewModel
class SettingsViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val appSettingsDao: AppSettingsDao,
    private val exportCsvUseCase: ExportCsvUseCase,
    private val backupDataUseCase: BackupDataUseCase,
    private val restoreDataUseCase: RestoreDataUseCase,
    private val clearAllDataUseCase: ClearAllDataUseCase,
    private val undoRedoManager: UndoRedoManager,
    private val undoUseCase: UndoUseCase,
    private val redoUseCase: RedoUseCase,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<SettingsEvent>()
    val events: SharedFlow<SettingsEvent> = _events.asSharedFlow()

    // Keep legacy exportEvent for CSV export compatibility
    private val _exportEvent = MutableSharedFlow<String>()
    val exportEvent: SharedFlow<String> = _exportEvent.asSharedFlow()

    init {
        viewModelScope.launch(dispatcherProvider.io) {
            val settings = appSettingsDao.getOnce()
            _uiState.update {
                it.copy(
                    hydrationGoalLiters = settings?.hydrationGoalLiters ?: 2.7,
                    isLoading = false
                )
            }
            refreshBackupList()
        }
        // Observe undo/redo state
        viewModelScope.launch {
            undoRedoManager.canUndo.collect { canUndo ->
                _uiState.update { it.copy(canUndo = canUndo) }
            }
        }
        viewModelScope.launch {
            undoRedoManager.canRedo.collect { canRedo ->
                _uiState.update { it.copy(canRedo = canRedo) }
            }
        }
        viewModelScope.launch {
            undoRedoManager.lastUndoDescription.collect { desc ->
                _uiState.update { it.copy(lastUndoDescription = desc) }
            }
        }
        viewModelScope.launch {
            undoRedoManager.lastRedoDescription.collect { desc ->
                _uiState.update { it.copy(lastRedoDescription = desc) }
            }
        }
    }

    fun updateHydrationGoal(liters: Double) {
        val clamped = liters.coerceIn(1.5, 3.5)
        _uiState.update { it.copy(hydrationGoalLiters = clamped) }
        viewModelScope.launch(dispatcherProvider.io) {
            appSettingsDao.upsert(AppSettingsEntity(hydrationGoalLiters = clamped))
        }
    }

    fun exportCsv() {
        viewModelScope.launch(dispatcherProvider.io) {
            _uiState.update { it.copy(isExporting = true) }
            try {
                val csv = exportCsvUseCase()
                _exportEvent.emit(csv)
            } finally {
                _uiState.update { it.copy(isExporting = false) }
            }
        }
    }

    fun backup() {
        viewModelScope.launch(dispatcherProvider.io) {
            _uiState.update { it.copy(isBackingUp = true) }
            try {
                val json = backupDataUseCase()
                val dateStr = java.time.LocalDate.now().format(
                    com.laniao.util.DateFormatters.DATE_FILE
                )
                val file = java.io.File(appContext.filesDir, "laniao_backup_$dateStr.json")
                val tempFile = java.io.File(appContext.filesDir, "laniao_backup_$dateStr.json.tmp")
                tempFile.writeText(json)
                tempFile.renameTo(file)
                refreshBackupList()
                _events.emit(SettingsEvent.BackupReady(file.name))
            } catch (e: Exception) {
                _events.emit(SettingsEvent.Error("Backup failed: ${e.message}"))
            } finally {
                _uiState.update { it.copy(isBackingUp = false) }
            }
        }
    }

    fun restoreFromFile(fileName: String) {
        viewModelScope.launch(dispatcherProvider.io) {
            _uiState.update { it.copy(isRestoring = true) }
            try {
                val file = java.io.File(appContext.filesDir, fileName)
                val jsonContent = file.readText()
                val result = restoreDataUseCase(jsonContent)
                val settings = appSettingsDao.getOnce()
                _uiState.update {
                    it.copy(hydrationGoalLiters = settings?.hydrationGoalLiters ?: 2.7)
                }
                _events.emit(SettingsEvent.RestoreComplete(result))
            } catch (e: UnsupportedSchemaVersionException) {
                _events.emit(SettingsEvent.Error("This backup is from a newer version of LaNiao and cannot be restored."))
            } catch (e: Exception) {
                _events.emit(SettingsEvent.Error("Restore failed: ${e.message}"))
            } finally {
                _uiState.update { it.copy(isRestoring = false) }
            }
        }
    }

    fun refreshBackupList() {
        val backups = appContext.filesDir.listFiles { file ->
            file.name.startsWith("laniao_backup_") && file.name.endsWith(".json")
        }?.map { file ->
            BackupFileInfo(
                fileName = file.name,
                sizeBytes = file.length(),
                lastModified = file.lastModified()
            )
        }?.sortedByDescending { it.lastModified } ?: emptyList()
        _uiState.update { it.copy(availableBackups = backups) }
    }

    fun clearAllData() {
        viewModelScope.launch(dispatcherProvider.io) {
            try {
                clearAllDataUseCase()
                undoRedoManager.clear()
                _uiState.update { it.copy(hydrationGoalLiters = 2.7) }
                _events.emit(SettingsEvent.DataCleared)
            } catch (e: Exception) {
                _events.emit(SettingsEvent.Error("Failed to clear data: ${e.message}"))
            }
        }
    }

    fun undo() {
        viewModelScope.launch(dispatcherProvider.io) {
            try {
                val message = undoUseCase() ?: return@launch
                _events.emit(SettingsEvent.UndoComplete(message))
            } catch (e: Exception) {
                _events.emit(SettingsEvent.Error("Undo failed: ${e.message}"))
            }
        }
    }

    fun redo() {
        viewModelScope.launch(dispatcherProvider.io) {
            try {
                val message = redoUseCase() ?: return@launch
                _events.emit(SettingsEvent.RedoComplete(message))
            } catch (e: Exception) {
                _events.emit(SettingsEvent.Error("Redo failed: ${e.message}"))
            }
        }
    }
}
