package org.example.project.nasa.state

import org.example.project.core.state.Effect

/**
 * NASA APOD機能の副作用を表すシールドクラス。
 */
sealed class NasaSideEffect : Effect {
    /**
     * エラーが発生したことを示す副作用。
     *
     * @property error 発生した例外
     */
    data class Error(val error: Exception) : NasaSideEffect()

    /**
     * キャッシュがクリアされたことを示す副作用。
     */
    object CacheCleared : NasaSideEffect()
}