package com.laniao.presentation.ui.components

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.laniao.domain.model.ExerciseCategory
import com.laniao.domain.model.ExerciseStatus
import com.laniao.domain.model.ExerciseType

/**
 * Exercise checklist displayed on the Home screen.
 * Groups exercises by category (Kegel, Relaxation).
 */
@Composable
fun ExerciseChecklist(
    exercises: List<ExerciseStatus>,
    streak: Int,
    onComplete: (ExerciseType, Long?) -> Unit,
    onConfigureClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Don't show anything if no exercises configured
    if (exercises.isEmpty()) return

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = com.laniao.presentation.theme.LaNiaoColors.ExerciseCardBackground
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .width(4.dp)
                            .height(24.dp)
                            .background(com.laniao.presentation.theme.LaNiaoColors.ExerciseAccent, MaterialTheme.shapes.small)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "\uD83D\uDCAA Exercises",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = com.laniao.presentation.theme.LaNiaoColors.ExerciseAccent
                    )
                }
                if (streak > 0) {
                    StreakBadge(streak = streak)
                }
            }

            if (exercises.all { it.isComplete }) {
                Text(
                    text = "All done! \u2713",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = com.laniao.presentation.theme.LaNiaoColors.ExerciseAccent
                )
            } else {
                // Simple emoji row — tap to complete
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    exercises.forEach { status ->
                        Surface(
                            onClick = { if (!status.isComplete) onComplete(status.exerciseType, status.scheduleItemId) },
                            shape = CircleShape,
                            color = if (status.isComplete) com.laniao.presentation.theme.LaNiaoColors.ExerciseCardBackground else MaterialTheme.colorScheme.surface,
                            tonalElevation = 1.dp
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = if (status.isComplete) "\u2705" else status.exerciseType.emoji,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Text(
                                    text = "${status.completedToday}/${status.sessionsRequired}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (status.isComplete) MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.onSurfaceVariant
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
private fun ExerciseChecklistItem(
    status: ExerciseStatus,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bgColor by animateColorAsState(
        targetValue = if (status.isComplete)
            com.laniao.presentation.theme.LaNiaoColors.ExerciseCardBackground
        else
            MaterialTheme.colorScheme.surface,
        label = "exerciseBg"
    )

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        color = bgColor,
        tonalElevation = 1.dp,
        onClick = { if (!status.isComplete) onComplete() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (status.isComplete) "\u2705" else status.exerciseType.emoji,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = status.exerciseType.displayName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            }

            Text(
                text = "${status.completedToday} of ${status.sessionsRequired}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun StreakBadge(
    streak: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        color = com.laniao.presentation.theme.LaNiaoColors.ExerciseScheduleBackground,
        tonalElevation = 0.dp
    ) {
        Text(
            text = "\uD83D\uDD25 $streak day${if (streak != 1) "s" else ""}",
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = com.laniao.presentation.theme.LaNiaoColors.StreaksAccent
        )
    }
}
