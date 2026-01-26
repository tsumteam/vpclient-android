package ru.mercury.vpclient.core.persistence.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.mercury.vpclient.core.persistence.database.entity.PaymentMethodEntity
import ru.mercury.vpclient.core.DeliveryId
import ru.mercury.vpclient.core.PaymentMethodId

@Dao
interface PaymentMethodDao {

    @Query("SELECT EXISTS(SELECT 1 FROM paymentMethods WHERE deliveryId = :deliveryId)")
    suspend fun isExist(deliveryId: DeliveryId): Boolean

    @Query("SELECT * FROM paymentMethods WHERE deliveryId = :deliveryId AND paymentMethodId = :paymentMethodId")
    suspend fun select(deliveryId: DeliveryId, paymentMethodId: PaymentMethodId): PaymentMethodEntity

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entities: List<PaymentMethodEntity>)

    @Query("DELETE FROM paymentMethods WHERE deliveryId = :deliveryId")
    suspend fun remove(deliveryId: DeliveryId)
}
