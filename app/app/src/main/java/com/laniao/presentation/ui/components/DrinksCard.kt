package com.laniao.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.laniao.domain.model.DrinkEntry
import com.laniao.domain.model.DrinkType
import com.laniao.domain.model.DrinkUnit
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * Card for logging and displaying today's drink entries.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrinksCard(
    todayDrinks: List<DrinkEntry>,
    todayLiters: Double,
    onAddDrink: (DrinkType, Double, DrinkUnit, String?) -> Unit,
    onDeleteDrink: (DrinkEntry) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedType by remember { mutableStateOf(DrinkType.WATER) }
    var selectedUnit by remember { mutableStateOf(DrinkUnit.OZ) }
    var amountText by remember { mutableStateOf("") }
    var customName by remember { mutableStateOf("") }
    var showTypeDropdown by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.secondaryContainer,
        tonalElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header with total
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Drinks",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    text = "%.2f L today".format(todayLiters),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }

            // Type selector dropdown
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

            // Custom name field (only for CUSTOM type)
            if (selectedType == DrinkType.CUSTOM) {
                OutlinedTextField(
                    value = customName,
                    onValueChange = { customName = it.take(50) },
                    label = { Text("Drink name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            // Amount + unit row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it },
                    label = { Text("Amount") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )

                // Unit chips
                DrinkUnit.entries.forEach { unit ->
                    FilterChip(
                        selected = selectedUnit == unit,
                        onClick = { selectedUnit = unit },
                        label = { Text(unit.displayName) }
                    )
                }
            }

            // Add button
            TextButton(
                onClick = {
                    val amt = amountText.toDoubleOrNull()
                    if (amt != null && amt > 0) {
                        onAddDrink(
                            selectedType,
                            amt,
                            selectedUnit,
                            if (selectedType == DrinkType.CUSTOM) customName.ifBlank { null } else null
                        )
                        amountText = ""
                    }
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.padding(end = 4.dp))
                Text("Add Drink")
            }

            // Today's drink entries
            if (todayDrinks.isNotEmpty()) {
                Text(
                    text = "Today's drinks",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                )
                todayDrinks.forEach { drink ->
                    DrinkRow(drink = drink, onDelete = { onDeleteDrink(drink) })
                }
            }
        }
    }
}

@Composable
private fun DrinkRow(
    drink: DrinkEntry,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val timeFormatter = remember { com.laniao.util.DateFormatters.TIME_SHORT }
    val time = remember(drink.timestamp) {
        drink.timestamp.atZone(ZoneId.systemDefault()).toLocalTime().format(timeFormatter)
    }
    val label = if (drink.type == DrinkType.CUSTOM && !drink.customName.isNullOrBlank()) {
        drink.customName
    } else {
        drink.type.displayName
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "$label — ${"%.0f".format(drink.amount)} ${drink.unit.displayName}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Text(
                text = "$time · ${"%.3f".format(drink.liters)} L",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.6f)
            )
        }
        IconButton(onClick = onDelete) {
            Icon(
                Icons.Default.Delete,
                contentDescription = "Remove drink",
                tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
            )
        }
    }
}
