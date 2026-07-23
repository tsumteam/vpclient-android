package ru.mercury.vpclient.shared.data.persistence.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterEntity

@Dao
interface CatalogFilterDao {

    @Query("SELECT * FROM CatalogFilter WHERE categoryId = :categoryId AND titleCategoryId = :titleCategoryId AND searchText = :searchText LIMIT 1")
    fun selectFlow(categoryId: Int, titleCategoryId: Int, searchText: String): Flow<CatalogFilterEntity?>

    @Query("SELECT * FROM CatalogFilter WHERE categoryId = :categoryId AND titleCategoryId = :titleCategoryId AND searchText = :searchText LIMIT 1")
    suspend fun select(categoryId: Int, titleCategoryId: Int, searchText: String): CatalogFilterEntity?

    @Upsert
    suspend fun upsert(entity: CatalogFilterEntity)

    @Query("DELETE FROM CatalogFilter WHERE categoryId = :categoryId AND titleCategoryId = :titleCategoryId AND searchText = :searchText")
    suspend fun delete(categoryId: Int, titleCategoryId: Int, searchText: String)
}
