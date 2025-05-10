package org.example.project.di

import io.ktor.client.*
import kotlinx.serialization.json.Json
import org.example.project.core.storage.PlatformStorage
import org.example.project.nasa.NasaApiClient
import org.example.project.nasa.NasaRepository
import org.example.project.nasa.state.NasaStore
import org.example.project.nasa.storage.NasaStorage
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val mainModule = module {
    // 基本依存関係
    single {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
        }
    }
    single { HttpClient() }

    // プラットフォーム固有のストレージ
    single<PlatformStorage> { AndroidPlatformStorage(androidContext()) }

    // NASAデータ関連の依存関係
    single { NasaApiClient(get(), get()) }
    single { NasaStorage(get(), get()) } // Settingsを注入
    single { NasaRepository(get(), get()) }
    single { NasaStore(get()) }
}