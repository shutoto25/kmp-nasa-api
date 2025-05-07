package org.example.project.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import org.example.project.model.ApodResponse

interface NasaApi {
    suspend fun getApod(date: String? = null): ApodResponse
}

class NasaApiClientImpl(
    private val httpClient: HttpClient,
    private val apiKey: String,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : NasaApi {
    override suspend fun getApod(date: String?): ApodResponse = withContext(dispatcher) {
        try {
            val response = httpClient.get("$BASE_URL/planetary/apod") {
                parameter("api_key", apiKey)
                date?.let { parameter("date", it) }
            }
            response.body()
        } catch (e: Exception) {
            throw NasaApiException("Failed to fetch APOD data: ${e.message}", e)
        }
    }

    companion object {
        private const val BASE_URL = "https://api.nasa.gov"
    }
}

class NasaApiException(message: String, cause: Throwable? = null) : Exception(message, cause) 