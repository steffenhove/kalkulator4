package no.steffenhove.betongkalkulator.ui.screens

// ... (importer beholdes) ...

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    context: Context,
    onNavigateToSettings: () -> Unit,
    onNavigateToCalculation: () -> Unit
) {
    // ... (kode beholdes) ...

    LazyColumn(modifier = Modifier.padding(paddingValues)) {
        items(calculations.take(50)) { calculation ->  // Begrens til 50 elementer
            HistoryItem(calculation) { id ->
                // ... (kode beholdes) ...
            }
        }
    }

    // ... (kode beholdes) ...
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
