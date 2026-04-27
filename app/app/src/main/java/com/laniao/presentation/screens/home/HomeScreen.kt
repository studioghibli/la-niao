package com.laniao.presentation.screens.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.laniao.presentation.ui.components.AddDrinkDialog
import com.laniao.presentation.ui.components.AddEntryDialog
import com.laniao.presentation.ui.components.AddExerciseDialog
import com.laniao.presentation.ui.components.ExerciseChecklist
import com.laniao.presentation.ui.components.RecentEntriesList
import com.laniao.presentation.ui.components.SummaryCard
import com.laniao.presentation.viewmodel.HomeEvent
import com.laniao.presentation.viewmodel.HomeViewModel
import kotlinx.coroutines.flow.collectLatest

/**
 * Home screen - displays today's summary, water intake, quick add, and exercises.
 */
@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToAddEntry: (Long?) -> Unit,
    onNavigateToSchedule: () -> Unit,
    onNavigateToCalendar: () -> Unit,
    onNavigateToSettings: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var showAddDrinkDialog by remember { mutableStateOf(false) }
    var showAddExerciseDialog by remember { mutableStateOf(false) }
    var showAddEntryDialog by remember { mutableStateOf(false) }
    var pendingExercise by remember { mutableStateOf<Pair<com.laniao.domain.model.ExerciseType, Long?>?>(null) }

    // Handle one-time events
    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is HomeEvent.QuickAddSuccess -> {
                    snackbarHostState.showSnackbar("Entry logged!")
                }
                is HomeEvent.QuickAddError -> {
                    snackbarHostState.showSnackbar(event.message)
                }
                is HomeEvent.DrinkAdded -> {
                    snackbarHostState.showSnackbar("Drink added!")
                }
                is HomeEvent.DrinkError -> {
                    snackbarHostState.showSnackbar(event.message)
                }
                is HomeEvent.ExerciseCompleted -> {
                    snackbarHostState.showSnackbar("Exercise completed!")
                }
                is HomeEvent.EntrySaved -> {
                    showAddEntryDialog = false
                    snackbarHostState.showSnackbar("Entry saved!")
                }
                is HomeEvent.EntryDeleted -> {
                    showAddEntryDialog = false
                    snackbarHostState.showSnackbar("Entry deleted")
                }
                is HomeEvent.EntryError -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.refreshTodayData()
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // Main content - scrollable
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(top = 32.dp, bottom = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Loading indicator
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                // Today's Summary Card
                SummaryCard(
                    summary = uiState.summary,
                    scheduleProgress = uiState.scheduleProgress,
                    hasActiveSchedule = uiState.hasActiveSchedule
                )

                // Drinks total
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    color = com.laniao.presentation.theme.LaNiaoColors.DrinksCardBackground,
                    tonalElevation = 1.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .width(4.dp)
                                    .height(24.dp)
                                    .background(com.laniao.presentation.theme.LaNiaoColors.DrinksAccent, MaterialTheme.shapes.small)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "\uD83E\uDD64 Drinks",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = com.laniao.presentation.theme.LaNiaoColors.DrinksAccent
                            )
                        }
                        Text(
                            text = "%.2f L today".format(uiState.todayLiters),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = com.laniao.presentation.theme.LaNiaoColors.DrinksAccent
                        )
                    }
                }

                // Exercise Checklist
                ExerciseChecklist(
                    exercises = uiState.exerciseStatuses,
                    streak = uiState.exerciseStreak,
                    onComplete = { exerciseType, scheduleItemId ->
                        pendingExercise = Pair(exerciseType, scheduleItemId)
                    },
                    onConfigureClick = onNavigateToSchedule
                )

                // Streak Cards
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = com.laniao.presentation.theme.LaNiaoColors.StreaksCardBackground
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .width(4.dp)
                                    .height(24.dp)
                                    .background(com.laniao.presentation.theme.LaNiaoColors.StreaksAccent, MaterialTheme.shapes.small)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "\uD83D\uDD25 Streaks",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = com.laniao.presentation.theme.LaNiaoColors.StreaksAccent
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("\uD83D\uDCAA", style = MaterialTheme.typography.titleLarge)
                                Text(
                                    "${uiState.exerciseStreak}",
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = com.laniao.presentation.theme.LaNiaoColors.StreaksAccent
                                )
                                Text(
                                    "Exercise",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = com.laniao.presentation.theme.LaNiaoColors.StreaksAccent.copy(alpha = 0.8f)
                                )
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("\uD83C\uDF0A", style = MaterialTheme.typography.titleLarge)
                                Text(
                                    "${uiState.hydrationStreak}",
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = com.laniao.presentation.theme.LaNiaoColors.StreaksAccent
                                )
                                Text(
                                    "Hydration",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = com.laniao.presentation.theme.LaNiaoColors.StreaksAccent.copy(alpha = 0.8f)
                                )
                            }
                        }
                    }
                }

                // Spacer for FAB clearance
                Box(modifier = Modifier.padding(bottom = 80.dp))
            }
        }

        // FABs in bottom-right corner
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.End
        ) {
            // Add Exercise FAB
            FloatingActionButton(
                onClick = { showAddExerciseDialog = true },
                containerColor = com.laniao.presentation.theme.LaNiaoColors.ExerciseFabBackground,
                contentColor = com.laniao.presentation.theme.LaNiaoColors.ExerciseFabContent
            ) {
                Text("\uD83D\uDCAA", style = MaterialTheme.typography.titleMedium)
            }

            // Add Drink FAB
            FloatingActionButton(
                onClick = { showAddDrinkDialog = true },
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            ) {
                Text("\uD83E\uDD64", style = MaterialTheme.typography.titleMedium)
            }

            // Quick Add FAB - saves immediately, no form
            FloatingActionButton(
                onClick = { viewModel.quickAdd() },
                containerColor = com.laniao.presentation.theme.LaNiaoColors.QuickAddFabBackground,
                contentColor = com.laniao.presentation.theme.LaNiaoColors.QuickAddFabContent,
                modifier = Modifier
            ) {
                if (uiState.isQuickAddInProgress) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("⚡", style = MaterialTheme.typography.titleMedium)
                }
            }

            // Regular Add FAB (opens dialog)
            ExtendedFloatingActionButton(
                onClick = { showAddEntryDialog = true },
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Log Entry") }
            )
        }

        // Snackbar host
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }

    // Add Entry Dialog (void/urge/leak)
    if (showAddEntryDialog) {
        val unclaimedTimes = uiState.scheduleProgress
            .filter {
                it.status == com.laniao.domain.usecase.ScheduledTimeStatus.UPCOMING ||
                it.status == com.laniao.domain.usecase.ScheduledTimeStatus.OVERDUE
            }
            .map { it.scheduledTime }
        com.laniao.presentation.ui.components.AddEntryDialog(
            onDismiss = { showAddEntryDialog = false },
            onSave = { entry -> viewModel.savePeeEntry(entry) },
            unclaimedTimes = unclaimedTimes
        )
    }

    // Add Drink Dialog
    if (showAddDrinkDialog) {
        AddDrinkDialog(
            onDismiss = { showAddDrinkDialog = false },
            onSave = { type, amount, unit, customName, timestamp ->
                viewModel.addDrink(type, amount, unit, customName, timestamp)
            }
        )
    }

    // Add Exercise Dialog (any type, no schedule required)
    if (showAddExerciseDialog) {
        @OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
        AddExerciseDialog(
            onDismiss = { showAddExerciseDialog = false },
            onSave = { exerciseType, timestamp ->
                viewModel.completeExerciseAt(exerciseType, null, timestamp)
                showAddExerciseDialog = false
            }
        )
    }

    // Exercise completion dialog with time picker
    pendingExercise?.let { (exerciseType, scheduleItemId) ->
        @OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
        val timePickerState = androidx.compose.material3.rememberTimePickerState(
            initialHour = java.time.LocalTime.now().hour,
            initialMinute = java.time.LocalTime.now().minute
        )

        androidx.compose.material3.AlertDialog(
            onDismissRequest = { pendingExercise = null },
            title = { Text("${exerciseType.emoji} ${exerciseType.displayName}") },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Mark as completed at:")
                    @OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
                    androidx.compose.material3.TimePicker(state = timePickerState)
                }
            },
            confirmButton = {
                androidx.compose.material3.TextButton(
                    onClick = {
                        val now = java.time.LocalDate.now()
                        val time = java.time.LocalTime.of(timePickerState.hour, timePickerState.minute)
                        val timestamp = java.time.ZonedDateTime.of(now, time, java.time.ZoneId.systemDefault()).toInstant()
                        viewModel.completeExerciseAt(exerciseType, scheduleItemId, timestamp)
                        pendingExercise = null
                    }
                ) { Text("OK") }
            },
            dismissButton = {
                androidx.compose.material3.TextButton(
                    onClick = { pendingExercise = null }
                ) { Text("Cancel") }
            }
        )
    }
}
