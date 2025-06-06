package org.example.project.nasa.state

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.example.project.core.state.Store
import org.example.project.core.util.DateUtils.getTodayDate
import org.example.project.nasa.NasaRepository

/**
 * NASA APOD機能の状態管理を行うストアクラス
 *
 * @property nasaRepository APODデータの取得とキャッシュ管理を行うリポジトリ
 */
class NasaStore(
    private val nasaRepository: NasaRepository
) : Store<NasaState, NasaAction, NasaSideEffect>, CoroutineScope by CoroutineScope(Dispatchers.Main) {
    private val TAG = "NasaStore"
    private val state = MutableStateFlow(NasaState())
    private val sideEffect = MutableSharedFlow<NasaSideEffect>()

    init {
        val todayDate = getTodayDate()

        // 初期化時にデータを読み込む
        launch {
            dispatch(NasaAction.LoadApod(forceUpdate = true, date = todayDate))
        }
    }

    override fun observeState(): StateFlow<NasaState> = state
    override fun observeSideEffect(): Flow<NasaSideEffect> = sideEffect

    /**
     * アクションをディスパッチし、適切な状態更新と副作用の発生を行います。
     *
     * @param action 実行するアクション
     */
    override fun dispatch(action: NasaAction) {
        val oldState = state.value

        val newState = when (action) {
            is NasaAction.LoadApod -> {
                if (oldState.loading) {
                    launch { sideEffect.emit(NasaSideEffect.Error(Exception("既に読み込み中です"))) }
                    oldState
                } else {
                    launch { loadApod(action.forceUpdate, action.date) }
                    oldState.copy(loading = true, errorMessage = null)
                }
            }
            is NasaAction.ApodLoaded -> {
                oldState.copy(loading = false, apod = action.apod, errorMessage = null)
            }
            is NasaAction.Error -> {

                // APIに特定の日付のデータがない場合の特別なハンドリング
                if (action.error.message?.contains("No data available for date") == true) {
                    // 最新データを再取得
                    launch { loadApod(true, null) }
                    oldState.copy(errorMessage = "指定された日付のデータがありません。最新のデータを表示します。")
                } else {
                    launch { sideEffect.emit(NasaSideEffect.Error(action.error)) }
                    oldState.copy(loading = false, errorMessage = action.error.message)
                }
            }
            is NasaAction.ClearError -> {
                oldState.copy(errorMessage = null)
            }
            is NasaAction.ClearCache -> {
                launch { clearCache() }
                oldState
            }
        }

        if (newState != oldState) {
            state.value = newState
        }
    }

    /**
     * APODデータを読み込みます。
     *
     * @param forceUpdate キャッシュを無視して強制的に更新するかどうか
     * @param date 取得する日付（nullの場合は最新のデータを取得）
     */
    private suspend fun loadApod(forceUpdate: Boolean, date: String? = null) {
        try {
            val apod = nasaRepository.getAstronomyPictureOfDay(forceUpdate, date)
            dispatch(NasaAction.ApodLoaded(apod))
        } catch (e: Exception) {
            dispatch(NasaAction.Error(e))
        }
    }

    /**
     * キャッシュをクリアします。
     */
    private suspend fun clearCache() {
        try {
            nasaRepository.clearCache()
            sideEffect.emit(NasaSideEffect.CacheCleared)
        } catch (e: Exception) {
            sideEffect.emit(NasaSideEffect.Error(e))
        }
    }
}