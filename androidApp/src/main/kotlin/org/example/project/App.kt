package org.example.project

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(listOf(coreModule, nasaModule))
        }
    }

    private val coreModule = module {
        // 共通のインスタンス
        single { HttpClient() }
        single { 
            Json {
                ignoreUnknownKeys = true
                isLenient = true
                encodeDefaults = false
            }
        }
        single { 
            com.russhwolf.settings.Settings(
                this@App.getSharedPreferences("app_prefs", MODE_PRIVATE)
                ) 
    }
}
}