package org.example.project.di

import android.content.Context
import org.example.project.core.storage.PlatformStorage
import androidx.core.content.edit


class AndroidPlatformStorage(private val context: Context) : PlatformStorage {
    private val prefs = context.getSharedPreferences("nasa_storage", Context.MODE_PRIVATE)

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