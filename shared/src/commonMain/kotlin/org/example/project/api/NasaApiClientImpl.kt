package org.example.project.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.example.project.model.ApodResponse

class NasaApiClientImpl(
    private val apiKey: String,
    private val httpClient: HttpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }
) : NasaApiClient {
    override suspend fun fetchApod(date: String?): ApodResponse {
        val url = "https://api.nasa.gov/planetary/apod"
        return httpClient.get(url) {
            parameter("api_key", apiKey)
            if (date != null) parameter("date", date)
        }.body()
    }
} 