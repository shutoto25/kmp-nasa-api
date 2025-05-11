package org.example.project.core.di

import android.content.Context
import org.koin.android.ext.koin.androidContext

/**
 * Android版の初期化はApplicationクラスで行うため空実装
 */
actual fun initPlatformKoin() {
    // Android側では別途App.ktで初期化を行う
}

/**
 * Application.onCreateから呼び出すための関数
 */
fun initKoinAndroid(appContext: Context) {
    // Koinの初期化とAndroidのContext登録を同時に行う
    initKoin().androidContext(appContext)

    // platformModule()は既にinitKoin()内で登録済みなので
    // 追加で何かをロードする必要はない
}