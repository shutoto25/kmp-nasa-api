package org.example.project.nasa.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NasaApodData(
    @SerialName("copyright") val copyright: String? = null,
    @SerialName("date") val date: String,
    @SerialName("explanation") val explanation: String,
    @SerialName("hdurl") val hdUrl: String? = null,
    @SerialName("media_type") val mediaType: String,
    @SerialName("service_version") val serviceVersion: String,
    @SerialName("title") val title: String,
    @SerialName("url") val url: String
)