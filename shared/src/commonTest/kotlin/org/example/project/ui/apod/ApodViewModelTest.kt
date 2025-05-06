package org.example.project.ui.apod

import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.example.project.api.NasaApi
import org.example.project.model.ApodResponse
import kotlin.test.*

/**
 * APOD画面のViewModelのテストクラス
 * 
 * このクラスは、APOD画面のViewModelの主要な機能をテストします。
 * モックエンジンを使用してAPIレスポンスをシミュレートし、
 * 実際のAPIを呼び出さずにテストを実行します。
 */
class ApodViewModelTest {
    private lateinit var mockEngine: MockEngine
    private lateinit var mockClient: HttpClient
    private lateinit var nasaApi: NasaApi
    private lateinit var viewModel: ApodViewModel
    
    @BeforeTest
    fun setup() {
        mockEngine = MockEngine { request ->
            val url = request.url.toString()
            when {
                url.contains("api_key=test") && !url.contains("date=") -> {
                    respond(
                        content = """
                            {
                                "date": "2024-03-20",
                                "explanation": "Test explanation",
                                "media_type": "image",
                                "service_version": "v1",
                                "title": "Test Title",
                                "url": "https://example.com/image.jpg"
                            }
                        """.trimIndent(),
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }
                url.contains("date=2024-03-19") -> {
                    respond(
                        content = """
                            {
                                "date": "2024-03-19",
                                "explanation": "Test explanation for specific date",
                                "media_type": "image",
                                "service_version": "v1",
                                "title": "Test Title for specific date",
                                "url": "https://example.com/specific-image.jpg"
                            }
                        """.trimIndent(),
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }
                else -> error("Unhandled ${request.url}")
            }
        }
        
        mockClient = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
        }
        
        nasaApi = NasaApi("test")
        viewModel = ApodViewModel(nasaApi)
    }
    
    @Test
    fun `test initial state`() = runTest {
        assertNull(viewModel.apod.value)
        assertFalse(viewModel.isLoading.value)
        assertTrue(viewModel.favorites.value.isEmpty())
    }
    
    @Test
    fun `test load today image`() = runTest {
        viewModel.loadTodayImage()
        
        assertNotNull(viewModel.apod.value)
        assertEquals("2024-03-20", viewModel.apod.value?.date)
        assertEquals("Test Title", viewModel.apod.value?.title)
    }
    
    @Test
    fun `test load image by date`() = runTest {
        viewModel.loadImageByDate("2024-03-19")
        
        assertNotNull(viewModel.apod.value)
        assertEquals("2024-03-19", viewModel.apod.value?.date)
        assertEquals("Test Title for specific date", viewModel.apod.value?.title)
    }
    
    @Test
    fun `test toggle favorite`() = runTest {
        viewModel.loadTodayImage()
        val apod = viewModel.apod.value!!
        
        // Add to favorites
        viewModel.toggleFavorite(apod)
        assertTrue(viewModel.favorites.value.contains(apod))
        assertTrue(viewModel.isFavorite(apod))
        
        // Remove from favorites
        viewModel.toggleFavorite(apod)
        assertFalse(viewModel.favorites.value.contains(apod))
        assertFalse(viewModel.isFavorite(apod))
    }
} 