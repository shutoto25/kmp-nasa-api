package org.example.project.di

import org.example.project.nasa.NasaRepository
import org.example.project.nasa.state.NasaStore
import org.koin.dsl.module

val mainModule = module {
    // 基本依存関係
//    single { Json { ignoreUnknownKeys = true } }
//    single { HttpClient(Android) { /* 設定 */ } }
//
//    // APIクライアント
//    single { NasaApiClient(get(), get()) } // HttpClientとJsonは自動的に解決される
//
//    // Storageとリポジトリ
//    single { NasaStorage(androidContext()) }
    single { NasaRepository(get(), get()) } // ApiClientとStorageは自動解決

    // Store
    single { NasaStore(get()) }
}