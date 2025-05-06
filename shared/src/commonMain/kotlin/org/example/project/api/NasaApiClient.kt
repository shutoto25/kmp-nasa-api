package org.example.project.api

import org.example.project.model.ApodResponse

interface NasaApiClient {
    suspend fun fetchApod(date: String? = null): ApodResponse
} 