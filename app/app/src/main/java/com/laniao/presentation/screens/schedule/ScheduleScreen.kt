package com.laniao.presentation.screens.schedule

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.laniao.presentation.ui.components.CreateExerciseScheduleDialog
import com.laniao.presentation.ui.components.CreateScheduleDialog
import com.laniao.presentation.ui.components.ScheduleCard
import com.laniao.presentation.ui.components.ScheduleProgressList
import com.laniao.presentation.viewmodel.ScheduleEvent
import com.laniao.presentation.viewmodel.ScheduleViewModel
import kotlinx.coroutines.flow.collectLatest

/**
 * Schedule screen - displays void schedule management.
 */
@Composable
fun ScheduleScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ScheduleViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Handle one-time events
    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is ScheduleEvent.ScheduleCreated -> {
                    snackbarHostState.showSnackbar("Schedule created!")
                }
                is ScheduleEvent.ScheduleUpdated -> {
                    snackbarHostState.showSnackbar("Schedule updated!")
                }
                is ScheduleEvent.ScheduleDeleted -> {
                    snackbarHostState.showSnackbar("Schedule deleted")
                }
                is ScheduleEvent.ExpiryUpdated -> {
                    snackbarHostState.showSnackbar("Expiry updated")
                }
                is ScheduleEvent.Error -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    var showPastSchedulesDialog by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize()) {
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            val entryBubbleColor = MaterialTheme.colorScheme.surfaceVariant // match active/progress gray
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .padding(top = 32.dp, bottom = 16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // ==================== ACTIVE SCHEDULES (Blue tint) ====================
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
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
                                    .background(MaterialTheme.colorScheme.primary, MaterialTheme.shapes.small)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Active Schedules",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        val hasActiveVoid = uiState.activeSchedule != null
                        val hasActiveExercise = uiState.activeExerciseSchedule != null

                        if (!hasActiveVoid && !hasActiveExercise) {
                            Text(
                                text = "No active schedules",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }

                        if (hasActiveVoid) {
                            uiState.activeSchedule?.let { activeSchedule ->
                                UnifiedScheduleCard(
                                    typeBadge = "\uD83D\uDEBD",
                                    typeLabel = "Void",
                                    subtitle = "${activeSchedule.intervalMinutes} min interval",
                                    daysRemaining = activeSchedule.daysRemaining(java.time.LocalDate.now()),
                                    onEdit = { viewModel.showEditDialog() },
                                    onDelete = { viewModel.deleteSchedule() },
                                    onCopy = { viewModel.copySchedule(activeSchedule) }
                                )
                            }
                        }

                        if (hasActiveExercise) {
                            uiState.activeExerciseSchedule?.let { activeExerciseSchedule ->
                                UnifiedScheduleCard(
                                    typeBadge = "\uD83D\uDCAA",
                                    typeLabel = "Exercise",
                                    subtitle = "${uiState.exerciseItems.size} exercises",
                                    daysRemaining = activeExerciseSchedule.daysRemaining(),
                                    onEdit = { viewModel.showEditExerciseDialog() },
                                    onDelete = { viewModel.deleteExerciseSchedule() },
                                    onCopy = { viewModel.copyExerciseSchedule(activeExerciseSchedule) },
                                extraContent = {
                                    val kegelItems = uiState.exerciseItems.filter { it.exerciseType.category == com.laniao.domain.model.ExerciseCategory.KEGEL }
                                    val relaxItems = uiState.exerciseItems.filter { it.exerciseType.category == com.laniao.domain.model.ExerciseCategory.RELAXATION }

                                    if (kegelItems.isNotEmpty()) {
                                        Text("Kegel", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                                        kegelItems.forEach { item ->
                                            Surface(
                                                modifier = Modifier.fillMaxWidth(),
                                                shape = MaterialTheme.shapes.small,
                                                color = MaterialTheme.colorScheme.surface,
                                                tonalElevation = 0.dp
                                            ) {
                                                Row(
                                                    modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 8.dp),
                                                    horizontalArrangement = Arrangement.SpaceBetween
                                                ) {
                                                    Text("${item.exerciseType.emoji} ${item.exerciseType.displayName}", style = MaterialTheme.typography.bodyMedium)
                                                    Text("${item.sessionsPerDay}x/day", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                                }
                                            }
                                        }
                                    }
                                    if (relaxItems.isNotEmpty()) {
                                        Text("Relaxation", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                                        relaxItems.forEach { item ->
                                            Surface(
                                                modifier = Modifier.fillMaxWidth(),
                                                shape = MaterialTheme.shapes.small,
                                                color = MaterialTheme.colorScheme.surface,
                                                tonalElevation = 0.dp
                                            ) {
                                                Row(
                                                    modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 8.dp),
                                                    horizontalArrangement = Arrangement.SpaceBetween
                                                ) {
                                                    Text("${item.exerciseType.emoji} ${item.exerciseType.displayName}", style = MaterialTheme.typography.bodyMedium)
                                                    Text("${item.sessionsPerDay}x/day", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                                }
                                            }
                                        }
                                    }
                                }
                            )
                            }
                        }
                    }
                }

                // ==================== PROGRESS (Green tint) ====================
                val hasActiveVoid2 = uiState.activeSchedule != null
                val hasActiveExercise2 = uiState.activeExerciseSchedule != null

                if (hasActiveVoid2 || hasActiveExercise2) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = com.laniao.presentation.theme.LaNiaoColors.ExerciseCardBackground.copy(alpha = 0.5f)
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
                                    .background(com.laniao.presentation.theme.LaNiaoColors.ExerciseAccent, MaterialTheme.shapes.small)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Today\u2019s Progress",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = com.laniao.presentation.theme.LaNiaoColors.ExerciseAccent
                            )
                        }
                            if (hasActiveVoid2) {
                                Text("\uD83D\uDEBD Void Schedule", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = MaterialTheme.shapes.medium,
                                    color = entryBubbleColor,
                                    tonalElevation = 0.dp
                                ) {
                                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                        ScheduleProgressList(progress = uiState.todayProgress)
                                    }
                                }
                            }

                            if (hasActiveExercise2) {
                                Text("\uD83D\uDCAA Exercise Schedule", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = MaterialTheme.shapes.medium,
                                    color = entryBubbleColor,
                                    tonalElevation = 0.dp
                                ) {
                                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                        val kegelStatuses = uiState.exerciseStatuses.filter { it.exerciseType.category == com.laniao.domain.model.ExerciseCategory.KEGEL }
                                        val relaxStatuses = uiState.exerciseStatuses.filter { it.exerciseType.category == com.laniao.domain.model.ExerciseCategory.RELAXATION }

                                        if (kegelStatuses.isNotEmpty()) {
                                            Text("Kegel", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                                            kegelStatuses.forEach { status ->
                                                ExerciseProgressBubble(status)
                                            }
                                        }
                                        if (relaxStatuses.isNotEmpty()) {
                                            Text("Relaxation", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                                            relaxStatuses.forEach { status ->
                                                ExerciseProgressBubble(status)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // ==================== UPCOMING (Amber tint) ====================
                val hasUpcoming = uiState.upcomingSchedules.isNotEmpty() || uiState.upcomingExerciseSchedules.isNotEmpty()

                if (hasUpcoming) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = com.laniao.presentation.theme.LaNiaoColors.ExerciseScheduleBackground.copy(alpha = 0.5f)
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
                                    .background(com.laniao.presentation.theme.LaNiaoColors.ScheduleAccent, MaterialTheme.shapes.small)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Upcoming Schedules",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = com.laniao.presentation.theme.LaNiaoColors.ScheduleAccent
                            )
                        }
                            val dateFormatter = remember { com.laniao.util.DateFormatters.DATE_SHORT }

                            uiState.upcomingSchedules.forEach { schedule ->
                                UnifiedUpcomingCard(
                                    typeBadge = "\uD83D\uDEBD",
                                    typeLabel = "Void",
                                    startDate = schedule.createdAt.format(dateFormatter),
                                    endDate = schedule.expiresAt.format(dateFormatter),
                                    onEdit = { viewModel.showEditDialog(schedule) },
                                    onDelete = { viewModel.deleteScheduleById(schedule) }
                                )
                            }

                            uiState.upcomingExerciseSchedules.forEach { schedule ->
                                UnifiedUpcomingCard(
                                    typeBadge = "\uD83D\uDCAA",
                                    typeLabel = "Exercise",
                                    startDate = schedule.startDate.format(dateFormatter),
                                    endDate = schedule.endDate?.format(dateFormatter) ?: "No end date",
                                    onEdit = { viewModel.showEditExerciseDialogForSchedule(schedule.id) },
                                    onDelete = { viewModel.deleteExerciseScheduleById(schedule.id) }
                                )
                            }
                        }
                    }
                }

                // Past schedules link
                if (uiState.pastSchedules.isNotEmpty() || uiState.pastExerciseSchedules.isNotEmpty()) {
                    TextButton(
                        onClick = { showPastSchedulesDialog = true },
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                    ) {
                        Text("See past schedules")
                    }
                }

                // Spacer for FAB clearance
                Box(modifier = Modifier.padding(bottom = 48.dp))
            }

            // FAB with bubble menu
            var showScheduleTypeMenu by remember { mutableStateOf(false) }
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Bubble options (shown above FAB)
                    androidx.compose.animation.AnimatedVisibility(visible = showScheduleTypeMenu) {
                        Column(
                            horizontalAlignment = Alignment.End,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FloatingActionButton(
                                onClick = {
                                    showScheduleTypeMenu = false
                                    viewModel.showCreateDialog()
                                },
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                            ) {
                                Text("\uD83D\uDEBD", style = MaterialTheme.typography.titleMedium)
                            }
                            FloatingActionButton(
                                onClick = {
                                    showScheduleTypeMenu = false
                                    viewModel.showCreateExerciseDialog()
                                },
                                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                            ) {
                                Text("\uD83D\uDCAA", style = MaterialTheme.typography.titleMedium)
                            }
                        }
                    }

                    // Main FAB
                    ExtendedFloatingActionButton(
                        onClick = { showScheduleTypeMenu = !showScheduleTypeMenu },
                        icon = { Icon(Icons.Default.Add, contentDescription = null) },
                        text = { Text("New Schedule") }
                    )
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }

    // Create schedule dialog (also used for copy — pre-filled from pendingScheduleParams)
    if (uiState.showCreateDialog) {
        val params = uiState.pendingScheduleParams
        CreateScheduleDialog(
            onDismiss = { viewModel.hideCreateDialog() },
            onCreate = { startTime, endTime, intervalMinutes, startDate, endDate ->
                viewModel.createSchedule(startTime, endTime, intervalMinutes, startDate, endDate)
            },
            initialStartTime = params?.startTime ?: java.time.LocalTime.of(9, 0),
            initialEndTime = params?.endTime ?: java.time.LocalTime.of(21, 0),
            initialIntervalMinutes = params?.intervalMinutes ?: 120,
            initialStartDate = params?.startDate ?: java.time.LocalDate.now(),
            initialEndDate = params?.endDate ?: java.time.LocalDate.now().plusDays(6)
        )
    }

    // Edit schedule dialog (pre-filled with current values)
    val scheduleToEdit = uiState.editingSchedule ?: uiState.activeSchedule
    if (uiState.showEditDialog && scheduleToEdit != null) {
        val schedule = scheduleToEdit
        CreateScheduleDialog(
            onDismiss = { viewModel.hideEditDialog() },
            onCreate = { startTime, endTime, intervalMinutes, startDate, endDate ->
                viewModel.editSchedule(startTime, endTime, intervalMinutes, startDate, endDate)
            },
            isEditMode = true,
            initialStartTime = schedule.startTime,
            initialEndTime = schedule.endTime,
            initialIntervalMinutes = schedule.intervalMinutes,
            initialStartDate = schedule.createdAt,
            initialEndDate = schedule.expiresAt
        )
    }

    // Overlap confirmation dialog
    if (uiState.showOverlapConfirmation) {
        AlertDialog(
            onDismissRequest = { viewModel.cancelReplaceSchedule() },
            title = { Text("Replace existing schedule?") },
            text = {
                Text("An active schedule already exists. Creating a new one will replace it.")
            },
            confirmButton = {
                TextButton(onClick = { viewModel.confirmReplaceSchedule() }) {
                    Text("Replace")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.cancelReplaceSchedule() }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Exercise schedule dialog (create or edit)
    if (uiState.showCreateExerciseDialog) {
        val isEditing = uiState.editingExerciseScheduleId != null
        CreateExerciseScheduleDialog(
            onDismiss = { viewModel.hideCreateExerciseDialog() },
            onCreate = { startDate, endDate, items ->
                if (isEditing) {
                    uiState.editingExerciseScheduleId?.let { id ->
                        viewModel.updateExerciseSchedule(id, startDate, endDate, items)
                    }
                } else {
                    viewModel.createExerciseSchedule(startDate, endDate, items)
                }
            },
            existingItems = uiState.editingExerciseItems,
            initialStartDate = uiState.editingExerciseStartDate,
            initialEndDate = uiState.editingExerciseEndDate
        )
    }

    // Past schedules dialog
    if (showPastSchedulesDialog) {
        val dateFormatter = remember { com.laniao.util.DateFormatters.DATE_SHORT }
        var pastFilter by remember { mutableStateOf("All") } // "All", "Void", "Exercise"
        AlertDialog(
            onDismissRequest = { showPastSchedulesDialog = false },
            title = { Text("Past Schedules") },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Filter chips
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("All", "Void", "Exercise").forEach { label ->
                            FilterChip(
                                selected = pastFilter == label,
                                onClick = { pastFilter = label },
                                label = { Text(label) }
                            )
                        }
                    }

                    if (uiState.pastSchedules.isEmpty() && uiState.pastExerciseSchedules.isEmpty()) {
                        Text("No past schedules", style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }

                    // Merge void and exercise past schedules, sorted by end date descending
                    data class PastScheduleItem(
                        val endDate: java.time.LocalDate,
                        val voidSchedule: com.laniao.domain.model.VoidSchedule? = null,
                        val exerciseSchedule: com.laniao.domain.model.ExerciseSchedule? = null
                    )
                    val merged = remember(uiState.pastSchedules, uiState.pastExerciseSchedules) {
                        val voidItems = uiState.pastSchedules.map { s ->
                            PastScheduleItem(endDate = s.expiresAt, voidSchedule = s)
                        }
                        val exerciseItems = uiState.pastExerciseSchedules.map { s ->
                            PastScheduleItem(endDate = s.endDate ?: s.startDate, exerciseSchedule = s)
                        }
                        (voidItems + exerciseItems).sortedByDescending { it.endDate }
                    }
                    val filtered = when (pastFilter) {
                        "Void" -> merged.filter { it.voidSchedule != null }
                        "Exercise" -> merged.filter { it.exerciseSchedule != null }
                        else -> merged
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f, fill = false)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        filtered.forEach { item ->
                        if (item.voidSchedule != null) {
                            val schedule = item.voidSchedule
                            UnifiedUpcomingCard(
                                typeBadge = "\uD83D\uDEBD",
                                typeLabel = "Void",
                                startDate = schedule.createdAt.format(dateFormatter),
                                endDate = schedule.expiresAt.format(dateFormatter),
                                onEdit = {
                                    showPastSchedulesDialog = false
                                    viewModel.showEditDialog(schedule)
                                },
                                onDelete = { viewModel.deleteScheduleById(schedule) }
                            )
                        } else if (item.exerciseSchedule != null) {
                            val schedule = item.exerciseSchedule
                            UnifiedUpcomingCard(
                                typeBadge = "\uD83D\uDCAA",
                                typeLabel = "Exercise",
                                startDate = schedule.startDate.format(dateFormatter),
                                endDate = schedule.endDate?.format(dateFormatter) ?: "No end date",
                                onEdit = {
                                    showPastSchedulesDialog = false
                                    viewModel.showEditExerciseDialogForSchedule(schedule.id)
                                },
                                onDelete = { viewModel.deleteExerciseScheduleById(schedule.id) }
                            )
                        }
                    }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showPastSchedulesDialog = false }) {
                    Text("Close")
                }
            }
        )
    }
}

@Composable
private fun ExerciseProgressBubble(status: com.laniao.domain.model.ExerciseStatus) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(if (status.isComplete) "\u2705" else "\u23F3", style = MaterialTheme.typography.bodyMedium)
                Text("${status.exerciseType.emoji} ${status.exerciseType.displayName}", style = MaterialTheme.typography.bodyMedium)
            }
            Text(
                "${status.completedToday}/${status.sessionsRequired}",
                style = MaterialTheme.typography.bodyMedium,
                color = if (status.isComplete) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun UnifiedScheduleCard(
    typeBadge: String,
    typeLabel: String,
    subtitle: String,
    daysRemaining: Int?,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onCopy: (() -> Unit)? = null,
    extraContent: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(typeBadge, style = MaterialTheme.typography.headlineSmall)
                    Column {
                        Text(typeLabel, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                Column(horizontalAlignment = Alignment.End) {
                    val daysText = daysRemaining?.let { "$it days left" } ?: "No end date"
                    Text(daysText, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Row {
                        if (onCopy != null) {
                            IconButton(onClick = onCopy) {
                                Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = MaterialTheme.colorScheme.primary)
                            }
                        }
                        IconButton(onClick = onEdit) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = MaterialTheme.colorScheme.primary)
                        }
                        IconButton(onClick = onDelete) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
            extraContent?.invoke()
        }
    }
}

@Composable
private fun UnifiedUpcomingCard(
    typeBadge: String,
    typeLabel: String,
    startDate: String,
    endDate: String,
    onEdit: (() -> Unit)?,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Text(typeBadge, style = MaterialTheme.typography.headlineSmall)
                Column {
                    Text("$typeLabel \u2022 Starts $startDate", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                    Text("Ends: $endDate", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Row {
                if (onEdit != null) {
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit", tint = MaterialTheme.colorScheme.primary)
                    }
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
private fun ExerciseScheduleCard(
    schedule: com.laniao.domain.model.ExerciseSchedule,
    items: List<com.laniao.domain.model.ExerciseScheduleItem>,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    androidx.compose.material3.Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Active", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary)
                    val daysText = schedule.daysRemaining()?.let { "$it days remaining" } ?: "No end date"
                    Text(daysText, style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Row {
                    TextButton(onClick = onEdit) { Text("Edit") }
                    TextButton(onClick = onDelete) { Text("Delete", color = MaterialTheme.colorScheme.error) }
                }
            }

            items.forEach { item ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("${item.exerciseType.emoji} ${item.exerciseType.displayName}",
                        style = MaterialTheme.typography.bodyMedium)
                    Text("${item.sessionsPerDay}x/day",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

@Composable
private fun UpcomingExerciseSchedulesSection(
    schedules: List<com.laniao.domain.model.ExerciseSchedule>,
    onDelete: (com.laniao.domain.model.ExerciseSchedule) -> Unit
) {
    val dateFormatter = remember { com.laniao.util.DateFormatters.DATE_SHORT }

    Text(
        text = "Upcoming Exercise Schedules",
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.Medium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    schedules.forEach { schedule ->
        androidx.compose.material3.Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Starts ${schedule.startDate.format(dateFormatter)}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                    val endText = schedule.endDate?.format(dateFormatter) ?: "No end date"
                    Text(
                        text = "Ends: $endText",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                TextButton(onClick = { onDelete(schedule) }) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
private fun EmptyScheduleState(
    onCreateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "\uD83D\uDCC5",
            style = MaterialTheme.typography.displayLarge
        )
        Text(
            text = "No active schedule",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = "Create a void schedule to help train your bladder",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
        )
        ExtendedFloatingActionButton(
            onClick = onCreateClick,
            icon = { Icon(Icons.Default.Add, contentDescription = null) },
            text = { Text("Create Schedule") }
        )
    }
}
