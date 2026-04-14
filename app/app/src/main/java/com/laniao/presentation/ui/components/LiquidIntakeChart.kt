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
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.laniao.domain.usecase.DayLiquidIntake
import java.time.format.DateTimeFormatter

@Composable
fun LiquidIntakeChart(
    data: List<DayLiquidIntake>,
    hydrationGoalLiters: Double = 2.7,
    modifier: Modifier = Modifier
) {
    val animationProgress = remember { Animatable(0f) }
    LaunchedEffect(data) {
        animationProgress.snapTo(0f)
        animationProgress.animateTo(1f, animationSpec = tween(durationMillis = 600))
    }

    val labelColor = MaterialTheme.colorScheme.onSurfaceVariant
    val gridColor = MaterialTheme.colorScheme.outlineVariant
    val barColorMet = com.laniao.presentation.theme.LaNiaoColors.ChartBlue
    val barColorBelow = com.laniao.presentation.theme.LaNiaoColors.ChartBlueDim
    val goalLineColor = com.laniao.presentation.theme.LaNiaoColors.SummaryAccent

    if (data.isEmpty()) return

    val maxLiters = (data.maxOfOrNull { it.totalLiters } ?: 0.5)
        .coerceAtLeast(hydrationGoalLiters) // ensure goal line is always visible
        .coerceAtLeast(0.5)
    val dateFormatter = remember {
        com.laniao.util.DateFormatters.DATE_CHART
    }
    val labelStep = when {
        data.size <= 10 -> 1
        data.size <= 14 -> 2
        data.size <= 21 -> 3
        else -> 5
    }

    // Nice Y-axis step: 0.5L increments
    val stepSize = when {
        maxLiters <= 2.0 -> 0.5
        maxLiters <= 5.0 -> 1.0
        else -> 2.0
    }
    val yMax = (Math.ceil(maxLiters / stepSize) * stepSize).coerceAtLeast(stepSize)
    val gridSteps = (yMax / stepSize).toInt()

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

        // Draw horizontal gridlines
        for (i in 0..gridSteps) {
            val labelValue = i * stepSize
            val y = chartHeight - (chartHeight * labelValue / yMax).toFloat()
            drawLine(
                color = gridColor,
                start = Offset(0f, y),
                end = Offset(chartWidth, y),
                strokeWidth = 1f
            )
            drawContext.canvas.nativeCanvas.drawText(
                "%.1f".format(labelValue),
                -4.dp.toPx(),
                y + 4.dp.toPx(),
                android.graphics.Paint().apply {
                    color = labelColor.hashCode()
                    textSize = 10.sp.toPx()
                    textAlign = android.graphics.Paint.Align.RIGHT
                }
            )
        }

        // Draw bars
        data.forEachIndexed { index, day ->
            val barHeight = if (yMax > 0) {
                (day.totalLiters / yMax).toFloat() * chartHeight * animationProgress.value
            } else 0f

            val barColor = if (day.totalLiters >= hydrationGoalLiters) barColorMet else barColorBelow
            val x = index * (barWidth + barGap) + barGap / 2
            drawRect(
                color = barColor,
                topLeft = Offset(x, chartHeight - barHeight),
                size = Size(barWidth, barHeight)
            )

            // X-axis label
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

        // Hydration goal line
        val goalY = chartHeight - (chartHeight * hydrationGoalLiters / yMax).toFloat()
        drawLine(
            color = goalLineColor,
            start = Offset(0f, goalY),
            end = Offset(chartWidth, goalY),
            strokeWidth = 2f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 6f))
        )
    }
}
