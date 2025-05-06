package org.example.project.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.example.project.model.ApodResponse

class NasaApi(private val apiKey: String) {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    suspend fun getApod(date: String? = null): ApodResponse {
        val url = buildString {
            append("https://api.nasa.gov/planetary/apod")
            append("?api_key=$apiKey")
            if (date != null) {
                append("&date=$date")
            }
        }
        return client.get(url).body()
    }
} 