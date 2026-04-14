package com.laniao.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.laniao.domain.model.DailySummary
import com.laniao.domain.usecase.ScheduleProgressItem
import com.laniao.domain.usecase.ScheduledTimeStatus
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

/**
 * Card displaying today's summary statistics.
 * Shows void count, urge-only count, and total output volume.
 */
@Composable
fun SummaryCard(
    summary: DailySummary,
    scheduleProgress: List<ScheduleProgressItem> = emptyList(),
    hasActiveSchedule: Boolean = false,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = com.laniao.presentation.theme.LaNiaoColors.SummaryCardBackground,
        tonalElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .height(24.dp)
                        .background(com.laniao.presentation.theme.LaNiaoColors.SummaryAccent, MaterialTheme.shapes.small)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "\uD83D\uDCCB Today's Summary",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = com.laniao.presentation.theme.LaNiaoColors.SummaryAccent
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                SummaryStatItem(
                    value = summary.voidCount.toString(),
                    label = "Voids",
                    emoji = "\uD83D\uDEBD"
                )
                SummaryStatItem(
                    value = summary.urgeOnlyCount.toString(),
                    label = "Urges Only",
                    emoji = "\uD83D\uDE23"
                )
                SummaryStatItem(
                    value = summary.totalLeakCount.toString(),
                    label = "Leaks",
                    emoji = "\uD83C\uDF88"
                )
            }

            // Schedule status
            val timeFormatter = com.laniao.util.DateFormatters.TIME_SHORT
            // Find first actionable scheduled time (skip completed and manually missed)
            val firstUnclaimed = scheduleProgress.firstOrNull {
                it.status != ScheduledTimeStatus.COMPLETED &&
                it.status != ScheduledTimeStatus.COMPLETED_BURST &&
                it.status != ScheduledTimeStatus.MISSED
            }
            val allCompleted = hasActiveSchedule && scheduleProgress.isNotEmpty() && scheduleProgress.all {
                it.status == ScheduledTimeStatus.COMPLETED || it.status == ScheduledTimeStatus.COMPLETED_BURST
            }
            val allDone = hasActiveSchedule && scheduleProgress.isNotEmpty() && scheduleProgress.all {
                it.status == ScheduledTimeStatus.COMPLETED ||
                it.status == ScheduledTimeStatus.COMPLETED_BURST ||
                it.status == ScheduledTimeStatus.MISSED
            }

            val scheduleText = when {
                !hasActiveSchedule -> "\u23F0 No void schedule active"
                allCompleted -> "\u2705 Schedule complete for today!"
                allDone -> "\u2705 Schedule complete for today!"
                firstUnclaimed != null && firstUnclaimed.status == ScheduledTimeStatus.OVERDUE ->
                    "\u26A0\uFE0F Overdue: ${firstUnclaimed.scheduledTime.format(timeFormatter)}"
                firstUnclaimed != null ->
                    "\u23F0 Next: ${firstUnclaimed.scheduledTime.format(timeFormatter)}"
                else -> "\u23F0 No more scheduled times today"
            }
            val scheduleColor = when {
                !hasActiveSchedule -> com.laniao.presentation.theme.LaNiaoColors.SummaryAccent.copy(alpha = 0.5f)
                allCompleted || allDone -> com.laniao.presentation.theme.LaNiaoColors.ExerciseAccent
                firstUnclaimed?.status == ScheduledTimeStatus.OVERDUE -> MaterialTheme.colorScheme.error
                else -> com.laniao.presentation.theme.LaNiaoColors.SummaryAccent.copy(alpha = 0.8f)
            }
            Text(
                text = scheduleText,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = scheduleColor,
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
private fun SummaryStatItem(
    value: String,
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
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = value,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = com.laniao.presentation.theme.LaNiaoColors.SummaryAccent
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = com.laniao.presentation.theme.LaNiaoColors.SummaryAccent.copy(alpha = 0.8f)
        )
    }
}
