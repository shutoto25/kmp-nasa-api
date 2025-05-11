package org.example.project.core.di

import android.content.Context
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import io.ktor.client.*
import org.example.project.core.storage.AndroidPlatformStorage
import org.example.project.core.storage.PlatformStorage
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Android固有のモジュール設定
 */
actual fun platformModule(): Module = module {
    // PlatformStorage
    single<PlatformStorage> { AndroidPlatformStorage(get()) }
    // HttpClient
    single { HttpClient() }
    // SharedPreferences
    single<Settings> {
        val context: Context = get()
        SharedPreferencesSettings(
            context.getSharedPreferences("app_storage", Context.MODE_PRIVATE)
        )
    }
}