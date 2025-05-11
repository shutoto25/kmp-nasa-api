package org.example.project.core.di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Koin初期化関数
 */
fun initKoin(): KoinApplication {
    stopKoin()
    return startKoin {
        modules(getAllModules())
    }
}

/**
 * すべてのモジュールをまとめる関数
 */
fun getAllModules(): List<Module> {
    return listOf(
        commonModule,
        nasaModule,
        platformModule()
    )
}

/**
 * プラットフォーム固有の初期化
 */
expect fun initPlatformKoin()