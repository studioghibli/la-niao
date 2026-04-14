package com.laniao.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.laniao.domain.model.ExerciseCategory
import com.laniao.domain.model.ExerciseScheduleItem
import com.laniao.domain.model.ExerciseType
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateExerciseScheduleDialog(
    onDismiss: () -> Unit,
    onCreate: (startDate: LocalDate, endDate: LocalDate?, items: List<ExerciseScheduleItem>) -> Unit,
    existingItems: List<ExerciseScheduleItem> = emptyList(),
    isEditMode: Boolean = existingItems.isNotEmpty(),
    initialStartDate: LocalDate? = null,
    initialEndDate: LocalDate? = null
) {
    val enabled = remember {
        mutableStateMapOf<ExerciseType, Boolean>().apply {
            ExerciseType.entries.forEach { put(it, existingItems.any { e -> e.exerciseType == it }) }
        }
    }
    val sessions = remember { mutableStateMapOf<ExerciseType, Int>().apply { ExerciseType.entries.forEach { put(it, existingItems.find { e -> e.exerciseType == it }?.sessionsPerDay ?: it.defaultSessionsPerDay) } } }
    val sets = remember { mutableStateMapOf<ExerciseType, Int>().apply { ExerciseType.entries.forEach { put(it, existingItems.find { e -> e.exerciseType == it }?.sets ?: it.defaultSets) } } }
    val reps = remember { mutableStateMapOf<ExerciseType, Int>().apply { ExerciseType.entries.forEach { put(it, existingItems.find { e -> e.exerciseType == it }?.reps ?: it.defaultReps) } } }
    val holds = remember { mutableStateMapOf<ExerciseType, Int>().apply { ExerciseType.entries.forEach { put(it, existingItems.find { e -> e.exerciseType == it }?.holdSeconds ?: it.defaultHoldSeconds) } } }

    var startDate by remember { mutableStateOf(initialStartDate ?: LocalDate.now()) }
    var hasEndDate by remember { mutableStateOf(initialEndDate != null) }
    var endDate by remember { mutableStateOf(initialEndDate ?: (initialStartDate ?: LocalDate.now()).plusDays(7)) }
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    val dateFormatter = remember { com.laniao.util.DateFormatters.DATE_COMPACT }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isEditMode) "Edit Exercise Schedule" else "Exercise Schedule") },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Start date - clickable field with DatePicker
                Text("Start Date", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Medium)
                Box(modifier = Modifier.fillMaxWidth().clickable { showStartDatePicker = true }) {
                    OutlinedTextField(
                        value = startDate.format(dateFormatter),
                        onValueChange = {},
                        readOnly = true,
                        enabled = false,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = MaterialTheme.colorScheme.onSurface,
                            disabledBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )
                }

                // End date toggle + picker
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Set end date", style = MaterialTheme.typography.bodyMedium)
                    Switch(checked = hasEndDate, onCheckedChange = { hasEndDate = it })
                }
                if (hasEndDate) {
                    Box(modifier = Modifier.fillMaxWidth().clickable { showEndDatePicker = true }) {
                        OutlinedTextField(
                            value = endDate.format(dateFormatter),
                            onValueChange = {},
                            readOnly = true,
                            enabled = false,
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                                disabledBorderColor = MaterialTheme.colorScheme.outline
                            )
                        )
                    }
                }

                HorizontalDivider()
                Text("Kegel Exercises", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)

                ExerciseType.entries.filter { it.category == ExerciseCategory.KEGEL }.forEach { type ->
                    ExerciseToggleCard(type, enabled[type] == true, sessions[type]!!, sets[type]!!, reps[type]!!, holds[type]!!,
                        onToggle = { enabled[type] = it },
                        onSessions = { sessions[type] = it },
                        onSets = { sets[type] = it },
                        onReps = { reps[type] = it },
                        onHold = { holds[type] = it }
                    )
                }

                HorizontalDivider()
                Text("Relaxation Exercises", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)

                ExerciseType.entries.filter { it.category == ExerciseCategory.RELAXATION }.forEach { type ->
                    ExerciseToggleCard(type, enabled[type] == true, sessions[type]!!, sets[type]!!, reps[type]!!, holds[type]!!,
                        onToggle = { enabled[type] = it },
                        onSessions = { sessions[type] = it },
                        onSets = { sets[type] = it },
                        onReps = { reps[type] = it },
                        onHold = { holds[type] = it }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val actualEndDate = if (hasEndDate) endDate else null
                    val items = enabled.filter { it.value }.map { (type, _) ->
                        ExerciseScheduleItem(
                            scheduleId = 0,
                            exerciseType = type,
                            sessionsPerDay = sessions[type]!!,
                            sets = sets[type]!!,
                            reps = reps[type]!!,
                            holdSeconds = holds[type]!!
                        )
                    }
                    onCreate(startDate, actualEndDate, items)
                },
                enabled = enabled.any { it.value }
            ) { Text(if (isEditMode) "Save" else "Create") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )

    // Start date picker
    if (showStartDatePicker) {
        val state = rememberDatePickerState(
            initialSelectedDateMillis = startDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
        )
        DatePickerDialog(
            onDismissRequest = { showStartDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    state.selectedDateMillis?.let { millis ->
                        val picked = Instant.ofEpochMilli(millis).atZone(ZoneOffset.UTC).toLocalDate()
                        val delta = java.time.temporal.ChronoUnit.DAYS.between(startDate, picked)
                        endDate = endDate.plusDays(delta)
                        startDate = picked
                    }
                    showStartDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = { TextButton(onClick = { showStartDatePicker = false }) { Text("Cancel") } }
        ) { DatePicker(state = state) }
    }

    // End date picker
    if (showEndDatePicker) {
        val minDateMillis = startDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
        val state = rememberDatePickerState(
            initialSelectedDateMillis = endDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli(),
            selectableDates = object : androidx.compose.material3.SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean = utcTimeMillis >= minDateMillis
            }
        )
        DatePickerDialog(
            onDismissRequest = { showEndDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    state.selectedDateMillis?.let { millis ->
                        val picked = Instant.ofEpochMilli(millis).atZone(ZoneOffset.UTC).toLocalDate()
                        if (picked >= startDate) endDate = picked
                    }
                    showEndDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = { TextButton(onClick = { showEndDatePicker = false }) { Text("Cancel") } }
        ) { DatePicker(state = state) }
    }
}

@Composable
private fun ExerciseToggleCard(
    type: ExerciseType,
    isEnabled: Boolean,
    sessionsPerDay: Int, setsVal: Int, repsVal: Int, holdVal: Int,
    onToggle: (Boolean) -> Unit,
    onSessions: (Int) -> Unit,
    onSets: (Int) -> Unit,
    onReps: (Int) -> Unit,
    onHold: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isEnabled) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            else MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("${type.emoji} ${type.displayName}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                    Text(type.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Switch(checked = isEnabled, onCheckedChange = onToggle)
            }
            if (isEnabled) {
                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                NumberStepperSmall("Sessions/day", sessionsPerDay, onSessions, 1, 10)
                if (type.category == ExerciseCategory.KEGEL) {
                    NumberStepperSmall("Sets", setsVal, onSets, 1, 10)
                    NumberStepperSmall("Reps", repsVal, onReps, 1, 50)
                    NumberStepperSmall("Hold (sec)", holdVal, onHold, 1, 60)
                } else {
                    NumberStepperSmall("Hold (sec)", holdVal, onHold, 5, 120)
                }
            }
        }
    }
}

@Composable
private fun NumberStepperSmall(label: String, value: Int, onChange: (Int) -> Unit, min: Int, max: Int) {
    var isEditing by remember { mutableStateOf(false) }
    var editText by remember(value) { mutableStateOf("$value") }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, style = MaterialTheme.typography.bodySmall)
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { if (value > min) onChange(value - 1) }, enabled = value > min) {
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Decrease")
            }
            if (isEditing) {
                androidx.compose.material3.OutlinedTextField(
                    value = editText,
                    onValueChange = { newText ->
                        editText = newText.filter { it.isDigit() }
                    },
                    modifier = Modifier.width(56.dp),
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    ),
                    singleLine = true,
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                        keyboardType = androidx.compose.ui.text.input.KeyboardType.Number,
                        imeAction = androidx.compose.ui.text.input.ImeAction.Done
                    ),
                    keyboardActions = androidx.compose.foundation.text.KeyboardActions(
                        onDone = {
                            val parsed = editText.toIntOrNull()?.coerceIn(min, max) ?: value
                            onChange(parsed)
                            isEditing = false
                        }
                    )
                )
            } else {
                Text(
                    "$value",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .width(28.dp)
                        .clickable { isEditing = true; editText = "$value" },
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            IconButton(onClick = { if (value < max) onChange(value + 1) }, enabled = value < max) {
                Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Increase")
            }
        }
    }
}
