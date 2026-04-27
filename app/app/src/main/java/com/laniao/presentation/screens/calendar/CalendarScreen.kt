package com.laniao.presentation.screens.calendar

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.laniao.domain.model.DrinkEntry
import com.laniao.domain.model.DrinkType
import com.laniao.domain.model.ExerciseCompletion
import com.laniao.domain.model.ExerciseType
import com.laniao.presentation.ui.components.AddDrinkDialog
import com.laniao.presentation.ui.components.CalendarView
import com.laniao.presentation.ui.components.DayTimelineCard
import com.laniao.presentation.viewmodel.CalendarEvent
import com.laniao.presentation.viewmodel.CalendarDetailMode
import com.laniao.presentation.viewmodel.CalendarViewModel
import com.laniao.presentation.viewmodel.CalendarViewMode
import com.laniao.presentation.viewmodel.EntryTypeFilter
import com.laniao.presentation.viewmodel.FeedItem

/**
 * Calendar screen - displays calendar view and day list of entries.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    onNavigateToEntry: (Long) -> Unit,
    modifier: Modifier = Modifier,
    initialShowList: Boolean = false,
    viewModel: CalendarViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var editingDrink by remember { mutableStateOf<DrinkEntry?>(null) }
    var editingExercise by remember { mutableStateOf<com.laniao.domain.model.ExerciseCompletion?>(null) }
    var editingPeeEntry by remember { mutableStateOf<com.laniao.domain.model.PeeEntry?>(null) }
    var showEntryTypePicker by remember { mutableStateOf(false) }
    var showAddEntryDialog by remember { mutableStateOf(false) }
    var showAddDrinkDialog by remember { mutableStateOf(false) }
    var showAddExerciseDialog by remember { mutableStateOf(false) }
    var tappedScheduleItem by remember { mutableStateOf<com.laniao.domain.usecase.TimelineItem?>(null) }

    // Switch to list mode if requested
    LaunchedEffect(initialShowList) {
        if (initialShowList) {
            viewModel.showList()
        }
    }
    
    // Handle back press: if in day list view, go back to calendar instead of leaving screen
    BackHandler(enabled = uiState.viewMode == CalendarViewMode.DAY_LIST) {
        viewModel.showCalendar()
    }
    
    // Handle events
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is CalendarEvent.EntryDeleted -> {
                    snackbarHostState.showSnackbar("Entry deleted")
                }
                is CalendarEvent.DrinkUpdated -> {
                    snackbarHostState.showSnackbar("Drink updated")
                }
                is CalendarEvent.Error -> {
                    snackbarHostState.showSnackbar(event.message)
                }
                is CalendarEvent.EntrySaved -> {
                    showAddEntryDialog = false
                    editingPeeEntry = null
                    snackbarHostState.showSnackbar("Entry saved")
                }
                is CalendarEvent.DrinkAdded -> {
                    showAddDrinkDialog = false
                    snackbarHostState.showSnackbar("Drink added")
                }
                is CalendarEvent.ExerciseCompleted -> {
                    showAddExerciseDialog = false
                    snackbarHostState.showSnackbar("Exercise logged")
                }
                is CalendarEvent.ExerciseUpdated -> {
                    snackbarHostState.showSnackbar("Exercise updated")
                }
            }
        }
    }
    
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Calendar", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold) },
                windowInsets = androidx.compose.foundation.layout.WindowInsets(0),
                actions = {
                    // Today button
                    TextButton(onClick = { viewModel.goToToday() }) {
                        Text("Today")
                    }

                    if (uiState.viewMode == CalendarViewMode.DAY_LIST) {
                        TextButton(onClick = { viewModel.showTimeline() }) {
                            Text(
                                text = "Timeline",
                                color = if (uiState.detailMode == CalendarDetailMode.TIMELINE) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                }
                            )
                        }
                        TextButton(onClick = { viewModel.showList() }) {
                            Text(
                                text = "List",
                                color = if (uiState.detailMode == CalendarDetailMode.LIST) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                }
                            )
                        }
                    }

                    // Calendar toggle
                    if (uiState.viewMode == CalendarViewMode.DAY_LIST) {
                        IconButton(onClick = { viewModel.showCalendar() }) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Show calendar"
                            )
                        }
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Bubble options (shown above FAB)
                androidx.compose.animation.AnimatedVisibility(visible = showEntryTypePicker) {
                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val selectedDate = uiState.selectedDate
                        val defaultDate = if (selectedDate.isAfter(java.time.LocalDate.now())) java.time.LocalDate.now() else selectedDate

                        androidx.compose.material3.FloatingActionButton(
                            onClick = {
                                showEntryTypePicker = false
                                showAddEntryDialog = true
                            },
                            containerColor = com.laniao.presentation.theme.LaNiaoColors.VoidFabBackground,
                            contentColor = com.laniao.presentation.theme.LaNiaoColors.VoidFabContent
                        ) {
                            Text("\uD83D\uDEBD", style = MaterialTheme.typography.titleMedium)
                        }
                        androidx.compose.material3.FloatingActionButton(
                            onClick = {
                                showEntryTypePicker = false
                                showAddDrinkDialog = true
                            },
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                        ) {
                            Text("\uD83E\uDD64", style = MaterialTheme.typography.titleMedium)
                        }
                        androidx.compose.material3.FloatingActionButton(
                            onClick = {
                                showEntryTypePicker = false
                                showAddExerciseDialog = true
                            },
                            containerColor = com.laniao.presentation.theme.LaNiaoColors.ExerciseFabBackground,
                            contentColor = com.laniao.presentation.theme.LaNiaoColors.ExerciseFabContent
                        ) {
                            Text("\uD83D\uDCAA", style = MaterialTheme.typography.titleMedium)
                        }
                    }
                }

                // Main FAB
                androidx.compose.material3.FloatingActionButton(
                    onClick = { showEntryTypePicker = !showEntryTypePicker }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add entry")
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            when (uiState.viewMode) {
                CalendarViewMode.CALENDAR -> {
                    CalendarView(
                        yearMonth = uiState.selectedMonth,
                        selectedDate = uiState.selectedDate,
                        dayStats = uiState.dayStats,
                        onDateSelected = { viewModel.selectDate(it) },
                        onMonthChange = { viewModel.changeMonth(it) },
                        onPreviousMonth = { viewModel.previousMonth() },
                        onNextMonth = { viewModel.nextMonth() },
                        modifier = Modifier
                    )
                    
                    // Show hint
                    Text(
                        text = "Tap a date to view entries",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
                
                CalendarViewMode.DAY_LIST -> {
                    // Filter chips — visible in timeline and list views
                    EntryFilterChips(
                        activeFilters = uiState.activeFilters,
                        onToggleFilter = { viewModel.toggleFilter(it) },
                        onClearAll = { viewModel.clearFilters() },
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    val direction = uiState.slideDirection
                    AnimatedContent(
                        targetState = uiState.selectedDate,
                        transitionSpec = {
                            if (direction >= 0) {
                                slideInHorizontally { fullWidth -> fullWidth } togetherWith
                                    slideOutHorizontally { fullWidth -> -fullWidth }
                            } else {
                                slideInHorizontally { fullWidth -> -fullWidth } togetherWith
                                    slideOutHorizontally { fullWidth -> fullWidth }
                            }
                        },
                        label = "daySlide"
                    ) { targetDate ->
                    if (uiState.isLoading) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    } else {
                        if (uiState.detailMode == CalendarDetailMode.TIMELINE) {
                            Column(modifier = Modifier.fillMaxSize()) {
                            // Date header for timeline
                            val dateLabel = targetDate.format(
                                com.laniao.util.DateFormatters.DATE_MEDIUM
                            )
                            val isToday = targetDate == java.time.LocalDate.now()
                            Text(
                                text = if (isToday) "$dateLabel \u2022 Today" else dateLabel,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                color = if (isToday) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )

                            DayTimelineCard(
                                date = targetDate,
                                items = filterTimelineItems(uiState.timelineItems, uiState.activeFilters),
                                onEntryClick = { entryId ->
                                    viewModel.loadPeeEntryForEdit(entryId) { entry ->
                                        editingPeeEntry = entry
                                    }
                                },
                                onDrinkClick = { drink -> editingDrink = drink },
                                onExerciseClick = { exercise -> editingExercise = exercise },
                                onScheduleMarkerClick = { item ->
                                    tappedScheduleItem = item
                                },
                                onSwipeLeft = { viewModel.nextDay() },
                                onSwipeRight = { viewModel.previousDay() },
                                scrollToNowPending = uiState.scrollToNowPending,
                                scrollToNowAnimated = uiState.scrollToNowAnimated,
                                onScrollToNowHandled = { viewModel.clearScrollToNow() },
                                initialScrollOffsetPx = uiState.timelineScrollOffsetPx,
                                initialHourHeightDp = uiState.timelineHourHeightDp,
                                onScrollStateChanged = { offset, height ->
                                    viewModel.updateTimelineScrollState(offset, height)
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                            } // end Column
                        } else {
                            // Chronological feed with scroll-up pagination
                            ChronologicalFeed(
                                feedItems = filterFeedItems(uiState.feedItems, uiState.activeFilters),
                                feedEarliestDate = uiState.feedEarliestDate,
                                isLoadingMore = uiState.isLoadingMore,
                                scrollToBottomToken = uiState.feedScrollToBottomToken,
                                onLoadMore = { viewModel.loadMoreHistory() },
                                onPeeEntryClick = {
                                    viewModel.loadPeeEntryForEdit(it.id) { entry ->
                                        editingPeeEntry = entry
                                    }
                                },
                                onPeeEntryDelete = { viewModel.deleteEntry(it) },
                                onDrinkClick = { editingDrink = it },
                                onExerciseClick = { editingExercise = it },
                                onBackToCalendar = { viewModel.showCalendar() },
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                    }
                }
            }
        }
    }

    // Edit drink dialog
    editingDrink?.let { drink ->
        AddDrinkDialog(
            onDismiss = { editingDrink = null },
            onSave = { type, amount, unit, customName, timestamp ->
                viewModel.updateDrink(
                    drink.copy(
                        type = type,
                        amount = amount,
                        unit = unit,
                        customName = customName,
                        timestamp = timestamp
                    )
                )
            },
            existingEntry = drink,
            onDelete = { viewModel.deleteDrink(it); editingDrink = null }
        )
    }

    // Exercise edit/delete dialog
    val exerciseToEdit = editingExercise
    if (exerciseToEdit != null) {
        val zoneId = remember { java.time.ZoneId.systemDefault() }
        val initTime = remember(exerciseToEdit.completedAt) {
            exerciseToEdit.completedAt.atZone(zoneId).toLocalTime()
        }
        val timePickerState = rememberTimePickerState(
            initialHour = initTime.hour,
            initialMinute = initTime.minute
        )
        var selectedType by remember(exerciseToEdit) { mutableStateOf(exerciseToEdit.exerciseType) }
        var typeMenuExpanded by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { editingExercise = null },
            title = { Text("Edit Exercise") },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Exercise type selector
                    Text("Type:", style = MaterialTheme.typography.labelMedium)
                    Box {
                        OutlinedButton(onClick = { typeMenuExpanded = true }) {
                            Text("${selectedType.emoji} ${selectedType.displayName}")
                        }
                        DropdownMenu(
                            expanded = typeMenuExpanded,
                            onDismissRequest = { typeMenuExpanded = false }
                        ) {
                            Text(
                                "Kegel",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                            )
                            com.laniao.domain.model.ExerciseType.entries
                                .filter { it.category == com.laniao.domain.model.ExerciseCategory.KEGEL }
                                .forEach { type ->
                                    DropdownMenuItem(
                                        text = { Text("${type.emoji} ${type.displayName}") },
                                        onClick = {
                                            selectedType = type
                                            typeMenuExpanded = false
                                        }
                                    )
                                }
                            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                            Text(
                                "Relaxation",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                            )
                            com.laniao.domain.model.ExerciseType.entries
                                .filter { it.category == com.laniao.domain.model.ExerciseCategory.RELAXATION }
                                .forEach { type ->
                                    DropdownMenuItem(
                                        text = { Text("${type.emoji} ${type.displayName}") },
                                        onClick = {
                                            selectedType = type
                                            typeMenuExpanded = false
                                        }
                                    )
                                }
                        }
                    }

                    Text("Completed at:")
                    TimePicker(state = timePickerState)
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    val newTime = java.time.LocalTime.of(timePickerState.hour, timePickerState.minute)
                    val date = exerciseToEdit.scheduledDate
                    val newTimestamp = java.time.ZonedDateTime.of(date, newTime, zoneId).toInstant()
                    viewModel.updateExerciseCompletion(exerciseToEdit.id, newTimestamp, selectedType)
                    editingExercise = null
                }) {
                    Text("Save")
                }
            },
            dismissButton = {
                Row {
                    TextButton(
                        onClick = {
                            viewModel.deleteExerciseCompletion(exerciseToEdit.id)
                            editingExercise = null
                        }
                    ) {
                        Text("Delete", color = MaterialTheme.colorScheme.error)
                    }
                    TextButton(onClick = { editingExercise = null }) {
                        Text("Cancel")
                    }
                }
            }
        )
    }

    // Schedule marker dialog — mark as missed / view status
    tappedScheduleItem?.let { scheduleItem ->
        val timeText = scheduleItem.time.format(com.laniao.util.DateFormatters.TIME_SHORT)
        var manuallyMissed by remember(scheduleItem) { mutableStateOf(scheduleItem.isManuallyMissed) }
        AlertDialog(
            onDismissRequest = { tappedScheduleItem = null },
            title = { Text("Scheduled: $timeText") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (scheduleItem.isClaimed) {
                        Text("✅ Completed", style = MaterialTheme.typography.bodyLarge)
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { manuallyMissed = !manuallyMissed }
                        ) {
                            Checkbox(
                                checked = manuallyMissed,
                                onCheckedChange = { manuallyMissed = it }
                            )
                            Text("Mark as missed", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            },
            confirmButton = {
                if (!scheduleItem.isClaimed) {
                    TextButton(onClick = {
                        if (manuallyMissed) {
                            viewModel.markScheduledTimeAsMissed(scheduleItem.time)
                        } else {
                            viewModel.unmarkScheduledTimeAsMissed(scheduleItem.time)
                        }
                        tappedScheduleItem = null
                    }) {
                        Text("Save")
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { tappedScheduleItem = null }) {
                    Text(if (scheduleItem.isClaimed) "Close" else "Cancel")
                }
            }
        )
    }

    // Add void/urge/leak entry dialog (new or retroactive)
    if (showAddEntryDialog) {
        val selectedDate = uiState.selectedDate
        val defaultDate = if (selectedDate.isAfter(java.time.LocalDate.now())) java.time.LocalDate.now() else selectedDate
        val unclaimedTimes = uiState.timelineItems
            .filter { it.isScheduled && !it.isClaimed && !it.isManuallyMissed }
            .map { it.scheduledTime ?: it.time }
        com.laniao.presentation.ui.components.AddEntryDialog(
            onDismiss = { showAddEntryDialog = false },
            onSave = { entry -> viewModel.savePeeEntry(entry) },
            defaultDate = defaultDate,
            unclaimedTimes = unclaimedTimes
        )
    }

    // Edit pee entry dialog
    editingPeeEntry?.let { entry ->
        val editUnclaimedTimes = uiState.timelineItems
            .filter { it.isScheduled && !it.isClaimed && !it.isManuallyMissed }
            .map { it.scheduledTime ?: it.time }
        com.laniao.presentation.ui.components.AddEntryDialog(
            onDismiss = { editingPeeEntry = null },
            onSave = { updated ->
                viewModel.savePeeEntry(updated)
                editingPeeEntry = null
            },
            onDelete = { id ->
                viewModel.deleteEntry(entry)
                editingPeeEntry = null
            },
            existingEntry = entry,
            unclaimedTimes = editUnclaimedTimes
        )
    }

    // Add drink dialog (retroactive)
    if (showAddDrinkDialog) {
        val selectedDate = uiState.selectedDate
        val defaultDate = if (selectedDate.isAfter(java.time.LocalDate.now())) java.time.LocalDate.now() else selectedDate
        com.laniao.presentation.ui.components.AddDrinkDialog(
            onDismiss = { showAddDrinkDialog = false },
            onSave = { type, amount, unit, customName, timestamp ->
                viewModel.addDrink(type, amount, unit, customName, timestamp)
            },
            defaultDate = defaultDate
        )
    }

    // Add exercise dialog (retroactive)
    if (showAddExerciseDialog) {
        val selectedDate = uiState.selectedDate
        val defaultDate = if (selectedDate.isAfter(java.time.LocalDate.now())) java.time.LocalDate.now() else selectedDate
        com.laniao.presentation.ui.components.AddExerciseDialog(
            onDismiss = { showAddExerciseDialog = false },
            onSave = { exerciseType, timestamp ->
                viewModel.completeExercise(exerciseType, timestamp)
            },
            defaultDate = defaultDate
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ChronologicalFeed(
    feedItems: List<FeedItem>,
    feedEarliestDate: java.time.LocalDate,
    isLoadingMore: Boolean,
    scrollToBottomToken: Int,
    onLoadMore: () -> Unit,
    onPeeEntryClick: (com.laniao.domain.model.PeeEntry) -> Unit,
    onPeeEntryDelete: (com.laniao.domain.model.PeeEntry) -> Unit,
    onDrinkClick: (DrinkEntry) -> Unit,
    onExerciseClick: (ExerciseCompletion) -> Unit,
    onBackToCalendar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val timeFormatter = remember { com.laniao.util.DateFormatters.TIME_SHORT }
    val dateFormatter = remember { com.laniao.util.DateFormatters.DATE_FEED }
    val zoneId = remember { java.time.ZoneId.systemDefault() }

    // Count total LazyColumn items (headers + data rows + loading indicator)
    val dateGroups = remember(feedItems) { feedItems.map { it.date }.distinct() }

    // Load more when user scrolls to the top
    val shouldLoadMore by remember {
        derivedStateOf {
            !isLoadingMore && (
                feedItems.isEmpty() ||
                (listState.firstVisibleItemIndex <= 1 &&
                listState.firstVisibleItemScrollOffset == 0 &&
                !listState.canScrollBackward &&
                listState.isScrollInProgress)
            )
        }
    }

    // Anchor key for scroll restoration after load-more
    var anchorKey by remember { mutableStateOf<String?>(null) }
    var anchorOffset by remember { mutableIntStateOf(0) }
    var isLoadingHistory by remember { mutableStateOf(false) }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            // Capture anchor: the first visible real item key
            val firstIdx = listState.firstVisibleItemIndex
            val layoutInfo = listState.layoutInfo
            val firstVisible = layoutInfo.visibleItemsInfo.firstOrNull { it.key is String && !(it.key as String).startsWith("loading") }
            anchorKey = firstVisible?.key as? String
            anchorOffset = firstVisible?.offset ?: 0
            isLoadingHistory = true
            onLoadMore()
        }
    }

    // After load-more completes, restore scroll position by finding the anchor key
    LaunchedEffect(feedItems.size, isLoadingHistory) {
        if (isLoadingHistory && !isLoadingMore && anchorKey != null) {
            // Find the anchor key in the new layout
            // Build the list of keys in order: loading?, then (header, items)* 
            val allKeys = mutableListOf<String>()
            if (isLoadingMore) allKeys.add("loading")
            var lastDate: java.time.LocalDate? = null
            feedItems.forEach { item ->
                if (item.date != lastDate) {
                    lastDate = item.date
                    allKeys.add("header_${item.date}")
                }
                allKeys.add(item.key)
            }
            val anchorIndex = allKeys.indexOf(anchorKey)
            if (anchorIndex >= 0) {
                listState.scrollToItem(anchorIndex, anchorOffset)
            }
            isLoadingHistory = false
            anchorKey = null
        }
    }

    // Scroll to bottom on initial load or explicit request (Today button)
    var lastScrollToken by remember { mutableIntStateOf(scrollToBottomToken) }
    LaunchedEffect(scrollToBottomToken, feedItems.size) {
        if (scrollToBottomToken != lastScrollToken && feedItems.isNotEmpty()) {
            // Calculate total item count including headers
            val totalItems = dateGroups.size + feedItems.size
            listState.scrollToItem(totalItems - 1)
            lastScrollToken = scrollToBottomToken
        }
    }

    Column(modifier = modifier) {
        // Header
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surfaceVariant,
            tonalElevation = 1.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onBackToCalendar)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "All Entries",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "\u2190 Calendar",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize()
        ) {
            // Loading indicator at top
            if (isLoadingMore) {
                item(key = "loading") {
                    Box(modifier = Modifier.fillMaxWidth().padding(8.dp),
                        contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                    }
                }
            }

            if (feedItems.isEmpty() && !isLoadingMore) {
                item(key = "empty") {
                    Box(modifier = Modifier.fillParentMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No entries yet today", style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f))
                    }
                }
            }

            // Group items by date and add sticky date headers
                var lastDate: java.time.LocalDate? = null
                feedItems.forEach { feedItem ->
                    if (feedItem.date != lastDate) {
                        lastDate = feedItem.date
                        val headerDate = feedItem.date
                        stickyHeader(key = "header_$headerDate") {
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                color = MaterialTheme.colorScheme.surface,
                                tonalElevation = 2.dp
                            ) {
                                Text(
                                    text = headerDate.format(dateFormatter),
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                )
                            }
                        }
                    }

                    item(key = feedItem.key) {
                        if (feedItem.peeEntry != null) {
                            FeedPeeRow(
                                entry = feedItem.peeEntry,
                                timeFormatter = timeFormatter,
                                zoneId = zoneId,
                                onClick = { onPeeEntryClick(feedItem.peeEntry) }
                            )
                        } else if (feedItem.drinkEntry != null) {
                            FeedDrinkRow(
                                drink = feedItem.drinkEntry,
                                timeFormatter = timeFormatter,
                                zoneId = zoneId,
                                onClick = { onDrinkClick(feedItem.drinkEntry) }
                            )
                        } else if (feedItem.exerciseCompletion != null) {
                            FeedExerciseRow(
                                exercise = feedItem.exerciseCompletion,
                                timeFormatter = timeFormatter,
                                zoneId = zoneId,
                                onClick = { onExerciseClick(feedItem.exerciseCompletion) }
                            )
                        } else if (feedItem.missedScheduleTime != null) {
                            FeedMissedScheduleRow(
                                scheduledTime = feedItem.missedScheduleTime,
                                timeFormatter = timeFormatter
                            )
                        }
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    }
                }
            }
    }
}

@Composable
private fun FeedPeeRow(
    entry: com.laniao.domain.model.PeeEntry,
    timeFormatter: java.time.format.DateTimeFormatter,
    zoneId: java.time.ZoneId,
    onClick: () -> Unit
) {
    val time = remember(entry.timestamp) {
        entry.timestamp.atZone(zoneId).toLocalTime().format(timeFormatter)
    }
    val emoji = when {
        entry.didVoid -> "\uD83D\uDEBD"
        entry.isLeakOnly -> "\uD83C\uDF88"
        else -> "\uD83D\uDE23"
    }
    val label = when {
        entry.didVoid && entry.hasLeak -> "Void + Leak"
        entry.didVoid -> "Void"
        entry.isLeakOnly -> "Leak"
        else -> "Urge"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(emoji, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(end = 12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(time, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
            Text(label, style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f))
        }
    }
}

@Composable
private fun FeedDrinkRow(
    drink: DrinkEntry,
    timeFormatter: java.time.format.DateTimeFormatter,
    zoneId: java.time.ZoneId,
    onClick: () -> Unit
) {
    val time = remember(drink.timestamp) {
        drink.timestamp.atZone(zoneId).toLocalTime().format(timeFormatter)
    }
    val emoji = when (drink.type) {
        DrinkType.WATER -> "\uD83D\uDCA7"
        DrinkType.SPARKLING_WATER -> "\u2728"
        DrinkType.TEA -> "\uD83C\uDF75"
        DrinkType.COFFEE -> "\u2615"
        DrinkType.MILK -> "\uD83E\uDD5B"
        DrinkType.JUICE -> "\uD83E\uDDC3"
        DrinkType.CUSTOM -> "\uD83E\uDD64"
    }
    val name = if (drink.type == DrinkType.CUSTOM && !drink.customName.isNullOrBlank())
        drink.customName else drink.type.displayName

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(emoji, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(end = 12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(time, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium)
            Text("$name \u2022 ${"%.0f".format(drink.amount)} ${drink.unit.displayName}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f))
        }
    }
}

@Composable
private fun FeedExerciseRow(
    exercise: ExerciseCompletion,
    timeFormatter: java.time.format.DateTimeFormatter,
    zoneId: java.time.ZoneId,
    onClick: (() -> Unit)? = null
) {
    val time = remember(exercise.completedAt) {
        exercise.completedAt.atZone(zoneId).toLocalTime().format(timeFormatter)
    }
    val emoji = exercise.exerciseType.emoji
    val name = exercise.exerciseType.displayName

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(horizontal = 16.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(emoji, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(end = 12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(time, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium)
            Text(name, style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f))
        }
    }
}

@Composable
private fun FeedMissedScheduleRow(
    scheduledTime: java.time.LocalTime,
    timeFormatter: java.time.format.DateTimeFormatter
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("\u274C", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(end = 12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(scheduledTime.format(timeFormatter), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
            Text("Missed scheduled void", style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error.copy(alpha = 0.8f))
        }
    }
}

private fun filterTimelineItems(
    items: List<com.laniao.domain.usecase.TimelineItem>,
    filters: Set<EntryTypeFilter>
): List<com.laniao.domain.usecase.TimelineItem> {
    if (filters.isEmpty()) return items // no filter = show all
    return items.filter { item ->
        when {
            // Always show schedule markers (they're not entries)
            item.isScheduled && item.entry == null && !item.isDrink && !item.isExercise -> true
            item.isDrink -> EntryTypeFilter.DRINK in filters
            item.isExercise -> EntryTypeFilter.EXERCISE in filters
            item.entry != null -> {
                val e = item.entry
                when {
                    e.isLeakOnly -> EntryTypeFilter.LEAK in filters
                    e.isUrgeOnly -> EntryTypeFilter.URGE in filters
                    e.didVoid -> EntryTypeFilter.VOID in filters
                    else -> true
                }
            }
            else -> true
        }
    }
}

private fun filterFeedItems(
    items: List<FeedItem>,
    filters: Set<EntryTypeFilter>
): List<FeedItem> {
    if (filters.isEmpty()) return items
    return items.filter { item ->
        when {
            item.drinkEntry != null -> EntryTypeFilter.DRINK in filters
            item.exerciseCompletion != null -> EntryTypeFilter.EXERCISE in filters
            item.peeEntry != null -> {
                val e = item.peeEntry
                when {
                    e.isLeakOnly -> EntryTypeFilter.LEAK in filters
                    e.isUrgeOnly -> EntryTypeFilter.URGE in filters
                    e.didVoid -> EntryTypeFilter.VOID in filters
                    else -> true
                }
            }
            else -> true
        }
    }
}

@Composable
private fun EntryFilterChips(
    activeFilters: Set<EntryTypeFilter>,
    onToggleFilter: (EntryTypeFilter) -> Unit,
    onClearAll: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // "All" chip — selected when no filters active
        FilterChip(
            selected = activeFilters.isEmpty(),
            onClick = { onClearAll() },
            label = { Text("All", style = MaterialTheme.typography.labelSmall) },
            modifier = Modifier.height(32.dp)
        )
        EntryTypeFilter.entries.forEach { filter ->
            FilterChip(
                selected = filter in activeFilters,
                onClick = { onToggleFilter(filter) },
                label = { Text("${filter.emoji} ${filter.label}", style = MaterialTheme.typography.labelSmall) },
                modifier = Modifier.height(32.dp)
            )
        }
    }
}

