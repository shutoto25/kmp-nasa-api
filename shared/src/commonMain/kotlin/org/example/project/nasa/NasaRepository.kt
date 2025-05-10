package org.example.project.nasa

import org.example.project.nasa.data.NasaApodData
import org.example.project.nasa.storage.NasaStorage

class NasaRepository(
    private val apiClient: NasaApiClient,
    private val storage: NasaStorage
) {
    suspend fun getAstronomyPictureOfDay(forceUpdate: Boolean = false, date: String? = null): NasaApodData {
        // キャッシュからデータを取得
        val cached = if (date != null) storage.getApod(date) else storage.getLatestApod()

        // 強制更新または、キャッシュがない場合は新しいデータを取得
        return if (forceUpdate || cached == null) {
            val fresh = apiClient.getAstronomyPictureOfDay(date)
            storage.saveApod(fresh)
            fresh
        } else {
            cached
        }
    }

    suspend fun clearCache() {
        storage.clearCache()
    }
}