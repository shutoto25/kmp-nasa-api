package org.example.project.core.storage

import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

/**
 * データの永続化のための基底クラス。
 * 共通のストレージ操作を提供します。
 *
 * @property settings 設定の保存に使用するSettingsインスタンス
 * @property json JSONシリアライズ/デシリアライズのためのJsonインスタンス
 */
abstract class BaseStorage(
    protected val settings: Settings,
    protected val json: Json
) {
    /**
     * 指定されたキーに関連付けられたオブジェクトを取得します。
     *
     * @param key オブジェクトを取得するためのキー
     * @param serializer オブジェクトのデシリアライズに使用するシリアライザー
     * @return デシリアライズされたオブジェクト、または存在しない場合はnull
     */
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
    
    /**
     * オブジェクトを指定されたキーで保存します。
     *
     * @param key オブジェクトを保存するためのキー
     * @param value 保存するオブジェクト
     * @param serializer オブジェクトのシリアライズに使用するシリアライザー
     */
    protected fun <T> saveObject(key: String, value: T, serializer: KSerializer<T>) {
        val serialized = json.encodeToString(serializer, value)
        settings[key] = serialized
    }
    
    /**
     * 指定されたキーに関連付けられたオブジェクトを削除します。
     *
     * @param key 削除するオブジェクトのキー
     */
    protected fun removeObject(key: String) {
        settings.remove(key)
    }
    
    /**
     * 指定されたプレフィックスを持つ全てのオブジェクトを削除します。
     *
     * @param prefix 削除するオブジェクトのキーのプレフィックス
     */
    protected fun removeAllWithPrefix(prefix: String) {
        settings.keys.filter { it.startsWith(prefix) }.forEach {
            settings.remove(it)
        }
    }
}