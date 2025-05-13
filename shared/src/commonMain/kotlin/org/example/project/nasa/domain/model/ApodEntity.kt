package org.example.project.nasa.domain.model

/**
 * Astronomy Picture of the Day (APOD)のドメインモデル
 *
 * @property copyright 画像の著作権情報（オプション）
 * @property date 画像の日付（YYYY-MM-DD形式）
 * @property explanation 画像の説明文
 * @property hdUrl 高解像度画像のURL（オプション）
 * @property mediaType メディアの種類（"image"または"video"）
 * @property serviceVersion APIのバージョン
 * @property title 画像のタイトル
 * @property url 画像のURL
 */
data class ApodEntity(
    val copyright: String? = null,
    val date: String = "",
    val explanation: String = "",
    val hdUrl: String? = null,
    val mediaType: String = "",
    val serviceVersion: String = "",
    val title: String = "",
    val url: String = ""
) 