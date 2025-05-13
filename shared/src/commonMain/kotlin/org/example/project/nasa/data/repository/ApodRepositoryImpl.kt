package org.example.project.nasa.data.repository

import org.example.project.nasa.data.source.local.ApodLocalDataSource
import org.example.project.nasa.data.source.remote.ApodRemoteDataSource
import org.example.project.nasa.domain.model.ApodEntity
import org.example.project.nasa.domain.repository.ApodRepository

/**
 * APODリポジトリの実装クラス
 *
 * @property remoteDataSource リモートデータソース
 * @property localDataSource ローカルデータソース
 */
class ApodRepositoryImpl(
    private val remoteDataSource: ApodRemoteDataSource,
    private val localDataSource: ApodLocalDataSource
) : ApodRepository {
    
    /**
     * 指定した日付のAstronomy Picture of the Dayを取得します
     *
     * @param date 取得する日付（nullの場合は最新のデータを取得）
     * @param forceUpdate キャッシュを無視して強制的に更新するかどうか
     * @return APODのエンティティ
     */
    override suspend fun getApod(date: String?, forceUpdate: Boolean): ApodEntity {
        // キャッシュからデータを取得
        val cached = if (date != null) {
            localDataSource.getApod(date)
        } else {
            localDataSource.getLatestApod()
        }
        
        // 強制更新または、キャッシュがない場合は新しいデータを取得
        return if (forceUpdate || cached == null) {
            try {
                val fresh = remoteDataSource.getApod(date)
                localDataSource.saveApod(fresh)
                fresh
            } catch (e: Exception) {
                if (cached != null) {
                    // APIエラーが発生したがキャッシュが利用可能な場合はキャッシュを使用
                    cached
                } else {
                    // キャッシュもない場合は例外を再スロー
                    throw e
                }
            }
        } else {
            cached
        }
    }
    
    /**
     * キャッシュをクリアします
     */
    override suspend fun clearCache() {
        localDataSource.clearCache()
    }
} 