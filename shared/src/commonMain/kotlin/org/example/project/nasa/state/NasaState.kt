package org.example.project.nasa.state

import org.example.project.core.state.State
import org.example.project.nasa.data.NasaApodData

data class NasaState(
    val loading: Boolean = false,
    val apod: NasaApodData? = null,
    val errorMessage: String? = null
) : State