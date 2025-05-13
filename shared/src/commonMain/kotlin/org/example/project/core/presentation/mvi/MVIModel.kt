package org.example.project.core.presentation.mvi

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

/**
 * MVI（Model-View-Intent）アーキテクチャのViewModel基本インターフェース
 * 
 * @param S 状態の型
 * @param I インテント（ユーザーアクション）の型
 * @param E 副作用の型
 */
interface MVIModel<S : UiState, I : UiIntent, E : UiEffect> {
    /**
     * 現在のUI状態を監視するためのStateFlow
     */
    val state: StateFlow<S>
    
    /**
     * 副作用を監視するためのFlow
     */
    val effect: Flow<E>
    
    /**
     * ユーザーのインテント（アクション）を処理します
     * 
     * @param intent 処理するインテント
     */
    fun processIntent(intent: I)
} 