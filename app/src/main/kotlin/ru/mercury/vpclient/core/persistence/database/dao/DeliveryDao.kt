package ru.mercury.vpclient.core.persistence.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.core.DeliveryId
import ru.mercury.vpclient.core.persistence.database.entity.DeliveryEntity
import ru.mercury.vpclient.core.persistence.database.pojo.DeliveryPojo
import ru.mercury.vpclient.core.persistence.database.pojo.PaymentPojo

@Dao
interface DeliveryDao {

    @Transaction
    @Query("SELECT * FROM deliveries WHERE deliveryId = :deliveryId")
    fun selectFlow(deliveryId: DeliveryId): Flow<DeliveryPojo>

    @Transaction
    @Query("SELECT * FROM deliveries WHERE deliveryId = :deliveryId")
    fun selectPaymentPojoFlow(deliveryId: DeliveryId): Flow<PaymentPojo>

    @Query("SELECT * FROM deliveries WHERE deliveryId = :deliveryId")
    suspend fun select(deliveryId: DeliveryId): DeliveryEntity

    @Transaction
    @Query("SELECT * FROM deliveries WHERE deliveryId = :deliveryId")
    suspend fun selectPojo(deliveryId: DeliveryId): DeliveryPojo

    @Transaction
    @Query("SELECT * FROM deliveries WHERE deliveryId = :deliveryId")
    suspend fun selectPaymentPojo(deliveryId: DeliveryId): PaymentPojo

    @Query("SELECT * FROM deliveries WHERE parentDeliveryId = :parentId")
    suspend fun selectChildren(parentId: DeliveryId): List<DeliveryEntity>

    @Query("SELECT * FROM deliveries")
    suspend fun selectAll(): List<DeliveryEntity>

    @Transaction
    @Query("SELECT * FROM deliveries WHERE isParent = 1")
    suspend fun selectParentAll(): List<DeliveryPojo>

    @Transaction
    @Query("SELECT * FROM deliveries")
    suspend fun selectPojoAll(): List<DeliveryPojo>

    @Query("SELECT deliveryId FROM deliveries")
    suspend fun selectIds(): List<DeliveryId>

    @Query("SELECT COALESCE(MAX(position), 0) FROM deliveries")
    suspend fun maxPosition(): Int

    @Update
    suspend fun update(entity: DeliveryEntity)

    @Update
    suspend fun update(entities: List<DeliveryEntity>)

    @Upsert
    suspend fun upsert(entity: DeliveryEntity)

    @Upsert
    suspend fun upsert(entities: List<DeliveryEntity>)

    @Delete
    suspend fun delete(entity: DeliveryEntity)

    @Query("DELETE FROM deliveries")
    suspend fun delete()
}
