package ru.mercury.vpclient.shared.data.persistence.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsQuantityEntity

@Dao
interface CatalogFilterProductsQuantityDao {

    @Query("SELECT * FROM CatalogFilterProductsQuantity WHERE categoryId = :categoryId AND titleCategoryId = :titleCategoryId AND searchText = :searchText LIMIT 1")
    fun selectFlow(categoryId: Int, titleCategoryId: Int, searchText: String): Flow<CatalogFilterProductsQuantityEntity?>

    @Upsert
    suspend fun upsert(entity: CatalogFilterProductsQuantityEntity)

    @Query("DELETE FROM CatalogFilterProductsQuantity WHERE categoryId = :categoryId AND titleCategoryId = :titleCategoryId AND searchText = :searchText")
    suspend fun delete(categoryId: Int, titleCategoryId: Int, searchText: String)
}
