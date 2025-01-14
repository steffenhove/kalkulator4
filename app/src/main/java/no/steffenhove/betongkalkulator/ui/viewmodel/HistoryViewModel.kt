package no.steffenhove.betongkalkulator.ui.viewmodel

// ... (importer beholdes) ...

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
