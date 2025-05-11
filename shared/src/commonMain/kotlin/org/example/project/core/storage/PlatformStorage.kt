package org.example.project.core.storage

/**
 * プラットフォーム固有のストレージ操作を定義するインターフェイス
 */
interface PlatformStorage {
    fun getString(key: String): String?
    fun putString(key: String, value: String)
    fun remove(key: String)
    fun clear()
}

expect fun createPlatformStorage(): PlatformStorage