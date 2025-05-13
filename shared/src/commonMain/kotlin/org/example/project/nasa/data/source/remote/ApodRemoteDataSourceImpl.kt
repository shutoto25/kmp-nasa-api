package org.example.project.nasa.data.source.remote

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.example.project.nasa.data.model.ApodDto
import org.example.project.nasa.data.model.toEntity
import org.example.project.nasa.domain.model.ApodEntity

/**
 * APODリモートデータソースの実装クラス
 *
 * @property httpClient HTTPクライアント
 * @property json JSONパーサー
 */
class ApodRemoteDataSourceImpl(
    private val httpClient: HttpClient,
    private val json: Json
) : ApodRemoteDataSource {
    
    private val apiKey = "jlcJZqpPn2zTMVza55skA3g0bWdXeFnczsHSp4T9"
    private val baseUrl = "https://api.nasa.gov/planetary/apod"
    
    /**
     * APIから指定した日付のAPODを取得します
     *
     * @param date 取得する日付（nullの場合は最新のデータを取得）
     * @return APODエンティティ
     */
    override suspend fun getApod(date: String?): ApodEntity {
        return fetchFromApi(date, false)
    }
    
    /**
     * APIからデータを取得する内部メソッド
     *
     * @param date 取得する日付
     * @param isRetry 再試行かどうか
     * @return APODエンティティ
     */
    private suspend fun fetchFromApi(date: String?, isRetry: Boolean = false): ApodEntity {
        val params = mutableMapOf<String, String>()
        params["api_key"] = apiKey
        
        // 日付がnullでない場合のみ追加
        if (date != null && date.isNotEmpty()) {
            params["date"] = date
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
                    return fetchFromApi(null, true)
                } else {
                    // 再試行中にもエラーが発生した場合は例外をスロー
                    val errorJson = json.parseToJsonElement(responseText).jsonObject
                    val errorMsg = errorJson["msg"]?.jsonPrimitive?.content ?: "Unknown error"
                    throw Exception(errorMsg)
                }
            }
            
            val result = json.decodeFromString(ApodDto.serializer(), responseText)
            return result.toEntity()
        } catch (e: Exception) {
            throw handleNetworkError(e)
        }
    }
    
    /**
     * ネットワークエラーを処理します
     *
     * @param error 元の例外
     * @return 適切なエラーメッセージを持つ例外
     */
    private fun handleNetworkError(error: Exception): Exception {
        // エラーメッセージの加工やログ出力などを行う
        return Exception("ネットワークエラー: ${error.message}", error)
    }
} 