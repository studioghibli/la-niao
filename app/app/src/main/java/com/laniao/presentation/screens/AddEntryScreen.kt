package com.laniao.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.laniao.domain.model.LeakAmount
import com.laniao.presentation.components.ColorPicker
import com.laniao.presentation.components.DateTimePicker
import com.laniao.presentation.components.ScheduledTimeDropdown
import com.laniao.presentation.components.UrgencySelector
import com.laniao.presentation.components.VolumeSelector
import com.laniao.presentation.viewmodel.AddEntryEvent
import com.laniao.presentation.viewmodel.EntryType
import com.laniao.presentation.viewmodel.AddEntryViewModel
import kotlinx.coroutines.flow.collectLatest

/**
 * Screen for adding or editing a pee entry.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEntryScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddEntryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var showDeleteDialog by remember { mutableStateOf(false) }

    // Handle one-time events
    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is AddEntryEvent.SaveSuccess -> onNavigateBack()
                is AddEntryEvent.DeleteSuccess -> onNavigateBack()
                is AddEntryEvent.Error -> snackbarHostState.showSnackbar(event.message)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when {
                            uiState.isQuickAdd -> "Quick Add"
                            uiState.isEditMode -> "Edit Entry"
                            else -> "Add Entry"
                        }
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Go back"
                        )
                    }
                },
                actions = {
                    if (uiState.isEditMode) {
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete entry",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
                .imePadding(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Date & Time picker
            DateTimePicker(
                timestamp = uiState.timestamp,
                onTimestampChanged = viewModel::onTimestampChanged,
                modifier = Modifier.fillMaxWidth()
            )

            // Entry type selector
            val entryType = when {
                uiState.didVoid -> EntryType.VOID
                uiState.leakAmount != LeakAmount.NONE -> EntryType.LEAK_ONLY
                else -> EntryType.URGE_ONLY
            }

            Text(
                text = "Entry Type",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = entryType == EntryType.VOID,
                    onClick = { viewModel.onEntryTypeChanged(EntryType.VOID) },
                    label = { Text("Void") }
                )
                FilterChip(
                    selected = entryType == EntryType.URGE_ONLY,
                    onClick = { viewModel.onEntryTypeChanged(EntryType.URGE_ONLY) },
                    label = { Text("Urge-only") }
                )
                FilterChip(
                    selected = entryType == EntryType.LEAK_ONLY,
                    onClick = { viewModel.onEntryTypeChanged(EntryType.LEAK_ONLY) },
                    label = { Text("Leak-only") }
                )
            }

            // Leak amount — available on void and leak-only entries (not urge-only)
            if (entryType != EntryType.URGE_ONLY) {
                Text(
                    text = if (uiState.didVoid) "Had a leak too?" else "Leak Amount",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // "None" chip only shown for void entries (for leak-only it must be > NONE)
                    if (uiState.didVoid) {
                        FilterChip(
                            selected = uiState.leakAmount == LeakAmount.NONE,
                            onClick = { viewModel.onLeakAmountChanged(LeakAmount.NONE) },
                            label = { Text("None") }
                        )
                    }
                    listOf(LeakAmount.SMALL, LeakAmount.MEDIUM, LeakAmount.LARGE).forEach { amount ->
                        FilterChip(
                            selected = uiState.leakAmount == amount,
                            onClick = { viewModel.onLeakAmountChanged(amount) },
                            label = {
                                Text(amount.name.lowercase().replaceFirstChar { it.uppercase() })
                            }
                        )
                    }
                }
            }

            // Volume selector (only show if voided)
            if (uiState.didVoid) {
                VolumeSelector(
                    selectedSize = uiState.volumeSize,
                    onSizeSelected = viewModel::onVolumeSizeChanged,
                    modifier = Modifier.fillMaxWidth()
                )

                // Color picker (only show if voided)
                ColorPicker(
                    selectedColor = uiState.color,
                    onColorSelected = viewModel::onColorChanged,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Urgency selector
            UrgencySelector(
                selectedUrgency = uiState.urgency,
                onUrgencySelected = viewModel::onUrgencyChanged,
                modifier = Modifier.fillMaxWidth()
            )

            // Scheduled time dropdown (void entries only)
            if (uiState.didVoid && (uiState.unclaimedTimes.isNotEmpty() || uiState.scheduledTime != null)) {
                ScheduledTimeDropdown(
                    selectedTime = uiState.scheduledTime,
                    unclaimedTimes = uiState.unclaimedTimes,
                    onTimeSelected = viewModel::onScheduledTimeChanged,
                    suggestedTime = uiState.suggestedTime,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Notes field
            OutlinedTextField(
                value = uiState.notes,
                onValueChange = viewModel::onNotesChanged,
                label = { Text("Notes (optional)") },
                placeholder = { Text("Add any notes...") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                maxLines = 4,
                supportingText = {
                    Text("${uiState.notes.length}/500")
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Save button
            Button(
                onClick = viewModel::save,
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = when {
                        uiState.isLoading -> "Saving..."
                        uiState.isEditMode -> "Update Entry"
                        else -> "Save Entry"
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // Delete confirmation dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Entry?") },
            text = { Text("This action cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        viewModel.delete()
                    }
                ) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
