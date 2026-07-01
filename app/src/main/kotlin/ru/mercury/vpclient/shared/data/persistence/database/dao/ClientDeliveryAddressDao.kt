package ru.mercury.vpclient.shared.data.persistence.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.shared.data.persistence.database.entity.ClientDeliveryAddressEntity

@Dao
interface ClientDeliveryAddressDao {

    @Query("SELECT * FROM ClientDeliveryAddressEntity ORDER BY id ASC")
    fun selectAllFlow(): Flow<List<ClientDeliveryAddressEntity>>

    @Upsert
    suspend fun upsert(entities: List<ClientDeliveryAddressEntity>)

    @Upsert
    suspend fun upsert(entity: ClientDeliveryAddressEntity)

    @Query("DELETE FROM ClientDeliveryAddressEntity")
    suspend fun delete()

    @Query("DELETE FROM ClientDeliveryAddressEntity WHERE id = :id")
    suspend fun delete(id: Int)
}
