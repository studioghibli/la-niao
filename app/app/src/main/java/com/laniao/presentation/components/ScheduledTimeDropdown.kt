package com.laniao.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * Dropdown for selecting a scheduled time from unclaimed times.
 * Shows "Unscheduled" option plus available scheduled times.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduledTimeDropdown(
    selectedTime: LocalTime?,
    unclaimedTimes: List<LocalTime>,
    onTimeSelected: (LocalTime?) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    suggestedTime: LocalTime? = null
) {
    var expanded by remember { mutableStateOf(false) }
    val timeFormatter = remember { com.laniao.util.DateFormatters.TIME_SHORT }

    val displayText = when {
        selectedTime == null -> "Unscheduled"
        else -> selectedTime.format(timeFormatter)
    }

    Column(modifier = modifier) {
        Text(
            text = "Scheduled Time",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { if (enabled) expanded = it },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            OutlinedTextField(
                value = displayText,
                onValueChange = {},
                readOnly = true,
                enabled = enabled,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    .semantics {
                        contentDescription = "Scheduled time: $displayText"
                    }
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                // Unscheduled option
                DropdownMenuItem(
                    text = { Text("Unscheduled") },
                    onClick = {
                        onTimeSelected(null)
                        expanded = false
                    }
                )

                // Available scheduled times
                unclaimedTimes.forEach { time ->
                    val isSuggested = time == suggestedTime
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = if (isSuggested) {
                                    "${time.format(timeFormatter)} (suggested)"
                                } else {
                                    time.format(timeFormatter)
                                }
                            )
                        },
                        onClick = {
                            onTimeSelected(time)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
