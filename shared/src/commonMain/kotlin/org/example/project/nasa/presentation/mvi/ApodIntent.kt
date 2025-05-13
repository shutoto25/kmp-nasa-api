package org.example.project.nasa.presentation.mvi

import org.example.project.core.presentation.mvi.UiIntent

/**
 * APOD画面のユーザーインテント（アクション）を表すシールドクラス
 */
sealed class ApodIntent : UiIntent {
    /**
     * APODデータの読み込みを要求するインテント
     * 
     * @property forceUpdate キャッシュを無視して強制的に更新するかどうか
     * @property date 取得する日付（nullの場合は最新のデータを取得）
     */
    data class LoadApod(val forceUpdate: Boolean = false, val date: String? = null) : ApodIntent()
    
    /**
     * エラー状態をクリアするインテント
     */
    object ClearError : ApodIntent()
    
    /**
     * キャッシュをクリアするインテント
     */
    object ClearCache : ApodIntent()
} 