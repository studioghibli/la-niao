package com.laniao.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.laniao.domain.model.PeeEntry
import java.time.format.DateTimeFormatter

/**
 * Displays a list of recent pee entries.
 * Each entry is tappable to navigate to edit screen.
 */
@Composable
fun RecentEntriesList(
    entries: List<PeeEntry>,
    onEntryClick: (PeeEntry) -> Unit,
    onSeeAllClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recent Entries",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                
                TextButton(onClick = onSeeAllClick) {
                    Text("See all")
                }
            }

            if (entries.isEmpty()) {
                Text(
                    text = "No entries yet today",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            } else {
                entries.forEachIndexed { index, entry ->
                    RecentEntryRow(
                        entry = entry,
                        onClick = { onEntryClick(entry) }
                    )
                    if (index < entries.lastIndex) {
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.outlineVariant,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RecentEntryRow(
    entry: PeeEntry,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Entry type indicator
        Text(
            text = when {
                entry.didVoid -> "\uD83D\uDEBD"
                entry.isLeakOnly -> "\uD83C\uDF88"
                else -> "\uD83D\uDE23"
            },
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(end = 12.dp)
        )

        // Entry details
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = entry.timestamp.atZone(java.time.ZoneId.systemDefault())
                    .format(TIME_FORMATTER),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            
            val details = buildEntryDetails(entry)
            if (details.isNotEmpty()) {
                Text(
                    text = details,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
        }

        // Arrow indicator
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "View details",
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
        )
    }
}

private fun buildEntryDetails(entry: PeeEntry): String {
    val parts = mutableListOf<String>()
    
    if (entry.didVoid) {
        if (entry.volumeSize != com.laniao.domain.model.VolumeSize.UNKNOWN) {
            parts.add(entry.volumeSize.toDisplayName())
        }
        if (entry.color != com.laniao.domain.model.PeeColor.UNKNOWN) {
            parts.add(entry.color.toDisplayName())
        }
    } else if (entry.isLeakOnly) {
        parts.add("Leak (${entry.leakAmount.name.lowercase().replaceFirstChar { it.uppercase() }})")
    } else {
        parts.add("Urge")
    }

    if (entry.didVoid && entry.hasLeak) {
        parts.add("Leak: ${entry.leakAmount.name.lowercase().replaceFirstChar { it.uppercase() }}")
    }
    
    if (entry.urgency != com.laniao.domain.model.Urgency.UNKNOWN) {
        parts.add("Urgency: ${entry.urgency.toDisplayName()}")
    }
    
    if (entry.scheduledTime != null) {
        parts.add("Scheduled")
    }
    
    return parts.joinToString(" • ")
}

/**
 * Extension to convert VolumeSize to user-friendly display name.
 */
private fun com.laniao.domain.model.VolumeSize.toDisplayName(): String = when (this) {
    com.laniao.domain.model.VolumeSize.UNKNOWN -> "Unknown"
    com.laniao.domain.model.VolumeSize.SMALL -> "Small"
    com.laniao.domain.model.VolumeSize.MEDIUM -> "Medium"
    com.laniao.domain.model.VolumeSize.LARGE -> "Large"
}

/**
 * Extension to convert PeeColor to user-friendly display name.
 */
private fun com.laniao.domain.model.PeeColor.toDisplayName(): String = when (this) {
    com.laniao.domain.model.PeeColor.UNKNOWN -> "Unknown"
    com.laniao.domain.model.PeeColor.CLEAR -> "Clear"
    com.laniao.domain.model.PeeColor.LIGHT_YELLOW -> "Light Yellow"
    com.laniao.domain.model.PeeColor.YELLOW -> "Yellow"
    com.laniao.domain.model.PeeColor.DARK_YELLOW -> "Dark Yellow"
    com.laniao.domain.model.PeeColor.AMBER -> "Amber"
}

/**
 * Extension to convert Urgency to user-friendly display name.
 */
private fun com.laniao.domain.model.Urgency.toDisplayName(): String = when (this) {
    com.laniao.domain.model.Urgency.UNKNOWN -> "Unknown"
    com.laniao.domain.model.Urgency.NONE -> "None"
    com.laniao.domain.model.Urgency.LOW -> "Low"
    com.laniao.domain.model.Urgency.MEDIUM -> "Medium"
    com.laniao.domain.model.Urgency.HIGH -> "High"
    com.laniao.domain.model.Urgency.BURST -> "Burst \uD83C\uDF88"
}

private val TIME_FORMATTER: DateTimeFormatter = com.laniao.util.DateFormatters.TIME_SHORT
