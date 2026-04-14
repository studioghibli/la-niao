package com.laniao.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.laniao.domain.model.DrinkEntry
import com.laniao.domain.model.DrinkType
import com.laniao.domain.model.ExerciseCompletion
import com.laniao.domain.model.ExerciseType
import com.laniao.domain.model.PeeEntry
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * Displays a list of all entries for a specific day.
 */
@Composable
fun DayEntryList(
    date: LocalDate,
    entries: List<PeeEntry>,
    drinks: List<DrinkEntry> = emptyList(),
    exercises: List<ExerciseCompletion> = emptyList(),
    onEntryClick: (PeeEntry) -> Unit,
    onEntryDelete: (PeeEntry) -> Unit,
    onDrinkClick: ((DrinkEntry) -> Unit)? = null,
    onExerciseClick: ((ExerciseCompletion) -> Unit)? = null,
    onBackToCalendar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dateFormatter = remember { com.laniao.util.DateFormatters.DATE_LONG }
    val timeFormatter = remember { com.laniao.util.DateFormatters.TIME_SHORT }
    val zoneId = remember { ZoneId.systemDefault() }
    
    Column(modifier = modifier.fillMaxWidth()) {
        // Date header with back button
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
                    text = date.format(dateFormatter),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "← Calendar",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        
        // Entry count
        val totalCount = entries.size + drinks.size + exercises.size
        Text(
            text = "$totalCount ${if (totalCount == 1) "entry" else "entries"}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        
        // Merge pee entries, drinks, and exercises into a single time-sorted list
        data class MergedItem(val pee: PeeEntry?, val drink: DrinkEntry?, val exercise: ExerciseCompletion?, val sortTime: java.time.Instant, val key: String)
        val mergedItems = remember(entries, drinks, exercises) {
            val peeItems = entries.map { MergedItem(pee = it, drink = null, exercise = null, sortTime = it.timestamp, key = "pee_${it.id}") }
            val drinkItems = drinks.map { MergedItem(pee = null, drink = it, exercise = null, sortTime = it.timestamp, key = "drink_${it.id}") }
            val exerciseItems = exercises.map { MergedItem(pee = null, drink = null, exercise = it, sortTime = it.completedAt, key = "exercise_${it.id}") }
            (peeItems + drinkItems + exerciseItems).sortedByDescending { it.sortTime }
        }

        if (mergedItems.isEmpty()) {
            Text(
                text = "No entries yet today",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                modifier = Modifier.padding(16.dp)
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                items(
                    items = mergedItems,
                    key = { it.key }
                ) { item ->
                    if (item.pee != null) {
                        DayEntryRow(
                            entry = item.pee,
                            timeFormatter = timeFormatter,
                            zoneId = zoneId,
                            onClick = { onEntryClick(item.pee) },
                            onDelete = { onEntryDelete(item.pee) }
                        )
                    } else if (item.drink != null) {
                        DayDrinkRow(
                            drink = item.drink,
                            timeFormatter = timeFormatter,
                            zoneId = zoneId,
                            onClick = { onDrinkClick?.invoke(item.drink) }
                        )
                    } else if (item.exercise != null) {
                        DayExerciseRow(
                            exercise = item.exercise,
                            timeFormatter = timeFormatter,
                            zoneId = zoneId,
                            onClick = { onExerciseClick?.invoke(item.exercise) }
                        )
                    }
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                }
            }
        }
    }
}

@Composable
private fun DayEntryRow(
    entry: PeeEntry,
    timeFormatter: DateTimeFormatter,
    zoneId: ZoneId,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val entryTime = remember(entry.timestamp) {
        entry.timestamp.atZone(zoneId).toLocalTime().format(timeFormatter)
    }
    
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Time
                Text(
                    text = entryTime,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                
                // Entry details
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Type (void/urge/leak)
                    Text(
                        text = when {
                            entry.didVoid && entry.hasLeak -> "\uD83D\uDEBD Void + \uD83C\uDF88 Leak"
                            entry.didVoid -> "\uD83D\uDEBD Void"
                            entry.isLeakOnly -> "\uD83C\uDF88 Leak (${entry.leakAmount.name.lowercase().replaceFirstChar { it.uppercase() }})"
                            else -> "\uD83D\uDE23 Urge"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    // Volume if void
                    if (entry.didVoid) {
                        Text(
                            text = "• ${entry.volumeSize.name.lowercase().replaceFirstChar { it.uppercase() }}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    // Color
                    Text(
                        text = "• ${entry.color.name.lowercase().replace('_', ' ').replaceFirstChar { it.uppercase() }}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    // Scheduled indicator
                    if (entry.scheduledTime != null) {
                        Text(
                            text = "• ⏰",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
                
                // Notes preview if any
                if (!entry.notes.isNullOrBlank()) {
                    Text(
                        text = entry.notes.take(50) + if (entry.notes.length > 50) "..." else "",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
            
            // Actions
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                    )
                }
                
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Edit",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun DayDrinkRow(
    drink: DrinkEntry,
    timeFormatter: DateTimeFormatter,
    zoneId: ZoneId,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val drinkTime = remember(drink.timestamp) {
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

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = emoji, style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(end = 12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = drinkTime,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "$name \u2022 ${"%.0f".format(drink.amount)} ${drink.unit.displayName} \u2022 ${"%.3f".format(drink.liters)} L",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Edit",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun DayExerciseRow(
    exercise: ExerciseCompletion,
    timeFormatter: DateTimeFormatter,
    zoneId: ZoneId,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val exerciseTime = remember(exercise.completedAt) {
        exercise.completedAt.atZone(zoneId).toLocalTime().format(timeFormatter)
    }
    val emoji = exercise.exerciseType.emoji
    val name = exercise.exerciseType.displayName

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = emoji, style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(end = 12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = exerciseTime,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
        }
    }
}
