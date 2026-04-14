package com.laniao.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.laniao.domain.model.ExerciseCategory
import com.laniao.domain.model.ExerciseSchedule
import com.laniao.domain.model.ExerciseScheduleItem
import com.laniao.domain.model.ExerciseStatus
import com.laniao.domain.model.ExerciseType
import com.laniao.domain.model.VoidSchedule
import com.laniao.domain.usecase.CheckScheduleOverlapUseCase
import com.laniao.domain.usecase.CreateVoidScheduleUseCase
import com.laniao.domain.usecase.DeleteVoidScheduleUseCase
import com.laniao.domain.usecase.GetActiveSchedulesUseCase
import com.laniao.domain.usecase.GetAllSchedulesUseCase
import com.laniao.domain.usecase.GetScheduleProgressUseCase
import com.laniao.domain.usecase.GetTodayExerciseStatusUseCase
import com.laniao.domain.usecase.ManageExerciseScheduleUseCase
import com.laniao.domain.usecase.ScheduleProgressItem
import com.laniao.domain.usecase.UndoRedoManager
import com.laniao.domain.usecase.UndoableAction
import com.laniao.domain.usecase.UpdateVoidScheduleExpiryUseCase
import com.laniao.domain.repository.ExerciseRepository
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
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

/**
 * UI state for the Schedule screen.
 */
data class ScheduleUiState(
    val activeSchedule: VoidSchedule? = null,
    val upcomingSchedules: List<VoidSchedule> = emptyList(),
    val editingSchedule: VoidSchedule? = null,
    val todayProgress: List<ScheduleProgressItem> = emptyList(),
    // Exercise schedule state
    val activeExerciseSchedule: ExerciseSchedule? = null,
    val exerciseItems: List<ExerciseScheduleItem> = emptyList(),
    val exerciseStatuses: List<ExerciseStatus> = emptyList(),
    val upcomingExerciseSchedules: List<ExerciseSchedule> = emptyList(),
    val pastSchedules: List<VoidSchedule> = emptyList(),
    val pastExerciseSchedules: List<ExerciseSchedule> = emptyList(),
    val showCreateExerciseDialog: Boolean = false,
    val editingExerciseScheduleId: Long? = null,
    val editingExerciseItems: List<ExerciseScheduleItem> = emptyList(),
    val editingExerciseStartDate: LocalDate? = null,
    val editingExerciseEndDate: LocalDate? = null,
    // Shared state
    val isLoading: Boolean = true,
    val showCreateDialog: Boolean = false,
    val showEditDialog: Boolean = false,
    val showOverlapConfirmation: Boolean = false,
    val pendingScheduleParams: ScheduleParams? = null
)

/**
 * Parameters for creating a schedule (used for pending confirmations).
 */
data class ScheduleParams(
    val startTime: LocalTime,
    val endTime: LocalTime,
    val intervalMinutes: Int,
    val startDate: LocalDate,
    val endDate: LocalDate
)

/**
 * One-time events emitted by the Schedule ViewModel.
 */
sealed interface ScheduleEvent {
    data object ScheduleCreated : ScheduleEvent
    data object ScheduleUpdated : ScheduleEvent
    data object ScheduleDeleted : ScheduleEvent
    data object ExpiryUpdated : ScheduleEvent
    data class Error(val message: String) : ScheduleEvent
}

/**
 * ViewModel for the Schedule screen.
 */
@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val getActiveSchedulesUseCase: GetActiveSchedulesUseCase,
    private val getAllSchedulesUseCase: GetAllSchedulesUseCase,
    private val getScheduleProgressUseCase: GetScheduleProgressUseCase,
    private val createVoidScheduleUseCase: CreateVoidScheduleUseCase,
    private val updateVoidScheduleExpiryUseCase: UpdateVoidScheduleExpiryUseCase,
    private val deleteVoidScheduleUseCase: DeleteVoidScheduleUseCase,
    private val checkScheduleOverlapUseCase: CheckScheduleOverlapUseCase,
    private val manageExerciseScheduleUseCase: ManageExerciseScheduleUseCase,
    private val getTodayExerciseStatusUseCase: GetTodayExerciseStatusUseCase,
    private val exerciseRepository: ExerciseRepository,
    private val undoRedoManager: UndoRedoManager,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScheduleUiState())
    val uiState: StateFlow<ScheduleUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<ScheduleEvent>()
    val events: SharedFlow<ScheduleEvent> = _events.asSharedFlow()

    init {
        observeScheduleData()
    }

    private fun observeScheduleData() {
        val today = LocalDate.now()

        // Observe active void schedules
        viewModelScope.launch(dispatcherProvider.io) {
            getActiveSchedulesUseCase().collect { schedules ->
                val activeSchedule = schedules.firstOrNull()
                _uiState.update { it.copy(activeSchedule = activeSchedule, isLoading = false) }
            }
        }

        // Observe active exercise schedule
        viewModelScope.launch(dispatcherProvider.io) {
            exerciseRepository.getActiveForDateFlow(today).collect { schedule ->
                _uiState.update { it.copy(activeExerciseSchedule = schedule) }
                if (schedule != null) {
                    val items = exerciseRepository.getItemsForSchedule(schedule.id)
                    _uiState.update { it.copy(exerciseItems = items) }
                } else {
                    _uiState.update { it.copy(exerciseItems = emptyList()) }
                }
            }
        }

        // Observe all exercise schedules for upcoming and past
        viewModelScope.launch(dispatcherProvider.io) {
            exerciseRepository.getAllSchedules().collect { schedules ->
                val upcoming = schedules
                    .filter { it.enabled && it.startDate > today }
                    .sortedBy { it.startDate }
                val past = schedules
                    .filter { it.endDate != null && it.endDate < today }
                    .sortedByDescending { it.endDate }
                _uiState.update { it.copy(upcomingExerciseSchedules = upcoming, pastExerciseSchedules = past) }
            }
        }

        // Observe today's exercise completion status
        viewModelScope.launch(dispatcherProvider.io) {
            getTodayExerciseStatusUseCase(today).collect { statuses ->
                _uiState.update { it.copy(exerciseStatuses = statuses) }
            }
        }

        // Observe all schedules and derive upcoming and past schedules
        viewModelScope.launch(dispatcherProvider.io) {
            getAllSchedulesUseCase().collect { schedules ->
                val upcoming = schedules
                    .filter { it.enabled && it.createdAt > today }
                    .sortedBy { it.createdAt }
                val past = schedules
                    .filter { it.expiresAt < today }
                    .sortedByDescending { it.expiresAt }

                _uiState.update { it.copy(upcomingSchedules = upcoming, pastSchedules = past) }
            }
        }

        // Observe today's progress
        viewModelScope.launch(dispatcherProvider.io) {
            getScheduleProgressUseCase().collect { progress ->
                _uiState.update { it.copy(todayProgress = progress) }
            }
        }
    }

    /**
     * Show the create schedule dialog.
     */
    fun showCreateDialog() {
        _uiState.update { it.copy(showCreateDialog = true) }
    }

    /**
     * Copy an existing schedule — opens create dialog pre-filled with the source schedule's parameters.
     * Start date defaults to the day after the source schedule expires.
     */
    fun copySchedule(source: VoidSchedule) {
        val durationDays = (source.expiresAt.toEpochDay() - source.createdAt.toEpochDay()).toInt()
        val newStartDate = source.expiresAt.plusDays(1)
        val newEndDate = newStartDate.plusDays(durationDays.toLong())
        _uiState.update {
            it.copy(
                showCreateDialog = true,
                pendingScheduleParams = ScheduleParams(
                    startTime = source.startTime,
                    endTime = source.endTime,
                    intervalMinutes = source.intervalMinutes,
                    startDate = newStartDate,
                    endDate = newEndDate
                )
            )
        }
    }

    /**
     * Hide the create schedule dialog.
     */
    fun hideCreateDialog() {
        _uiState.update { it.copy(showCreateDialog = false, pendingScheduleParams = null) }
    }

    /**
     * Show the edit schedule dialog (pre-filled with current values).
     */
    fun showEditDialog(schedule: VoidSchedule? = null) {
        _uiState.update {
            it.copy(
                showEditDialog = true,
                editingSchedule = schedule ?: it.activeSchedule
            )
        }
    }

    /**
     * Hide the edit schedule dialog.
     */
    fun hideEditDialog() {
        _uiState.update { it.copy(showEditDialog = false, editingSchedule = null) }
    }

    /**
     * Edit the existing schedule by replacing it with updated parameters.
     */
    fun editSchedule(
        startTime: LocalTime,
        endTime: LocalTime,
        intervalMinutes: Int,
        startDate: LocalDate,
        endDate: LocalDate
    ) {
        viewModelScope.launch(dispatcherProvider.io) {
            try {
                createVoidScheduleUseCase(
                    startTime = startTime,
                    endTime = endTime,
                    intervalMinutes = intervalMinutes,
                    replaceExisting = true,
                    startDate = startDate,
                    endDate = endDate
                )
                _uiState.update { it.copy(showEditDialog = false, editingSchedule = null) }
                _events.emit(ScheduleEvent.ScheduleCreated)
            } catch (e: IllegalArgumentException) {
                _events.emit(ScheduleEvent.Error(e.message ?: "Invalid schedule parameters"))
            } catch (e: Exception) {
                _events.emit(ScheduleEvent.Error(e.message ?: "Failed to update schedule"))
            }
        }
    }

    /**
     * Create a new schedule.
     */
    fun createSchedule(
        startTime: LocalTime,
        endTime: LocalTime,
        intervalMinutes: Int,
        startDate: LocalDate,
        endDate: LocalDate
    ) {
        viewModelScope.launch(dispatcherProvider.io) {
            try {
                // Check for overlap first
                val hasOverlap = checkScheduleOverlapUseCase(startDate, endDate)

                if (hasOverlap) {
                    // Store params and show confirmation dialog
                    _uiState.update {
                        it.copy(
                            showCreateDialog = false,
                            showOverlapConfirmation = true,
                            pendingScheduleParams = ScheduleParams(
                                startTime, endTime, intervalMinutes, startDate, endDate
                            )
                        )
                    }
                } else {
                    // Create directly
                    val schedule = createVoidScheduleUseCase(
                        startTime = startTime,
                        endTime = endTime,
                        intervalMinutes = intervalMinutes,
                        replaceExisting = false,
                        startDate = startDate,
                        endDate = endDate
                    )
                    undoRedoManager.push(UndoableAction.CreateSchedule(schedule))
                    _uiState.update { it.copy(showCreateDialog = false) }
                    _events.emit(ScheduleEvent.ScheduleCreated)
                }
            } catch (e: IllegalArgumentException) {
                _events.emit(ScheduleEvent.Error(e.message ?: "Invalid schedule parameters"))
            } catch (e: Exception) {
                _events.emit(ScheduleEvent.Error(e.message ?: "Failed to create schedule"))
            }
        }
    }

    /**
     * Confirm replacing the existing schedule.
     */
    fun confirmReplaceSchedule() {
        val params = _uiState.value.pendingScheduleParams ?: return

        viewModelScope.launch(dispatcherProvider.io) {
            try {
                val schedule = createVoidScheduleUseCase(
                    startTime = params.startTime,
                    endTime = params.endTime,
                    intervalMinutes = params.intervalMinutes,
                    replaceExisting = true,
                    startDate = params.startDate,
                    endDate = params.endDate
                )
                undoRedoManager.push(UndoableAction.CreateSchedule(schedule))
                _uiState.update {
                    it.copy(
                        showOverlapConfirmation = false,
                        pendingScheduleParams = null
                    )
                }
                _events.emit(ScheduleEvent.ScheduleCreated)
            } catch (e: Exception) {
                _events.emit(ScheduleEvent.Error(e.message ?: "Failed to create schedule"))
            }
        }
    }

    /**
     * Cancel replacing the existing schedule.
     */
    fun cancelReplaceSchedule() {
        _uiState.update {
            it.copy(
                showOverlapConfirmation = false,
                pendingScheduleParams = null,
                showCreateDialog = true
            )
        }
    }

    /**
     * Update the expiry date of the active schedule.
     */
    fun updateExpiry(newExpiryDate: LocalDate) {
        val scheduleId = _uiState.value.activeSchedule?.id ?: return

        viewModelScope.launch(dispatcherProvider.io) {
            try {
                updateVoidScheduleExpiryUseCase(scheduleId, newExpiryDate)
                _events.emit(ScheduleEvent.ExpiryUpdated)
            } catch (e: IllegalArgumentException) {
                _events.emit(ScheduleEvent.Error(e.message ?: "Invalid expiry date"))
            } catch (e: Exception) {
                _events.emit(ScheduleEvent.Error(e.message ?: "Failed to update expiry"))
            }
        }
    }

    /**
     * Delete the active schedule.
     */
    fun deleteSchedule() {
        val schedule = _uiState.value.activeSchedule ?: return

        viewModelScope.launch(dispatcherProvider.io) {
            try {
                deleteVoidScheduleUseCase(schedule.id)
                undoRedoManager.push(UndoableAction.DeleteSchedule(schedule))
                _events.emit(ScheduleEvent.ScheduleDeleted)
            } catch (e: Exception) {
                _events.emit(ScheduleEvent.Error(e.message ?: "Failed to delete schedule"))
            }
        }
    }

    /**
     * Delete a specific void schedule by ID (used for upcoming schedules).
     */
    fun deleteScheduleById(schedule: VoidSchedule) {
        viewModelScope.launch(dispatcherProvider.io) {
            try {
                deleteVoidScheduleUseCase(schedule.id)
                undoRedoManager.push(UndoableAction.DeleteSchedule(schedule))
                _events.emit(ScheduleEvent.ScheduleDeleted)
            } catch (e: Exception) {
                _events.emit(ScheduleEvent.Error(e.message ?: "Failed to delete schedule"))
            }
        }
    }

    // ==================== Exercise Schedule ====================

    fun showCreateExerciseDialog() {
        _uiState.update { it.copy(showCreateExerciseDialog = true, editingExerciseScheduleId = null, editingExerciseItems = emptyList(), editingExerciseStartDate = null, editingExerciseEndDate = null) }
    }

    /**
     * Copy an existing exercise schedule — opens create dialog pre-filled with the source schedule's items and shifted dates.
     */
    fun copyExerciseSchedule(schedule: ExerciseSchedule) {
        viewModelScope.launch(dispatcherProvider.io) {
            val items = exerciseRepository.getItemsForSchedule(schedule.id)
            val endDate = schedule.endDate
            val durationDays = if (endDate != null) {
                (endDate.toEpochDay() - schedule.startDate.toEpochDay()).toInt()
            } else {
                6 // default 7 days
            }
            val newStartDate = (endDate ?: schedule.startDate).plusDays(1)
            val newEndDate = newStartDate.plusDays(durationDays.toLong())
            _uiState.update {
                it.copy(
                    showCreateExerciseDialog = true,
                    editingExerciseScheduleId = null, // null = create new, not edit
                    editingExerciseItems = items,
                    editingExerciseStartDate = newStartDate,
                    editingExerciseEndDate = newEndDate
                )
            }
        }
    }

    fun showEditExerciseDialog() {
        val schedule = _uiState.value.activeExerciseSchedule ?: return
        viewModelScope.launch(dispatcherProvider.io) {
            val items = exerciseRepository.getItemsForSchedule(schedule.id)
            _uiState.update { it.copy(showCreateExerciseDialog = true, editingExerciseScheduleId = schedule.id, editingExerciseItems = items, editingExerciseStartDate = schedule.startDate, editingExerciseEndDate = schedule.endDate) }
        }
    }

    fun showEditExerciseDialogForSchedule(scheduleId: Long) {
        viewModelScope.launch(dispatcherProvider.io) {
            val schedule = exerciseRepository.getScheduleById(scheduleId) ?: return@launch
            val items = exerciseRepository.getItemsForSchedule(scheduleId)
            _uiState.update { it.copy(showCreateExerciseDialog = true, editingExerciseScheduleId = scheduleId, editingExerciseItems = items, editingExerciseStartDate = schedule.startDate, editingExerciseEndDate = schedule.endDate) }
        }
    }

    fun hideCreateExerciseDialog() {
        _uiState.update { it.copy(showCreateExerciseDialog = false, editingExerciseScheduleId = null, editingExerciseItems = emptyList(), editingExerciseStartDate = null, editingExerciseEndDate = null) }
    }

    fun createExerciseSchedule(
        startDate: LocalDate,
        endDate: LocalDate?,
        items: List<ExerciseScheduleItem>
    ) {
        viewModelScope.launch(dispatcherProvider.io) {
            try {
                val scheduleId = manageExerciseScheduleUseCase.create(
                    startDate = startDate,
                    endDate = endDate,
                    items = items,
                    replaceExisting = true
                )
                val schedule = exerciseRepository.getScheduleById(scheduleId)
                val savedItems = exerciseRepository.getItemsForSchedule(scheduleId)
                if (schedule != null) {
                    undoRedoManager.push(UndoableAction.CreateExerciseSchedule(schedule, savedItems))
                }
                _uiState.update { it.copy(showCreateExerciseDialog = false, editingExerciseScheduleId = null, editingExerciseItems = emptyList()) }
                _events.emit(ScheduleEvent.ScheduleCreated)
            } catch (e: Exception) {
                _events.emit(ScheduleEvent.Error(e.message ?: "Failed to create exercise schedule"))
            }
        }
    }

    fun updateExerciseSchedule(
        scheduleId: Long,
        startDate: LocalDate,
        endDate: LocalDate?,
        items: List<ExerciseScheduleItem>
    ) {
        viewModelScope.launch(dispatcherProvider.io) {
            try {
                val beforeSchedule = exerciseRepository.getScheduleById(scheduleId)
                val beforeItems = exerciseRepository.getItemsForSchedule(scheduleId)
                manageExerciseScheduleUseCase.update(scheduleId, startDate, endDate, items)
                val afterSchedule = exerciseRepository.getScheduleById(scheduleId)
                val afterItems = exerciseRepository.getItemsForSchedule(scheduleId)
                if (beforeSchedule != null && afterSchedule != null) {
                    undoRedoManager.push(UndoableAction.UpdateExerciseSchedule(beforeSchedule, beforeItems, afterSchedule, afterItems))
                }
                _uiState.update { it.copy(showCreateExerciseDialog = false, editingExerciseScheduleId = null, editingExerciseItems = emptyList()) }
                _events.emit(ScheduleEvent.ScheduleUpdated)
            } catch (e: Exception) {
                _events.emit(ScheduleEvent.Error(e.message ?: "Failed to update exercise schedule"))
            }
        }
    }

    fun deleteExerciseSchedule() {
        val scheduleId = _uiState.value.activeExerciseSchedule?.id ?: return
        viewModelScope.launch(dispatcherProvider.io) {
            try {
                val schedule = exerciseRepository.getScheduleById(scheduleId)
                val items = exerciseRepository.getItemsForSchedule(scheduleId)
                manageExerciseScheduleUseCase.delete(scheduleId)
                if (schedule != null) {
                    undoRedoManager.push(UndoableAction.DeleteExerciseSchedule(schedule, items))
                }
                _events.emit(ScheduleEvent.ScheduleDeleted)
            } catch (e: Exception) {
                _events.emit(ScheduleEvent.Error(e.message ?: "Failed to delete exercise schedule"))
            }
        }
    }

    fun deleteExerciseScheduleById(scheduleId: Long) {
        viewModelScope.launch(dispatcherProvider.io) {
            try {
                val schedule = exerciseRepository.getScheduleById(scheduleId)
                val items = exerciseRepository.getItemsForSchedule(scheduleId)
                manageExerciseScheduleUseCase.delete(scheduleId)
                if (schedule != null) {
                    undoRedoManager.push(UndoableAction.DeleteExerciseSchedule(schedule, items))
                }
                _events.emit(ScheduleEvent.ScheduleDeleted)
            } catch (e: Exception) {
                _events.emit(ScheduleEvent.Error(e.message ?: "Failed to delete exercise schedule"))
            }
        }
    }
}
