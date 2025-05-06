package org.example.project

import org.example.project.model.ApodResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

/**
 * NASA APIとの通信を担当するクラス
 * 
 * このクラスは、NASAが提供する各種APIエンドポイントへのアクセスを提供します。
 * 現在はAPOD（Astronomy Picture of the Day）APIの実装のみを含みます。
 * 
 * @property apiKey NASA APIの認証キー
 * @property engine テスト用のモックエンジン（オプション）
 * 
 * @see ApodResponse APOD APIのレスポンスモデル
 */
class NasaApi(
    private val apiKey: String,
    engine: HttpClientEngine? = null
) {
    /**
     * HTTPクライアントの設定
     * 
     * テスト時はモックエンジンを使用し、本番環境ではデフォルトのエンジンを使用します。
     * JSONのシリアライズ/デシリアライズの設定も含まれます。
     * 
     * @implNote 未知のキーは無視され、JSONの解析は寛容なモードで行われます
     */
    private val client = if (engine != null) {
        HttpClient(engine) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
        }
    } else {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
        }
    }

    /** NASA APIのベースURL */
    private val baseUrl = "https://api.nasa.gov"

    /**
     * Astronomy Picture of the Day (APOD) APIから画像データを取得
     * 
     * 指定された日付のAPOD画像データを取得します。日付が指定されない場合は、
     * 今日の画像データを取得します。
     * 
     * @param date 取得する画像の日付（YYYY-MM-DD形式）。nullの場合は今日の画像を取得
     * @return APODの画像データ
     * @throws io.ktor.client.plugins.ClientRequestException APIリクエストが失敗した場合
     * 
     * @sample
     * ```
     * val api = NasaApi(apiKey)
     * val todayImage = api.getApod()
     * val specificDateImage = api.getApod("2024-03-20")
     * ```
     */
    suspend fun getApod(date: String? = null): ApodResponse {
        val url = buildString {
            append("$baseUrl/planetary/apod")
            append("?api_key=$apiKey")
            if (date != null) {
                append("&date=$date")
            }
        }
        return client.get(url).body()
    }
} 