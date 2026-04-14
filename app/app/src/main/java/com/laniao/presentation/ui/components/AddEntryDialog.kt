package com.laniao.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.laniao.domain.model.LeakAmount
import com.laniao.domain.model.PeeColor
import com.laniao.domain.model.PeeEntry
import com.laniao.domain.model.Urgency
import com.laniao.domain.model.VolumeSize
import com.laniao.presentation.components.ColorPicker
import com.laniao.presentation.components.ScheduledTimeDropdown
import com.laniao.presentation.components.UrgencySelector
import com.laniao.presentation.components.VolumeSelector
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

private enum class DialogEntryType { VOID, URGE_ONLY, LEAK_ONLY }

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddEntryDialog(
    onDismiss: () -> Unit,
    onSave: (PeeEntry) -> Unit,
    onDelete: ((Long) -> Unit)? = null,
    existingEntry: PeeEntry? = null,
    defaultDate: LocalDate? = null,
    unclaimedTimes: List<LocalTime> = emptyList()
) {
    val isEditMode = existingEntry != null
    val zoneId = ZoneId.systemDefault()

    // Determine initial time from existing entry or now
    val initialTime = if (existingEntry != null) {
        existingEntry.timestamp.atZone(zoneId).toLocalTime()
    } else {
        LocalTime.now()
    }

    val timePickerState = rememberTimePickerState(
        initialHour = initialTime.hour,
        initialMinute = initialTime.minute
    )

    // Determine initial date
    val initialDate = if (existingEntry != null) {
        existingEntry.timestamp.atZone(zoneId).toLocalDate()
    } else {
        val requestedDate = defaultDate ?: LocalDate.now()
        if (requestedDate.isAfter(LocalDate.now())) LocalDate.now() else requestedDate
    }

    var selectedDate by remember { mutableStateOf(initialDate) }
    var showDatePicker by remember { mutableStateOf(false) }

    var entryType by remember {
        mutableStateOf(
            when {
                existingEntry == null -> DialogEntryType.VOID
                existingEntry.isUrgeOnly -> DialogEntryType.URGE_ONLY
                existingEntry.isLeakOnly -> DialogEntryType.LEAK_ONLY
                else -> DialogEntryType.VOID
            }
        )
    }
    var leakAmount by remember { mutableStateOf(existingEntry?.leakAmount ?: LeakAmount.NONE) }
    var volumeSize by remember { mutableStateOf(existingEntry?.volumeSize ?: VolumeSize.UNKNOWN) }
    var color by remember { mutableStateOf(existingEntry?.color ?: PeeColor.UNKNOWN) }
    var urgency by remember { mutableStateOf(existingEntry?.urgency ?: Urgency.UNKNOWN) }
    var notes by remember { mutableStateOf(existingEntry?.notes ?: "") }
    var selectedScheduledTime by remember { mutableStateOf(existingEntry?.scheduledTime) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                if (isEditMode) "Edit Void Entry" else "Log Void Entry",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Date selector
                OutlinedButton(
                    onClick = { showDatePicker = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "\uD83D\uDCC5 ${selectedDate.format(com.laniao.util.DateFormatters.DATE_SHORT)}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                // Time picker
                Text("Time", style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                TimePicker(state = timePickerState)

                // Entry type
                Text("Entry Type", style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    FilterChip(selected = entryType == DialogEntryType.VOID,
                        onClick = { entryType = DialogEntryType.VOID },
                        label = { Text("Void") })
                    FilterChip(selected = entryType == DialogEntryType.URGE_ONLY,
                        onClick = {
                            entryType = DialogEntryType.URGE_ONLY
                            leakAmount = LeakAmount.NONE
                            volumeSize = VolumeSize.UNKNOWN
                            color = PeeColor.UNKNOWN
                            selectedScheduledTime = null
                        },
                        label = { Text("Urge") })
                    FilterChip(selected = entryType == DialogEntryType.LEAK_ONLY,
                        onClick = {
                            entryType = DialogEntryType.LEAK_ONLY
                            if (leakAmount == LeakAmount.NONE) leakAmount = LeakAmount.SMALL
                            volumeSize = VolumeSize.UNKNOWN
                            color = PeeColor.UNKNOWN
                            selectedScheduledTime = null
                        },
                        label = { Text("Leak") })
                }

                // Leak amount (for void + leak, or leak-only)
                if (entryType != DialogEntryType.URGE_ONLY) {
                    Text(
                        if (entryType == DialogEntryType.VOID) "Had a leak too?" else "Leak Amount",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        if (entryType == DialogEntryType.VOID) {
                            FilterChip(selected = leakAmount == LeakAmount.NONE,
                                onClick = { leakAmount = LeakAmount.NONE },
                                label = { Text("None") })
                        }
                        listOf(LeakAmount.SMALL, LeakAmount.MEDIUM, LeakAmount.LARGE).forEach { amt ->
                            FilterChip(selected = leakAmount == amt,
                                onClick = { leakAmount = amt },
                                label = { Text(amt.name.lowercase().replaceFirstChar { it.uppercase() }) })
                        }
                    }
                }

                // Volume & Color (void only)
                if (entryType == DialogEntryType.VOID) {
                    VolumeSelector(
                        selectedSize = volumeSize,
                        onSizeSelected = { volumeSize = it },
                        modifier = Modifier.fillMaxWidth()
                    )
                    ColorPicker(
                        selectedColor = color,
                        onColorSelected = { color = it },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Urgency
                UrgencySelector(
                    selectedUrgency = urgency,
                    onUrgencySelected = { urgency = it },
                    modifier = Modifier.fillMaxWidth()
                )

                // Scheduled time dropdown (void only)
                if (entryType == DialogEntryType.VOID && (unclaimedTimes.isNotEmpty() || selectedScheduledTime != null)) {
                    ScheduledTimeDropdown(
                        selectedTime = selectedScheduledTime,
                        unclaimedTimes = unclaimedTimes,
                        onTimeSelected = { selectedScheduledTime = it },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Notes
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it.take(500) },
                    label = { Text("Notes (optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    maxLines = 3,
                    supportingText = { Text("${notes.length}/500") }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val time = LocalTime.of(timePickerState.hour, timePickerState.minute)
                val timestamp = ZonedDateTime.of(selectedDate, time, zoneId).toInstant()
                val didVoid = entryType == DialogEntryType.VOID
                val entry = PeeEntry(
                    id = existingEntry?.id ?: 0,
                    timestamp = timestamp,
                    didVoid = didVoid,
                    leakAmount = if (entryType == DialogEntryType.URGE_ONLY) LeakAmount.NONE else leakAmount,
                    volumeSize = if (didVoid) volumeSize else VolumeSize.UNKNOWN,
                    color = if (didVoid) color else PeeColor.UNKNOWN,
                    urgency = urgency,
                    notes = notes.ifBlank { null },
                    scheduledTime = selectedScheduledTime
                )
                onSave(entry)
            }) { Text("Save") }
        },
        dismissButton = {
            Row {
                if (isEditMode && onDelete != null) {
                    TextButton(onClick = { showDeleteDialog = true }) {
                        Text("Delete", color = MaterialTheme.colorScheme.error)
                    }
                }
                TextButton(onClick = onDismiss) { Text("Cancel") }
            }
        }
    )

    // Delete confirmation
    if (showDeleteDialog && existingEntry != null && onDelete != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Entry?") },
            text = { Text("This action cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    onDelete(existingEntry.id)
                }) { Text("Delete", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("Cancel") }
            }
        )
    }

    // Date picker dialog
    if (showDatePicker) {
        val todayMillis = LocalDate.now().atStartOfDay(ZoneId.of("UTC")).toInstant().toEpochMilli()
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate.atStartOfDay(ZoneId.of("UTC")).toInstant().toEpochMilli(),
            selectableDates = object : androidx.compose.material3.SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    return utcTimeMillis <= todayMillis + 86400000L // allow today
                }
            }
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        selectedDate = Instant.ofEpochMilli(millis).atZone(ZoneId.of("UTC")).toLocalDate()
                    }
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
