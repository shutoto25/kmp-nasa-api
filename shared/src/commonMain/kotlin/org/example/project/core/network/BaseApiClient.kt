package org.example.project.core.network

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

abstract class BaseApiClient(
    protected val httpClient: HttpClient,
    protected val json: Json
) {
    // 共通のHTTPリクエストメソッド
    protected suspend inline fun <reified T> get(url: String, serializer: KSerializer<T>): T {
        val response = httpClient.get(url).bodyAsText()
        return json.decodeFromString(serializer, response)
    }

    // 異なるパラメータでGETリクエスト
    protected suspend inline fun <reified T> get(
        baseUrl: String,
        params: Map<String, String?>,
        serializer: KSerializer<T>
    ): T {
        val queryParams = params.entries
            .filter { it.value != null }
            .joinToString("&") { "${it.key}=${it.value}" }

        val url = if (queryParams.isNotEmpty()) "$baseUrl?$queryParams" else baseUrl
        return get(url, serializer)
    }

    // エラーハンドリングなどの共通メソッド
    protected fun handleNetworkError(e: Exception): Exception {
        // 共通のエラーハンドリングロジック
        // 実際のプロジェクトではより詳細な実装が必要かもしれません
        return NetworkException("ネットワークエラーが発生しました: ${e.message}", e)
    }
}

class NetworkException(message: String, cause: Throwable? = null) : Exception(message, cause)