package org.example.project.core.presentation.mvi

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * MVIモデルの基本実装クラス
 *
 * @param S UIの状態型
 * @param I ユーザーインテント（アクション）の型
 * @param E UI副作用の型
 * @property initialState 初期状態
 */
abstract class BaseMVIModel<S : UiState, I : UiIntent, E : UiEffect>(
    initialState: S
) : MVIModel<S, I, E>, CoroutineScope by CoroutineScope(Dispatchers.Main) {
    
    // 状態を管理するStateFlow
    private val _state = MutableStateFlow(initialState)
    override val state: StateFlow<S> = _state.asStateFlow()
    
    // 副作用を管理するSharedFlow
    private val _effect = MutableSharedFlow<E>()
    override val effect: Flow<E> = _effect.asSharedFlow()
    
    // 現在の状態を取得するためのプロパティ
    protected val currentState: S get() = state.value
    
    /**
     * 状態を更新します
     *
     * @param reducer 現在の状態を新しい状態に変換する関数
     */
    protected fun updateState(reducer: S.() -> S) {
        val newState = currentState.reducer()
        if (newState != currentState) {
            _state.value = newState
        }
    }
    
    /**
     * 副作用を発行します
     *
     * @param effect 発行する副作用
     */
    protected suspend fun emitEffect(effect: E) {
        _effect.emit(effect)
    }
    
    /**
     * ユーザーのインテント（アクション）を処理します
     * 
     * @param intent ユーザーインテント
     */
    override fun processIntent(intent: I) {
        handleIntent(intent)
    }
    
    /**
     * ユーザーインテントを処理する具体的な実装
     * 
     * サブクラスでオーバーライドして、各インテントの処理ロジックを実装します
     * 
     * @param intent 処理するインテント
     */
    protected abstract fun handleIntent(intent: I)
} 