package org.example.project.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Astronomy Picture of the Day (APOD) APIのレスポンスを表すデータクラス
 * 
 * NASAのAPOD APIから返される画像データの構造を定義します。
 * このクラスはJSONのシリアライズ/デシリアライズに対応しています。
 * 
 * @property date 画像の日付（YYYY-MM-DD形式）
 * @property explanation 画像の説明文
 * @property hdUrl 高解像度画像のURL（存在しない場合はnull）
 * @property mediaType メディアの種類（"image"または"video"）
 * @property serviceVersion APIのバージョン
 * @property title 画像のタイトル
 * @property url 画像のURL
 * 
 * @see NasaApi APOD APIのクライアント実装
 */
@Serializable
data class ApodResponse(
    val date: String,
    val explanation: String,
    @SerialName("hdurl")
    val hdUrl: String?,
    @SerialName("media_type")
    val mediaType: String,
    @SerialName("service_version")
    val serviceVersion: String,
    val title: String,
    val url: String
) 