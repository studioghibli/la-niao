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
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.laniao.domain.usecase.DailyVoidGap

/**
 * Bar chart showing average void gap (in hours) per day.
 */
@Composable
fun VoidGapChart(
    data: List<DailyVoidGap>,
    modifier: Modifier = Modifier
) {
    val animationProgress = remember { Animatable(0f) }
    LaunchedEffect(data) {
        animationProgress.snapTo(0f)
        animationProgress.animateTo(1f, animationSpec = tween(durationMillis = 600))
    }

    val labelColor = MaterialTheme.colorScheme.onSurfaceVariant
    val gridColor = MaterialTheme.colorScheme.outlineVariant
    val barColorMet = com.laniao.presentation.theme.LaNiaoColors.ChartPurple
    val barColorBelow = com.laniao.presentation.theme.LaNiaoColors.ChartPurpleDim
    val goalLineColor = MaterialTheme.colorScheme.primary

    if (data.isEmpty()) return

    val maxMinutes = run {
        val dataMax = data.maxOfOrNull { it.averageGapMinutes } ?: 60.0
        val goalMax = data.mapNotNull { it.scheduleGoalMinutes }.maxOrNull()?.toDouble() ?: 0.0
        maxOf(dataMax, goalMax).coerceAtLeast(60.0)
    }
    val dateFormatter = remember { com.laniao.util.DateFormatters.DATE_CHART }
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
            .padding(start = 28.dp, end = 24.dp, top = 8.dp, bottom = 24.dp)
    ) {
        val chartWidth = size.width
        val chartHeight = size.height
        val barWidth = (chartWidth / data.size) * 0.7f

        // Y-axis in hours
        val maxHours = (maxMinutes / 60.0).let { h ->
            when {
                h <= 2 -> 2.0
                h <= 3 -> 3.0
                h <= 4 -> 4.0
                h <= 6 -> 6.0
                h <= 8 -> 8.0
                else -> (Math.ceil(h / 2) * 2)
            }
        }
        val yMaxMinutes = maxHours * 60

        // Gridlines every hour
        val hourStep = when {
            maxHours <= 4 -> 1
            maxHours <= 8 -> 2
            else -> 3
        }
        val gridSteps = (maxHours / hourStep).toInt()
        for (i in 0..gridSteps) {
            val hourValue = i * hourStep
            val y = chartHeight - (chartHeight * hourValue * 60 / yMaxMinutes).toFloat()
            drawLine(
                color = gridColor,
                start = Offset(0f, y),
                end = Offset(chartWidth, y),
                strokeWidth = 1f
            )
            drawContext.canvas.nativeCanvas.drawText(
                "${hourValue}h",
                -4.dp.toPx(),
                y + 4.dp.toPx(),
                android.graphics.Paint().apply {
                    color = labelColor.hashCode()
                    textSize = 10.sp.toPx()
                    textAlign = android.graphics.Paint.Align.RIGHT
                }
            )
        }

        // Per-day goal gap dotted line segments
        val colWidth = chartWidth / data.size
        data.forEachIndexed { index, day ->
            val goal = day.scheduleGoalMinutes
            if (goal != null && goal > 0) {
                val goalY = chartHeight - (chartHeight * goal / yMaxMinutes).toFloat()
                val segStartX = colWidth * index
                val segEndX = colWidth * (index + 1)
                drawLine(
                    color = goalLineColor,
                    start = Offset(segStartX, goalY),
                    end = Offset(segEndX, goalY),
                    strokeWidth = 2f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 6f))
                )
            }
        }

        // Draw bars
        data.forEachIndexed { index, day ->
            val barHeight = (chartHeight * day.averageGapMinutes / yMaxMinutes).toFloat() * animationProgress.value
            val x = (chartWidth / data.size) * index + (chartWidth / data.size - barWidth) / 2
            val goal = day.scheduleGoalMinutes
            val barColor = if (goal != null && goal > 0 && day.averageGapMinutes >= goal) barColorMet else barColorBelow

            drawRect(
                color = barColor,
                topLeft = Offset(x, chartHeight - barHeight),
                size = Size(barWidth, barHeight)
            )

            // X-axis date label
            if (index % labelStep == 0 || index == data.size - 1) {
                val label = day.date.format(dateFormatter)
                drawContext.canvas.nativeCanvas.drawText(
                    label,
                    x + barWidth / 2,
                    chartHeight + 14.dp.toPx(),
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
