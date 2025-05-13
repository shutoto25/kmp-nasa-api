package org.example.project.nasa.domain.usecase

import org.example.project.core.domain.usecase.NoParamUseCase
import org.example.project.nasa.domain.repository.ApodRepository

/**
 * APODのキャッシュをクリアするユースケース
 *
 * @property repository APODリポジトリ
 */
class ClearApodCacheUseCase(
    private val repository: ApodRepository
) : NoParamUseCase<Unit> {
    
    /**
     * ユースケースを実行します
     */
    override suspend fun invoke() {
        repository.clearCache()
    }
} 