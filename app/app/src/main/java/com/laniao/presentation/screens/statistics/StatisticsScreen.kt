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
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "Void Frequency",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Voids per day (colored by dominant pee color)",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        VoidFrequencyChart(
                            data = uiState.voidFrequency,
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Summary stats
                        val totalVoids = uiState.voidFrequency.sumOf { it.voidCount }
                        val avgVoids = if (uiState.voidFrequency.isNotEmpty())
                            totalVoids.toFloat() / uiState.voidFrequency.size
                        else 0f
                        val maxDay = uiState.voidFrequency.maxByOrNull { it.voidCount }

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("$totalVoids", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Text("Total", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("%.1f".format(avgVoids), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Text("Avg/day", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            if (maxDay != null && maxDay.voidCount > 0) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("${maxDay.voidCount}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                    Text("Max", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }
                    }
                }

                // Average Void Gap Card
                if (uiState.averageVoidGap.isNotEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "Average Gap Between Voids",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Average time between consecutive voids per day",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )

                            // Bar chart
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
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = formatGap(todayGap.averageGapMinutes),
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text("Latest day", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = formatGap(overallAvg),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text("${daysWithData.size}-day avg", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                if (maxGapDay != null) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = formatGap(maxGapDay.averageGapMinutes),
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text("Best", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }
                            }
                        }
                    }
                }

                // Schedule Adherence Chart
                if (uiState.scheduleAdherence.isNotEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "Schedule Adherence",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "% of scheduled voids completed each day",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )

                            ScheduleAdherenceChart(
                                data = uiState.scheduleAdherence,
                                modifier = Modifier.fillMaxWidth()
                            )

                            // Summary stats
                            val totalScheduled = uiState.scheduleAdherence.sumOf { it.totalScheduled }
                            val totalCompleted = uiState.scheduleAdherence.sumOf { it.completed }
                            val overallRate = if (totalScheduled > 0)
                                (totalCompleted.toFloat() / totalScheduled * 100f)
                            else 0f

                            Row(
                                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("%.0f%%".format(overallRate), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                    Text("Overall", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("$totalCompleted/$totalScheduled", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                    Text("Completed", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("${uiState.scheduleAdherence.size}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                    Text("Days", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }
                    }
                }

                // Liquid Intake Chart
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "Liquid Intake",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Liters consumed per day",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        LiquidIntakeChart(
                            data = uiState.liquidIntake,
                            hydrationGoalLiters = uiState.hydrationGoalLiters,
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Summary stats
                        val totalLiters = uiState.liquidIntake.sumOf { it.totalLiters }
                        val avgLiters = if (uiState.liquidIntake.isNotEmpty())
                            totalLiters / uiState.liquidIntake.size
                        else 0.0
                        val maxDay = uiState.liquidIntake.maxByOrNull { it.totalLiters }

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("%.1fL".format(totalLiters), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Text("Total", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("%.1fL".format(avgLiters), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Text("Avg/day", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            if (maxDay != null && maxDay.totalLiters > 0) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("%.1fL".format(maxDay.totalLiters), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                    Text("Max", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }
                    }
                }

                // Exercise Completion Chart
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "Exercise Completion",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "% of scheduled exercises completed each day",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        ExerciseCompletionChart(
                            data = uiState.exerciseCompletion,
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Summary stats
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
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("%.0f%%".format(overallRate), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Text("Overall", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("$totalDone/$totalReq", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Text("Completed", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("$activeDays", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Text("Active days", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
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
