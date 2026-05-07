package com.laniao.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

/**
 * Dialog for creating or editing a void schedule.
 * Supports setting start/end dates alongside start/end times and interval.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateScheduleDialog(
    onDismiss: () -> Unit,
    onCreate: (startTime: LocalTime, endTime: LocalTime, intervalMinutes: Int, startDate: LocalDate, endDate: LocalDate) -> Unit,
    modifier: Modifier = Modifier,
    isEditMode: Boolean = false,
    initialStartTime: LocalTime = LocalTime.of(9, 0),
    initialEndTime: LocalTime = LocalTime.of(21, 0),
    initialIntervalMinutes: Int = 120,
    initialStartDate: LocalDate = LocalDate.now(),
    initialEndDate: LocalDate = LocalDate.now().plusDays(6)
) {
    var startTime by remember { mutableStateOf(initialStartTime) }
    var endTime by remember { mutableStateOf(initialEndTime) }
    var intervalMinutes by rememberSaveable { mutableIntStateOf(initialIntervalMinutes) }
    var startDate by remember { mutableStateOf(initialStartDate) }
    var endDate by remember { mutableStateOf(initialEndDate) }
    var showStartDatePicker by rememberSaveable { mutableStateOf(false) }
    var showEndDatePicker by rememberSaveable { mutableStateOf(false) }

    val today = remember { LocalDate.now() }
    val durationDays = remember(startDate, endDate) {
        (endDate.toEpochDay() - startDate.toEpochDay()).toInt() + 1
    }

    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }
    var showIntervalInput by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (isEditMode) "Edit Schedule" else "Create Schedule",
                fontWeight = FontWeight.SemiBold
            )
        },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Start Date
                DateSelectorField(
                    label = "Start Date",
                    selectedDate = startDate,
                    onClick = { showStartDatePicker = true }
                )

                // End Date
                DateSelectorField(
                    label = "End Date",
                    selectedDate = endDate,
                    onClick = { showEndDatePicker = true }
                )

                // Start Time — clickable field
                TimeClickField(
                    label = "Start Time",
                    time = startTime,
                    onClick = { showStartTimePicker = true }
                )

                // End Time — clickable field
                TimeClickField(
                    label = "End Time",
                    time = endTime,
                    onClick = { showEndTimePicker = true }
                )

                // Interval — clickable field
                Column {
                    Text("Interval", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Medium)
                    OutlinedTextField(
                        value = formatInterval(intervalMinutes),
                        onValueChange = {},
                        readOnly = true,
                        enabled = false,
                        modifier = Modifier.fillMaxWidth().padding(top = 4.dp).clickable { showIntervalInput = true },
                        colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                            disabledTextColor = MaterialTheme.colorScheme.onSurface,
                            disabledBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )
                }

                // Preview
                val previewTimes = remember(startTime, endTime, intervalMinutes) {
                    calculatePreviewTimes(startTime, endTime, intervalMinutes)
                }
                Text(
                    text = "Preview: ${previewTimes.size} voids/day × $durationDays days",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onCreate(startTime, endTime, intervalMinutes, startDate, endDate) },
                enabled = startTime < endTime && endDate >= startDate
            ) {
                Text(if (isEditMode) "Save" else "Create")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        modifier = modifier
    )

    // Start date picker dialog
    if (showStartDatePicker) {
        val startPickerState = rememberDatePickerState(
            initialSelectedDateMillis = startDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
        )
        DatePickerDialog(
            onDismissRequest = { showStartDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    startPickerState.selectedDateMillis?.let { millis ->
                        val picked = Instant.ofEpochMilli(millis)
                            .atZone(ZoneOffset.UTC)
                            .toLocalDate()
                        val delta = java.time.temporal.ChronoUnit.DAYS.between(startDate, picked)
                        endDate = endDate.plusDays(delta)
                        startDate = picked
                    }
                    showStartDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showStartDatePicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = startPickerState)
        }
    }

    // End date picker dialog
    if (showEndDatePicker) {
        val minDateMillis = startDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
        val endPickerState = rememberDatePickerState(
            initialSelectedDateMillis = endDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli(),
            selectableDates = object : androidx.compose.material3.SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean = utcTimeMillis >= minDateMillis
            }
        )
        DatePickerDialog(
            onDismissRequest = { showEndDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    endPickerState.selectedDateMillis?.let { millis ->
                        val picked = Instant.ofEpochMilli(millis)
                            .atZone(ZoneOffset.UTC)
                            .toLocalDate()
                        if (picked >= startDate) {
                            endDate = picked
                        }
                    }
                    showEndDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showEndDatePicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = endPickerState)
        }
    }

    // Start time picker
    if (showStartTimePicker) {
        val tpState = rememberTimePickerState(initialHour = startTime.hour, initialMinute = startTime.minute, is24Hour = true)
        androidx.compose.ui.window.Dialog(onDismissRequest = { showStartTimePicker = false }) {
            androidx.compose.material3.Surface(shape = MaterialTheme.shapes.extraLarge, tonalElevation = 6.dp) {
                Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Start Time", style = MaterialTheme.typography.labelMedium, modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp))
                    TimePicker(state = tpState)
                    Row(modifier = Modifier.fillMaxWidth().padding(top = 16.dp), horizontalArrangement = Arrangement.End) {
                        TextButton(onClick = { showStartTimePicker = false }) { Text("Cancel") }
                        TextButton(onClick = { startTime = LocalTime.of(tpState.hour, tpState.minute); showStartTimePicker = false }) { Text("OK") }
                    }
                }
            }
        }
    }

    // End time picker
    if (showEndTimePicker) {
        val tpState = rememberTimePickerState(initialHour = endTime.hour, initialMinute = endTime.minute, is24Hour = true)
        androidx.compose.ui.window.Dialog(onDismissRequest = { showEndTimePicker = false }) {
            androidx.compose.material3.Surface(shape = MaterialTheme.shapes.extraLarge, tonalElevation = 6.dp) {
                Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("End Time", style = MaterialTheme.typography.labelMedium, modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp))
                    TimePicker(state = tpState)
                    Row(modifier = Modifier.fillMaxWidth().padding(top = 16.dp), horizontalArrangement = Arrangement.End) {
                        TextButton(onClick = { showEndTimePicker = false }) { Text("Cancel") }
                        TextButton(onClick = { endTime = LocalTime.of(tpState.hour, tpState.minute); showEndTimePicker = false }) { Text("OK") }
                    }
                }
            }
        }
    }

    // Interval input dialog
    if (showIntervalInput) {
        var intervalText by remember { mutableStateOf("$intervalMinutes") }
        AlertDialog(
            onDismissRequest = { showIntervalInput = false },
            title = { Text("Interval (minutes)") },
            text = {
                OutlinedTextField(
                    value = intervalText,
                    onValueChange = { intervalText = it.filter { c -> c.isDigit() } },
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    val parsed = intervalText.toIntOrNull()?.coerceIn(1, 360) ?: intervalMinutes
                    intervalMinutes = parsed
                    showIntervalInput = false
                }) { Text("OK") }
            },
            dismissButton = { TextButton(onClick = { showIntervalInput = false }) { Text("Cancel") } }
        )
    }
}

@Composable
private fun TimeClickField(
    label: String,
    time: LocalTime,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(label, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Medium)
        OutlinedTextField(
            value = time.format(TIME_FORMATTER),
            onValueChange = {},
            readOnly = true,
            enabled = false,
            modifier = Modifier.fillMaxWidth().padding(top = 4.dp).clickable(onClick = onClick),
            colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledBorderColor = MaterialTheme.colorScheme.outline
            )
        )
    }
}

private fun formatInterval(minutes: Int): String {
    val h = minutes / 60
    val m = minutes % 60
    return when {
        h > 0 && m > 0 -> "${h}h ${m}m"
        h > 0 -> "${h}h"
        else -> "${m}m"
    }
}

@Composable
private fun DateSelectorField(
    label: String,
    selectedDate: LocalDate,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Medium
        )
        OutlinedTextField(
            value = selectedDate.format(DATE_DISPLAY_FORMATTER),
            onValueChange = {},
            readOnly = true,
            enabled = false,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
                .clickable(onClick = onClick),
            colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledBorderColor = MaterialTheme.colorScheme.outline,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}

private fun calculatePreviewTimes(
    startTime: LocalTime,
    endTime: LocalTime,
    intervalMinutes: Int
): List<LocalTime> {
    if (startTime >= endTime || intervalMinutes <= 0) return emptyList()
    
    val times = mutableListOf<LocalTime>()
    var current = startTime
    
    while (current <= endTime) {
        times.add(current)
        val next = current.plusMinutes(intervalMinutes.toLong())
        // Check for midnight wraparound (next < current means it wrapped past midnight)
        if (next < current || next > endTime) break
        current = next
    }
    return times
}

private val TIME_FORMATTER: DateTimeFormatter = com.laniao.util.DateFormatters.TIME_SHORT
private val DATE_DISPLAY_FORMATTER: DateTimeFormatter = com.laniao.util.DateFormatters.DATE_SHORT
