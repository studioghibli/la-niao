package com.laniao.presentation.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.laniao.domain.model.DrinkEntry
import com.laniao.domain.model.DrinkType
import com.laniao.domain.model.ExerciseCompletion
import com.laniao.domain.model.ExerciseType
import com.laniao.domain.model.PeeColor
import com.laniao.domain.usecase.TimelineItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private const val HOUR_LABEL_WIDTH_DP = 64f
private const val MIN_HOUR_HEIGHT_DP = 40f
private const val MAX_HOUR_HEIGHT_DP = 160f
private const val DEFAULT_HOUR_HEIGHT_DP = 60f
private const val EVENT_HEIGHT_DP = 28f
private const val DRINK_HEIGHT_DP = 18f
private const val TOTAL_HOURS = 24.5f // 24h + 0.5h bottom padding for late entries

/**
 * Full-day scrollable timeline with pinch-to-zoom, fixed hour rows,
 * and events positioned at their actual minute offset.
 */
@Composable
fun DayTimelineCard(
    date: LocalDate,
    items: List<TimelineItem>,
    onEntryClick: (Long) -> Unit,
    onDrinkClick: ((DrinkEntry) -> Unit)? = null,
    onExerciseClick: ((ExerciseCompletion) -> Unit)? = null,
    onScheduleMarkerClick: ((TimelineItem) -> Unit)? = null,
    onSwipeLeft: (() -> Unit)? = null,
    onSwipeRight: (() -> Unit)? = null,
    scrollToNowPending: Boolean = false,
    scrollToNowAnimated: Boolean = false,
    onScrollToNowHandled: (() -> Unit)? = null,
    initialScrollOffsetPx: Float = 0f,
    initialHourHeightDp: Float = DEFAULT_HOUR_HEIGHT_DP,
    onScrollStateChanged: ((scrollOffsetPx: Float, hourHeightDp: Float) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var currentNow by remember { mutableStateOf(LocalDateTime.now()) }

    // Update current time every minute for the red line indicator
    LaunchedEffect(Unit) {
        while (isActive) {
            delay(60_000L)
            currentNow = LocalDateTime.now()
        }
    }

    var hourHeightDp by remember { mutableFloatStateOf(initialHourHeightDp) }
    var scrollOffsetPx by remember { mutableFloatStateOf(initialScrollOffsetPx) }
    var horizontalDragPx by remember { mutableFloatStateOf(0f) }
    val density = LocalDensity.current
    val is24Hour = android.text.format.DateFormat.is24HourFormat(LocalContext.current)
    val hourFormatter = remember(is24Hour) {
        if (is24Hour) DateTimeFormatter.ofPattern("HH:00")
        else DateTimeFormatter.ofPattern("h:00 a")
    }

    // Animate scroll to current time when pending (one-shot, then releases control)
    LaunchedEffect(scrollToNowPending) {
        if (scrollToNowPending && date == LocalDate.now()) {
            val nowMinute = LocalDateTime.now().hour * 60 + LocalDateTime.now().minute
            val nowPx = hourHeightDp * nowMinute / 60f * density.density
            val viewportHeight = 600f * density.density
            val totalPx = hourHeightDp * TOTAL_HOURS * density.density
            val maxScroll = (totalPx - viewportHeight).coerceAtLeast(0f)
            val target = (nowPx - viewportHeight / 2).coerceIn(0f, maxScroll)
            if (scrollToNowAnimated) {
                val animatable = Animatable(scrollOffsetPx)
                animatable.animateTo(target, animationSpec = tween(500)) {
                    scrollOffsetPx = value
                }
            } else {
                scrollOffsetPx = target
            }
            onScrollToNowHandled?.invoke()
        }
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clipToBounds()
                    .pointerInput(Unit) {
                        awaitEachGesture {
                            awaitFirstDown(requireUnconsumed = false)
                            do {
                                val event = awaitPointerEvent()
                                val pointers = event.changes
                                if (pointers.size >= 2) {
                                    // Two+ fingers: pinch to zoom
                                    val zoom = event.calculateZoom()
                                    if (zoom != 1f) {
                                        hourHeightDp = (hourHeightDp * zoom)
                                            .coerceIn(MIN_HOUR_HEIGHT_DP, MAX_HOUR_HEIGHT_DP)
                                        // Clamp scroll so content stays visible after zoom
                                        val newTotalPx = hourHeightDp * TOTAL_HOURS * density.density
                                        val newMaxScroll = (newTotalPx - size.height).coerceAtLeast(0f)
                                        scrollOffsetPx = scrollOffsetPx.coerceIn(0f, newMaxScroll)
                                        pointers.forEach { it.consume() }
                                    }
                                } else if (pointers.size == 1) {
                                    // Single finger: scroll vertically + track horizontal
                                    val pan = event.calculatePan()
                                    if (pan.y != 0f) {
                                        val totalPx = hourHeightDp * TOTAL_HOURS * density.density
                                        val maxScroll = (totalPx - size.height).coerceAtLeast(0f)
                                        scrollOffsetPx = (scrollOffsetPx - pan.y).coerceIn(0f, maxScroll)
                                        pointers.forEach { if (it.positionChanged()) it.consume() }
                                    }
                                    horizontalDragPx += pan.x
                                }
                            } while (pointers.any { it.pressed })
                            // Report scroll state back to parent
                            onScrollStateChanged?.invoke(scrollOffsetPx, hourHeightDp)
                            // Gesture ended — check for horizontal swipe
                            if (horizontalDragPx > 300f) {
                                onSwipeRight?.invoke()
                            } else if (horizontalDragPx < -300f) {
                                onSwipeLeft?.invoke()
                            }
                            horizontalDragPx = 0f
                        }
                    }
            ) {
                val totalHeight = (hourHeightDp * TOTAL_HOURS).dp // includes bottom padding for late entries
                val scrollOffsetDp = with(density) { scrollOffsetPx.toDp() }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(totalHeight)
                        .offset(y = -scrollOffsetDp)
                ) {
                        // Hour lines & labels
                        for (hour in 0..23) {
                            val yOffset = (hourHeightDp * hour).dp
                            HourLine(
                                hour = hour,
                                yOffset = yOffset,
                                lineColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                                formatter = hourFormatter
                            )
                        }

                        // Assign columns so overlapping events sit side-by-side
                        val layoutItems = assignColumns(items, hourHeightDp)

                        layoutItems.forEach { li ->
                            val yDp = (hourHeightDp * li.minuteOfDay / 60f).dp

                            if (li.item.isDrink) {
                                DrinkTimelineEvent(
                                    item = li.item,
                                    yOffset = yDp,
                                    column = li.column,
                                    totalColumns = li.totalColumns,
                                    onClick = { li.item.drinkEntry?.let { onDrinkClick?.invoke(it) } }
                                )
                            } else if (li.item.isExercise) {
                                ExerciseTimelineEvent(
                                    item = li.item,
                                    yOffset = yDp,
                                    column = li.column,
                                    totalColumns = li.totalColumns,
                                    onClick = { li.item.exerciseCompletion?.let { onExerciseClick?.invoke(it) } }
                                )
                            } else {
                                TimelineEvent(
                                    item = li.item,
                                    date = date,
                                    now = currentNow,
                                    yOffset = yDp,
                                    column = li.column,
                                    totalColumns = li.totalColumns,
                                    onEntryClick = onEntryClick,
                                    onScheduleMarkerClick = onScheduleMarkerClick
                                )
                            }
                        }

                        // Current time indicator (red dot + line) — only for today
                        if (date == currentNow.toLocalDate()) {
                            val nowMinute = currentNow.hour * 60 + currentNow.minute
                            val nowYDp = (hourHeightDp * nowMinute / 60f).dp
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .offset { IntOffset(0, nowYDp.roundToPx()) }
                                    .padding(start = (HOUR_LABEL_WIDTH_DP - 4).dp)
                            ) {
                                Canvas(modifier = Modifier.fillMaxWidth().height(8.dp)) {
                                    // Red dot at left end
                                    drawCircle(
                                        color = Color.Red,
                                        radius = 4.dp.toPx(),
                                        center = Offset(4.dp.toPx(), size.height / 2)
                                    )
                                    // Red line from dot to right edge
                                    drawLine(
                                        color = Color.Red,
                                        start = Offset(8.dp.toPx(), size.height / 2),
                                        end = Offset(size.width, size.height / 2),
                                        strokeWidth = 2f
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

@Composable
private fun HourLine(
    hour: Int,
    yOffset: Dp,
    lineColor: Color,
    formatter: DateTimeFormatter = HOUR_FORMATTER
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .offset { IntOffset(0, yOffset.roundToPx()) }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = LocalTime.of(hour, 0).format(formatter),
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                softWrap = false,
                modifier = Modifier.width(HOUR_LABEL_WIDTH_DP.dp)
            )
            Canvas(modifier = Modifier.fillMaxWidth().height(1.dp)) {
                drawLine(
                    color = lineColor,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = 1f
                )
            }
        }
    }
}

/**
 * Positioned event with column assignment for side-by-side overlap rendering.
 */
private data class LayoutItem(
    val item: TimelineItem,
    val minuteOfDay: Int,
    val endMinute: Int,
    var column: Int = 0,
    var totalColumns: Int = 1
)

/**
 * Assign columns to overlapping events using a greedy interval-graph approach.
 * Events whose vertical extent overlaps are placed in adjacent columns.
 */
private fun assignColumns(items: List<TimelineItem>, hourHeightDp: Float): List<LayoutItem> {
    // Each event spans EVENT_HEIGHT_DP worth of dp → convert to minutes
    val eventMinuteSpan = (EVENT_HEIGHT_DP / hourHeightDp * 60f).toInt().coerceAtLeast(1)

    val sorted = items
        .map { item ->
            val minute = item.time.hour * 60 + item.time.minute
            LayoutItem(item, minute, minute + eventMinuteSpan)
        }
        .sortedBy { it.minuteOfDay }

    if (sorted.isEmpty()) return sorted

    // Build overlap groups (connected components)
    val groups = mutableListOf<MutableList<LayoutItem>>()
    var currentGroup = mutableListOf(sorted[0])
    var groupEnd = sorted[0].endMinute

    for (i in 1 until sorted.size) {
        val li = sorted[i]
        if (li.minuteOfDay < groupEnd) {
            // overlaps with current group
            currentGroup.add(li)
            groupEnd = maxOf(groupEnd, li.endMinute)
        } else {
            groups.add(currentGroup)
            currentGroup = mutableListOf(li)
            groupEnd = li.endMinute
        }
    }
    groups.add(currentGroup)

    // Assign columns within each group
    for (group in groups) {
        val columnEnds = mutableListOf<Int>() // tracks end-minute of each column
        for (li in group) {
            // find first column where this event fits (no overlap)
            val col = columnEnds.indexOfFirst { it <= li.minuteOfDay }
            if (col >= 0) {
                li.column = col
                columnEnds[col] = li.endMinute
            } else {
                li.column = columnEnds.size
                columnEnds.add(li.endMinute)
            }
        }
        val totalCols = columnEnds.size
        group.forEach { it.totalColumns = totalCols }
    }

    return sorted
}

@Composable
private fun TimelineEvent(
    item: TimelineItem,
    date: LocalDate,
    now: LocalDateTime,
    yOffset: Dp,
    column: Int,
    totalColumns: Int,
    onEntryClick: (Long) -> Unit,
    onScheduleMarkerClick: ((TimelineItem) -> Unit)? = null
) {
    val bgColor: Color
    val label: String

    when {
        item.entry != null && item.entry.scheduledTime != null -> {
            // Void entry associated with a scheduled time — show delta, no clock
            val entryTime = item.entry.timestamp.atZone(ZoneId.systemDefault()).toLocalTime()
            val delta = item.entry.scheduledTime?.let {
                Duration.between(it, entryTime).toMinutes().toInt()
            }
            val deltaText = delta?.let { m ->
                when {
                    m > 0 -> "+${m}m"
                    m < 0 -> "${m}m"
                    else -> "on time"
                }
            } ?: ""
            bgColor = colorChipColor(item.entry.color)
            label = "${entryTime.format(TIME_FORMATTER)} ${colorEmoji(item.entry.color)} $deltaText"
        }
        item.entry != null && item.entry.isLeakOnly -> {
            bgColor = if (androidx.compose.foundation.isSystemInDarkTheme()) Color(0xFF4A2020) else Color(0xFFFFCDD2) // pink
            label = "${item.time.format(TIME_FORMATTER)} \uD83C\uDF88 Leak"
        }
        item.entry != null && !item.entry.didVoid -> {
            bgColor = if (androidx.compose.foundation.isSystemInDarkTheme()) Color(0xFF3A2040) else Color(0xFFE1BEE7) // purple
            label = "${item.time.format(TIME_FORMATTER)} \uD83D\uDE23 Urge"
        }
        item.entry != null -> {
            bgColor = colorChipColor(item.entry.color)
            label = "${item.time.format(TIME_FORMATTER)} ${colorEmoji(item.entry.color)} Unscheduled"
        }
        item.isScheduled -> {
            // Schedule marker only — no entry logged for this time
            val upcoming = LocalDateTime.of(date, item.time).isAfter(now)
            when {
                item.isClaimed -> {
                    bgColor = if (androidx.compose.foundation.isSystemInDarkTheme()) Color(0xFF2A2A2A) else Color(0xFFEEEEEE)
                    label = "${item.time.format(TIME_FORMATTER)} \u23F0 \u2705"
                }
                item.isManuallyMissed -> {
                    bgColor = if (androidx.compose.foundation.isSystemInDarkTheme()) Color(0xFF3A2020) else Color(0xFFFFCDD2) // red
                    label = "${item.time.format(TIME_FORMATTER)} \u23F0 \u274C Missed"
                }
                upcoming -> {
                    bgColor = if (androidx.compose.foundation.isSystemInDarkTheme()) Color(0xFF2A2A2A) else Color(0xFFEEEEEE)
                    label = "${item.time.format(TIME_FORMATTER)} \u23F0 Upcoming"
                }
                else -> {
                    bgColor = if (androidx.compose.foundation.isSystemInDarkTheme()) Color(0xFF3A3020) else Color(0xFFFFF3E0) // amber/orange
                    label = "${item.time.format(TIME_FORMATTER)} \u23F0 Overdue"
                }
            }
        }
        else -> {
            bgColor = MaterialTheme.colorScheme.surfaceVariant
            label = item.time.format(TIME_FORMATTER)
        }
    }

    val clickMod = when {
        item.entry != null && item.entry.id > 0 -> Modifier.clickable { onEntryClick(item.entry.id) }
        item.isScheduled && item.entry == null && onScheduleMarkerClick != null -> Modifier.clickable { onScheduleMarkerClick(item) }
        else -> Modifier
    }

    // Override to pale red for burst urgency
    val finalBgColor = if (item.entry != null && item.entry.urgency == com.laniao.domain.model.Urgency.BURST) {
        if (androidx.compose.foundation.isSystemInDarkTheme()) Color(0xFF4A2020) else Color(0xFFFFCDD2)
    } else {
        bgColor
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .offset { IntOffset(0, yOffset.roundToPx()) }
            .padding(start = HOUR_LABEL_WIDTH_DP.dp + 4.dp, end = 8.dp)
    ) {
        val availableWidth = maxWidth
        val columnWidth = availableWidth / totalColumns
        val columnOffset = columnWidth * column

        Surface(
            modifier = Modifier
                .width(columnWidth - 2.dp)  // 2dp gap between columns
                .offset(x = columnOffset)
                .height(EVENT_HEIGHT_DP.dp)
                .then(clickMod),
            shape = RoundedCornerShape(6.dp),
            color = finalBgColor,
            tonalElevation = 2.dp
        ) {
            Box(
                modifier = Modifier.padding(horizontal = 8.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f)
                )
            }
        }
    }
}

@Composable
private fun colorChipColor(color: PeeColor): Color {
    val isDark = androidx.compose.foundation.isSystemInDarkTheme()
    return when (color) {
        PeeColor.CLEAR -> if (isDark) Color(0xFF1A2A3A) else Color(0xFFE3F2FD)
        PeeColor.LIGHT_YELLOW -> if (isDark) Color(0xFF2E2B1B) else Color(0xFFFFF9C4)
        PeeColor.YELLOW -> if (isDark) Color(0xFF3A3520) else Color(0xFFFFF9C4)
        PeeColor.DARK_YELLOW -> if (isDark) Color(0xFF3A2A18) else Color(0xFFFFE0B2)
        PeeColor.AMBER -> if (isDark) Color(0xFF3A2018) else Color(0xFFFFCCBC)
        PeeColor.UNKNOWN -> if (isDark) Color(0xFF2A2A2A) else Color(0xFFE0E0E0)
    }
}

@Composable
private fun DrinkTimelineEvent(
    item: TimelineItem,
    yOffset: Dp,
    column: Int,
    totalColumns: Int,
    onClick: () -> Unit
) {
    val drink = item.drinkEntry ?: return
    val emoji = drinkTypeEmoji(drink.type)
    val name = if (drink.type == DrinkType.CUSTOM && !drink.customName.isNullOrBlank())
        drink.customName else drink.type.displayName
    val label = "${item.time.format(TIME_FORMATTER)} $emoji $name"

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .offset { IntOffset(0, yOffset.roundToPx()) }
            .padding(start = HOUR_LABEL_WIDTH_DP.dp + 4.dp, end = 8.dp)
    ) {
        val availableWidth = maxWidth
        val columnWidth = availableWidth / totalColumns
        val columnOffset = columnWidth * column

        Surface(
            modifier = Modifier
                .width(columnWidth - 2.dp)
                .offset(x = columnOffset)
                .height(DRINK_HEIGHT_DP.dp)
                .clickable(onClick = onClick),
            shape = RoundedCornerShape(4.dp),
            color = com.laniao.presentation.theme.LaNiaoColors.DrinksCardBackground,
            tonalElevation = 1.dp
        ) {
            Box(
                modifier = Modifier.padding(horizontal = 6.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}

private fun drinkTypeEmoji(type: DrinkType): String = when (type) {
    DrinkType.WATER -> "\uD83D\uDCA7"
    DrinkType.SPARKLING_WATER -> "\u2728"
    DrinkType.TEA -> "\uD83C\uDF75"
    DrinkType.COFFEE -> "\u2615"
    DrinkType.MILK -> "\uD83E\uDD5B"
    DrinkType.JUICE -> "\uD83E\uDDC3"
    DrinkType.CUSTOM -> "\uD83E\uDD64"
}

@Composable
private fun ExerciseTimelineEvent(
    item: TimelineItem,
    yOffset: Dp,
    column: Int,
    totalColumns: Int,
    onClick: () -> Unit = {}
) {
    val exercise = item.exerciseCompletion ?: return
    val emoji = exercise.exerciseType.emoji
    val name = exercise.exerciseType.displayName
    val label = "${item.time.format(TIME_FORMATTER)} $emoji $name"

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .offset { IntOffset(0, yOffset.roundToPx()) }
            .padding(start = HOUR_LABEL_WIDTH_DP.dp + 4.dp, end = 8.dp)
    ) {
        val availableWidth = maxWidth
        val columnWidth = availableWidth / totalColumns
        val columnOffset = columnWidth * column

        Surface(
            modifier = Modifier
                .width(columnWidth - 2.dp)
                .offset(x = columnOffset)
                .height(DRINK_HEIGHT_DP.dp)
                .clickable(onClick = onClick),
            shape = RoundedCornerShape(4.dp),
            color = com.laniao.presentation.theme.LaNiaoColors.ExerciseCardBackground,
            tonalElevation = 1.dp
        ) {
            Box(
                modifier = Modifier.padding(horizontal = 6.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}

private fun colorEmoji(color: PeeColor): String = when (color) {
    PeeColor.CLEAR -> "\uD83E\uDEE7"
    PeeColor.LIGHT_YELLOW -> "\u26AA"
    PeeColor.YELLOW -> "\uD83D\uDFE1"
    PeeColor.DARK_YELLOW -> "\uD83D\uDFE0"
    PeeColor.AMBER -> "\uD83D\uDFE4"
    PeeColor.UNKNOWN -> ""
}

private val TIME_FORMATTER: DateTimeFormatter = com.laniao.util.DateFormatters.TIME_SHORT
private val HOUR_FORMATTER: DateTimeFormatter = com.laniao.util.DateFormatters.TIME_SHORT
