package org.example.project.ui.apod

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.example.project.api.NasaApi
import org.example.project.model.ApodResponse

/**
 * APOD画面のViewModel
 * 
 * このクラスは、APOD画面の状態管理とビジネスロジックを担当します。
 * 画像データの取得、お気に入り機能、日付選択機能を提供します。
 * 
 * @property nasaApi NASA APIとの通信を担当するクラス
 */
class ApodViewModel(private val nasaApi: NasaApi) {
    private val viewModelScope = CoroutineScope(Dispatchers.Main)
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    private val _apod = MutableStateFlow<ApodResponse?>(null)
    val apod: StateFlow<ApodResponse?> = _apod.asStateFlow()
    
    init {
        loadTodayImage()
    }
    
    /**
     * 今日のAPOD画像を取得
     * 
     * 今日のAPOD画像を取得し、状態を更新します。
     * 取得中はローディング状態をtrueに設定します。
     */
    fun loadTodayImage() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = nasaApi.getApod()
                _apod.value = response
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    /**
     * 特定の日付のAPOD画像を取得
     * 
     * @param date 取得する画像の日付（YYYY-MM-DD形式）
     */
    fun loadImageByDate(date: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                
                val response = nasaApi.getApod(date)
                _apod.value = response
            } catch (e: Exception) {
                println("Error in loadImageByDate: ${e.message}")
                println("Error type: ${e.javaClass.name}")
                e.printStackTrace()
                _error.value = e.message ?: "Unknown error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }
} 