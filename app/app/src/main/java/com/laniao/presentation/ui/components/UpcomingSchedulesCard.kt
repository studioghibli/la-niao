package com.laniao.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.laniao.domain.model.VoidSchedule
import java.time.format.DateTimeFormatter

/**
 * Card displaying schedules that start in the future.
 */
@Composable
fun UpcomingSchedulesCard(
    schedules: List<VoidSchedule>,
    onEditSchedule: (VoidSchedule) -> Unit,
    modifier: Modifier = Modifier
) {
    if (schedules.isEmpty()) return

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.secondaryContainer,
        tonalElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Upcoming Schedules",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )

            schedules.forEach { schedule ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = "${schedule.startTime.format(TIME_FORMATTER)} - ${schedule.endTime.format(TIME_FORMATTER)}",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Text(
                            text = "Starts ${schedule.createdAt.format(DATE_FORMATTER)} • every ${schedule.intervalMinutes} min",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
                        )
                    }

                    Column(horizontalAlignment = androidx.compose.ui.Alignment.End) {
                        Text(
                            text = "Not active yet",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        TextButton(onClick = { onEditSchedule(schedule) }) {
                            Text("Edit")
                        }
                    }
                }
            }
        }
    }
}

private val DATE_FORMATTER: DateTimeFormatter = com.laniao.util.DateFormatters.DATE_SHORT_NO_YEAR
private val TIME_FORMATTER: DateTimeFormatter = com.laniao.util.DateFormatters.TIME_SHORT
