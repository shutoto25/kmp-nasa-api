package org.example.project.core.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * クローズ可能なリソースを表すインターフェース。
 */
fun interface Closeable {
    /**
     * リソースをクローズします。
     */
    fun close()
}

/**
 * Flowのラッパークラス。
 * クロスプラットフォームでのFlowの監視を容易にするための機能を提供します。
 *
 * @param T Flowの要素の型
 * @property origin 元のFlowインスタンス
 */
class CFlow<T: Any> internal constructor(private val origin: Flow<T>) : Flow<T> by origin {
    /**
     * Flowの値を監視し、変更があった場合に指定されたブロックを実行します。
     *
     * @param block 値が変更されたときに実行されるブロック
     * @return 監視を停止するためのCloseableインスタンス
     */
    fun watch(block: (T) -> Unit): Closeable {
        val job = Job()

        onEach {
            block(it)
        }.launchIn(CoroutineScope(Dispatchers.Main + job))

        return Closeable { job.cancel() }
    }
}

/**
 * FlowをCFlowに変換する拡張関数。
 *
 * @param T Flowの要素の型
 * @return CFlowインスタンス
 */
internal fun <T: Any> Flow<T>.wrap(): CFlow<T> = CFlow(this)