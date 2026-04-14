package com.laniao.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * Date and time picker for entry timestamp.
 * Shows date and time in separate fields with picker dialogs.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimePicker(
    timestamp: Instant,
    onTimestampChanged: (Instant) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val zoneId = remember { ZoneId.systemDefault() }
    val zonedDateTime = remember(timestamp) { timestamp.atZone(zoneId) }
    val localDate = zonedDateTime.toLocalDate()
    val localTime = zonedDateTime.toLocalTime()

    val dateFormatter = remember { com.laniao.util.DateFormatters.DATE_COMPACT }
    val timeFormatter = remember { com.laniao.util.DateFormatters.TIME_SHORT }

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Text(
            text = "Date & Time",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Date field
            OutlinedTextField(
                value = localDate.format(dateFormatter),
                onValueChange = {},
                readOnly = true,
                enabled = enabled,
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }, enabled = enabled) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Select date"
                        )
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .semantics {
                        contentDescription = "Date: ${localDate.format(dateFormatter)}"
                    }
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Time field
            OutlinedTextField(
                value = localTime.format(timeFormatter),
                onValueChange = {},
                readOnly = true,
                enabled = enabled,
                trailingIcon = {
                    IconButton(onClick = { showTimePicker = true }, enabled = enabled) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Select time"
                        )
                    }
                },
                modifier = Modifier
                    .weight(0.7f)
                    .semantics {
                        contentDescription = "Time: ${localTime.format(timeFormatter)}"
                    }
            )
        }
    }

    // Date picker dialog
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = localDate.atStartOfDay(zoneId).toInstant().toEpochMilli()
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val newDate = Instant.ofEpochMilli(millis)
                                .atZone(zoneId)
                                .toLocalDate()
                            val newTimestamp = localTime.atDate(newDate)
                                .atZone(zoneId)
                                .toInstant()
                            onTimestampChanged(newTimestamp)
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // Time picker dialog
    if (showTimePicker) {
        val timePickerState = rememberTimePickerState(
            initialHour = localTime.hour,
            initialMinute = localTime.minute
        )

        TimePickerDialog(
            onDismiss = { showTimePicker = false },
            onConfirm = {
                val newTime = LocalTime.of(timePickerState.hour, timePickerState.minute)
                val newTimestamp = newTime.atDate(localDate)
                    .atZone(zoneId)
                    .toInstant()
                onTimestampChanged(newTimestamp)
                showTimePicker = false
            },
            timePickerState = timePickerState
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    timePickerState: TimePickerState
) {
    Dialog(onDismissRequest = onDismiss) {
        androidx.compose.material3.Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Select time",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp)
                )
                
                TimePicker(state = timePickerState)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    TextButton(onClick = onConfirm) {
                        Text("OK")
                    }
                }
            }
        }
    }
}
