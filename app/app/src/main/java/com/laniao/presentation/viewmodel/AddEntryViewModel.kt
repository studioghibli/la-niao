package com.laniao.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.laniao.domain.exception.DuplicateMinuteException
import com.laniao.domain.exception.MaxEntriesException
import com.laniao.domain.exception.ValidationException
import com.laniao.domain.model.LeakAmount
import com.laniao.domain.model.PeeColor
import com.laniao.domain.model.PeeEntry
import com.laniao.domain.model.Urgency
import com.laniao.domain.model.VolumeSize
import com.laniao.domain.usecase.AddPeeEntryUseCase
import com.laniao.domain.usecase.DeletePeeEntryUseCase
import com.laniao.domain.usecase.GetPeeEntryByIdUseCase
import com.laniao.domain.usecase.GetUnclaimedScheduledTimesUseCase
import com.laniao.domain.usecase.UpdatePeeEntryUseCase
import com.laniao.util.Clock
import com.laniao.util.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject

/**
 * UI state for the Add/Edit Entry screen.
 */
data class AddEntryUiState(
    val entryId: Long? = null,
    val timestamp: Instant = Instant.now(),
    val didVoid: Boolean = true,
    val leakAmount: LeakAmount = LeakAmount.NONE,
    val volumeSize: VolumeSize = VolumeSize.UNKNOWN,
    val color: PeeColor = PeeColor.UNKNOWN,
    val urgency: Urgency = Urgency.UNKNOWN,
    val activityContext: String = "",
    val notes: String = "",
    val scheduledTime: LocalTime? = null,
    val unclaimedTimes: List<LocalTime> = emptyList(),
    val suggestedTime: LocalTime? = null,
    val isLoading: Boolean = false,
    val isEditMode: Boolean = false,
    val isQuickAdd: Boolean = false
)

enum class EntryType {
    VOID,
    URGE_ONLY,
    LEAK_ONLY
}

/**
 * One-time events emitted by the ViewModel.
 */
sealed interface AddEntryEvent {
    data object SaveSuccess : AddEntryEvent
    data object DeleteSuccess : AddEntryEvent
    data class Error(val message: String) : AddEntryEvent
}

/**
 * ViewModel for adding or editing a pee entry.
 */
@HiltViewModel
class AddEntryViewModel @Inject constructor(
    private val addPeeEntryUseCase: AddPeeEntryUseCase,
    private val updatePeeEntryUseCase: UpdatePeeEntryUseCase,
    private val deletePeeEntryUseCase: DeletePeeEntryUseCase,
    private val getPeeEntryByIdUseCase: GetPeeEntryByIdUseCase,
    private val getUnclaimedScheduledTimesUseCase: GetUnclaimedScheduledTimesUseCase,
    private val clock: Clock,
    private val dispatcherProvider: DispatcherProvider,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddEntryUiState(timestamp = clock.now()))
    val uiState: StateFlow<AddEntryUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<AddEntryEvent>()
    val events: SharedFlow<AddEntryEvent> = _events.asSharedFlow()

    private val zoneId: ZoneId = ZoneId.systemDefault()

    init {
        // Check if we're editing an existing entry
        val entryId: Long? = savedStateHandle["entryId"]
        val quickAdd: Boolean = savedStateHandle["quickAdd"] ?: false

        if (quickAdd) {
            // Quick Add mode - save immediately
            performQuickAdd()
        } else if (entryId != null && entryId > 0) {
            // Edit mode - load existing entry
            loadEntry(entryId)
        } else {
            // New entry mode - load unclaimed times
            loadUnclaimedTimes()
        }
    }

    private fun loadEntry(entryId: Long) {
        viewModelScope.launch(dispatcherProvider.io) {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val entry = getPeeEntryByIdUseCase(entryId)
                if (entry != null) {
                    _uiState.update {
                        it.copy(
                            entryId = entry.id,
                            timestamp = entry.timestamp,
                            didVoid = entry.didVoid,
                            leakAmount = entry.leakAmount,
                            volumeSize = entry.volumeSize,
                            color = entry.color,
                            urgency = entry.urgency,
                            activityContext = entry.activityContext ?: "",
                            notes = entry.notes ?: "",
                            scheduledTime = entry.scheduledTime,
                            isLoading = false,
                            isEditMode = true
                        )
                    }
                    loadUnclaimedTimes(entry.scheduledTime)
                } else {
                    _uiState.update { it.copy(isLoading = false) }
                    _events.emit(AddEntryEvent.Error("Entry not found"))
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
                _events.emit(AddEntryEvent.Error(e.message ?: "Failed to load entry"))
            }
        }
    }

    private fun loadUnclaimedTimes(currentScheduledTime: LocalTime? = null) {
        viewModelScope.launch(dispatcherProvider.io) {
            try {
                val date = _uiState.value.timestamp.atZone(zoneId).toLocalDate()
                val unclaimedTimes = getUnclaimedScheduledTimesUseCase(date).toMutableList()
                
                // If editing and entry has a scheduled time, include it in the list
                if (currentScheduledTime != null && currentScheduledTime !in unclaimedTimes) {
                    unclaimedTimes.add(currentScheduledTime)
                    unclaimedTimes.sort()
                }

                // Get suggested time (nearest to current entry time)
                val entryTime = _uiState.value.timestamp.atZone(zoneId).toLocalTime()
                val suggested = if (!_uiState.value.isEditMode) {
                    getUnclaimedScheduledTimesUseCase.getNearestTo(date, entryTime)
                } else null

                _uiState.update {
                    it.copy(
                        unclaimedTimes = unclaimedTimes,
                        suggestedTime = suggested,
                        scheduledTime = if (!it.isEditMode && suggested != null) suggested else it.scheduledTime
                    )
                }
            } catch (e: Exception) {
                // Silently fail - unclaimed times are optional
            }
        }
    }

    private fun performQuickAdd() {
        viewModelScope.launch(dispatcherProvider.io) {
            _uiState.update { it.copy(isLoading = true, isQuickAdd = true) }
            try {
                val entry = PeeEntry(
                    timestamp = clock.now(),
                    didVoid = true,
                    leakAmount = LeakAmount.NONE,
                    volumeSize = VolumeSize.UNKNOWN,
                    color = PeeColor.UNKNOWN,
                    urgency = Urgency.UNKNOWN,
                    activityContext = null,
                    notes = null,
                    scheduledTime = null
                )
                addPeeEntryUseCase(entry)
                _uiState.update { it.copy(isLoading = false) }
                _events.emit(AddEntryEvent.SaveSuccess)
            } catch (e: MaxEntriesException) {
                _uiState.update { it.copy(isLoading = false) }
                _events.emit(AddEntryEvent.Error("Maximum 50 entries per day reached"))
            } catch (e: DuplicateMinuteException) {
                _uiState.update { it.copy(isLoading = false) }
                _events.emit(AddEntryEvent.Error("An entry already exists for this minute"))
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
                _events.emit(AddEntryEvent.Error(e.message ?: "Failed to save entry"))
            }
        }
    }

    fun onTimestampChanged(timestamp: Instant) {
        _uiState.update { it.copy(timestamp = timestamp) }
        // Reload unclaimed times for the new date
        loadUnclaimedTimes(_uiState.value.scheduledTime)
    }

    fun onDidVoidChanged(didVoid: Boolean) {
        _uiState.update { state ->
            state.copy(
                didVoid = didVoid,
                // leakAmount is kept — leak is independent of void status
                // Clear volume and color if not voiding
                volumeSize = if (didVoid) state.volumeSize else VolumeSize.UNKNOWN,
                color = if (didVoid) state.color else PeeColor.UNKNOWN,
                // Non-void entries cannot be associated with scheduled times
                scheduledTime = if (didVoid) state.scheduledTime else null
            )
        }
    }

    fun onEntryTypeChanged(type: EntryType) {
        _uiState.update { state ->
            when (type) {
                EntryType.VOID -> state.copy(
                    didVoid = true
                    // leakAmount is kept — voids can optionally have a leak
                )

                EntryType.URGE_ONLY -> state.copy(
                    didVoid = false,
                    leakAmount = LeakAmount.NONE,
                    volumeSize = VolumeSize.UNKNOWN,
                    color = PeeColor.UNKNOWN,
                    scheduledTime = null
                )

                EntryType.LEAK_ONLY -> state.copy(
                    didVoid = false,
                    leakAmount = if (state.leakAmount == LeakAmount.NONE) LeakAmount.SMALL else state.leakAmount,
                    volumeSize = VolumeSize.UNKNOWN,
                    color = PeeColor.UNKNOWN,
                    scheduledTime = null
                )
            }
        }
    }

    fun onLeakAmountChanged(leakAmount: LeakAmount) {
        _uiState.update { it.copy(leakAmount = leakAmount) }
    }

    fun onVolumeSizeChanged(volumeSize: VolumeSize) {
        _uiState.update { it.copy(volumeSize = volumeSize) }
    }

    fun onColorChanged(color: PeeColor) {
        _uiState.update { it.copy(color = color) }
    }

    fun onUrgencyChanged(urgency: Urgency) {
        _uiState.update { it.copy(urgency = urgency) }
    }

    fun onNotesChanged(notes: String) {
        // Limit notes to 500 characters
        val truncated = notes.take(500)
        _uiState.update { it.copy(notes = truncated) }
    }

    fun onActivityContextChanged(activityContext: String) {
        _uiState.update { it.copy(activityContext = activityContext.take(120)) }
    }

    fun onScheduledTimeChanged(scheduledTime: LocalTime?) {
        _uiState.update { state ->
            if (state.didVoid) {
                state.copy(scheduledTime = scheduledTime)
            } else {
                state.copy(scheduledTime = null)
            }
        }
    }

    fun save() {
        viewModelScope.launch(dispatcherProvider.io) {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val state = _uiState.value
                val entry = PeeEntry(
                    id = state.entryId ?: 0,
                    timestamp = state.timestamp,
                    didVoid = state.didVoid,
                    leakAmount = state.leakAmount,
                    volumeSize = if (state.didVoid) state.volumeSize else VolumeSize.UNKNOWN,
                    color = if (state.didVoid) state.color else PeeColor.UNKNOWN,
                    urgency = state.urgency,
                    activityContext = state.activityContext.ifBlank { null },
                    notes = state.notes.ifBlank { null },
                    scheduledTime = if (state.didVoid) state.scheduledTime else null
                )

                if (state.isEditMode) {
                    updatePeeEntryUseCase(entry)
                } else {
                    addPeeEntryUseCase(entry)
                }

                _uiState.update { it.copy(isLoading = false) }
                _events.emit(AddEntryEvent.SaveSuccess)
            } catch (e: MaxEntriesException) {
                _uiState.update { it.copy(isLoading = false) }
                _events.emit(AddEntryEvent.Error("Maximum 50 entries per day reached"))
            } catch (e: DuplicateMinuteException) {
                _uiState.update { it.copy(isLoading = false) }
                _events.emit(AddEntryEvent.Error("An entry already exists for this minute"))
            } catch (e: ValidationException) {
                _uiState.update { it.copy(isLoading = false) }
                _events.emit(AddEntryEvent.Error(e.message ?: "Validation failed"))
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
                _events.emit(AddEntryEvent.Error(e.message ?: "Failed to save entry"))
            }
        }
    }

    fun delete() {
        val entryId = _uiState.value.entryId ?: return

        viewModelScope.launch(dispatcherProvider.io) {
            _uiState.update { it.copy(isLoading = true) }
            try {
                deletePeeEntryUseCase(entryId)
                _uiState.update { it.copy(isLoading = false) }
                _events.emit(AddEntryEvent.DeleteSuccess)
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
                _events.emit(AddEntryEvent.Error(e.message ?: "Failed to delete entry"))
            }
        }
    }
}
