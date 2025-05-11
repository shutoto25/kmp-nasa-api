package org.example.project.core.storage

import platform.Foundation.NSUserDefaults

/**
 * iOS用のPlatformStorageの実装。
 * NSUserDefaultsを使用してデータを保存します。
 */
class IOSPlatformStorage : PlatformStorage {
    private val userDefaults = NSUserDefaults.standardUserDefaults

    override fun getString(key: String): String? {
        return userDefaults.stringForKey(key)
    }

    override fun putString(key: String, value: String) {
        userDefaults.setObject(value, key)
    }

    override fun remove(key: String) {
        userDefaults.removeObjectForKey(key)
    }

    override fun clear() {
        // UserDefaultsの全キーを取得
        val allKeys = userDefaults.dictionaryRepresentation().keys

        // アプリ固有のキー（プレフィックスが"key_"のもの）のみをクリア
        allKeys.forEach { key ->
            val keyString = key as String
            if (keyString.startsWith("key_")) {
                userDefaults.removeObjectForKey(keyString)
            }
        }
    }
}