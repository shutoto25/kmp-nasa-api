package org.example.project.nasa.data.source.local

import org.example.project.nasa.domain.model.ApodEntity

/**
 * APOD用のローカルデータソースインターフェース
 */
interface ApodLocalDataSource {
    /**
     * 指定した日付のAPODをローカルから取得します
     *
     * @param date 取得する日付
     * @return APODエンティティ
     */
    suspend fun getApod(date: String): ApodEntity?
    
    /**
     * 最新のAPODをローカルから取得します
     *
     * @return APODエンティティ
     */
    suspend fun getLatestApod(): ApodEntity?
    
    /**
     * APODをローカルに保存します
     *
     * @param apod 保存するAPODエンティティ
     */
    suspend fun saveApod(apod: ApodEntity)
    
    /**
     * キャッシュをクリアします
     */
    suspend fun clearCache()
} 