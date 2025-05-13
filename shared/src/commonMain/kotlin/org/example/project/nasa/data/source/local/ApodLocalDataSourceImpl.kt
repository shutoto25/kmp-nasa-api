package org.example.project.nasa.data.source.local

import kotlinx.serialization.json.Json
import org.example.project.core.storage.PlatformStorage
import org.example.project.nasa.data.model.ApodDto
import org.example.project.nasa.data.model.toEntity
import org.example.project.nasa.domain.model.ApodEntity

/**
 * APODローカルデータソースの実装クラス
 *
 * @property platformStorage プラットフォーム固有のストレージ実装
 * @property json JSONパーサー
 */
class ApodLocalDataSourceImpl(
    private val platformStorage: PlatformStorage,
    private val json: Json
) : ApodLocalDataSource {
    
    companion object {
        private const val KEY_LATEST_APOD = "key_latest_apod"
        private const val KEY_APOD_PREFIX = "key_apod_"
    }
    
    /**
     * 指定した日付のAPODをローカルから取得します
     *
     * @param date 取得する日付
     * @return APODエンティティ
     */
    override suspend fun getApod(date: String): ApodEntity? {
        return platformStorage.getString("$KEY_APOD_PREFIX$date")?.let {
            try {
                val dto = json.decodeFromString(ApodDto.serializer(), it)
                dto.toEntity()
            } catch (e: Exception) {
                null
            }
        }
    }
    
    /**
     * 最新のAPODをローカルから取得します
     *
     * @return APODエンティティ
     */
    override suspend fun getLatestApod(): ApodEntity? {
        return platformStorage.getString(KEY_LATEST_APOD)?.let {
            try {
                val dto = json.decodeFromString(ApodDto.serializer(), it)
                dto.toEntity()
            } catch (e: Exception) {
                null
            }
        }
    }
    
    /**
     * APODをローカルに保存します
     *
     * @param apod 保存するAPODエンティティ
     */
    override suspend fun saveApod(apod: ApodEntity) {
        val dto = org.example.project.nasa.data.model.toDto(apod)
        val serialized = json.encodeToString(ApodDto.serializer(), dto)
        platformStorage.putString(KEY_LATEST_APOD, serialized)
        platformStorage.putString("$KEY_APOD_PREFIX${apod.date}", serialized)
    }
    
    /**
     * キャッシュをクリアします
     */
    override suspend fun clearCache() {
        platformStorage.clear()
    }
} 