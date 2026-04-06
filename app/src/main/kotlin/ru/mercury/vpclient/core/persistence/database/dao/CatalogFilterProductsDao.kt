package ru.mercury.vpclient.core.persistence.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.core.persistence.database.entity.CatalogFilterProductsEntity

@Dao
interface CatalogFilterProductsDao {

    @Transaction
    @Query("SELECT * FROM CatalogFilterProducts WHERE categoryId = :categoryId AND titleCategoryId = :titleCategoryId ORDER BY position ASC")
    fun pagingSource(categoryId: Int, titleCategoryId: Int): PagingSource<Int, CatalogFilterProductsEntity>

    @Query("SELECT * FROM CatalogFilterProducts WHERE id = :id LIMIT 1")
    fun selectFlow(id: String): Flow<CatalogFilterProductsEntity>

    @Query("SELECT * FROM CatalogFilterProducts WHERE id = :id LIMIT 1")
    suspend fun select(id: String): CatalogFilterProductsEntity?

    @Upsert
    suspend fun upsert(entities: List<CatalogFilterProductsEntity>)

    @Query("DELETE FROM CatalogFilterProducts WHERE categoryId = :categoryId AND titleCategoryId = :titleCategoryId")
    suspend fun remove(categoryId: Int, titleCategoryId: Int)
}
