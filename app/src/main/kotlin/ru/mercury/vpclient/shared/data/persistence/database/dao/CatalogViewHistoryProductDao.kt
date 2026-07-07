package ru.mercury.vpclient.shared.data.persistence.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogViewHistoryProductEntity

@Dao
interface CatalogViewHistoryProductDao {

    @Query(
        """
        SELECT
            0 AS categoryId,
            0 AS titleCategoryId,
            id,
            itemId,
            colorId,
            name,
            price,
            priceWithoutDiscount,
            brand,
            urlBrandLogo,
            imageUrl,
            imageUrls,
            additionalColorPhotoUrls,
            position,
            actionLabels,
            NULL AS lookActionPrice,
            NULL AS lookActionPriceWithoutDiscount,
            NULL AS lookActionName,
            NULL AS lookActionDiscountPercentage,
            NULL AS availableSizes,
            0 AS isOneSize
        FROM CatalogViewHistoryProducts
        ORDER BY position ASC
        """
    )
    fun selectFlow(): Flow<List<CatalogFilterProductsEntity>>

    @Query(
        """
        SELECT
            0 AS categoryId,
            0 AS titleCategoryId,
            id,
            itemId,
            colorId,
            name,
            price,
            priceWithoutDiscount,
            brand,
            urlBrandLogo,
            imageUrl,
            imageUrls,
            additionalColorPhotoUrls,
            position,
            actionLabels,
            NULL AS lookActionPrice,
            NULL AS lookActionPriceWithoutDiscount,
            NULL AS lookActionName,
            NULL AS lookActionDiscountPercentage,
            NULL AS availableSizes,
            0 AS isOneSize
        FROM CatalogViewHistoryProducts
        ORDER BY position ASC
        """
    )
    fun pagingSource(): PagingSource<Int, CatalogFilterProductsEntity>

    @Query("SELECT * FROM CatalogViewHistoryProducts WHERE id = :id LIMIT 1")
    suspend fun select(id: String): CatalogViewHistoryProductEntity?

    @Query(
        """
        SELECT
            0 AS categoryId,
            0 AS titleCategoryId,
            id,
            itemId,
            colorId,
            name,
            price,
            priceWithoutDiscount,
            brand,
            urlBrandLogo,
            imageUrl,
            imageUrls,
            additionalColorPhotoUrls,
            position,
            actionLabels,
            NULL AS lookActionPrice,
            NULL AS lookActionPriceWithoutDiscount,
            NULL AS lookActionName,
            NULL AS lookActionDiscountPercentage,
            NULL AS availableSizes,
            0 AS isOneSize
        FROM CatalogViewHistoryProducts
        WHERE id = :id
        LIMIT 1
        """
    )
    suspend fun selectCatalogProduct(id: String): CatalogFilterProductsEntity?

    @Transaction
    suspend fun replace(entities: List<CatalogViewHistoryProductEntity>) {
        delete()
        if (entities.isNotEmpty()) {
            upsert(entities)
        }
    }

    @Upsert
    suspend fun upsert(entities: List<CatalogViewHistoryProductEntity>)

    @Query("DELETE FROM CatalogViewHistoryProducts")
    suspend fun delete()
}
