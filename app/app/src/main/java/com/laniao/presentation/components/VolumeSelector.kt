package com.laniao.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.laniao.domain.model.VolumeSize

/**
 * Volume size selector with Small, Medium, Large chips.
 */
@Composable
fun VolumeSelector(
    selectedSize: VolumeSize,
    onSizeSelected: (VolumeSize) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Column(modifier = modifier) {
        Text(
            text = "Volume",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Skip UNKNOWN - user selects from Small, Medium, Large
            listOf(VolumeSize.SMALL, VolumeSize.MEDIUM, VolumeSize.LARGE).forEach { size ->
                val isSelected = selectedSize == size
                FilterChip(
                    selected = isSelected,
                    onClick = { onSizeSelected(size) },
                    label = { Text(size.name.lowercase().replaceFirstChar { it.uppercase() }) },
                    enabled = enabled,
                    modifier = Modifier.semantics {
                        contentDescription = "${size.name} volume, ${if (isSelected) "selected" else "not selected"}"
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            }
        }
    }
}
