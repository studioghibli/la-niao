package com.laniao.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.laniao.data.local.dao.AppSettingsDao
import com.laniao.domain.usecase.DayExerciseCompletion
import com.laniao.domain.usecase.DayLiquidIntake
import com.laniao.domain.usecase.DayScheduleAdherence
import com.laniao.domain.usecase.DayVoidFrequency
import com.laniao.domain.usecase.GetAverageVoidGapUseCase
import com.laniao.domain.usecase.DailyVoidGap
import com.laniao.domain.usecase.GetExerciseCompletionUseCase
import com.laniao.domain.usecase.GetLiquidIntakeUseCase
import com.laniao.domain.usecase.GetScheduleAdherenceUseCase
import com.laniao.domain.usecase.GetVoidFrequencyUseCase
import com.laniao.util.Clock
import com.laniao.util.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

enum class DateRange(val label: String, val days: Int) {
    SEVEN_DAYS("7 days", 7),
    THIRTY_DAYS("30 days", 30),
    CUSTOM("Custom", 0)
}

data class StatisticsUiState(
    val selectedRange: DateRange = DateRange.SEVEN_DAYS,
    val startDate: LocalDate = LocalDate.now().minusDays(6),
    val endDate: LocalDate = LocalDate.now(),
    val voidFrequency: List<DayVoidFrequency> = emptyList(),
    val scheduleAdherence: List<DayScheduleAdherence> = emptyList(),
    val liquidIntake: List<DayLiquidIntake> = emptyList(),
    val exerciseCompletion: List<DayExerciseCompletion> = emptyList(),
    val averageVoidGap: List<DailyVoidGap> = emptyList(),
    val hydrationGoalLiters: Double = 2.7,
    val isLoading: Boolean = true
)

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val getVoidFrequencyUseCase: GetVoidFrequencyUseCase,
    private val getScheduleAdherenceUseCase: GetScheduleAdherenceUseCase,
    private val getLiquidIntakeUseCase: GetLiquidIntakeUseCase,
    private val getExerciseCompletionUseCase: GetExerciseCompletionUseCase,
    private val getAverageVoidGapUseCase: GetAverageVoidGapUseCase,
    private val appSettingsDao: AppSettingsDao,
    private val clock: Clock,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(StatisticsUiState())
    val uiState: StateFlow<StatisticsUiState> = _uiState.asStateFlow()

    private var voidFreqJob: Job? = null

    init {
        val today = clock.now().atZone(ZoneId.systemDefault()).toLocalDate()
        _uiState.update { it.copy(endDate = today, startDate = today.minusDays(6)) }
        loadData()
    }

    fun selectRange(range: DateRange) {
        val today = clock.now().atZone(ZoneId.systemDefault()).toLocalDate()
        val start = when (range) {
            DateRange.SEVEN_DAYS -> today.minusDays(6)
            DateRange.THIRTY_DAYS -> today.minusDays(29)
            DateRange.CUSTOM -> _uiState.value.startDate
        }
        _uiState.update { it.copy(selectedRange = range, startDate = start, endDate = today) }
        loadData()
    }

    fun setCustomRange(start: LocalDate, end: LocalDate) {
        _uiState.update { it.copy(selectedRange = DateRange.CUSTOM, startDate = start, endDate = end) }
        loadData()
    }

    private fun loadData() {
        val state = _uiState.value
        voidFreqJob?.cancel()
        voidFreqJob = viewModelScope.launch(dispatchers.io) {
            _uiState.update { it.copy(isLoading = true) }

            // Load hydration goal
            val goal = appSettingsDao.getOnce()?.hydrationGoalLiters ?: 2.7
            _uiState.update { it.copy(hydrationGoalLiters = goal) }

            // Load schedule adherence (suspend, not Flow)
            val adherence = getScheduleAdherenceUseCase(state.startDate, state.endDate)
            _uiState.update { it.copy(scheduleAdherence = adherence) }

            // Load exercise completion (suspend, not Flow)
            val exerciseComp = getExerciseCompletionUseCase(state.startDate, state.endDate)
            _uiState.update { it.copy(exerciseCompletion = exerciseComp) }

            // Load average void gap
            val voidGap = getAverageVoidGapUseCase(state.startDate, state.endDate)
            _uiState.update { it.copy(averageVoidGap = voidGap) }

            // Collect liquid intake in parallel-ish via another launch
            launch {
                getLiquidIntakeUseCase(state.startDate, state.endDate).collect { data ->
                    _uiState.update { it.copy(liquidIntake = data) }
                }
            }

            getVoidFrequencyUseCase(state.startDate, state.endDate).collect { data ->
                _uiState.update { it.copy(voidFrequency = data, isLoading = false) }
            }
        }
    }
}
