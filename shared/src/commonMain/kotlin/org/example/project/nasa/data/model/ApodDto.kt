package org.example.project.nasa.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.example.project.nasa.domain.model.ApodEntity

/**
 * NASAのAstronomy Picture of the Day (APOD) APIのレスポンスデータを表すDTO
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
@Serializable
data class ApodDto(
    @SerialName("copyright") val copyright: String? = null,
    @SerialName("date") val date: String = "",
    @SerialName("explanation") val explanation: String = "",
    @SerialName("hdurl") val hdUrl: String? = null,
    @SerialName("media_type") val mediaType: String = "",
    @SerialName("service_version") val serviceVersion: String = "",
    @SerialName("title") val title: String = "",
    @SerialName("url") val url: String = ""
)

/**
 * DTOをドメインエンティティに変換する拡張関数
 */
fun ApodDto.toEntity(): ApodEntity {
    return ApodEntity(
        copyright = this.copyright,
        date = this.date,
        explanation = this.explanation,
        hdUrl = this.hdUrl,
        mediaType = this.mediaType,
        serviceVersion = this.serviceVersion,
        title = this.title,
        url = this.url
    )
}

/**
 * ドメインエンティティをDTOに変換する拡張関数
 */
fun ApodEntity.toDto(): ApodDto {
    return ApodDto(
        copyright = this.copyright,
        date = this.date,
        explanation = this.explanation,
        hdUrl = this.hdUrl,
        mediaType = this.mediaType,
        serviceVersion = this.serviceVersion,
        title = this.title,
        url = this.url
    )
} 