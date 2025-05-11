package org.example.project.core.storage

import android.content.Context
import androidx.core.content.edit


class AndroidPlatformStorage(private val context: Context) : PlatformStorage {
    private val prefs = context.getSharedPreferences("app_storage", Context.MODE_PRIVATE)

    override fun getString(key: String): String? {
        return prefs.getString(key, null)
    }

    override fun putString(key: String, value: String) {
        prefs.edit { putString(key, value) }
    }

    override fun remove(key: String) {
        prefs.edit { remove(key) }
    }

    override fun clear() {
        prefs.edit { clear() }
    }
}

/**
 * Android環境では実際に呼ばれないが、expect/actual規約を満たすために提供
 */
actual fun createPlatformStorage(): PlatformStorage {
    error("Android環境ではこのメソッドは使用されません。代わりにKoinのDIを使用してください。")
}