package com.yess.simulador_ahorro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.yess.simulador_ahorro.ui.CreditScreen
import com.yess.simulador_ahorro.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme(darkTheme = false, dynamicColor = false) {
                CreditScreen()
            }
        }
    }
}