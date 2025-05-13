package org.example.project.core.domain.usecase

/**
 * ユースケースの基本インターフェース
 *
 * @param P パラメータの型
 * @param R 結果の型
 */
interface UseCase<in P, out R> {
    /**
     * ユースケースを実行します
     *
     * @param params 入力パラメータ
     * @return 結果
     */
    suspend operator fun invoke(params: P): R
}

/**
 * パラメータなしのユースケースの基本インターフェース
 *
 * @param R 結果の型
 */
interface NoParamUseCase<out R> {
    /**
     * ユースケースを実行します
     *
     * @return 結果
     */
    suspend operator fun invoke(): R
} 