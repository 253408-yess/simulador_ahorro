package com.yess.simulador_ahorro.model

enum class RiskLevel {
    LOW,      // <= 30% del sueldo (Verde)
    MODERATE, // 30.1% - 40% del sueldo (Naranja)
    HIGH      // > 40% del sueldo (Rojo)
}

data class ScenarioItem(
    val id: Int,
    val title: String,
    val detail: String,
    val isApproved: Boolean
)

data class CreditUiState(
    val amountText: String = "50000",
    val salaryText: String = "25000",
    val months: Float = 24f,
    val interestRate: Float = 16f,
    val includeInsurance: Boolean = true,

    // Valores calculados dinámicamente
    val monthlyPayment: Double = 0.0,
    val totalInterest: Double = 0.0,
    val debtRatio: Float = 0f,
    val riskLevel: RiskLevel = RiskLevel.LOW,
    val capitalRatio: Float = 0.5f,
    val scenarios: List<ScenarioItem> = emptyList()
)