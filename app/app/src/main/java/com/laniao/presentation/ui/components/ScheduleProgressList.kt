package com.laniao.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.laniao.domain.usecase.ScheduleProgressItem
import com.laniao.domain.usecase.ScheduledTimeStatus
import java.time.format.DateTimeFormatter

/**
 * List showing progress of scheduled void times for the day.
 * Completed ✅ (or 🎈 if burst), Missed ⚠️, Upcoming ○
 */
@Composable
fun ScheduleProgressList(
    progress: List<ScheduleProgressItem>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        if (progress.isEmpty()) {
                Text(
                    text = "No scheduled times yet",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            } else {
                // Summary counts
                val completed = progress.count { 
                    it.status == ScheduledTimeStatus.COMPLETED || 
                    it.status == ScheduledTimeStatus.COMPLETED_BURST 
                }
                val overdue = progress.count { it.status == ScheduledTimeStatus.OVERDUE }
                val missed = progress.count { it.status == ScheduledTimeStatus.MISSED }
                val upcoming = progress.count { it.status == ScheduledTimeStatus.UPCOMING }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ProgressSummaryItem(count = completed, label = "Done", emoji = "✅")
                    ProgressSummaryItem(count = overdue, label = "Overdue", emoji = "⏰")
                    ProgressSummaryItem(count = missed, label = "Missed", emoji = "❌")
                    ProgressSummaryItem(count = upcoming, label = "Upcoming", emoji = "⏳")
                }

                // Individual time items
                progress.forEach { item ->
                    ProgressTimeItem(item = item)
                }
            }
    }
}

@Composable
private fun ProgressSummaryItem(
    count: Int,
    label: String,
    emoji: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = emoji,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = count.toString(),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
        )
    }
}

@Composable
private fun ProgressTimeItem(
    item: ScheduleProgressItem,
    modifier: Modifier = Modifier
) {
    val (emoji, textColor) = when (item.status) {
        ScheduledTimeStatus.COMPLETED -> "✅" to MaterialTheme.colorScheme.primary
        ScheduledTimeStatus.COMPLETED_BURST -> "\uD83C\uDF88" to MaterialTheme.colorScheme.error
        ScheduledTimeStatus.OVERDUE -> "⏰" to MaterialTheme.colorScheme.tertiary
        ScheduledTimeStatus.MISSED -> "❌" to MaterialTheme.colorScheme.error
        ScheduledTimeStatus.UPCOMING -> "⏳" to MaterialTheme.colorScheme.onSurfaceVariant
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = emoji,
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = item.scheduledTime.format(TIME_FORMATTER),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = textColor
            )

            if (item.entry != null && item.status == ScheduledTimeStatus.COMPLETED_BURST) {
                Text(
                    text = "Burst urgency!",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

private val TIME_FORMATTER: DateTimeFormatter = com.laniao.util.DateFormatters.TIME_SHORT
