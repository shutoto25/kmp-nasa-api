package org.example.project.core.storage

import platform.Foundation.NSUserDefaults

/**
 * NSUserDefaultsのデータを保存
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

/**
 * actual実装
 */
actual fun createPlatformStorage(): PlatformStorage {
    return IOSPlatformStorage()
}