package org.example.project.nasa.state

import org.example.project.core.state.Effect

sealed class NasaSideEffect : Effect {
    data class Error(val error: Exception) : NasaSideEffect()
    object CacheCleared : NasaSideEffect()
}