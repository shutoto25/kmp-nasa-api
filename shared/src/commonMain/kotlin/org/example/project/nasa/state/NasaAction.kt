package org.example.project.nasa.state

import org.example.project.core.state.Action
import org.example.project.nasa.data.NasaApodData

sealed class NasaAction : Action {
    data class LoadApod(val forceUpdate: Boolean = false, val date: String? = null) : NasaAction()
    data class ApodLoaded(val apod: NasaApodData) : NasaAction()
    data class Error(val error: Exception) : NasaAction()
    object ClearError : NasaAction()
    object ClearCache : NasaAction()
}