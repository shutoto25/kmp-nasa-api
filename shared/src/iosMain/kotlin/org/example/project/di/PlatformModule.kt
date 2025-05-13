package org.example.project.di

import org.example.project.core.storage.IOSPlatformStorage
import org.example.project.core.storage.PlatformStorage
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * iOS用のプラットフォーム固有の依存性を提供するモジュール
 */
actual val AppModule.platformModule: Module
    get() = module {
        single<PlatformStorage> { IOSPlatformStorage() }
    } 