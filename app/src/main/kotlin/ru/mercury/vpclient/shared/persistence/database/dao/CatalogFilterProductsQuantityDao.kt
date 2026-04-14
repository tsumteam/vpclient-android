package ru.mercury.vpclient.shared.persistence.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.shared.persistence.database.entity.CatalogFilterProductsQuantityEntity

@Dao
interface CatalogFilterProductsQuantityDao {

    @Query("SELECT * FROM CatalogFilterProductsQuantity WHERE categoryId = :categoryId AND titleCategoryId = :titleCategoryId LIMIT 1")
    fun selectFlow(categoryId: Int, titleCategoryId: Int): Flow<CatalogFilterProductsQuantityEntity?>

    @Upsert
    suspend fun upsert(entity: CatalogFilterProductsQuantityEntity)
}
