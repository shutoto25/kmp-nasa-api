package org.example.project.di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import kotlinx.serialization.json.Json
import org.example.project.nasa.data.repository.ApodRepositoryImpl
import org.example.project.nasa.data.source.local.ApodLocalDataSource
import org.example.project.nasa.data.source.local.ApodLocalDataSourceImpl
import org.example.project.nasa.data.source.remote.ApodRemoteDataSource
import org.example.project.nasa.data.source.remote.ApodRemoteDataSourceImpl
import org.example.project.nasa.domain.repository.ApodRepository
import org.example.project.nasa.domain.usecase.ClearApodCacheUseCase
import org.example.project.nasa.domain.usecase.GetApodUseCase
import org.example.project.nasa.presentation.ApodViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

/**
 * アプリケーション全体の依存性注入の設定を行うクラス
 */
object AppModule {
    /**
     * Koinの初期化を行います
     *
     * @param appDeclaration アプリケーション固有の設定
     */
    fun init(appDeclaration: KoinAppDeclaration = {}) {
        startKoin {
            appDeclaration()
            modules(
                platformModule,
                coreModule,
                nasaModule
            )
        }
    }
    
    /**
     * プラットフォーム固有のモジュール
     * 実際の実装はプラットフォーム固有のソースで定義されます
     */
    expect val platformModule: Module
    
    /**
     * コア機能のモジュール
     */
    val coreModule = module {
        // JSON Parser
        single {
            Json {
                isLenient = true
                ignoreUnknownKeys = true
                useAlternativeNames = false
            }
        }
        
        // HTTP Client
        single {
            HttpClient {
                install(Logging) {
                    logger = object : Logger {
                        override fun log(message: String) {
                            println("HTTP Client: $message")
                        }
                    }
                    level = LogLevel.ALL
                }
            }
        }
    }
    
    /**
     * NASA機能のモジュール
     */
    val nasaModule = module {
        // Data Sources
        factory<ApodRemoteDataSource> { ApodRemoteDataSourceImpl(get(), get()) }
        factory<ApodLocalDataSource> { ApodLocalDataSourceImpl(get(), get()) }
        
        // Repository
        factory<ApodRepository> { ApodRepositoryImpl(get(), get()) }
        
        // Use Cases
        factory { GetApodUseCase(get()) }
        factory { ClearApodCacheUseCase(get()) }
        
        // ViewModel
        factory { ApodViewModel(get(), get()) }
    }
} 