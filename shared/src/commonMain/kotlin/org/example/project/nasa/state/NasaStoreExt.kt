package org.example.project.nasa.state

import org.example.project.core.util.CFlow
import org.example.project.core.util.wrap

/**
 * NasaStoreの状態を監視するための拡張関数。
 * クロスプラットフォームでの状態監視を容易にします。
 *
 * @return 状態を監視するためのCFlowインスタンス
 */
fun NasaStore.watchState(): CFlow<NasaState> = observeState().wrap()

/**
 * NasaStoreの副作用を監視するための拡張関数。
 * クロスプラットフォームでの副作用監視を容易にします。
 *
 * @return 副作用を監視するためのCFlowインスタンス
 */
fun NasaStore.watchSideEffect(): CFlow<NasaSideEffect> = observeSideEffect().wrap()