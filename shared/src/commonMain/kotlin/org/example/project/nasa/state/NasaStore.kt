package org.example.project.nasa.state

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.example.project.core.state.Store
import org.example.project.nasa.NasaRepository

class NasaStore(
    private val nasaRepository: NasaRepository
) : Store<NasaState, NasaAction, NasaSideEffect>, CoroutineScope by CoroutineScope(Dispatchers.Main) {

    private val state = MutableStateFlow(NasaState())
    private val sideEffect = MutableSharedFlow<NasaSideEffect>()

    override fun observeState(): StateFlow<NasaState> = state
    override fun observeSideEffect(): Flow<NasaSideEffect> = sideEffect

    override fun dispatch(action: NasaAction) {
        println("NasaStore - Action: $action")
        val oldState = state.value

        val newState = when (action) {
            is NasaAction.LoadApod -> {
                if (oldState.loading) {
                    launch { sideEffect.emit(NasaSideEffect.Error(Exception("既に読み込み中です"))) }
                    oldState
                } else {
                    launch { loadApod(action.forceUpdate, action.date) }
                    oldState.copy(loading = true, errorMessage = null)
                }
            }
            is NasaAction.ApodLoaded -> {
                oldState.copy(loading = false, apod = action.apod, errorMessage = null)
            }
            is NasaAction.Error -> {
                launch { sideEffect.emit(NasaSideEffect.Error(action.error)) }
                oldState.copy(loading = false, errorMessage = action.error.message)
            }
            is NasaAction.ClearError -> {
                oldState.copy(errorMessage = null)
            }
            is NasaAction.ClearCache -> {
                launch { clearCache() }
                oldState
            }
        }

        if (newState != oldState) {
            println("NasaStore - NewState: $newState")
            state.value = newState
        }
    }

    private suspend fun loadApod(forceUpdate: Boolean, date: String? = null) {
        try {
            val apod = nasaRepository.getAstronomyPictureOfDay(forceUpdate, date)
            dispatch(NasaAction.ApodLoaded(apod))
        } catch (e: Exception) {
            dispatch(NasaAction.Error(e))
        }
    }

    private suspend fun clearCache() {
        try {
            nasaRepository.clearCache()
            sideEffect.emit(NasaSideEffect.CacheCleared)
        } catch (e: Exception) {
            sideEffect.emit(NasaSideEffect.Error(e))
        }
    }
}