package no.steffenhove.betongkalkulator.ui.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import no.steffenhove.betongkalkulator.ui.model.CalculationEntity
import no.steffenhove.betongkalkulator.ui.repository.CalculationRepository

class HistoryViewModel(private val repository: CalculationRepository) : ViewModel() {
    val allCalculations: Flow<List<CalculationEntity>> = repository.allCalculations

    fun insert(calculation: CalculationEntity) = viewModelScope.launch {
        repository.insert(calculation)
    }

    fun deleteCalculations(ids: List<Int>) {
        viewModelScope.launch {
            repository.deleteCalculations(ids)
        }
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }
}
