package org.example.project.nasa.data.source.remote

import org.example.project.nasa.domain.model.ApodEntity

/**
 * APOD用のリモートデータソースインターフェース
 */
interface ApodRemoteDataSource {
    /**
     * APIから指定した日付のAPODを取得します
     *
     * @param date 取得する日付（nullの場合は最新のデータを取得）
     * @return APODエンティティ
     */
    suspend fun getApod(date: String? = null): ApodEntity
} 