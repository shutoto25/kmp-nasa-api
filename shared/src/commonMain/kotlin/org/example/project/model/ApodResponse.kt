package org.example.project.model

import kotlinx.serialization.Serializable

/**
 * Astronomy Picture of the Day (APOD) APIのレスポンスを表すデータクラス
 * 
 * NASAのAPOD APIから返される画像データの構造を定義します。
 * このクラスはJSONのシリアライズ/デシリアライズに対応しています。
 * 
 * @property copyright 著作権情報（存在しない場合はnull）
 * @property date 画像の日付（YYYY-MM-DD形式）
 * @property explanation 画像の説明文
 * @property hdurl 高解像度画像のURL（存在しない場合はnull）
 * @property media_type メディアの種類（"image"または"video"）
 * @property service_version APIのバージョン
 * @property title 画像のタイトル
 * @property url 画像のURL
 * 
 * @see NasaApi APOD APIのクライアント実装
 */
@Serializable
data class ApodResponse(
    val copyright: String? = null,
    val date: String,
    val explanation: String,
    val hdurl: String? = null,
    val media_type: String,
    val service_version: String,
    val title: String,
    val url: String
) 