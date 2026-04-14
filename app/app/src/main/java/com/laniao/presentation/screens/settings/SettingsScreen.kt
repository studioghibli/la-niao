package com.laniao.presentation.screens.settings

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import com.laniao.presentation.viewmodel.SettingsEvent
import com.laniao.presentation.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    var showClearDataDialog by remember { mutableStateOf(false) }
    var showRestorePickerDialog by remember { mutableStateOf(false) }
    var showRestoreWarning by remember { mutableStateOf(false) }
    var pendingRestoreFileName by remember { mutableStateOf<String?>(null) }

    // Handle CSV export events
    LaunchedEffect(Unit) {
        viewModel.exportEvent.collect { csvContent ->
            try {
                val dateStr = java.time.LocalDate.now().format(com.laniao.util.DateFormatters.DATE_FILE)
                val file = java.io.File(context.cacheDir, "laniao_export_$dateStr.csv")
                file.writeText(csvContent)
                val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/csv"
                    putExtra(Intent.EXTRA_STREAM, uri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                context.startActivity(Intent.createChooser(intent, "Share CSV"))
            } catch (e: Exception) {
                snackbarHostState.showSnackbar("Export failed: ${e.message}")
            }
        }
    }

    // Handle settings events
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is SettingsEvent.BackupReady -> {
                    snackbarHostState.showSnackbar("Backup saved: ${event.fileName}")
                }
                is SettingsEvent.RestoreComplete -> {
                    val r = event.result
                    snackbarHostState.showSnackbar(
                        "Restored ${r.peeEntryCount} entries, ${r.drinkEntryCount} drinks, ${r.scheduleCount} schedules, ${r.exerciseCompletionCount} exercises"
                    )
                }
                is SettingsEvent.DataCleared -> {
                    snackbarHostState.showSnackbar("All data cleared")
                }
                is SettingsEvent.UndoComplete -> {
                    snackbarHostState.showSnackbar(event.message)
                }
                is SettingsEvent.RedoComplete -> {
                    snackbarHostState.showSnackbar(event.message)
                }
                is SettingsEvent.Error -> {
                    snackbarHostState.showSnackbar(event.message)
                }
                is SettingsEvent.CsvReady -> { /* handled by exportEvent */ }
            }
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Settings", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold) },
                windowInsets = androidx.compose.foundation.layout.WindowInsets(0)
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Undo & Redo Section
            Text(
                text = "Undo & Redo",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { viewModel.undo() },
                        enabled = uiState.canUndo,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (uiState.lastUndoDescription != null)
                                "Undo: ${uiState.lastUndoDescription}"
                            else
                                "Undo"
                        )
                    }

                    Button(
                        onClick = { viewModel.redo() },
                        enabled = uiState.canRedo,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Text(
                            text = if (uiState.lastRedoDescription != null)
                                "Redo: ${uiState.lastRedoDescription}"
                            else
                                "Redo"
                        )
                    }

                    Text(
                        text = "Undo/redo history is cleared when the app is restarted",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Hydration Goal Section
            Text(
                text = "Hydration Goal",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Daily goal",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "${"%,.1f".format(uiState.hydrationGoalLiters)} L",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Slider(
                        value = uiState.hydrationGoalLiters.toFloat(),
                        onValueChange = { viewModel.updateHydrationGoal(it.toDouble()) },
                        valueRange = 1.5f..3.5f,
                        steps = 19
                    )
                    Text(
                        text = "\uD83C\uDF0A shown in calendar when daily drinks reach this goal",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Data Section
            Text(
                text = "Data",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Export CSV
                    Text(
                        text = "Export all entries, drinks, and exercises as CSV",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    OutlinedButton(
                        onClick = { viewModel.exportCsv() },
                        enabled = !uiState.isExporting,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (uiState.isExporting) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Export CSV")
                        }
                    }

                    // Backup
                    Text(
                        text = "Backup all data to JSON",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    OutlinedButton(
                        onClick = { viewModel.backup() },
                        enabled = !uiState.isBackingUp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (uiState.isBackingUp) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Backup Data")
                        }
                    }

                    // Restore
                    Text(
                        text = "Restore data from a previous backup",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    OutlinedButton(
                        onClick = {
                            viewModel.refreshBackupList()
                            showRestorePickerDialog = true
                        },
                        enabled = !uiState.isRestoring,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (uiState.isRestoring) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Restore from Backup")
                        }
                    }
                }
            }

            // Danger Zone
            Text(
                text = "Danger Zone",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Permanently delete all entries, schedules, and settings",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    OutlinedButton(
                        onClick = { showClearDataDialog = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Clear All Data", color = MaterialTheme.colorScheme.error)
                    }
                }
            }

            // About Section
            Text(
                text = "About",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "LaNiao",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Version 1.0.0",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    // Clear data confirmation dialog
    if (showClearDataDialog) {
        AlertDialog(
            onDismissRequest = { showClearDataDialog = false },
            title = { Text("Clear All Data") },
            text = { Text("Are you sure? This will permanently delete all your entries, schedules, exercises, and settings. This cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    showClearDataDialog = false
                    viewModel.clearAllData()
                }) {
                    Text("Yes", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDataDialog = false }) {
                    Text("No")
                }
            }
        )
    }

    // Restore: pick a backup file dialog
    if (showRestorePickerDialog) {
        val backups = uiState.availableBackups
        AlertDialog(
            onDismissRequest = { showRestorePickerDialog = false },
            title = { Text("Select Backup") },
            text = {
                if (backups.isEmpty()) {
                    Text("No backups found. Tap \"Backup Data\" to create one first.")
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        backups.forEach { backup ->
                            val dateLabel = remember(backup.lastModified) {
                                java.text.SimpleDateFormat("MMM d, yyyy HH:mm", java.util.Locale.getDefault())
                                    .format(java.util.Date(backup.lastModified))
                            }
                            val sizeLabel = remember(backup.sizeBytes) {
                                "%.1f KB".format(backup.sizeBytes / 1024.0)
                            }
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        showRestorePickerDialog = false
                                        pendingRestoreFileName = backup.fileName
                                        showRestoreWarning = true
                                    },
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surface
                                )
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = backup.fileName,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = "$dateLabel · $sizeLabel",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showRestorePickerDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Restore warning confirmation dialog
    if (showRestoreWarning) {
        AlertDialog(
            onDismissRequest = {
                showRestoreWarning = false
                pendingRestoreFileName = null
            },
            title = { Text("Restore Data") },
            text = { Text("This will replace all current data with the backup. Any existing data will be lost. Continue?") },
            confirmButton = {
                TextButton(onClick = {
                    showRestoreWarning = false
                    pendingRestoreFileName?.let { viewModel.restoreFromFile(it) }
                    pendingRestoreFileName = null
                }) {
                    Text("Restore")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showRestoreWarning = false
                    pendingRestoreFileName = null
                }) {
                    Text("Cancel")
                }
            }
        )
    }
}
