package org.example.project.nasa.domain.usecase

import org.example.project.core.domain.usecase.UseCase
import org.example.project.nasa.domain.model.ApodEntity
import org.example.project.nasa.domain.repository.ApodRepository

/**
 * Astronomy Picture of the Day (APOD)を取得するユースケース
 *
 * @property repository APODリポジトリ
 */
class GetApodUseCase(
    private val repository: ApodRepository
) : UseCase<GetApodUseCase.Params, ApodEntity> {
    
    /**
     * ユースケースを実行します
     *
     * @param params 入力パラメータ
     * @return APODエンティティ
     */
    override suspend fun invoke(params: Params): ApodEntity {
        return repository.getApod(params.date, params.forceUpdate)
    }
    
    /**
     * ユースケースのパラメータ
     *
     * @property date 取得する日付（nullの場合は最新のデータを取得）
     * @property forceUpdate キャッシュを無視して強制的に更新するかどうか
     */
    data class Params(
        val date: String? = null,
        val forceUpdate: Boolean = false
    )
} 