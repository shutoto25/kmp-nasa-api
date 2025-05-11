package org.example.project.core.di

import kotlinx.serialization.json.Json
import org.koin.dsl.module

// 両プラットフォー用の共通モジュール設定
val commonModule = module {
    // Json
    single {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
        }
    }
}