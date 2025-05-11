package org.example.project.nasa

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.example.project.core.network.BaseApiClient
import org.example.project.nasa.data.NasaApodData

class NasaApiClient(
    httpClient: HttpClient,
    json: Json
) : BaseApiClient(httpClient, json) {
    private val TAG = "NasaApiClient"
    private val apiKey = "jlcJZqpPn2zTMVza55skA3g0bWdXeFnczsHSp4T9"
    private val baseUrl = "https://api.nasa.gov/planetary/apod"

    suspend fun getAstronomyPictureOfDay(date: String? = null, isRetry: Boolean = false): NasaApodData {
        val params = mutableMapOf<String, String>()
        params["api_key"] = apiKey

        // 日付がnullでない場合のみ追加
        if (date != null && date.isNotEmpty()) {
            params["date"] = date
        } else {
        }

        // クエリパラメータの構築
        val queryString = params.entries.joinToString("&") { "${it.key}=${it.value}" }
        val fullUrl = "$baseUrl?$queryString"

        try {
            val response = httpClient.get(fullUrl)
            val responseText = response.bodyAsText()

            // エラーレスポンスの検出
            if (responseText.contains("\"code\":404")) {
                if (!isRetry && date != null) {
                    // 指定した日付のデータがない場合、最新データを取得
                    return getAstronomyPictureOfDay(null, true)
                } else {
                    // 再試行中にもエラーが発生した場合は例外をスロー
                    val errorJson = json.parseToJsonElement(responseText).jsonObject
                    val errorMsg = errorJson["msg"]?.jsonPrimitive?.content ?: "Unknown error"
                    throw Exception(errorMsg)
                }
            }
            
            val result = json.decodeFromString(NasaApodData.serializer(), responseText)
            return result
        } catch (e: Exception) {
            throw handleNetworkError(e)
        }
    }
}