package org.example.project.core.network

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

/**
 * APIクライアントの基底クラス。
 * 共通のHTTPリクエスト処理とエラーハンドリングを提供します。
 *
 * @property httpClient HTTPリクエストを実行するためのクライアント
 * @property json JSONシリアライズ/デシリアライズのためのJsonインスタンス
 */
abstract class BaseApiClient(
    protected val httpClient: HttpClient,
    protected val json: Json
) {
    /**
     * 指定されたURLに対してGETリクエストを実行し、レスポンスを指定された型にデシリアライズします。
     *
     * @param url リクエスト先のURL
     * @param serializer レスポンスのデシリアライズに使用するシリアライザー
     * @return デシリアライズされたレスポンス
     */
    protected suspend inline fun <reified T> get(url: String, serializer: KSerializer<T>): T {
        val response = httpClient.get(url).bodyAsText()
        return json.decodeFromString(serializer, response)
    }

    /**
     * クエリパラメータ付きのGETリクエストを実行します。
     *
     * @param baseUrl ベースURL
     * @param params クエリパラメータのマップ
     * @param serializer レスポンスのデシリアライズに使用するシリアライザー
     * @return デシリアライズされたレスポンス
     */
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

    /**
     * ネットワークエラーを処理し、適切な例外を返します。
     *
     * @param e 発生した例外
     * @return 処理された例外
     */
    protected fun handleNetworkError(e: Exception): Exception {
        // 共通のエラーハンドリングロジック
        // 実際のプロジェクトではより詳細な実装が必要かもしれません
        return NetworkException("ネットワークエラーが発生しました: ${e.message}", e)
    }
}

/**
 * ネットワーク関連の例外を表すクラス。
 *
 * @property message エラーメッセージ
 * @property cause 元の例外
 */
class NetworkException(message: String, cause: Throwable? = null) : Exception(message, cause)