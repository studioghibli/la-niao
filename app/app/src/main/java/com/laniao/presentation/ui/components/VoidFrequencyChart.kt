package com.laniao.presentation.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.laniao.domain.model.PeeColor
import com.laniao.domain.usecase.DayVoidFrequency
import java.time.format.DateTimeFormatter

/**
 * Bar chart showing void frequency per day.
 * Bars are colored by the dominant pee color for that day.
 */
@Composable
fun VoidFrequencyChart(
    data: List<DayVoidFrequency>,
    modifier: Modifier = Modifier
) {
    val animationProgress = remember { Animatable(0f) }
    LaunchedEffect(data) {
        animationProgress.snapTo(0f)
        animationProgress.animateTo(1f, animationSpec = tween(durationMillis = 600))
    }

    val density = LocalDensity.current
    val labelColor = MaterialTheme.colorScheme.onSurfaceVariant
    val gridColor = MaterialTheme.colorScheme.outlineVariant

    if (data.isEmpty()) return

    val maxCount = (data.maxOfOrNull { it.voidCount } ?: 1).coerceAtLeast(1)
    val dateFormatter = remember {
        com.laniao.util.DateFormatters.DATE_CHART
    }
    // Show every Nth label to avoid overlap
    val labelStep = when {
        data.size <= 10 -> 1
        data.size <= 14 -> 2
        data.size <= 21 -> 3
        else -> 5
    }

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(start = 24.dp, end = 24.dp, top = 8.dp, bottom = 24.dp)
    ) {
        val chartWidth = size.width
        val chartHeight = size.height
        val barWidth = (chartWidth / data.size) * 0.7f
        val barGap = (chartWidth / data.size) * 0.3f

        // Draw horizontal gridlines with consistent integer steps
        // Pick a nice step so labels are always evenly spaced whole numbers
        val stepSize = when {
            maxCount <= 5 -> 1
            maxCount <= 10 -> 2
            maxCount <= 25 -> 5
            else -> 10
        }
        // Round maxCount up to next multiple of stepSize for clean top label
        val yMax = ((maxCount + stepSize - 1) / stepSize) * stepSize
        val gridSteps = yMax / stepSize
        for (i in 0..gridSteps) {
            val labelValue = i * stepSize
            val y = chartHeight - (chartHeight * labelValue / yMax)
            drawLine(
                color = gridColor,
                start = Offset(0f, y),
                end = Offset(chartWidth, y),
                strokeWidth = 1f
            )
            // Y-axis label
            drawContext.canvas.nativeCanvas.drawText(
                "$labelValue",
                -4.dp.toPx(),
                y + 4.dp.toPx(),
                android.graphics.Paint().apply {
                    color = labelColor.hashCode()
                    textSize = 10.sp.toPx()
                    textAlign = android.graphics.Paint.Align.RIGHT
                }
            )
        }

        // Draw stacked bars by pee color
        // Order: CLEAR (bottom) -> LIGHT_YELLOW -> YELLOW -> DARK_YELLOW -> AMBER -> UNKNOWN (top)
        val colorOrder = listOf(
            PeeColor.CLEAR, PeeColor.LIGHT_YELLOW, PeeColor.YELLOW,
            PeeColor.DARK_YELLOW, PeeColor.AMBER, PeeColor.UNKNOWN
        )

        data.forEachIndexed { index, day ->
            val totalBarHeight = if (yMax > 0) {
                (day.voidCount.toFloat() / yMax) * chartHeight * animationProgress.value
            } else 0f

            val x = index * (barWidth + barGap) + barGap / 2
            var currentY = chartHeight // start from bottom

            for (color in colorOrder) {
                val count = day.colorCounts[color] ?: 0
                if (count == 0) continue

                val segmentHeight = if (day.voidCount > 0) {
                    (count.toFloat() / day.voidCount) * totalBarHeight
                } else 0f

                currentY -= segmentHeight
                drawRect(
                    color = peeColorToChartColor(color),
                    topLeft = Offset(x, currentY),
                    size = Size(barWidth, segmentHeight)
                )
            }

            // X-axis label (skip some labels to avoid overlap)
            if (index % labelStep == 0 || index == data.size - 1) {
                val label = day.date.format(dateFormatter)
                drawContext.canvas.nativeCanvas.drawText(
                    label,
                    x + barWidth / 2,
                    chartHeight + 16.dp.toPx(),
                    android.graphics.Paint().apply {
                        color = labelColor.hashCode()
                        textSize = 9.sp.toPx()
                        textAlign = android.graphics.Paint.Align.CENTER
                    }
                )
            }
        }
    }
}

private fun peeColorToChartColor(color: PeeColor): Color = when (color) {
    PeeColor.CLEAR -> Color(0xFF90CAF9)       // light blue
    PeeColor.LIGHT_YELLOW -> Color(0xFFFFF9C4) // pale yellow
    PeeColor.YELLOW -> Color(0xFFFFF176)       // yellow
    PeeColor.DARK_YELLOW -> Color(0xFFFFCC80)  // orange-yellow
    PeeColor.AMBER -> Color(0xFFFFAB91)        // amber-orange
    PeeColor.UNKNOWN -> Color(0xFFBDBDBD)      // gray
}
