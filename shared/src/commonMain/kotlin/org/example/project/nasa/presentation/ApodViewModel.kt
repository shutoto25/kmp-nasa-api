package org.example.project.nasa.presentation

import kotlinx.coroutines.launch
import org.example.project.core.presentation.mvi.BaseMVIModel
import org.example.project.core.util.DateUtils
import org.example.project.nasa.domain.usecase.ClearApodCacheUseCase
import org.example.project.nasa.domain.usecase.GetApodUseCase
import org.example.project.nasa.presentation.mvi.ApodEffect
import org.example.project.nasa.presentation.mvi.ApodIntent
import org.example.project.nasa.presentation.mvi.ApodUiState

/**
 * APOD画面のViewModel
 * 
 * @property getApodUseCase APODを取得するユースケース
 * @property clearApodCacheUseCase APODキャッシュをクリアするユースケース
 */
class ApodViewModel(
    private val getApodUseCase: GetApodUseCase,
    private val clearApodCacheUseCase: ClearApodCacheUseCase
) : BaseMVIModel<ApodUiState, ApodIntent, ApodEffect>(ApodUiState()) {
    
    init {
        val todayDate = DateUtils.getTodayDate()
        
        // 初期化時にデータを読み込む
        processIntent(ApodIntent.LoadApod(date = todayDate))
    }
    
    /**
     * ユーザーインテントを処理します
     * 
     * @param intent 処理するインテント
     */
    override fun handleIntent(intent: ApodIntent) {
        when (intent) {
            is ApodIntent.LoadApod -> loadApod(intent.forceUpdate, intent.date)
            is ApodIntent.ClearError -> clearError()
            is ApodIntent.ClearCache -> clearCache()
        }
    }
    
    /**
     * APODデータを読み込みます
     * 
     * @param forceUpdate キャッシュを無視して強制的に更新するかどうか
     * @param date 取得する日付（nullの場合は最新のデータを取得）
     */
    private fun loadApod(forceUpdate: Boolean, date: String? = null) {
        if (currentState.isLoading) {
            return
        }
        
        updateState { copy(isLoading = true, errorMessage = null) }
        
        launch {
            try {
                val params = GetApodUseCase.Params(date, forceUpdate)
                val apod = getApodUseCase(params)
                updateState { copy(isLoading = false, apod = apod, errorMessage = null) }
            } catch (e: Exception) {
                // APIに特定の日付のデータがない場合の特別なハンドリング
                if (e.message?.contains("No data available for date") == true) {
                    // 最新データを再取得
                    updateState { 
                        copy(
                            isLoading = false, 
                            errorMessage = "指定された日付のデータがありません。最新のデータを表示します。"
                        ) 
                    }
                    loadApod(true, null)
                } else {
                    updateState { copy(isLoading = false, errorMessage = e.message) }
                    launch { emitEffect(ApodEffect.Error(e)) }
                }
            }
        }
    }
    
    /**
     * エラー状態をクリアします
     */
    private fun clearError() {
        updateState { copy(errorMessage = null) }
    }
    
    /**
     * キャッシュをクリアします
     */
    private fun clearCache() {
        launch {
            try {
                clearApodCacheUseCase()
                emitEffect(ApodEffect.CacheCleared)
            } catch (e: Exception) {
                emitEffect(ApodEffect.Error(e))
            }
        }
    }
} 