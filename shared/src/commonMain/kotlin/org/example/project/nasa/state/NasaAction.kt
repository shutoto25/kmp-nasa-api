package org.example.project.nasa.state

import org.example.project.core.state.Action
import org.example.project.nasa.data.NasaApodData

/**
 * NASA APOD機能のアクションを表すシールドクラス。
 */
sealed class NasaAction : Action {
    /**
     * APODデータの読み込みを要求するアクション。
     *
     * @property forceUpdate キャッシュを無視して強制的に更新するかどうか
     * @property date 取得する日付（nullの場合は最新のデータを取得）
     */
    data class LoadApod(val forceUpdate: Boolean = false, val date: String? = null) : NasaAction()

    /**
     * APODデータの読み込みが完了したことを示すアクション。
     *
     * @property apod 読み込まれたAPODデータ
     */
    data class ApodLoaded(val apod: NasaApodData) : NasaAction()

    /**
     * エラーが発生したことを示すアクション。
     *
     * @property error 発生した例外
     */
    data class Error(val error: Exception) : NasaAction()

    /**
     * エラー状態をクリアするアクション。
     */
    object ClearError : NasaAction()

    /**
     * キャッシュをクリアするアクション。
     */
    object ClearCache : NasaAction()
}