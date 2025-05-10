package org.example.project.nasa

import io.ktor.client.*
import kotlinx.serialization.json.Json
import org.example.project.core.network.BaseApiClient
import org.example.project.nasa.data.NasaApodData

class NasaApiClient(
    httpClient: HttpClient,
    json: Json
) : BaseApiClient(httpClient, json) {

    private val apiKey = "jlcJZqpPn2zTMVza55skA3g0bWdXeFnczsHSp4T9"
    private val baseUrl = "https://api.nasa.gov/planetary/apod"

    suspend fun getAstronomyPictureOfDay(date: String? = null): NasaApodData {
        val params = mapOf(
            "api_key" to apiKey,
            "date" to date
        )

        return try {
            get(baseUrl, params, NasaApodData.serializer())
        } catch (e: Exception) {
            throw handleNetworkError(e)
        }
    }
}