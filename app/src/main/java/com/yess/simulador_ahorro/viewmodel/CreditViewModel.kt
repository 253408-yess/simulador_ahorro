package com.yess.simulador_ahorro.viewmodel

import androidx.lifecycle.ViewModel
import com.yess.simulador_ahorro.model.CreditUiState
import com.yess.simulador_ahorro.model.RiskLevel
import com.yess.simulador_ahorro.model.ScenarioItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.pow

class CreditViewModel : ViewModel() {

    // Flujo de estado inmutable (UDF)
    private val _uiState = MutableStateFlow(CreditUiState())
    val uiState: StateFlow<CreditUiState> = _uiState.asStateFlow()

    init {
        recalculate()
    }

    // Manejadores de eventos de la UI
    fun onAmountChanged(newAmount: String) {
        _uiState.update { it.copy(amountText = newAmount) }
        recalculate()
    }

    fun onSalaryChanged(newSalary: String) {
        _uiState.update { it.copy(salaryText = newSalary) }
        recalculate()
    }

    fun onMonthsChanged(newMonths: Float) {
        _uiState.update { it.copy(months = newMonths) }
        recalculate()
    }

    fun onInterestRateChanged(newRate: Float) {
        _uiState.update { it.copy(interestRate = newRate) }
        recalculate()
    }

    fun onInsuranceToggled(enabled: Boolean) {
        _uiState.update { it.copy(includeInsurance = enabled) }
        recalculate()
    }

    // Lógica matemática 100% en memoria
    private fun recalculate() {
        val state = _uiState.value
        val principal = state.amountText.toDoubleOrNull() ?: 0.0
        val salary = state.salaryText.toDoubleOrNull() ?: 0.0
        val n = state.months.toInt()

        val annualRate = if (state.includeInsurance) state.interestRate + 1.5f else state.interestRate
        val monthlyRate = (annualRate / 12f / 100f).toDouble()

        val monthlyPayment: Double
        val totalInterest: Double

        if (principal > 0 && monthlyRate > 0 && n > 0) {
            // Amortización Francesa
            monthlyPayment = (principal * monthlyRate * (1 + monthlyRate).pow(n)) /
                    ((1 + monthlyRate).pow(n) - 1)
            val totalPaid = monthlyPayment * n
            totalInterest = (totalPaid - principal).coerceAtLeast(0.0)
        } else {
            monthlyPayment = 0.0
            totalInterest = 0.0
        }

        val debtRatio = if (salary > 0) ((monthlyPayment / salary) * 100).toFloat() else 0f

        val riskLevel = when {
            debtRatio <= 30f -> RiskLevel.LOW
            debtRatio <= 40f -> RiskLevel.MODERATE
            else -> RiskLevel.HIGH
        }

        val totalAmountPaid = principal + totalInterest
        val capitalRatio = if (totalAmountPaid > 0) (principal / totalAmountPaid).toFloat() else 1f

        val scenarios = listOf(
            ScenarioItem(1, "Aprobación Automática", if (riskLevel == RiskLevel.LOW) "Pre-aprobado" else "Requiere Aval", riskLevel == RiskLevel.LOW),
            ScenarioItem(2, "Capacidad de Ahorro", "Sueldo libre: $${String.format("%,.0f", (salary - monthlyPayment).coerceAtLeast(0.0))}", riskLevel != RiskLevel.HIGH),
            ScenarioItem(3, "Fondo de Emergencia", if (debtRatio < 35) "Saludable" else "Comprometido", debtRatio < 35)
        )

        _uiState.update {
            it.copy(
                monthlyPayment = monthlyPayment,
                totalInterest = totalInterest,
                debtRatio = debtRatio,
                riskLevel = riskLevel,
                capitalRatio = capitalRatio,
                scenarios = scenarios
            )
        }
    }
}