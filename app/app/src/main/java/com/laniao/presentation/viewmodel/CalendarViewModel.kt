package com.laniao.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.laniao.domain.model.CalendarDayStats
import com.laniao.domain.model.DrinkEntry
import com.laniao.domain.model.DrinkType
import com.laniao.domain.model.DrinkUnit
import com.laniao.domain.model.ExerciseCompletion
import com.laniao.domain.model.ExerciseType
import com.laniao.domain.model.PeeEntry
import com.laniao.domain.repository.DrinkEntryRepository
import com.laniao.domain.repository.ExerciseRepository
import com.laniao.domain.repository.ManuallyMissedTimeRepository
import com.laniao.domain.repository.PeeEntryRepository
import com.laniao.domain.repository.VoidScheduleRepository
import com.laniao.domain.usecase.AddPeeEntryUseCase
import com.laniao.domain.usecase.DeletePeeEntryUseCase
import com.laniao.domain.usecase.GetDayTimelineUseCase
import com.laniao.domain.usecase.GetDaysWithEntriesUseCase
import com.laniao.domain.usecase.GetMonthDayStatsUseCase
import com.laniao.domain.usecase.TimelineItem
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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import javax.inject.Inject

/**
 * View mode for the calendar screen.
 */
enum class CalendarViewMode {
    CALENDAR,
    DAY_LIST
}

enum class CalendarDetailMode {
    TIMELINE,
    LIST
}

/**
 * Filter types for calendar entries.
 */
enum class EntryTypeFilter(val label: String, val emoji: String) {
    VOID("Voids", "\uD83D\uDEBD"),
    URGE("Urges", "\uD83D\uDE23"),
    LEAK("Leaks", "\uD83C\uDF88"),
    DRINK("Drinks", "\uD83D\uDCA7"),
    EXERCISE("Exercises", "\uD83D\uDCAA")
}

/**
 * A merged feed item representing either a pee entry, drink entry, or missed schedule time.
 */
data class FeedItem(
    val peeEntry: PeeEntry? = null,
    val drinkEntry: DrinkEntry? = null,
    val exerciseCompletion: ExerciseCompletion? = null,
    val missedScheduleTime: java.time.LocalTime? = null,
    val timestamp: Instant,
    val date: LocalDate,
    val key: String
)

/**
 * UI state for the Calendar screen.
 */
data class CalendarUiState(
    val viewMode: CalendarViewMode = CalendarViewMode.CALENDAR,
    val detailMode: CalendarDetailMode = CalendarDetailMode.TIMELINE,
    val selectedMonth: YearMonth = YearMonth.now(),
    val selectedDate: LocalDate = LocalDate.now(),
    val slideDirection: Int = 0, // 1 = forward (slide left), -1 = backward (slide right)
    val dayStats: Map<LocalDate, CalendarDayStats> = emptyMap(),
    val timelineItems: List<TimelineItem> = emptyList(),
    val allEntries: List<PeeEntry> = emptyList(),
    val dayDrinks: List<DrinkEntry> = emptyList(),
    val dayExercises: List<ExerciseCompletion> = emptyList(),
    // Feed state for list mode
    val feedItems: List<FeedItem> = emptyList(),
    val feedEarliestDate: LocalDate = LocalDate.now(),
    val isLoadingMore: Boolean = false,
    val isLoading: Boolean = true,
    val scrollToNowPending: Boolean = false,
    val scrollToNowAnimated: Boolean = false,
    val timelineScrollOffsetPx: Float = 0f,
    val timelineHourHeightDp: Float = 60f,
    val activeFilters: Set<EntryTypeFilter> = emptySet() // empty = show all
)

/**
 * One-time events emitted by the Calendar ViewModel.
 */
sealed interface CalendarEvent {
    data object EntryDeleted : CalendarEvent
    data object EntrySaved : CalendarEvent
    data object DrinkUpdated : CalendarEvent
    data object DrinkAdded : CalendarEvent
    data object ExerciseCompleted : CalendarEvent
    data object ExerciseUpdated : CalendarEvent
    data class Error(val message: String) : CalendarEvent
}

/**
 * ViewModel for the Calendar screen.
 * Manages calendar view and day timeline/list views.
 */
@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val getDaysWithEntriesUseCase: GetDaysWithEntriesUseCase,
    private val getDayTimelineUseCase: GetDayTimelineUseCase,
    private val getMonthDayStatsUseCase: GetMonthDayStatsUseCase,
    private val deleteEntryUseCase: DeletePeeEntryUseCase,
    private val addPeeEntryUseCase: AddPeeEntryUseCase,
    private val updatePeeEntryUseCase: UpdatePeeEntryUseCase,
    private val peeEntryRepository: PeeEntryRepository,
    private val drinkEntryRepository: DrinkEntryRepository,
    private val exerciseRepository: ExerciseRepository,
    private val voidScheduleRepository: VoidScheduleRepository,
    private val manuallyMissedTimeRepository: ManuallyMissedTimeRepository,
    private val undoRedoManager: UndoRedoManager,
    private val clock: Clock,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState: StateFlow<CalendarUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<CalendarEvent>()
    val events: SharedFlow<CalendarEvent> = _events.asSharedFlow()

    private var calendarJob: Job? = null
    private var dayStatsJob: Job? = null
    private var dayJob: Job? = null
    private var drinksJob: Job? = null
    private var exercisesJob: Job? = null
    private var feedJob: Job? = null

    init {
        val today = clock.now().atZone(ZoneId.systemDefault()).toLocalDate()
        _uiState.update { it.copy(selectedDate = today, selectedMonth = YearMonth.from(today)) }
        loadMonthDayStats()
        loadDayData()
    }

    /**
     * Select a date from the calendar month grid — always opens timeline view.
     */
    fun selectDate(date: LocalDate) {
        val direction = date.compareTo(_uiState.value.selectedDate).coerceIn(-1, 1)
        _uiState.update { 
            it.copy(
                selectedDate = date,
                slideDirection = direction,
                viewMode = CalendarViewMode.DAY_LIST,
                detailMode = CalendarDetailMode.TIMELINE,
                scrollToNowPending = true,
                scrollToNowAnimated = false
            )
        }
        loadDayData()
    }

    /**
     * Navigate to a different month.
     */
    fun changeMonth(yearMonth: YearMonth) {
        _uiState.update { it.copy(selectedMonth = yearMonth) }
        loadMonthDayStats()
    }

    /**
     * Go to previous month.
     */
    fun previousMonth() {
        changeMonth(_uiState.value.selectedMonth.minusMonths(1))
    }

    /**
     * Go to next month.
     */
    fun nextMonth() {
        changeMonth(_uiState.value.selectedMonth.plusMonths(1))
    }

    /**
     * Go to previous day (swipe right on timeline).
     */
    fun previousDay() {
        val newDate = _uiState.value.selectedDate.minusDays(1)
        _uiState.update {
            it.copy(
                selectedDate = newDate,
                slideDirection = -1,
                selectedMonth = YearMonth.from(newDate)
            )
        }
        loadMonthDayStats()
        loadDayData()
    }

    /**
     * Go to next day (swipe left on timeline).
     */
    fun nextDay() {
        val newDate = _uiState.value.selectedDate.plusDays(1)
        _uiState.update {
            it.copy(
                selectedDate = newDate,
                slideDirection = 1,
                selectedMonth = YearMonth.from(newDate)
            )
        }
        loadMonthDayStats()
        loadDayData()
    }

    /**
     * Toggle between calendar and day list view.
     */
    fun toggleViewMode() {
        _uiState.update { state ->
            state.copy(
                viewMode = when (state.viewMode) {
                    CalendarViewMode.CALENDAR -> CalendarViewMode.DAY_LIST
                    CalendarViewMode.DAY_LIST -> CalendarViewMode.CALENDAR
                }
            )
        }
    }

    /**
     * Switch to calendar view.
     */
    fun showCalendar() {
        _uiState.update { it.copy(viewMode = CalendarViewMode.CALENDAR) }
    }

    fun showTimeline() {
        _uiState.update { it.copy(detailMode = CalendarDetailMode.TIMELINE) }
    }

    fun showList() {
        _uiState.update { it.copy(detailMode = CalendarDetailMode.LIST) }
        loadFeed()
    }

    /**
     * Load the chronological feed starting from today only.
     * Past entries are loaded on demand when user scrolls up.
     */
    private fun loadFeed() {
        feedJob?.cancel()
        val zoneId = ZoneId.systemDefault()
        val today = clock.now().atZone(zoneId).toLocalDate()
        _uiState.update { it.copy(feedEarliestDate = today, isLoading = true) }

        feedJob = viewModelScope.launch(dispatchers.io) {
            peeEntryRepository.getByDateRange(today, today)
                .combine(drinkEntryRepository.getByDateRange(today, today)) { peeEntries, drinkEntries ->
                    Pair(peeEntries, drinkEntries)
                }
                .combine(exerciseRepository.getCompletionsByDateRange(today, today)) { (peeEntries, drinkEntries), exercises ->
                    Triple(peeEntries, drinkEntries, exercises)
                }
                .combine(manuallyMissedTimeRepository.getByDateRange(today, today)) { (peeEntries, drinkEntries, exercises), missedTimes ->
                    buildFeedItems(peeEntries, drinkEntries, exercises, missedTimes, zoneId)
                }
                .collect { items ->
                    _uiState.update { it.copy(feedItems = items, isLoading = false) }
                }
        }
    }

    /**
     * Load one more week of history (called when user scrolls to the top).
     */
    fun loadMoreHistory() {
        if (_uiState.value.isLoadingMore) return
        _uiState.update { it.copy(isLoadingMore = true) }

        feedJob?.cancel()
        val zoneId = ZoneId.systemDefault()
        val today = clock.now().atZone(zoneId).toLocalDate()
        val newEarliest = _uiState.value.feedEarliestDate.minusDays(7)
        _uiState.update { it.copy(feedEarliestDate = newEarliest) }

        feedJob = viewModelScope.launch(dispatchers.io) {
            peeEntryRepository.getByDateRange(newEarliest, today)
                .combine(drinkEntryRepository.getByDateRange(newEarliest, today)) { peeEntries, drinkEntries ->
                    Pair(peeEntries, drinkEntries)
                }
                .combine(exerciseRepository.getCompletionsByDateRange(newEarliest, today)) { (peeEntries, drinkEntries), exercises ->
                    Triple(peeEntries, drinkEntries, exercises)
                }
                .combine(manuallyMissedTimeRepository.getByDateRange(newEarliest, today)) { (peeEntries, drinkEntries, exercises), missedTimes ->
                    buildFeedItems(peeEntries, drinkEntries, exercises, missedTimes, zoneId)
                }
                .collect { items ->
                    _uiState.update { it.copy(feedItems = items, isLoadingMore = false) }
                }
        }
    }

    private fun buildFeedItems(
        peeEntries: List<PeeEntry>,
        drinkEntries: List<DrinkEntry>,
        exercises: List<ExerciseCompletion>,
        missedTimes: List<Pair<LocalDate, java.time.LocalTime>>,
        zoneId: ZoneId
    ): List<FeedItem> {
        val peeItems = peeEntries.map {
            FeedItem(
                peeEntry = it,
                timestamp = it.timestamp,
                date = it.timestamp.atZone(zoneId).toLocalDate(),
                key = "pee_${it.id}"
            )
        }
        val drinkItems = drinkEntries.map {
            FeedItem(
                drinkEntry = it,
                timestamp = it.timestamp,
                date = it.timestamp.atZone(zoneId).toLocalDate(),
                key = "drink_${it.id}"
            )
        }
        val exerciseItems = exercises.map {
            FeedItem(
                exerciseCompletion = it,
                timestamp = it.completedAt,
                date = it.completedAt.atZone(zoneId).toLocalDate(),
                key = "exercise_${it.id}"
            )
        }
        val missedItems = missedTimes.map { (date, time) ->
            FeedItem(
                missedScheduleTime = time,
                timestamp = java.time.ZonedDateTime.of(date, time, zoneId).toInstant(),
                date = date,
                key = "missed_${date}_${time}"
            )
        }
        // Chronological: oldest first so the list reads top-to-bottom old→new
        return (peeItems + drinkItems + exerciseItems + missedItems).sortedBy { it.timestamp }
    }

    /**
     * Delete an entry.
     */
    fun deleteEntry(entry: PeeEntry) {
        viewModelScope.launch(dispatchers.io) {
            try {
                deleteEntryUseCase(entry)
                undoRedoManager.push(UndoableAction.DeleteEntry(entry))
                _events.emit(CalendarEvent.EntryDeleted)
            } catch (e: Exception) {
                _events.emit(CalendarEvent.Error(e.message ?: "Failed to delete entry"))
            }
        }
    }

    fun updateDrink(entry: DrinkEntry) {
        viewModelScope.launch(dispatchers.io) {
            try {
                val before = drinkEntryRepository.getById(entry.id)
                drinkEntryRepository.update(entry)
                if (before != null) {
                    undoRedoManager.push(UndoableAction.UpdateDrink(before, entry))
                }
                _events.emit(CalendarEvent.DrinkUpdated)
            } catch (e: Exception) {
                _events.emit(CalendarEvent.Error(e.message ?: "Failed to update drink"))
            }
        }
    }

    fun deleteDrink(entry: DrinkEntry) {
        viewModelScope.launch(dispatchers.io) {
            try {
                drinkEntryRepository.delete(entry)
                undoRedoManager.push(UndoableAction.DeleteDrink(entry))
                _events.emit(CalendarEvent.EntryDeleted)
            } catch (e: Exception) {
                _events.emit(CalendarEvent.Error(e.message ?: "Failed to delete drink"))
            }
        }
    }

    fun deleteExerciseCompletion(completionId: Long) {
        viewModelScope.launch(dispatchers.io) {
            try {
                val existing = exerciseRepository.getCompletionById(completionId)
                exerciseRepository.deleteCompletion(completionId)
                if (existing != null) {
                    undoRedoManager.push(UndoableAction.DeleteExercise(existing))
                }
                _events.emit(CalendarEvent.EntryDeleted)
            } catch (e: Exception) {
                _events.emit(CalendarEvent.Error(e.message ?: "Failed to delete exercise"))
            }
        }
    }

    fun updateExerciseCompletion(completionId: Long, newTimestamp: java.time.Instant, newExerciseType: com.laniao.domain.model.ExerciseType) {
        viewModelScope.launch(dispatchers.io) {
            try {
                val existing = exerciseRepository.getCompletionById(completionId) ?: return@launch
                val updated = existing.copy(completedAt = newTimestamp, exerciseType = newExerciseType)
                exerciseRepository.deleteCompletion(completionId)
                val newId = exerciseRepository.insertCompletion(updated.copy(id = 0))
                undoRedoManager.push(UndoableAction.UpdateExercise(existing, updated.copy(id = newId)))
                _events.emit(CalendarEvent.ExerciseUpdated)
            } catch (e: Exception) {
                _events.emit(CalendarEvent.Error(e.message ?: "Failed to update exercise"))
            }
        }
    }

    /**
     * Jump to today's date.
     */
    fun goToToday() {
        val today = clock.now().atZone(ZoneId.systemDefault()).toLocalDate()
        val currentDate = _uiState.value.selectedDate
        val currentDetailMode = _uiState.value.detailMode
        val direction = when {
            currentDate < today -> 1   // coming from past → slide left (forward)
            currentDate > today -> -1  // coming from future → slide right (backward)
            else -> 0
        }
        _uiState.update {
            it.copy(
                selectedDate = today,
                selectedMonth = YearMonth.from(today),
                viewMode = CalendarViewMode.DAY_LIST,
                slideDirection = direction,
                scrollToNowPending = true,
                scrollToNowAnimated = true
            )
        }
        loadMonthDayStats()
        loadDayData()
        if (currentDetailMode == CalendarDetailMode.LIST) {
            loadFeed()
        }
    }

    private fun loadMonthDayStats() {
        dayStatsJob?.cancel()
        dayStatsJob = viewModelScope.launch(dispatchers.io) {
            getMonthDayStatsUseCase(_uiState.value.selectedMonth).collect { stats ->
                _uiState.update { it.copy(dayStats = stats) }
            }
        }
    }

    private fun loadDayData() {
        dayJob?.cancel()
        drinksJob?.cancel()
        exercisesJob?.cancel()
        val date = _uiState.value.selectedDate

        dayJob = viewModelScope.launch(dispatchers.io) {
            getDayTimelineUseCase(date).collect { items ->
                _uiState.update { state ->
                    val uniqueEntries = items
                        .mapNotNull { it.entry }
                        .distinctBy { it.id }
                        .sortedByDescending { it.timestamp }
                    state.copy(
                        timelineItems = items,
                        allEntries = uniqueEntries,
                        isLoading = false
                    )
                }
            }
        }

        drinksJob = viewModelScope.launch(dispatchers.io) {
            drinkEntryRepository.getByDate(date).collect { drinks ->
                _uiState.update { it.copy(dayDrinks = drinks) }
            }
        }

        exercisesJob = viewModelScope.launch(dispatchers.io) {
            exerciseRepository.getCompletionsByDate(date).collect { exercises ->
                _uiState.update { it.copy(dayExercises = exercises) }
            }
        }
    }

    fun savePeeEntry(entry: PeeEntry) {
        viewModelScope.launch(dispatchers.io) {
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
                _events.emit(CalendarEvent.EntrySaved)
            } catch (e: Exception) {
                _events.emit(CalendarEvent.Error(e.message ?: "Failed to save entry"))
            }
        }
    }

    fun addDrink(type: DrinkType, amount: Double, unit: DrinkUnit, customName: String?, timestamp: Instant?) {
        viewModelScope.launch(dispatchers.io) {
            try {
                val entry = DrinkEntry(
                    timestamp = timestamp ?: clock.now(),
                    type = type,
                    amount = amount,
                    unit = unit,
                    customName = customName
                )
                val id = drinkEntryRepository.insert(entry)
                undoRedoManager.push(UndoableAction.AddDrink(entry.copy(id = id)))
                _events.emit(CalendarEvent.DrinkAdded)
            } catch (e: Exception) {
                _events.emit(CalendarEvent.Error(e.message ?: "Failed to add drink"))
            }
        }
    }

    fun completeExercise(exerciseType: ExerciseType, timestamp: Instant) {
        viewModelScope.launch(dispatchers.io) {
            try {
                val date = timestamp.atZone(ZoneId.systemDefault()).toLocalDate()
                val completion = ExerciseCompletion(
                    exerciseType = exerciseType,
                    completedAt = timestamp,
                    scheduledDate = date
                )
                val id = exerciseRepository.insertCompletion(completion)
                undoRedoManager.push(UndoableAction.AddExercise(completion.copy(id = id)))
                _events.emit(CalendarEvent.ExerciseCompleted)
            } catch (e: Exception) {
                _events.emit(CalendarEvent.Error(e.message ?: "Failed to log exercise"))
            }
        }
    }

    fun loadPeeEntryForEdit(entryId: Long, onLoaded: (PeeEntry) -> Unit) {
        viewModelScope.launch(dispatchers.io) {
            val entry = peeEntryRepository.getById(entryId)
            if (entry != null) {
                kotlinx.coroutines.withContext(dispatchers.main) {
                    onLoaded(entry)
                }
            }
        }
    }

    fun updateTimelineScrollState(scrollOffsetPx: Float, hourHeightDp: Float) {
        _uiState.update {
            it.copy(
                timelineScrollOffsetPx = scrollOffsetPx,
                timelineHourHeightDp = hourHeightDp
            )
        }
    }

    fun clearScrollToNow() {
        _uiState.update { it.copy(scrollToNowPending = false, scrollToNowAnimated = false) }
    }

    fun toggleFilter(filter: EntryTypeFilter) {
        _uiState.update { state ->
            val current = state.activeFilters
            val updated = if (filter in current) current - filter else current + filter
            state.copy(activeFilters = updated)
        }
    }

    fun clearFilters() {
        _uiState.update { it.copy(activeFilters = emptySet()) }
    }

    fun markScheduledTimeAsMissed(scheduledTime: java.time.LocalTime) {
        val date = _uiState.value.selectedDate
        viewModelScope.launch(dispatchers.io) {
            manuallyMissedTimeRepository.markMissed(date, scheduledTime)
            undoRedoManager.push(UndoableAction.MarkMissed(date, scheduledTime))
            loadDayData()
        }
    }

    fun unmarkScheduledTimeAsMissed(scheduledTime: java.time.LocalTime) {
        val date = _uiState.value.selectedDate
        viewModelScope.launch(dispatchers.io) {
            manuallyMissedTimeRepository.unmarkMissed(date, scheduledTime)
            undoRedoManager.push(UndoableAction.UnmarkMissed(date, scheduledTime))
            loadDayData()
        }
    }
}
