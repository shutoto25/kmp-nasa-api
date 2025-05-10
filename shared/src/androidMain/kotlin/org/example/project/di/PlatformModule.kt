package org.example.project.di

import android.content.Context
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import org.koin.dsl.module

// プラットフォーム固有のモジュール設定を公開
fun platformModule(context: Context) = module {
    // Android固有のSettings実装
    single<Settings> {
        SharedPreferencesSettings(
            context.getSharedPreferences("nasa_storage", Context.MODE_PRIVATE)
        )
    }
}