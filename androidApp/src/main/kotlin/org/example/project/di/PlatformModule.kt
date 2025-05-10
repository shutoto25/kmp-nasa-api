package org.example.project.di

import org.koin.dsl.module
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import org.koin.android.ext.koin.androidContext

val platformModule = module {
    // Android固有のSettings実装
    single<Settings> {
        SharedPreferencesSettings(
            androidContext().getSharedPreferences("nasa_storage", android.content.Context.MODE_PRIVATE)
        )
    }
}