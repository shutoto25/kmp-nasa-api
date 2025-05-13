package org.example.project

import android.app.Application
import org.example.project.di.AppModule
import org.koin.android.ext.koin.androidContext

/**
 * アプリケーションクラス
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        // Koinの初期化
        AppModule.init {
            androidContext(this@App)
        }
    }
}