package ru.mercury.vpclient.core.persistence.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.core.persistence.database.entity.PaymentEntity
import ru.mercury.vpclient.core.DeliveryId
import ru.mercury.vpclient.core.PaymentMethodId

@Dao
interface PaymentDao {

    @Query("SELECT * FROM payments WHERE deliveryId = :deliveryId AND paymentMethodId = :paymentMethodId")
    fun selectFlow(deliveryId: DeliveryId, paymentMethodId: PaymentMethodId): Flow<PaymentEntity>

    @Query("SELECT * FROM payments WHERE deliveryId = :deliveryId AND paymentMethodId = :paymentMethodId")
    suspend fun select(deliveryId: DeliveryId, paymentMethodId: PaymentMethodId): PaymentEntity

    @Query("SELECT * FROM payments WHERE deliveryId = :deliveryId")
    suspend fun selectAll(deliveryId: DeliveryId): List<PaymentEntity>

    @Query("SELECT EXISTS(SELECT 1 FROM payments WHERE deliveryId = :deliveryId)")
    suspend fun isExist(deliveryId: DeliveryId): Boolean

    @Upsert
    suspend fun upsert(entity: PaymentEntity)

    @Update
    suspend fun update(entity: PaymentEntity)

    @Delete
    suspend fun delete(entity: PaymentEntity)

    @Delete
    suspend fun delete(entities: List<PaymentEntity>)

    @Query("DELETE FROM payments WHERE deliveryId = :deliveryId AND paymentMethodId = :paymentMethodId")
    suspend fun remove(deliveryId: DeliveryId, paymentMethodId: PaymentMethodId)

    @Query("DELETE FROM payments WHERE deliveryId = :deliveryId")
    suspend fun remove(deliveryId: DeliveryId)
}
