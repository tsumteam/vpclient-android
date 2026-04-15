package ru.mercury.vpclient.shared.data.persistence.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import ru.mercury.vpclient.shared.data.persistence.database.entity.PagingKeyEntity

@Dao
interface PagingKeyDao {

    @Query("SELECT * FROM PagingKey WHERE categoryId = :categoryId AND titleCategoryId = :titleCategoryId LIMIT 1")
    suspend fun select(categoryId: Int, titleCategoryId: Int): PagingKeyEntity?

    @Upsert
    suspend fun upsert(entity: PagingKeyEntity)

    @Query("DELETE FROM PagingKey WHERE categoryId = :categoryId AND titleCategoryId = :titleCategoryId")
    suspend fun remove(categoryId: Int, titleCategoryId: Int)
}
