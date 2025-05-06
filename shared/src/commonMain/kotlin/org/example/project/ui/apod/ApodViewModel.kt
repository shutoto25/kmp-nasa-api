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
    
    private val _favorites = MutableStateFlow<List<ApodResponse>>(emptyList())
    val favorites: StateFlow<List<ApodResponse>> = _favorites.asStateFlow()
    
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
            try {
                _isLoading.value = true
                _error.value = null
                
                val response = nasaApi.getApod()
                _apod.value = response
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
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
                _error.value = e.message ?: "Unknown error"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    /**
     * 画像をお気に入りに追加・削除
     * 
     * @param apod お気に入りに追加・削除する画像データ
     */
    fun toggleFavorite(apod: ApodResponse) {
        val currentFavorites = _favorites.value.toMutableList()
        val existingIndex = currentFavorites.indexOfFirst { it.date == apod.date }
        
        if (existingIndex >= 0) {
            currentFavorites.removeAt(existingIndex)
        } else {
            currentFavorites.add(apod)
        }
        
        _favorites.value = currentFavorites
    }
    
    /**
     * 画像がお気に入りかどうかを判定
     * 
     * @param apod 判定する画像データ
     * @return お気に入りの場合はtrue、そうでない場合はfalse
     */
    fun isFavorite(apod: ApodResponse): Boolean {
        return _favorites.value.any { it.date == apod.date }
    }
} 