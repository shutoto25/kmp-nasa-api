package org.example.project.core.state

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

interface Store<S : State, A : Action, E : Effect> {
    fun observeState(): StateFlow<S>
    fun observeSideEffect(): Flow<E>
    fun dispatch(action: A)
}

abstract class BaseStore<S : State, A : Action, E : Effect>(
    initialState: S
) : Store<S, A, E>, CoroutineScope by CoroutineScope(Dispatchers.Main) {

    protected val state = MutableStateFlow(initialState)
    protected val sideEffect = MutableSharedFlow<E>()

    override fun observeState(): StateFlow<S> = state
    override fun observeSideEffect(): Flow<E> = sideEffect

    abstract override fun dispatch(action: A)

    // 状態の更新とログ出力を行うヘルパーメソッド
    protected fun updateState(newState: S) {
        val oldState = state.value
        if (newState != oldState) {
            logStateChange(oldState, newState)
            state.value = newState
        }
    }

    // 状態変更をログに出力するためのメソッド
    protected open fun logStateChange(oldState: S, newState: S) {
        println("${this::class.simpleName} - State changed: $oldState -> $newState")
    }
}