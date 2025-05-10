package org.example.project.nasa.storage

import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import kotlinx.serialization.json.Json
import org.example.project.nasa.data.NasaApodData

/**
 * NASA APODデータの永続化を管理するストレージクラス。
 *
 * @property settings 設定の保存に使用するSettingsインスタンス
 * @property json JSONシリアライズ/デシリアライズのためのJsonインスタンス
 */
class NasaStorage(
    private val settings: Settings,
    private val json: Json
) {
    private companion object {
        private const val KEY_LATEST_APOD = "key_latest_apod"
        private const val KEY_APOD_PREFIX = "key_apod_"
    }

    /**
     * 最新のAPODデータを取得します。
     *
     * @return 最新のAPODデータ、または保存されていない場合はnull
     */
    fun getLatestApod(): NasaApodData? {
        return settings.getStringOrNull(KEY_LATEST_APOD)?.let {
            try {
                json.decodeFromString(NasaApodData.serializer(), it)
            } catch (e: Exception) {
                null
            }
        }
    }

    /**
     * 指定された日付のAPODデータを取得します。
     *
     * @param date 取得する日付（YYYY-MM-DD形式）
     * @return 指定された日付のAPODデータ、または保存されていない場合はnull
     */
    fun getApod(date: String): NasaApodData? {
        return settings.getStringOrNull("$KEY_APOD_PREFIX$date")?.let {
            try {
                json.decodeFromString(NasaApodData.serializer(), it)
            } catch (e: Exception) {
                null
            }
        }
    }

    /**
     * APODデータを保存します。
     * 最新データとしても、日付指定のデータとしても保存されます。
     *
     * @param apod 保存するAPODデータ
     */
    fun saveApod(apod: NasaApodData) {
        val serialized = json.encodeToString(NasaApodData.serializer(), apod)
        settings[KEY_LATEST_APOD] = serialized
        settings["$KEY_APOD_PREFIX${apod.date}"] = serialized
    }

    /**
     * 保存されている全てのAPODデータを削除します。
     */
    fun clearCache() {
        settings.keys.filter { it.startsWith(KEY_APOD_PREFIX) || it == KEY_LATEST_APOD }.forEach {
            settings.remove(it)
        }
    }
}