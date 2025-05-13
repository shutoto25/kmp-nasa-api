package org.example.project.nasa.presentation.mvi

import org.example.project.core.presentation.mvi.UiEffect

/**
 * APOD画面の副作用（ワンタイムイベント）を表すシールドクラス
 */
sealed class ApodEffect : UiEffect {
    /**
     * エラーが発生したことを示す副作用
     * 
     * @property error 発生した例外
     */
    data class Error(val error: Exception) : ApodEffect()
    
    /**
     * キャッシュがクリアされたことを示す副作用
     */
    object CacheCleared : ApodEffect()
} 