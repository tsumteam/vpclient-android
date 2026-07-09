package ru.mercury.vpclient.shared.data.persistence.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogBrandEntity

@Dao
interface CatalogBrandDao {

    @Query("SELECT * FROM CatalogBrand WHERE pairedUserId = :pairedUserId AND categoryId = :categoryId ORDER BY name COLLATE NOCASE")
    fun selectByCategoryFlow(pairedUserId: String, categoryId: Int): Flow<List<CatalogBrandEntity>>

    @Upsert
    suspend fun upsert(entities: List<CatalogBrandEntity>)

    @Query("UPDATE CatalogBrand SET isFavorite = :isFavorite WHERE pairedUserId = :pairedUserId AND categoryId = :categoryId AND brandId IN (:brandIds)")
    suspend fun updateFavorite(pairedUserId: String, categoryId: Int, brandIds: Set<Int>, isFavorite: Boolean)

    @Query("DELETE FROM CatalogBrand WHERE pairedUserId = :pairedUserId")
    suspend fun delete(pairedUserId: String)
}
