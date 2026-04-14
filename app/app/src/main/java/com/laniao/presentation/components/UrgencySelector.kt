package com.laniao.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.laniao.domain.model.Urgency

/**
 * Urgency level selector with segmented buttons.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun UrgencySelector(
    selectedUrgency: Urgency,
    onUrgencySelected: (Urgency) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Column(modifier = modifier) {
        Text(
            text = "Urgency",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Show all urgency levels except UNKNOWN
            listOf(
                Urgency.NONE,
                Urgency.LOW,
                Urgency.MEDIUM,
                Urgency.HIGH,
                Urgency.BURST
            ).forEach { urgency ->
                val isSelected = selectedUrgency == urgency
                FilterChip(
                    selected = isSelected,
                    onClick = { onUrgencySelected(urgency) },
                    label = { 
                        Text(
                            text = urgency.displayName,
                            style = MaterialTheme.typography.labelMedium
                        ) 
                    },
                    enabled = enabled,
                    modifier = Modifier.semantics {
                        contentDescription = "${urgency.displayName}, ${if (isSelected) "selected" else "not selected"}"
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = getUrgencyColor(urgency),
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            }
        }
    }
}

@Composable
private fun getUrgencyColor(urgency: Urgency) = when (urgency) {
    Urgency.NONE -> MaterialTheme.colorScheme.surfaceVariant
    Urgency.LOW -> MaterialTheme.colorScheme.tertiaryContainer
    Urgency.MEDIUM -> MaterialTheme.colorScheme.secondaryContainer
    Urgency.HIGH -> MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.7f)
    Urgency.BURST -> MaterialTheme.colorScheme.errorContainer
    Urgency.UNKNOWN -> MaterialTheme.colorScheme.surfaceVariant
}
