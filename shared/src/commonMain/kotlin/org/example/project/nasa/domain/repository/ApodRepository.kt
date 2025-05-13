package org.example.project.nasa.domain.repository

import org.example.project.core.data.repository.Repository
import org.example.project.nasa.domain.model.ApodEntity

/**
 * Astronomy Picture of the Day (APOD)のリポジトリインターフェース
 */
interface ApodRepository : Repository {
    /**
     * 指定した日付のAstronomy Picture of the Dayを取得します
     *
     * @param date 取得する日付（nullの場合は最新のデータを取得）
     * @param forceUpdate キャッシュを無視して強制的に更新するかどうか
     * @return APODのエンティティ
     */
    suspend fun getApod(date: String? = null, forceUpdate: Boolean = false): ApodEntity
    
    /**
     * キャッシュをクリアします
     */
    suspend fun clearCache()
} 