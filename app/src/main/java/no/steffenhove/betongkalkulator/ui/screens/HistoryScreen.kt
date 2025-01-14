package no.steffenhove.betongkalkulator.ui.screens

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import no.steffenhove.betongkalkulator.ui.model.AppDatabase
import no.steffenhove.betongkalkulator.ui.model.CalculationEntity
import no.steffenhove.betongkalkulator.ui.repository.CalculationRepository
import no.steffenhove.betongkalkulator.ui.viewmodel.HistoryViewModel
import no.steffenhove.betongkalkulator.ui.viewmodel.HistoryViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    context: Context,
    onNavigateToSettings: () -> Unit,
    onNavigateToCalculation: () -> Unit
) {
    val calculationDao = AppDatabase.getDatabase(context).calculationDao()
    val repository = CalculationRepository(calculationDao)
    val factory = HistoryViewModelFactory(repository)
    val historyViewModel: HistoryViewModel = viewModel(factory = factory)
    val calculations by historyViewModel.allCalculations.collectAsState(initial = emptyList())
    var showDialog by remember { mutableStateOf(false) }
    var selectedIds by remember { mutableStateOf<List<Int>>(emptyList()) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Historikk") },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Filled.Settings, contentDescription = "Innstillinger")
                    }
                })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToCalculation) {
                Icon(Icons.Filled.Add, contentDescription = "Ny beregning")
            }
        }
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            items(calculations.take(50)) { calculation ->
                HistoryItem(calculation) { id ->
                    // Toggle selection
                    selectedIds = if (id in selectedIds) {
                        selectedIds - id
                    } else {
                        selectedIds + id
                    }
                }
            }
        }
    }

    // Bekreftelsesdialog for sletting
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Slett valgte") },
            text = { Text("Er du sikker på at du vil slette de valgte beregningene?") },
            confirmButton = {
                Button(onClick = {
                    historyViewModel.deleteCalculations(selectedIds)
                    selectedIds = emptyList()
                    showDialog = false
                }) {
                    Text("Slett")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Avbryt")
                }
            }
        )
    }

    // Knapp for å slette valgte elementer
    if (selectedIds.isNotEmpty()) {
        Button(onClick = { showDialog = true }, modifier = Modifier.padding(16.dp)) {
            Text("Slett valgte")
        }
    }
}

@Composable
fun HistoryItem(calculation: CalculationEntity, onItemClick: (Int) -> Unit) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .clickable { onItemClick(calculation.id) }
        .padding(16.dp)) {
        Text(
            "${calculation.form} - ${calculation.dimensions} - ${calculation.thickness} - ${calculation.concreteType}",
            style = MaterialTheme.typography.bodyLarge
        )
        Text("Resultat: ${calculation.result} ${calculation.unit}", style = MaterialTheme.typography.bodyMedium)
        Text(
            text = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(Date(calculation.timestamp)),
            style = MaterialTheme.typography.bodySmall
        )
    }
}
