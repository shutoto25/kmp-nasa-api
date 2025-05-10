package org.example.project.nasa.storage

import kotlinx.serialization.json.Json
import org.example.project.core.storage.PlatformStorage
import org.example.project.nasa.data.NasaApodData

/**
 * NASA APODデータの永続化を管理するストレージクラス。
 *
 * @property settings 設定の保存に使用するSettingsインスタンス
 * @property json JSONシリアライズ/デシリアライズのためのJsonインスタンス
 */
class NasaStorage(
    private val platformStorage: PlatformStorage,
    private val json: Json
) {
    private companion object {
        private const val KEY_LATEST_APOD = "key_latest_apod"
        private const val KEY_APOD_PREFIX = "key_apod_"
    }

    fun getLatestApod(): NasaApodData? {
        return platformStorage.getString(KEY_LATEST_APOD)?.let {
            try {
                json.decodeFromString(NasaApodData.serializer(), it)
            } catch (e: Exception) {
                null
            }
        }
    }

    fun getApod(date: String): NasaApodData? {
        return platformStorage.getString("$KEY_APOD_PREFIX$date")?.let {
            try {
                json.decodeFromString(NasaApodData.serializer(), it)
            } catch (e: Exception) {
                null
            }
        }
    }

    fun saveApod(apod: NasaApodData) {
        val serialized = json.encodeToString(NasaApodData.serializer(), apod)
        platformStorage.putString(KEY_LATEST_APOD, serialized)
        platformStorage.putString("$KEY_APOD_PREFIX${apod.date}", serialized)
    }

    fun clearCache() {
        platformStorage.clear()
    }
}