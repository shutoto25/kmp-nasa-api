package org.example.project.di

import org.example.project.core.storage.PlatformStorage
import org.example.project.nasa.state.NasaStore
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

// iOSからアクセスするためのヘルパークラス
class IosAppDependencies : KoinComponent {
    // 必要なコンポーネントをプロパティとして公開
    val nasaStore: NasaStore = get()
    val platformStorage: PlatformStorage = get()

    companion object {
        fun create(): IosAppDependencies {
            // Koinを初期化し、依存関係を返す
            initKoinIos()
            return IosAppDependencies()
        }
    }
}