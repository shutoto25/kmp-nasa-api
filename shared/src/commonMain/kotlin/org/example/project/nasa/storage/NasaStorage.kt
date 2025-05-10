package org.example.project.nasa.storage

import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import kotlinx.serialization.json.Json
import org.example.project.nasa.data.NasaApodData

class NasaStorage(
    private val settings: Settings,
    private val json: Json
) {
    private companion object {
        private const val KEY_LATEST_APOD = "key_latest_apod"
        private const val KEY_APOD_PREFIX = "key_apod_"
    }

    fun getLatestApod(): NasaApodData? {
        return settings.getStringOrNull(KEY_LATEST_APOD)?.let {
            try {
                json.decodeFromString(NasaApodData.serializer(), it)
            } catch (e: Exception) {
                null
            }
        }
    }

    fun getApod(date: String): NasaApodData? {
        return settings.getStringOrNull("$KEY_APOD_PREFIX$date")?.let {
            try {
                json.decodeFromString(NasaApodData.serializer(), it)
            } catch (e: Exception) {
                null
            }
        }
    }

    fun saveApod(apod: NasaApodData) {
        val serialized = json.encodeToString(NasaApodData.serializer(), apod)
        settings[KEY_LATEST_APOD] = serialized
        settings["$KEY_APOD_PREFIX${apod.date}"] = serialized
    }

    fun clearCache() {
        settings.keys.filter { it.startsWith(KEY_APOD_PREFIX) || it == KEY_LATEST_APOD }.forEach {
            settings.remove(it)
        }
    }
}