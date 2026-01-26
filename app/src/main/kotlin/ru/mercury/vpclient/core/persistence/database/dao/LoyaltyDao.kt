package ru.mercury.vpclient.core.persistence.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import ru.mercury.vpclient.core.persistence.database.entity.LoyaltyEntity
import ru.mercury.vpclient.core.DeliveryId

@Dao
interface LoyaltyDao {

    @Query("SELECT * FROM loyalty WHERE deliveryId = :deliveryId")
    suspend fun select(deliveryId: DeliveryId): LoyaltyEntity?

    @Query("SELECT * FROM loyalty WHERE deliveryId = :deliveryId")
    suspend fun selectNotNull(deliveryId: DeliveryId): LoyaltyEntity

    @Upsert
    suspend fun upsert(entity: LoyaltyEntity)

    @Update
    suspend fun update(entity: LoyaltyEntity)

    @Query("DELETE FROM loyalty WHERE deliveryId = :deliveryId")
    suspend fun remove(deliveryId: DeliveryId)
}
