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
    var length by remember { mutableStateOf("") }
    var width by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var result by remember { mutableStateOf(0.0) }
    var selectedForm by remember { mutableStateOf(Form.RECTANGLE) }
    var selectedUnit by remember { mutableStateOf(Unit.METRIC) }
    var selectedConcrete by remember { mutableStateOf(ConcreteType.CONCRETE) }
    val focusManager = LocalFocusManager.current

    // Hent densitet fra innstillinger
    val density = when (selectedConcrete) {
        ConcreteType.CONCRETE -> PreferencesHelper.getConcreteDensity(context)
        ConcreteType.LECA -> PreferencesHelper.getLecaDensity(context)
        ConcreteType.SIPOREX -> PreferencesHelper.getSiporexDensity(context)
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Form
        FormRadioGroup(selectedForm) { form -> selectedForm = form }

        // Dimensjoner
        Spacer(modifier = Modifier.height(16.dp))
        if (selectedForm == Form.RECTANGLE) {
            DimensionInputFields(
                length,
                width,
                height,
                onLengthChange = { length = it },
                onWidthChange = { width = it },
                onHeightChange = { height = it }
            )
        } else {
            DimensionInputFields(
                length,
                width,
                onLengthChange = { length = it },
                onWidthChange = { width = it }
            )
        }

        // Enhet
        Spacer(modifier = Modifier.height(16.dp))
        UnitRadioGroup(selectedUnit) { unit -> selectedUnit = unit }

        // Betongtype
        Spacer(modifier = Modifier.height(16.dp))
        ConcreteTypeRadioGroup(selectedConcrete) { concrete -> selectedConcrete = concrete }

        // Beregn-knapp
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                focusManager.clearFocus()
                result = calculationViewModel.calculate(
                    selectedForm,
                    selectedUnit,
                    selectedConcrete,
                    length,
                    width,
                    height,
                    density
                )

                // Lagre resultatet i databasen
                val dimensions = when (selectedForm) {
                    Form.RECTANGLE -> "$length x $width x $height"
                    Form.CIRCLE -> "$length x $width"
                }
                val calculationEntity = CalculationEntity(
                    form = selectedForm.displayName,
                    unit = selectedUnit.displayName,
                    concreteType = selectedConcrete.displayName,
                    dimensions = dimensions,
                    thickness = height, // Assuming height is thickness for now
                    density = density,
                    result = result
                )
                historyViewModel.insert(calculationEntity)
            }
        ) {
            Text("Beregn")
        }

        // Resultat
        Spacer(modifier = Modifier.height(16.dp))
        if (result > 0) {
            Text("Resultat: ${String.format("%.2f", result)} ${selectedConcrete.unit}")
        }
    }
}

@Composable
fun FormRadioGroup(selectedForm: Form, onFormChange: (Form) -> Unit) {
    Column {
        Text("Form")
        Row {
            Form.values().forEach { form ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = selectedForm == form,
                        onClick = { onFormChange(form) }
                    )
                    Text(form.displayName)
                }
            }
        }
    }
}

@Composable
fun DimensionInputFields(
    length: String,
    width: String,
    height: String = "",
    onLengthChange: (String) -> Unit,
    onWidthChange: (String) -> Unit,
    onHeightChange: (String) -> Unit = {}
) {
    Column {
        OutlinedTextField(
            value = length,
            onValueChange = onLengthChange,
            label = { Text("Lengde") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = width,
            onValueChange = onWidthChange,
            label = { Text("Bredde") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        if (height.isNotBlank()) {
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = height,
                onValueChange = onHeightChange,
                label = { Text("Høyde") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
    }
}

@Composable
fun UnitRadioGroup(selectedUnit: Unit, onUnitChange: (Unit) -> Unit) {
    Column {
        Text("Enhet")
        Row {
            Unit.values().forEach { unit ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = selectedUnit == unit,
                        onClick = { onUnitChange(unit) }
                    )
                    Text(unit.displayName)
                }
            }
        }
    }
}

@Composable
fun ConcreteTypeRadioGroup(
    selectedConcrete: ConcreteType,
    onConcreteChange: (ConcreteType) -> Unit
) {
    Column {
        Text("Betongtype")
        Row {
            ConcreteType.values().forEach { concrete ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = selectedConcrete == concrete,
                        onClick = { onConcreteChange(concrete) }
                    )
                    Text(concrete.displayName)
                }
            }
        }
    }
}
