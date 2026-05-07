package com.laniao.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.laniao.domain.model.DrinkEntry
import com.laniao.domain.model.DrinkType
import com.laniao.domain.model.DrinkUnit
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

/**
 * Dialog for adding or editing a drink entry with a time picker.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDrinkDialog(
    onDismiss: () -> Unit,
    onSave: (DrinkType, Double, DrinkUnit, String?, Instant) -> Unit,
    existingEntry: DrinkEntry? = null,
    onDelete: ((DrinkEntry) -> Unit)? = null,
    defaultDate: LocalDate? = null
) {
    val zoneId = remember { ZoneId.systemDefault() }
    val isEdit = existingEntry != null

    var selectedType by rememberSaveable { mutableStateOf(existingEntry?.type ?: DrinkType.WATER) }
    var selectedUnit by rememberSaveable { mutableStateOf(existingEntry?.unit ?: DrinkUnit.OZ) }
    var amountText by rememberSaveable {
        mutableStateOf(existingEntry?.amount?.let { "%.0f".format(it) } ?: "")
    }
    var customName by rememberSaveable { mutableStateOf(existingEntry?.customName ?: "") }
    var showTypeDropdown by remember { mutableStateOf(false) }

    var showDeleteDialog by rememberSaveable { mutableStateOf(false) }

    val drinkInitialDate = if (existingEntry != null) {
        existingEntry.timestamp.atZone(zoneId).toLocalDate()
    } else {
        val req = defaultDate ?: LocalDate.now()
        if (req.isAfter(LocalDate.now())) LocalDate.now() else req
    }
    var selectedDate by remember { mutableStateOf(drinkInitialDate) }
    var showDatePicker by remember { mutableStateOf(false) }

    val initialTime = remember {
        existingEntry?.timestamp?.atZone(zoneId)?.toLocalTime() ?: LocalTime.now()
    }
    val timePickerState = rememberTimePickerState(
        initialHour = initialTime.hour,
        initialMinute = initialTime.minute,
        is24Hour = true
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isEdit) "Edit Drink" else "Add Drink") },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
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
                TimePicker(state = timePickerState)

                // Type selector
                ExposedDropdownMenuBox(
                    expanded = showTypeDropdown,
                    onExpandedChange = { showTypeDropdown = it }
                ) {
                    OutlinedTextField(
                        value = if (selectedType == DrinkType.CUSTOM && customName.isNotBlank())
                            customName else selectedType.displayName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Type") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showTypeDropdown) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    )
                    ExposedDropdownMenu(
                        expanded = showTypeDropdown,
                        onDismissRequest = { showTypeDropdown = false }
                    ) {
                        DrinkType.entries.forEach { type ->
                            DropdownMenuItem(
                                text = { Text(type.displayName) },
                                onClick = {
                                    selectedType = type
                                    showTypeDropdown = false
                                }
                            )
                        }
                    }
                }

                // Custom name
                if (selectedType == DrinkType.CUSTOM) {
                    OutlinedTextField(
                        value = customName,
                        onValueChange = { customName = it.take(50) },
                        label = { Text("Drink name") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }

                // Amount
                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it },
                    label = { Text("Amount") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Unit chips
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DrinkUnit.entries.forEach { unit ->
                        FilterChip(
                            selected = selectedUnit == unit,
                            onClick = { selectedUnit = unit },
                            label = { Text(unit.displayName) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val amt = amountText.toDoubleOrNull()
                    if (amt != null && amt > 0) {
                        val pickedTime = LocalTime.of(timePickerState.hour, timePickerState.minute)
                        val date = existingEntry?.timestamp?.atZone(zoneId)?.toLocalDate()
                            ?: selectedDate
                        val ts = pickedTime.atDate(date).atZone(zoneId).toInstant()
                        onSave(
                            selectedType,
                            amt,
                            selectedUnit,
                            if (selectedType == DrinkType.CUSTOM) customName.ifBlank { null } else null,
                            ts
                        )
                        onDismiss()
                    }
                }
            ) {
                Text(if (isEdit) "Save" else "Add")
            }
        },
        dismissButton = {
            Row {
                if (isEdit && existingEntry != null && onDelete != null) {
                    TextButton(onClick = { showDeleteDialog = true }) {
                        Text("Delete", color = MaterialTheme.colorScheme.error)
                    }
                }
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        }
    )

    // Delete confirmation
    if (showDeleteDialog && existingEntry != null && onDelete != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Drink?") },
            text = { Text("This action cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    onDelete(existingEntry)
                    onDismiss()
                }) { Text("Delete", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("Cancel") }
            }
        )
    }

    // Date picker dialog
    if (showDatePicker) {
        val todayMillis = LocalDate.now().atStartOfDay(java.time.ZoneId.of("UTC")).toInstant().toEpochMilli()
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate.atStartOfDay(java.time.ZoneId.of("UTC")).toInstant().toEpochMilli(),
            selectableDates = object : androidx.compose.material3.SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    return utcTimeMillis <= todayMillis + 86400000L
                }
            }
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        selectedDate = java.time.Instant.ofEpochMilli(millis).atZone(java.time.ZoneId.of("UTC")).toLocalDate()
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
