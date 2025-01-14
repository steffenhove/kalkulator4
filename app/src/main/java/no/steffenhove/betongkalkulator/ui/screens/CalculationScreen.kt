package no.steffenhove.betongkalkulator.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import no.steffenhove.betongkalkulator.ui.model.*
import no.steffenhove.betongkalkulator.ui.utils.PreferencesHelper
import no.steffenhove.betongkalkulator.ui.viewmodel.CalculationViewModel
import no.steffenhove.betongkalkulator.ui.viewmodel.HistoryViewModel
import no.steffenhove.betongkalkulator.ui.viewmodel.HistoryViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculationScreen(
    context: Context,
    calculationViewModel: CalculationViewModel = viewModel(),
    historyViewModel: HistoryViewModel = viewModel(
        factory = HistoryViewModelFactory(
            CalculationRepository(AppDatabase.getDatabase(context).calculationDao())
        )
    )
) {
    // ... (resten av koden beholdes) ...
}

// ... (resten av funksjonene beholdes) ...
