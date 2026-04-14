package com.laniao.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.laniao.domain.exception.DuplicateMinuteException
import com.laniao.domain.exception.MaxEntriesException
import com.laniao.domain.model.DailySummary
import com.laniao.domain.model.DrinkEntry
import com.laniao.domain.model.DrinkType
import com.laniao.domain.model.DrinkUnit
import com.laniao.domain.model.ExerciseStatus
import com.laniao.domain.model.ExerciseType
import com.laniao.domain.model.PeeColor
import com.laniao.domain.model.PeeEntry
import com.laniao.domain.model.Urgency
import com.laniao.domain.model.VolumeSize
import com.laniao.domain.repository.DrinkEntryRepository
import com.laniao.domain.repository.ExerciseRepository
import com.laniao.domain.repository.PeeEntryRepository
import com.laniao.domain.usecase.AddDrinkEntryUseCase
import com.laniao.domain.usecase.AddPeeEntryUseCase
import com.laniao.domain.usecase.CompleteExerciseUseCase
import com.laniao.domain.usecase.DeleteDrinkEntryUseCase
import com.laniao.domain.usecase.DeletePeeEntryUseCase
import com.laniao.domain.usecase.GetExerciseStreakUseCase
import com.laniao.domain.usecase.GetHydrationStreakUseCase
import com.laniao.domain.usecase.GetRecentEntriesUseCase
import com.laniao.domain.usecase.GetTodayDrinksUseCase
import com.laniao.domain.usecase.GetTodayExerciseStatusUseCase
import com.laniao.domain.usecase.GetTodaySummaryUseCase
import com.laniao.domain.usecase.GetScheduleProgressUseCase
import com.laniao.domain.usecase.ScheduleProgressItem
import com.laniao.domain.usecase.UndoRedoManager
import com.laniao.domain.usecase.UndoableAction
import com.laniao.domain.usecase.UpdatePeeEntryUseCase
import com.laniao.util.Clock
import com.laniao.util.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

/**
 * UI state for the Home screen.
 */
data class HomeUiState(
    val summary: DailySummary = DailySummary(),
    val recentEntries: List<PeeEntry> = emptyList(),
    val todayDrinks: List<DrinkEntry> = emptyList(),
    val todayLiters: Double = 0.0,
    val exerciseStatuses: List<ExerciseStatus> = emptyList(),
    val exerciseStreak: Int = 0,
    val hydrationStreak: Int = 0,
    val scheduleProgress: List<ScheduleProgressItem> = emptyList(),
    val hasActiveSchedule: Boolean = false,
    val isQuickAddInProgress: Boolean = false,
    val isLoading: Boolean = true
)

/**
 * One-time events emitted by the Home ViewModel.
 */
sealed interface HomeEvent {
    data object QuickAddSuccess : HomeEvent
    data object DrinkAdded : HomeEvent
    data object ExerciseCompleted : HomeEvent
    data object EntrySaved : HomeEvent
    data object EntryDeleted : HomeEvent
    data class QuickAddError(val message: String) : HomeEvent
    data class DrinkError(val message: String) : HomeEvent
    data class EntryError(val message: String) : HomeEvent
}

/**
 * ViewModel for the Home screen.
 * Displays daily summary (including total output), recent entries, and handles Quick Add.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val addPeeEntryUseCase: AddPeeEntryUseCase,
    private val updatePeeEntryUseCase: UpdatePeeEntryUseCase,
    private val deletePeeEntryUseCase: DeletePeeEntryUseCase,
    private val getTodaySummaryUseCase: GetTodaySummaryUseCase,
    private val getRecentEntriesUseCase: GetRecentEntriesUseCase,
    private val addDrinkEntryUseCase: AddDrinkEntryUseCase,
    private val deleteDrinkEntryUseCase: DeleteDrinkEntryUseCase,
    private val getTodayDrinksUseCase: GetTodayDrinksUseCase,
    private val drinkEntryRepository: DrinkEntryRepository,
    private val peeEntryRepository: PeeEntryRepository,
    private val exerciseRepository: ExerciseRepository,
    private val getTodayExerciseStatusUseCase: GetTodayExerciseStatusUseCase,
    private val completeExerciseUseCase: CompleteExerciseUseCase,
    private val getExerciseStreakUseCase: GetExerciseStreakUseCase,
    private val getHydrationStreakUseCase: GetHydrationStreakUseCase,
    private val getScheduleProgressUseCase: GetScheduleProgressUseCase,
    private val undoRedoManager: UndoRedoManager,
    private val clock: Clock,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<HomeEvent>()
    val events: SharedFlow<HomeEvent> = _events.asSharedFlow()

    private var summaryJob: Job? = null
    private var recentEntriesJob: Job? = null
    private var drinksJob: Job? = null
    private var litersJob: Job? = null
    private var exerciseJob: Job? = null
    private var scheduleJob: Job? = null

    init {
        refreshTodayData()
    }

    fun refreshTodayData() {
        summaryJob?.cancel()
        recentEntriesJob?.cancel()
        drinksJob?.cancel()
        litersJob?.cancel()
        exerciseJob?.cancel()
        scheduleJob?.cancel()

        val today = clock.now().atZone(java.time.ZoneId.systemDefault()).toLocalDate()

        // Observe summary (includes total output volume)
        summaryJob = viewModelScope.launch(dispatcherProvider.io) {
            getTodaySummaryUseCase().collect { summary ->
                _uiState.update { it.copy(summary = summary, isLoading = false) }
            }
        }

        // Observe recent entries
        recentEntriesJob = viewModelScope.launch(dispatcherProvider.io) {
            getRecentEntriesUseCase().collect { entries ->
                _uiState.update { it.copy(recentEntries = entries) }
            }
        }

        // Observe today's drinks
        drinksJob = viewModelScope.launch(dispatcherProvider.io) {
            getTodayDrinksUseCase.entries().collect { drinks ->
                _uiState.update { it.copy(todayDrinks = drinks) }
            }
        }

        // Observe today's total liters
        litersJob = viewModelScope.launch(dispatcherProvider.io) {
            getTodayDrinksUseCase.totalLiters().collect { liters ->
                _uiState.update { it.copy(todayLiters = liters) }
            }
        }

        // Observe today's exercise status
        exerciseJob = viewModelScope.launch(dispatcherProvider.io) {
            getTodayExerciseStatusUseCase(today).collect { statuses ->
                val exerciseStreak = getExerciseStreakUseCase(today)
                val hydrationStreak = getHydrationStreakUseCase(today)
                _uiState.update { it.copy(exerciseStatuses = statuses, exerciseStreak = exerciseStreak, hydrationStreak = hydrationStreak) }
            }
        }

        // Observe today's schedule progress
        scheduleJob = viewModelScope.launch(dispatcherProvider.io) {
            getScheduleProgressUseCase().collect { progress ->
                _uiState.update { it.copy(scheduleProgress = progress, hasActiveSchedule = progress.isNotEmpty()) }
            }
        }
    }

    /**
     * Perform Quick Add - saves entry immediately with defaults.
     * No navigation to form screen.
     */
    fun quickAdd() {
        viewModelScope.launch(dispatcherProvider.io) {
            _uiState.update { it.copy(isQuickAddInProgress = true) }
            try {
                val entry = PeeEntry(
                    timestamp = clock.now(),
                    didVoid = true,
                    volumeSize = VolumeSize.UNKNOWN,
                    color = PeeColor.UNKNOWN,
                    urgency = Urgency.UNKNOWN,
                    notes = null,
                    scheduledTime = null
                )
                val id = addPeeEntryUseCase(entry)
                undoRedoManager.push(UndoableAction.AddEntry(entry.copy(id = id)))
                _uiState.update { it.copy(isQuickAddInProgress = false) }
                _events.emit(HomeEvent.QuickAddSuccess)
            } catch (e: MaxEntriesException) {
                _uiState.update { it.copy(isQuickAddInProgress = false) }
                _events.emit(HomeEvent.QuickAddError("Maximum 50 entries per day reached"))
            } catch (e: DuplicateMinuteException) {
                _uiState.update { it.copy(isQuickAddInProgress = false) }
                _events.emit(HomeEvent.QuickAddError("An entry already exists for this minute"))
            } catch (e: Exception) {
                _uiState.update { it.copy(isQuickAddInProgress = false) }
                _events.emit(HomeEvent.QuickAddError(e.message ?: "Failed to save entry"))
            }
        }
    }

    /**
     * Add a drink entry.
     */
    fun addDrink(type: DrinkType, amount: Double, unit: DrinkUnit, customName: String? = null, timestamp: Instant? = null) {
        viewModelScope.launch(dispatcherProvider.io) {
            try {
                val entry = DrinkEntry(
                    timestamp = timestamp ?: clock.now(),
                    type = type,
                    amount = amount,
                    unit = unit,
                    customName = if (type == DrinkType.CUSTOM) customName else null
                )
                val id = addDrinkEntryUseCase(entry)
                undoRedoManager.push(UndoableAction.AddDrink(entry.copy(id = id)))
                _events.emit(HomeEvent.DrinkAdded)
            } catch (e: Exception) {
                _events.emit(HomeEvent.DrinkError(e.message ?: "Failed to add drink"))
            }
        }
    }

    /**
     * Update an existing drink entry.
     */
    fun updateDrink(entry: DrinkEntry) {
        viewModelScope.launch(dispatcherProvider.io) {
            try {
                val before = drinkEntryRepository.getById(entry.id)
                drinkEntryRepository.update(entry)
                if (before != null) {
                    undoRedoManager.push(UndoableAction.UpdateDrink(before, entry))
                }
                _events.emit(HomeEvent.DrinkAdded)
            } catch (e: Exception) {
                _events.emit(HomeEvent.DrinkError(e.message ?: "Failed to update drink"))
            }
        }
    }

    /**
     * Delete a drink entry.
     */
    fun deleteDrink(entry: DrinkEntry) {
        viewModelScope.launch(dispatcherProvider.io) {
            try {
                deleteDrinkEntryUseCase(entry)
                undoRedoManager.push(UndoableAction.DeleteDrink(entry))
            } catch (e: Exception) {
                _events.emit(HomeEvent.DrinkError(e.message ?: "Failed to delete drink"))
            }
        }
    }

    /**
     * Complete an exercise session.
     */
    fun completeExercise(exerciseType: ExerciseType, scheduleItemId: Long? = null) {
        viewModelScope.launch(dispatcherProvider.io) {
            try {
                val completion = completeExerciseUseCase(exerciseType, scheduleItemId)
                undoRedoManager.push(UndoableAction.AddExercise(completion))
                _events.emit(HomeEvent.ExerciseCompleted)
            } catch (e: Exception) {
                // Silently ignore — UI will update via Flow
            }
        }
    }

    /**
     * Complete an exercise session at a specific timestamp.
     */
    fun completeExerciseAt(exerciseType: ExerciseType, scheduleItemId: Long? = null, timestamp: Instant) {
        viewModelScope.launch(dispatcherProvider.io) {
            try {
                val today = timestamp.atZone(java.time.ZoneId.systemDefault()).toLocalDate()
                val completion = com.laniao.domain.model.ExerciseCompletion(
                    scheduleItemId = scheduleItemId,
                    exerciseType = exerciseType,
                    completedAt = timestamp,
                    scheduledDate = today
                )
                val id = exerciseRepository.insertCompletion(completion)
                undoRedoManager.push(UndoableAction.AddExercise(completion.copy(id = id)))
                _events.emit(HomeEvent.ExerciseCompleted)
            } catch (e: Exception) {
                // Silently ignore
            }
        }
    }

    fun savePeeEntry(entry: PeeEntry) {
        viewModelScope.launch(dispatcherProvider.io) {
            try {
                if (entry.id > 0) {
                    val before = peeEntryRepository.getById(entry.id)
                    updatePeeEntryUseCase(entry)
                    if (before != null) {
                        undoRedoManager.push(UndoableAction.UpdateEntry(before, entry))
                    }
                } else {
                    val id = addPeeEntryUseCase(entry)
                    undoRedoManager.push(UndoableAction.AddEntry(entry.copy(id = id)))
                }
                _events.emit(HomeEvent.EntrySaved)
            } catch (e: MaxEntriesException) {
                _events.emit(HomeEvent.EntryError("Maximum 50 entries per day reached"))
            } catch (e: DuplicateMinuteException) {
                _events.emit(HomeEvent.EntryError("An entry already exists for this minute"))
            } catch (e: Exception) {
                _events.emit(HomeEvent.EntryError(e.message ?: "Failed to save entry"))
            }
        }
    }

    fun deletePeeEntry(entryId: Long) {
        viewModelScope.launch(dispatcherProvider.io) {
            try {
                val entry = peeEntryRepository.getById(entryId)
                deletePeeEntryUseCase(entryId)
                if (entry != null) {
                    undoRedoManager.push(UndoableAction.DeleteEntry(entry))
                }
                _events.emit(HomeEvent.EntryDeleted)
            } catch (e: Exception) {
                _events.emit(HomeEvent.EntryError(e.message ?: "Failed to delete entry"))
            }
        }
    }
}
