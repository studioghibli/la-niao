package com.laniao.presentation.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * Alert banner showing missed scheduled void times.
 */
@Composable
fun MissedScheduleAlert(
    missedTimes: List<LocalTime>,
    modifier: Modifier = Modifier
) {
    if (missedTimes.isEmpty()) return

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.errorContainer,
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onErrorContainer,
                modifier = Modifier.padding(end = 12.dp)
            )
            
            Text(
                text = buildMissedMessage(missedTimes),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
        }
    }
}

private fun buildMissedMessage(times: List<LocalTime>): String {
    val formatter = com.laniao.util.DateFormatters.TIME_SHORT
    val formattedTimes = times.take(3).joinToString(", ") { it.format(formatter) }
    
    return when {
        times.size == 1 -> "Overdue scheduled void at $formattedTimes"
        times.size <= 3 -> "Overdue scheduled voids at $formattedTimes"
        else -> "${times.size} overdue scheduled voids today"
    }
}
