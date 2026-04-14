package com.laniao.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.laniao.domain.model.CalendarDayStats
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

/**
 * Calendar view showing a month grid with rich day cells:
 * - Void count vs scheduled count (e.g. "7-5")
 * - 🔥 if kegels completed
 * - 💦 if leaked
 *
 * Tapping month name opens month picker, tapping year opens year picker.
 */
@Composable
fun CalendarView(
    yearMonth: YearMonth,
    selectedDate: LocalDate,
    dayStats: Map<LocalDate, CalendarDayStats>,
    onDateSelected: (LocalDate) -> Unit,
    onMonthChange: (YearMonth) -> Unit,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    modifier: Modifier = Modifier
) {
    val today = remember { LocalDate.now() }
    var showMonthPicker by remember { mutableStateOf(false) }
    var showYearPicker by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            // Month header with navigation
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onPreviousMonth) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Previous month"
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Tappable month name
                    Text(
                        text = yearMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault()),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .clickable { showMonthPicker = true }
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                    // Tappable year
                    Text(
                        text = yearMonth.year.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .clickable { showYearPicker = true }
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }

                IconButton(onClick = onNextMonth) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Next month"
                    )
                }
            }

            // Day of week headers
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val daysOfWeek = remember {
                    listOf(
                        DayOfWeek.SUNDAY, DayOfWeek.MONDAY, DayOfWeek.TUESDAY,
                        DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY,
                        DayOfWeek.SATURDAY
                    )
                }
                daysOfWeek.forEach { day ->
                    Text(
                        text = day.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Calendar grid
            val firstDayOfMonth = yearMonth.atDay(1)
            val daysInMonth = yearMonth.lengthOfMonth()
            val startOffset = (firstDayOfMonth.dayOfWeek.value % 7)
            val totalCells = startOffset + daysInMonth
            val rows = (totalCells + 6) / 7

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                for (row in 0 until rows) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        for (col in 0 until 7) {
                            val cellIndex = row * 7 + col
                            val dayOfMonth = cellIndex - startOffset + 1

                            if (dayOfMonth in 1..daysInMonth) {
                                val date = yearMonth.atDay(dayOfMonth)
                                val stats = dayStats[date]
                                val isSelected = date == selectedDate
                                val isToday = date == today

                                RichCalendarDay(
                                    day = dayOfMonth,
                                    stats = stats,
                                    isSelected = isSelected,
                                    isToday = isToday,
                                    onClick = { onDateSelected(date) },
                                    modifier = Modifier.weight(1f)
                                )
                            } else {
                                Box(modifier = Modifier.weight(1f).height(56.dp))
                            }
                        }
                    }
                }
            }
        }
    }

    // Month picker dialog
    if (showMonthPicker) {
        MonthPickerDialog(
            currentMonth = yearMonth.monthValue,
            onMonthSelected = { month ->
                onMonthChange(YearMonth.of(yearMonth.year, month))
                showMonthPicker = false
            },
            onDismiss = { showMonthPicker = false }
        )
    }

    // Year picker dialog
    if (showYearPicker) {
        YearPickerDialog(
            currentYear = yearMonth.year,
            onYearSelected = { year ->
                onMonthChange(YearMonth.of(year, yearMonth.monthValue))
                showYearPicker = false
            },
            onDismiss = { showYearPicker = false }
        )
    }
}

@Composable
private fun RichCalendarDay(
    day: Int,
    stats: CalendarDayStats?,
    isSelected: Boolean,
    isToday: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when {
        isSelected -> MaterialTheme.colorScheme.primary
        isToday -> MaterialTheme.colorScheme.primaryContainer
        else -> MaterialTheme.colorScheme.surface
    }

    val textColor = when {
        isSelected -> MaterialTheme.colorScheme.onPrimary
        isToday -> MaterialTheme.colorScheme.onPrimaryContainer
        else -> MaterialTheme.colorScheme.onSurface
    }

    val subtextColor = when {
        isSelected -> MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
        else -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
    }

    Surface(
        modifier = modifier
            .height(56.dp)
            .clip(RoundedCornerShape(6.dp))
            .clickable(onClick = onClick),
        color = backgroundColor,
        tonalElevation = if (stats != null) 1.dp else 0.dp,
        shape = RoundedCornerShape(6.dp)
    ) {
        Box(modifier = Modifier.padding(4.dp)) {
            // Day number in top-left
            Text(
                text = day.toString(),
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = if (isToday || isSelected) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 10.sp
                ),
                color = textColor,
                modifier = Modifier.align(Alignment.TopStart)
            )

            // Schedule indicator dots (top-right)
            if (stats != null && (stats.hasVoidSchedule || stats.hasExerciseSchedule)) {
                Row(
                    modifier = Modifier.align(Alignment.TopEnd),
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    if (stats.hasVoidSchedule) {
                        Box(
                            modifier = Modifier
                                .size(5.dp)
                                .background(Color(0xFFFFCA28), CircleShape)
                        )
                    }
                    if (stats.hasExerciseSchedule) {
                        Box(
                            modifier = Modifier
                                .size(5.dp)
                                .background(Color(0xFF81C784), CircleShape)
                        )
                    }
                }
            }

            // Stats and icons centered
            if (stats != null && (stats.voidCount > 0 || stats.scheduledCount > 0 || stats.hadBurstUrgency || stats.kegelsComplete || stats.metHydrationGoal || stats.perfectSchedule)) {
                Column(
                    modifier = Modifier.align(Alignment.Center).padding(top = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Icons row first
                    val icons = buildString {
                        if (stats.kegelsComplete) append("\uD83D\uDCAA")
                        if (stats.perfectSchedule) append("\u2705")
                        if (stats.metHydrationGoal) append("\uD83C\uDF0A")
                        if (stats.hadBurstUrgency) append("\uD83C\uDF88")
                    }
                    if (icons.isNotEmpty()) {
                        Text(
                            text = icons,
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                            maxLines = 1
                        )
                    }

                    // Stats row underneath: void-scheduled
                    if (stats.voidCount > 0 || stats.scheduledCount > 0) {
                        val label = if (stats.scheduledCount > 0) {
                            "${stats.voidCount}-${stats.scheduledCount}"
                        } else {
                            "${stats.voidCount}"
                        }
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                            color = subtextColor,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MonthPickerDialog(
    currentMonth: Int,
    onMonthSelected: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    val months = remember {
        (1..12).map { month ->
            java.time.Month.of(month).getDisplayName(TextStyle.FULL, Locale.getDefault())
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Month") },
        text = {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(months.size) { index ->
                    val month = index + 1
                    val isSelected = month == currentMonth
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onMonthSelected(month) },
                        color = if (isSelected)
                            MaterialTheme.colorScheme.primaryContainer
                        else
                            MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = months[index].take(3),
                            modifier = Modifier.padding(8.dp),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
private fun YearPickerDialog(
    currentYear: Int,
    onYearSelected: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    val years = remember { (2000..currentYear + 2).toList() }
    val initialIndex = remember { years.indexOf(currentYear).coerceAtLeast(0) }
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = (initialIndex - 2).coerceAtLeast(0))

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Year") },
        text = {
            LazyColumn(
                state = listState,
                modifier = Modifier.height(300.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(years) { year ->
                    val isSelected = year == currentYear
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onYearSelected(year) },
                        color = if (isSelected)
                            MaterialTheme.colorScheme.primaryContainer
                        else
                            MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = year.toString(),
                            modifier = Modifier.padding(12.dp).fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
