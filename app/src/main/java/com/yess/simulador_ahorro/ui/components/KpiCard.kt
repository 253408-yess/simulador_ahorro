package com.yess.simulador_ahorro.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yess.simulador_ahorro.model.RiskLevel

@Composable
fun KpiCard(
    monthlyPayment: Double,
    totalInterest: Double,
    riskLevel: RiskLevel,
    modifier: Modifier = Modifier
) {
    val targetColor = when (riskLevel) {
        RiskLevel.LOW -> Color(0xFFE8F5E9)
        RiskLevel.MODERATE -> Color(0xFFFFF3E0)
        RiskLevel.HIGH -> Color(0xFFFFEBEE)
    }

    val animatedBgColor by animateColorAsState(
        targetValue = targetColor,
        animationSpec = tween(durationMillis = 400),
        label = "CardBgAnimation"
    )


    val darkTextColor = Color(0xFF1C1B1F)
    val darkSubtitleColor = Color(0xFF49454F)

    Card(
        colors = CardDefaults.cardColors(containerColor = animatedBgColor),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Cuota Mensual Estimada",
                style = MaterialTheme.typography.labelMedium,
                color = darkSubtitleColor
            )
            Text(
                text = "$${String.format("%,.2f", monthlyPayment)}",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = darkTextColor
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Total Intereses: $${String.format("%,.2f", totalInterest)}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = darkSubtitleColor
            )
        }
    }
}