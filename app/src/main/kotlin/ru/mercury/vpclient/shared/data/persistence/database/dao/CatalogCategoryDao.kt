package ru.mercury.vpclient.shared.data.persistence.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogCategoryEntity
import ru.mercury.vpclient.shared.data.persistence.database.pojo.SubcategoryPojo

@Dao
interface CatalogCategoryDao {

    @Query("SELECT * FROM CatalogCategory WHERE id = :id LIMIT 1")
    fun selectFlow(id: Int): Flow<CatalogCategoryEntity>

    @Query("SELECT * FROM CatalogCategory")
    fun selectAllFlow(): Flow<List<CatalogCategoryEntity>>

    @Transaction
    @Query("SELECT * FROM CatalogCategory WHERE parentId = :parentId")
    fun selectPojosFlow(parentId: Int): Flow<List<SubcategoryPojo>>

    @Query("SELECT * FROM CatalogCategory WHERE id = :id LIMIT 1")
    suspend fun select(id: Int): CatalogCategoryEntity

    @Query("SELECT * FROM CatalogCategory WHERE id = :id LIMIT 1")
    suspend fun selectOrNull(id: Int): CatalogCategoryEntity?

    @Upsert
    suspend fun upsert(entities: List<CatalogCategoryEntity>)
}
