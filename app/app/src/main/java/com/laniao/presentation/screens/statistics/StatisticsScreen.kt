package com.laniao.presentation.screens.statistics

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.laniao.presentation.ui.components.ExerciseCompletionChart
import com.laniao.presentation.ui.components.LiquidIntakeChart
import com.laniao.presentation.ui.components.ScheduleAdherenceChart
import com.laniao.presentation.ui.components.VoidFrequencyChart
import com.laniao.presentation.ui.components.VoidGapChart
import com.laniao.presentation.viewmodel.DateRange
import com.laniao.presentation.viewmodel.StatisticsViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    modifier: Modifier = Modifier,
    viewModel: StatisticsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showDateRangePicker by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Statistics", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold) },
                windowInsets = androidx.compose.foundation.layout.WindowInsets(0)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Date range selector (no extra top spacing)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DateRange.entries.filter { it != DateRange.CUSTOM }.forEach { range ->
                    FilterChip(
                        selected = uiState.selectedRange == range,
                        onClick = { viewModel.selectRange(range) },
                        label = { Text(range.label) }
                    )
                }
                FilterChip(
                    selected = uiState.selectedRange == DateRange.CUSTOM,
                    onClick = { showDateRangePicker = true },
                    label = { Text("Custom") }
                )
            }

            // Date range display
            val dateFormatter = com.laniao.util.DateFormatters.DATE_SHORT
            Text(
                text = "${uiState.startDate.format(dateFormatter)} — ${uiState.endDate.format(dateFormatter)}",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                // Void Frequency Chart
                StatisticsCard(
                    title = "Void Frequency",
                    subtitle = "Voids per day (colored by dominant pee color)"
                ) {
                    VoidFrequencyChart(
                        data = uiState.voidFrequency,
                        modifier = Modifier.fillMaxWidth()
                    )

                    val totalVoids = uiState.voidFrequency.sumOf { it.voidCount }
                    val avgVoids = if (uiState.voidFrequency.isNotEmpty())
                        totalVoids.toFloat() / uiState.voidFrequency.size
                    else 0f
                    val maxDay = uiState.voidFrequency.maxByOrNull { it.voidCount }

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatSummaryItem("$totalVoids", "Total")
                        StatSummaryItem("%.1f".format(avgVoids), "Avg/day")
                        if (maxDay != null && maxDay.voidCount > 0) {
                            StatSummaryItem("${maxDay.voidCount}", "Max")
                        }
                    }
                }

                // Average Void Gap Card
                if (uiState.averageVoidGap.isNotEmpty()) {
                    StatisticsCard(
                        title = "Average Gap Between Voids",
                        subtitle = "Average time between consecutive voids per day"
                    ) {
                        VoidGapChart(
                            data = uiState.averageVoidGap,
                            modifier = Modifier.fillMaxWidth()
                        )

                        val daysWithData = uiState.averageVoidGap.filter { it.averageGapMinutes > 0 }
                        val overallAvg = if (daysWithData.isNotEmpty()) daysWithData.map { it.averageGapMinutes }.average() else 0.0
                        val todayGap = daysWithData.lastOrNull()
                        val maxGapDay = daysWithData.maxByOrNull { it.averageGapMinutes }

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            if (todayGap != null) {
                                StatSummaryItem(formatGap(todayGap.averageGapMinutes), "Latest day")
                            }
                            StatSummaryItem(formatGap(overallAvg), "${daysWithData.size}-day avg")
                            if (maxGapDay != null) {
                                StatSummaryItem(formatGap(maxGapDay.averageGapMinutes), "Best")
                            }
                        }
                    }
                }

                // Schedule Adherence Chart
                if (uiState.scheduleAdherence.isNotEmpty()) {
                    StatisticsCard(
                        title = "Schedule Adherence",
                        subtitle = "% of scheduled voids completed each day"
                    ) {
                        ScheduleAdherenceChart(
                            data = uiState.scheduleAdherence,
                            modifier = Modifier.fillMaxWidth()
                        )

                        val totalScheduled = uiState.scheduleAdherence.sumOf { it.totalScheduled }
                        val totalCompleted = uiState.scheduleAdherence.sumOf { it.completed }
                        val overallRate = if (totalScheduled > 0)
                            (totalCompleted.toFloat() / totalScheduled * 100f)
                        else 0f

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            StatSummaryItem("%.0f%%".format(overallRate), "Overall")
                            StatSummaryItem("$totalCompleted/$totalScheduled", "Completed")
                            StatSummaryItem("${uiState.scheduleAdherence.size}", "Days")
                        }
                    }
                }

                // Liquid Intake Chart
                StatisticsCard(
                    title = "Liquid Intake",
                    subtitle = "Liters consumed per day"
                ) {
                    LiquidIntakeChart(
                        data = uiState.liquidIntake,
                        hydrationGoalLiters = uiState.hydrationGoalLiters,
                        modifier = Modifier.fillMaxWidth()
                    )

                    val totalLiters = uiState.liquidIntake.sumOf { it.totalLiters }
                    val avgLiters = if (uiState.liquidIntake.isNotEmpty())
                        totalLiters / uiState.liquidIntake.size
                    else 0.0
                    val maxDay = uiState.liquidIntake.maxByOrNull { it.totalLiters }

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatSummaryItem("%.1fL".format(totalLiters), "Total")
                        StatSummaryItem("%.1fL".format(avgLiters), "Avg/day")
                    }
                }

                // Exercise Completion Chart
                StatisticsCard(
                    title = "Exercise Completion",
                    subtitle = "% of scheduled exercises completed each day"
                ) {
                    ExerciseCompletionChart(
                        data = uiState.exerciseCompletion,
                        modifier = Modifier.fillMaxWidth()
                    )

                    val totalReq = uiState.exerciseCompletion.sumOf { it.totalRequired }
                    val totalDone = uiState.exerciseCompletion.sumOf { it.completed }
                    val overallRate = if (totalReq > 0)
                        (totalDone.toFloat() / totalReq * 100f)
                    else 0f
                    val activeDays = uiState.exerciseCompletion.count { it.totalRequired > 0 }

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatSummaryItem("%.0f%%".format(overallRate), "Overall")
                        StatSummaryItem("$totalDone/$totalReq", "Completed")
                        StatSummaryItem("$activeDays", "Active days")
                    }
                }
            }
        }
    }

    // Date range picker
    if (showDateRangePicker) {
        val rangePickerState = rememberDateRangePickerState(
            initialSelectedStartDateMillis = uiState.startDate
                .atStartOfDay(ZoneId.of("UTC"))
                .toInstant()
                .toEpochMilli(),
            initialSelectedEndDateMillis = uiState.endDate
                .atStartOfDay(ZoneId.of("UTC"))
                .toInstant()
                .toEpochMilli()
        )
        DatePickerDialog(
            onDismissRequest = { showDateRangePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val startMillis = rangePickerState.selectedStartDateMillis
                    val endMillis = rangePickerState.selectedEndDateMillis
                    if (startMillis != null && endMillis != null) {
                        val start = Instant.ofEpochMilli(startMillis)
                            .atZone(ZoneId.of("UTC"))
                            .toLocalDate()
                        val end = Instant.ofEpochMilli(endMillis)
                            .atZone(ZoneId.of("UTC"))
                            .toLocalDate()
                        viewModel.setCustomRange(start, end)
                        showDateRangePicker = false
                    }
                }) { Text("Apply") }
            },
            dismissButton = {
                TextButton(onClick = { showDateRangePicker = false }) { Text("Cancel") }
            }
        ) {
            DateRangePicker(
                state = rangePickerState,
                title = {
                    Text(
                        "Select date range",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 24.dp, top = 16.dp)
                    )
                },
                headline = {
                    val startLabel = rangePickerState.selectedStartDateMillis?.let {
                        Instant.ofEpochMilli(it).atZone(ZoneId.of("UTC")).toLocalDate()
                            .format(com.laniao.util.DateFormatters.DATE_SHORT)
                    } ?: "Start"
                    val endLabel = rangePickerState.selectedEndDateMillis?.let {
                        Instant.ofEpochMilli(it).atZone(ZoneId.of("UTC")).toLocalDate()
                            .format(com.laniao.util.DateFormatters.DATE_SHORT)
                    } ?: "End"
                    Text(
                        "$startLabel – $endLabel",
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        modifier = Modifier.padding(start = 24.dp, bottom = 12.dp)
                    )
                },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

private fun formatGap(minutes: Double): String {
    val hours = (minutes / 60).toInt()
    val mins = (minutes % 60).toInt()
    return when {
        hours > 0 && mins > 0 -> "${hours}h ${mins}m"
        hours > 0 -> "${hours}h"
        else -> "${mins}m"
    }
}

/** Reusable card wrapper for statistics sections. */
@Composable
private fun StatisticsCard(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            content()
        }
    }
}

/** Reusable summary stat item (value + label). */
@Composable
private fun StatSummaryItem(
    value: String,
    label: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
