package no.steffenhove.betongkalkulator.ui.repository

import no.steffenhove.betongkalkulator.ui.model.CalculationDao
import no.steffenhove.betongkalkulator.ui.model.CalculationEntity

class CalculationRepository(private val calculationDao: CalculationDao) {
    val allCalculations = calculationDao.getAllCalculations()

    suspend fun insert(calculation: CalculationEntity) {
        calculationDao.insert(calculation)
    }

    suspend fun delete(calculation: CalculationEntity) {
        calculationDao.deleteCalculation(calculation)
    }

    suspend fun deleteAll() {
        calculationDao.deleteAllCalculations()
    }

    suspend fun deleteCalculations(ids: List<Int>) {
        calculationDao.deleteCalculations(ids)
    }
}
