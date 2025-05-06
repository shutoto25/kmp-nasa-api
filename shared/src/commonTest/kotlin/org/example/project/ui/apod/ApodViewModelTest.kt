package org.example.project.ui.apod

import io.ktor.client.engine.mock.*
import io.ktor.http.*
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * APOD画面のViewModelのテストクラス
 * 
 * このクラスは、APOD画面のViewModelの主要な機能をテストします。
 * モックエンジンを使用してAPIレスポンスをシミュレートし、
 * 実際のAPIを呼び出さずにテストを実行します。
 */
class ApodViewModelTest {
    /** NASA APIの認証キー */
    private val apiKey = "jlcJZqpPn2zTMVza55skA3g0bWdXeFnczsHSp4T9"

    /**
     * モックエンジンの設定
     * 
     * APIレスポンスをシミュレートするためのテストデータを提供します。
     * 実際のAPIレスポンスと同じ構造のJSONデータを返します。
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

    /** テスト対象のViewModelインスタンス */
    private val viewModel = ApodViewModel(NasaApi(apiKey, mockEngine))

    /**
     * 初期状態のテスト
     * 
     * ViewModelの初期状態が正しく設定されていることを確認します。
     */
    @Test
    fun `初期状態では画像データがnullで、ローディング状態がfalse`() = runTest {
        assertEquals(null, viewModel.currentImage.value)
        assertFalse(viewModel.isLoading.value)
    }

    /**
     * 今日の画像取得のテスト
     * 
     * 今日のAPOD画像を取得し、正しく状態が更新されることを確認します。
     */
    @Test
    fun `今日の画像を取得できる`() = runTest {
        viewModel.loadTodayImage()
        assertFalse(viewModel.isLoading.value)
        assertNotNull(viewModel.currentImage.value)
        assertEquals("Test Title", viewModel.currentImage.value?.title)
        assertEquals("Test explanation", viewModel.currentImage.value?.explanation)
    }

    /**
     * 特定の日付の画像取得のテスト
     * 
     * 指定した日付のAPOD画像を取得し、正しく状態が更新されることを確認します。
     */
    @Test
    fun `特定の日付の画像を取得できる`() = runTest {
        val date = "2024-03-20"
        viewModel.loadImageByDate(date)
        assertFalse(viewModel.isLoading.value)
        assertNotNull(viewModel.currentImage.value)
        assertEquals(date, viewModel.currentImage.value?.date)
    }

    /**
     * お気に入り機能のテスト
     * 
     * 画像をお気に入りに追加・削除できることを確認します。
     */
    @Test
    fun `画像をお気に入りに追加・削除できる`() = runTest {
        viewModel.loadTodayImage()
        val image = viewModel.currentImage.value!!
        
        // お気に入りに追加
        viewModel.toggleFavorite(image)
        assertTrue(viewModel.isFavorite(image))
        
        // お気に入りから削除
        viewModel.toggleFavorite(image)
        assertFalse(viewModel.isFavorite(image))
    }
} 