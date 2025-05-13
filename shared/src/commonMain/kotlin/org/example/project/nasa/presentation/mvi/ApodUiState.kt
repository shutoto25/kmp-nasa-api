package org.example.project.nasa.presentation.mvi

import org.example.project.core.presentation.mvi.UiState
import org.example.project.nasa.domain.model.ApodEntity

/**
 * APOD画面のUI状態を表すデータクラス
 * 
 * @property isLoading データ読み込み中かどうか
 * @property apod 現在表示中のAPODデータ
 * @property errorMessage エラーメッセージ（エラーが発生した場合）
 */
data class ApodUiState(
    val isLoading: Boolean = false,
    val apod: ApodEntity? = null,
    val errorMessage: String? = null
) : UiState 