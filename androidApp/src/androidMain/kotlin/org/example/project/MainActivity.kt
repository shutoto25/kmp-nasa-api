package org.example.project

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.example.project.ui.apod.ApodScreen
import org.example.project.ui.apod.ApodViewModel
import org.example.project.api.NasaApi
import org.example.project.api.NasaApiClientImpl

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val apiKey = BuildConfig.NASA_API_KEY
        Log.d("MainActivity", "API Key from BuildConfig: '$apiKey'")
        Log.d("MainActivity", "API Key length: ${apiKey.length}")
        
        val httpClient = HttpClient(Android) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
        }
        
        val nasaApi: NasaApi = NasaApiClientImpl(httpClient = httpClient, apiKey = apiKey)
        Log.d("MainActivity", "NasaApi instance created")
        
        setContent {
            MaterialTheme(
                colorScheme = lightColorScheme()
            ) {
                Surface {
                    ApodScreen(viewModel = ApodViewModel(nasaApi))
                }
            }
        }
    }
}