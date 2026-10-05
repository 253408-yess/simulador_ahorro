package com.yess.simulador_ahorro.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yess.simulador_ahorro.ui.components.KpiCard
import com.yess.simulador_ahorro.viewmodel.CreditViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreditScreen(viewModel: CreditViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val navyBlue = Color(0xFF0A192F)

    Scaffold(
        containerColor = navyBlue,
        contentColor = Color.White,
        topBar = {
            TopAppBar(
                title = { Text("Simulador de Crédito", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = navyBlue,
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(26.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            KpiCard(
                monthlyPayment = uiState.monthlyPayment,
                totalInterest = uiState.totalInterest,
                riskLevel = uiState.riskLevel
            )

            OutlinedTextField(
                value = uiState.amountText,
                onValueChange = viewModel::onAmountChanged,
                label = { Text("Monto del Préstamo ($)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.LightGray,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.LightGray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            OutlinedTextField(
                value = uiState.salaryText,
                onValueChange = viewModel::onSalaryChanged,
                label = { Text("Sueldo Mensual ($)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.LightGray,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.LightGray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            Text("Plazo: ${uiState.months.toInt()} meses")
            Slider(
                value = uiState.months,
                onValueChange = viewModel::onMonthsChanged,
                valueRange = 6f..200f
            )

            Text("Tasa de Interés Anual: ${String.format("%.1f", uiState.interestRate)}%")
            Slider(
                value = uiState.interestRate,
                onValueChange = viewModel::onInterestRateChanged,
                valueRange = 5f..35f
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Incluir Seguro (+1.5%)")
                Switch(
                    checked = uiState.includeInsurance,
                    onCheckedChange = viewModel::onInsuranceToggled
                )
            }

            Text("Compromiso del Sueldo (${uiState.debtRatio.toInt()}%)", style = MaterialTheme.typography.titleMedium)
            LinearProgressIndicator(
                progress = { (uiState.debtRatio / 100f).coerceIn(0f, 1f) },
                modifier = Modifier.fillMaxWidth().height(10.dp)
            )

            Text("Escenarios Evaluados", style = MaterialTheme.typography.titleMedium)
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.scenarios) { item ->
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (item.isApproved) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.errorContainer
                        ),
                        modifier = Modifier.width(180.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(item.title, fontWeight = FontWeight.Bold)
                            Text(item.detail, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}