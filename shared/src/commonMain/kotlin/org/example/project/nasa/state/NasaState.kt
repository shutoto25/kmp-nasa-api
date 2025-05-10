package org.example.project.nasa.state

import org.example.project.core.state.State
import org.example.project.nasa.data.NasaApodData

/**
 * NASA APOD機能の状態を表すデータクラス。
 *
 * @property loading データ読み込み中かどうか
 * @property apod 現在表示中のAPODデータ
 * @property errorMessage エラーメッセージ（エラーが発生した場合）
 */
data class NasaState(
    val loading: Boolean = false,
    val apod: NasaApodData? = null,
    val errorMessage: String? = null
) : State