package org.example.project.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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
    
    private val baseUrl = "https://api.nasa.gov/planetary/apod"
    
    suspend fun getApod(date: String? = null): ApodResponse = withContext(Dispatchers.IO) {
        try {
            val url = buildString {
                append(baseUrl)
                append("?api_key=")
                append(apiKey.trim())
                if (date != null) {
                    append("&date=")
                    append(date)
                }
            }
            
            println("Requesting URL: $url")
            val response = client.get(url)
            println("Response status: ${response.status}")
            
            val apodResponse = response.body<ApodResponse>()
            println("Received APOD response: $apodResponse")
            
            apodResponse
        } catch (e: Exception) {
            println("Error in getApod: ${e.message}")
            println("Error type: ${e.javaClass.name}")
            e.printStackTrace()
            throw e
        }
    }
} 