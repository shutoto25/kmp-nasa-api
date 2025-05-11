package org.example.project.di

import org.example.project.core.di.nasaModule
import org.example.project.core.storage.IOSPlatformStorage
import org.example.project.core.storage.PlatformStorage
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

// iOS向けのKoin初期化関数
fun initKoinIos(): KoinApplication {
    // 既存のKoinを停止（iOS側では複数回初期化される可能性がある）
    stopKoin()

    return startKoin {
        modules(
            nasaModule,
            // iOS固有のモジュールを必要に応じて追加
            iosPlatformModule
        )
    }
}

// iOS固有の実装を提供するモジュール
val iosPlatformModule = module {
    single<PlatformStorage> { IOSPlatformStorage() }
}