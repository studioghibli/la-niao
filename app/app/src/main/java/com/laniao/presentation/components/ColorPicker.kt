package com.laniao.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.laniao.domain.model.PeeColor

/**
 * Color picker with 7 circle chips for urine color selection.
 * UNKNOWN is shown as gray.
 */
@Composable
fun ColorPicker(
    selectedColor: PeeColor,
    onColorSelected: (PeeColor) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Column(modifier = modifier) {
        Text(
            text = "Color",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PeeColor.entries.forEach { color ->
                val isSelected = selectedColor == color
                ColorChip(
                    color = color,
                    isSelected = isSelected,
                    onClick = { if (enabled) onColorSelected(color) },
                    enabled = enabled
                )
            }
        }
    }
}

@Composable
private fun ColorChip(
    color: PeeColor,
    isSelected: Boolean,
    onClick: () -> Unit,
    enabled: Boolean
) {
    val displayColor = color.toDisplayColor()
    val borderColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.outline
    }

    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(displayColor)
            .border(
                width = if (isSelected) 3.dp else 1.dp,
                color = borderColor,
                shape = CircleShape
            )
            .clickable(enabled = enabled, onClick = onClick)
            .semantics {
                role = Role.RadioButton
                contentDescription = "${color.name.replace("_", " ")}, ${if (isSelected) "selected" else "not selected"}"
            },
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            // Show checkmark for selected color
            val checkColor = if (color == PeeColor.LIGHT_YELLOW) {
                Color.Black.copy(alpha = 0.6f)
            } else {
                Color.White
            }
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = checkColor,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
