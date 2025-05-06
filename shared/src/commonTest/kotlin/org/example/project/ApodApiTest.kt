package org.example.project

import io.ktor.client.engine.mock.*
import io.ktor.http.*
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * NasaApiクラスのAPOD API機能をテストするクラス
 * 
 * このクラスは、APOD APIの主要な機能をテストします。
 * モックエンジンを使用してAPIレスポンスをシミュレートし、
 * 実際のAPIを呼び出さずにテストを実行します。
 * 
 * @see NasaApi テスト対象のクラス
 */
class ApodApiTest {
    /** NASA APIの認証キー */
    private val apiKey = "jlcJZqpPn2zTMVza55skA3g0bWdXeFnczsHSp4T9"

    /**
     * モックエンジンの設定
     * 
     * APIレスポンスをシミュレートするためのテストデータを提供します。
     * 実際のAPIレスポンスと同じ構造のJSONデータを返します。
     * 
     * @implNote テスト用の固定データを使用
     */
    private val mockEngine = MockEngine { request ->
        respond(
            content = """
                {
                    "date": "2024-03-20",
                    "explanation": "Test explanation",
                    "hdurl": "https://example.com/hd.jpg",
                    "media_type": "image",
                    "service_version": "v1",
                    "title": "Test Title",
                    "url": "https://example.com/image.jpg"
                }
            """.trimIndent(),
            status = HttpStatusCode.OK,
            headers = headersOf("Content-Type", "application/json")
        )
    }

    /** テスト対象のNasaApiインスタンス */
    private val api = NasaApi(apiKey, mockEngine)

    /**
     * 今日のAPOD画像を取得できることを確認するテスト
     * 
     * 日付を指定せずにAPOD APIを呼び出し、
     * レスポンスが正しい形式で返されることを確認します。
     */
    @Test
    fun `APOD APIから今日の画像を取得できる`() = runTest {
        val response = api.getApod()
        assertNotNull(response)
        assertNotNull(response.url)
        assertNotNull(response.title)
        assertNotNull(response.explanation)
    }

    /**
     * 特定の日付のAPOD画像を取得できることを確認するテスト
     * 
     * 特定の日付を指定してAPOD APIを呼び出し、
     * レスポンスが正しい形式で返され、指定した日付と一致することを確認します。
     * 
     * @param date テストで使用する日付（YYYY-MM-DD形式）
     */
    @Test
    fun `APOD APIから特定の日付の画像を取得できる`() = runTest {
        val date = "2024-03-20"
        val response = api.getApod(date = date)
        assertNotNull(response)
        assertEquals(date, response.date)
        assertNotNull(response.url)
        assertNotNull(response.title)
        assertNotNull(response.explanation)
    }
} 