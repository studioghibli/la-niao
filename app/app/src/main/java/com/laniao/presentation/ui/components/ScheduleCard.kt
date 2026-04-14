package com.laniao.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.laniao.domain.model.VoidSchedule
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Card displaying active schedule details.
 */
@Composable
fun ScheduleCard(
    schedule: VoidSchedule,
    onEditExpiry: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
    today: LocalDate = LocalDate.now()
) {
    val daysRemaining = schedule.daysRemaining(today)
    
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.primaryContainer,
        tonalElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Active Schedule",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                
                Row {
                    IconButton(onClick = onEditExpiry) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit schedule",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    IconButton(onClick = onDelete) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete schedule",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            // Schedule details
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ScheduleDetailItem(
                    label = "Start",
                    value = schedule.startTime.format(TIME_FORMATTER)
                )
                ScheduleDetailItem(
                    label = "End",
                    value = schedule.endTime.format(TIME_FORMATTER)
                )
                ScheduleDetailItem(
                    label = "Interval",
                    value = formatInterval(schedule.intervalMinutes)
                )
            }

            // Days remaining
            Surface(
                shape = MaterialTheme.shapes.small,
                color = if (daysRemaining <= 3) {
                    MaterialTheme.colorScheme.errorContainer
                } else {
                    MaterialTheme.colorScheme.secondaryContainer
                }
            ) {
                Text(
                    text = when (daysRemaining) {
                        0 -> "Last day!"
                        1 -> "1 day remaining"
                        else -> "$daysRemaining days remaining"
                    },
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    color = if (daysRemaining <= 3) {
                        MaterialTheme.colorScheme.onErrorContainer
                    } else {
                        MaterialTheme.colorScheme.onSecondaryContainer
                    }
                )
            }

            // Expires date
            Text(
                text = "Expires: ${schedule.expiresAt.format(DATE_FORMATTER)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
private fun ScheduleDetailItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
        )
    }
}

private fun formatInterval(minutes: Int): String = when {
    minutes >= 60 && minutes % 60 == 0 -> "${minutes / 60}h"
    minutes >= 60 -> "${minutes / 60}h ${minutes % 60}m"
    else -> "${minutes}m"
}

private val TIME_FORMATTER: DateTimeFormatter = com.laniao.util.DateFormatters.TIME_SHORT
private val DATE_FORMATTER: DateTimeFormatter = com.laniao.util.DateFormatters.DATE_SHORT
