package org.example.project.ui.apod

import com.example.nasaapi.NasaApi
import com.example.nasaapi.model.ApodResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * APOD画面のViewModel
 * 
 * このクラスは、APOD画面の状態管理とビジネスロジックを担当します。
 * 画像データの取得、お気に入り機能、日付選択機能を提供します。
 * 
 * @property nasaApi NASA APIとの通信を担当するクラス
 */
class ApodViewModel(
    private val nasaApi: NasaApi
) {
    /** 現在表示中の画像データ */
    private val _currentImage = MutableStateFlow<ApodResponse?>(null)
    val currentImage: StateFlow<ApodResponse?> = _currentImage.asStateFlow()

    /** ローディング状態 */
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    /** お気に入り画像のIDリスト */
    private val _favoriteImageIds = MutableStateFlow<Set<String>>(emptySet())
    val favoriteImageIds: StateFlow<Set<String>> = _favoriteImageIds.asStateFlow()

    /** コルーチンスコープ */
    private val viewModelScope = CoroutineScope(Dispatchers.Main)

    /**
     * 今日のAPOD画像を取得
     * 
     * 今日のAPOD画像を取得し、状態を更新します。
     * 取得中はローディング状態をtrueに設定します。
     */
    fun loadTodayImage() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _currentImage.value = nasaApi.getApod()
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
            _isLoading.value = true
            try {
                _currentImage.value = nasaApi.getApod(date)
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * 画像をお気に入りに追加・削除
     * 
     * @param image お気に入りに追加・削除する画像データ
     */
    fun toggleFavorite(image: ApodResponse) {
        val currentFavorites = _favoriteImageIds.value.toMutableSet()
        if (currentFavorites.contains(image.date)) {
            currentFavorites.remove(image.date)
        } else {
            currentFavorites.add(image.date)
        }
        _favoriteImageIds.value = currentFavorites
    }

    /**
     * 画像がお気に入りかどうかを判定
     * 
     * @param image 判定する画像データ
     * @return お気に入りの場合はtrue、そうでない場合はfalse
     */
    fun isFavorite(image: ApodResponse): Boolean {
        return _favoriteImageIds.value.contains(image.date)
    }
} 