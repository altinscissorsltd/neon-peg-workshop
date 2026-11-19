package com.Kebcaqa1.Veghar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.Kebcaqa1.Veghar.core.di.ServiceLocator
import com.Kebcaqa1.Veghar.core.theme.AppTheme
import com.Kebcaqa1.Veghar.presentation.app.AppRoot

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        ServiceLocator.initialize(applicationContext)
        setContent {
            AppTheme {
                AppRoot()
            }
        }
    }
}
