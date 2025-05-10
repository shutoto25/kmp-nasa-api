package org.example.project.core.state

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

/**
 * アプリケーションの状態管理のためのインターフェース。
 *
 * @param S 状態の型
 * @param A アクションの型
 * @param E 副作用の型
 */
interface Store<S : State, A : Action, E : Effect> {
    /**
     * 現在の状態を監視するためのStateFlowを返します。
     */
    fun observeState(): StateFlow<S>

    /**
     * 副作用を監視するためのFlowを返します。
     */
    fun observeSideEffect(): Flow<E>

    /**
     * アクションをディスパッチします。
     *
     * @param action 実行するアクション
     */
    fun dispatch(action: A)
}

/**
 * Storeインターフェースの基本実装。
 * 状態管理と副作用の処理のための共通機能を提供します。
 *
 * @param S 状態の型
 * @param A アクションの型
 * @param E 副作用の型
 * @param initialState 初期状態
 */
abstract class BaseStore<S : State, A : Action, E : Effect>(
    initialState: S
) : Store<S, A, E>, CoroutineScope by CoroutineScope(Dispatchers.Main) {

    protected val state = MutableStateFlow(initialState)
    protected val sideEffect = MutableSharedFlow<E>()

    override fun observeState(): StateFlow<S> = state
    override fun observeSideEffect(): Flow<E> = sideEffect

    abstract override fun dispatch(action: A)

    /**
     * 状態を更新し、変更をログに出力します。
     *
     * @param newState 新しい状態
     */
    protected fun updateState(newState: S) {
        val oldState = state.value
        if (newState != oldState) {
            logStateChange(oldState, newState)
            state.value = newState
        }
    }

    /**
     * 状態の変更をログに出力します。
     * サブクラスでオーバーライドして、カスタムのログ出力を実装できます。
     *
     * @param oldState 変更前の状態
     * @param newState 変更後の状態
     */
    protected open fun logStateChange(oldState: S, newState: S) {
        println("${this::class.simpleName} - State changed: $oldState -> $newState")
    }
}