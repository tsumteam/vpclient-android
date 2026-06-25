package ru.mercury.vpclient.shared.data.persistence.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.shared.data.persistence.database.entity.FittingProductEntity

@Dao
interface FittingProductDao {

    @Query("SELECT * FROM FittingProduct ORDER BY deliveryPosition ASC, position ASC")
    fun selectAllFlow(): Flow<List<FittingProductEntity>>

    @Query("SELECT COALESCE(SUM(quantity * sizeCount), 0) FROM FittingProduct")
    fun countFlow(): Flow<Int>

    @Upsert
    suspend fun upsert(entities: List<FittingProductEntity>)

    @Query("DELETE FROM FittingProduct")
    suspend fun delete()
}
