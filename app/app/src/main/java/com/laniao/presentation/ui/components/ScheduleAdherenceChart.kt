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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.laniao.domain.usecase.DayScheduleAdherence
import java.time.format.DateTimeFormatter

@Composable
fun ScheduleAdherenceChart(
    data: List<DayScheduleAdherence>,
    modifier: Modifier = Modifier
) {
    val animationProgress = remember { Animatable(0f) }
    LaunchedEffect(data) {
        animationProgress.snapTo(0f)
        animationProgress.animateTo(1f, animationSpec = tween(durationMillis = 600))
    }

    val labelColor = MaterialTheme.colorScheme.onSurfaceVariant
    val gridColor = MaterialTheme.colorScheme.outlineVariant
    val greenColor = com.laniao.presentation.theme.LaNiaoColors.ChartGreen
    val amberColor = com.laniao.presentation.theme.LaNiaoColors.ChartGold
    val redColor = com.laniao.presentation.theme.LaNiaoColors.ChartRed

    if (data.isEmpty()) return

    val dateFormatter = remember {
        com.laniao.util.DateFormatters.DATE_CHART
    }
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

        // Y-axis: 0% to 100% in 25% steps
        for (i in 0..4) {
            val pct = i * 25
            val y = chartHeight - (chartHeight * pct / 100f)
            drawLine(
                color = gridColor,
                start = Offset(0f, y),
                end = Offset(chartWidth, y),
                strokeWidth = 1f
            )
            drawContext.canvas.nativeCanvas.drawText(
                "$pct",
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
            val pct = day.adherenceRate * 100f
            val barHeight = (pct / 100f) * chartHeight * animationProgress.value

            val barColor = when {
                pct >= 80f -> greenColor
                pct >= 50f -> amberColor
                else -> redColor
            }

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
    }
}
