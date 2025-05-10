package org.example.project.core.storage

import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

abstract class BaseStorage(
    protected val settings: Settings,
    protected val json: Json
) {
    // 共通のストレージメソッド
    protected fun <T> getObject(key: String, serializer: KSerializer<T>): T? {
        return settings.getStringOrNull(key)?.let {
            try {
                json.decodeFromString(serializer, it)
            } catch (e: Exception) {
                // デコードエラーの場合はnullを返す
                null
            }
        }
    }

    protected fun <T> saveObject(key: String, value: T, serializer: KSerializer<T>) {
        val serialized = json.encodeToString(serializer, value)
        settings[key] = serialized
    }

    protected fun removeObject(key: String) {
        settings.remove(key)
    }

    // キーのプレフィックスに基づいて全てのオブジェクトを削除
    protected fun removeAllWithPrefix(prefix: String) {
        settings.keys.filter { it.startsWith(prefix) }.forEach {
            settings.remove(it)
        }
    }
}