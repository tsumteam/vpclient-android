package ru.mercury.vpclient.shared.data.persistence.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.shared.data.persistence.database.entity.ProductEntity

@Dao
interface ProductDao {

    @Query("SELECT * FROM Product WHERE id = :id LIMIT 1")
    fun selectFlow(id: String): Flow<ProductEntity?>

    @Upsert
    suspend fun upsert(entity: ProductEntity)
}
