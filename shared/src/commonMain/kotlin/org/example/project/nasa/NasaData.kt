package org.example.project.nasa

import kotlinx.serialization.Serializable

@Serializable
data class NasaData(
    val date: String,
    val explanation: String,
    val hdurl: String?,
    val mediaType: String,
    val serviceVersion: String,
    val title: String,
    val url: String
) 