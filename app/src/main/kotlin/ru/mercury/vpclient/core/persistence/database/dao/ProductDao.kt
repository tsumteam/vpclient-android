package ru.mercury.vpclient.core.persistence.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.core.DeliveryId
import ru.mercury.vpclient.core.ProductLineId
import ru.mercury.vpclient.core.network.response.ProductResponse
import ru.mercury.vpclient.core.persistence.database.entity.ProductEntity

@Dao
interface ProductDao {

    @Query("SELECT * FROM products WHERE deliveryId = :deliveryId ORDER BY position ASC")
    fun deliveryProductsFlow(deliveryId: DeliveryId): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE deliveryId = :deliveryId AND status = :status AND isToPay = 0 ORDER BY position ASC")
    fun deliveryProductsUnpaymentFlow(deliveryId: DeliveryId, status: String): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE deliveryId = :deliveryId AND status = :status ORDER BY position ASC")
    fun deliveryProductsFlow(deliveryId: DeliveryId, status: String): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE deliveryId = :deliveryId AND lineId = :productLineId")
    fun selectFlow(deliveryId: DeliveryId, productLineId: ProductLineId): Flow<ProductEntity?>

    @Query("SELECT * FROM products WHERE deliveryId = :deliveryId AND barcode = :barcode")
    suspend fun productByBarcode(deliveryId: DeliveryId, barcode: String): ProductEntity?

    @Query("SELECT * FROM products WHERE deliveryId = :deliveryId ORDER BY position ASC")
    suspend fun selectAll(deliveryId: DeliveryId): List<ProductEntity>

    @Query("SELECT * FROM products WHERE deliveryId = :deliveryId AND lineId IN (:productLineIds) ORDER BY position ASC")
    suspend fun selectAll(deliveryId: DeliveryId, productLineIds: List<ProductLineId>): List<ProductEntity>

    @Query("SELECT * FROM products WHERE deliveryId = :deliveryId AND isToPay = 1 AND status != :deliveredStatus ORDER BY position ASC")
    suspend fun paymentSelectAll(deliveryId: DeliveryId, deliveredStatus: String = ProductResponse.STATUS_DELIVERED): List<ProductEntity>

    @Query("SELECT lineId FROM products WHERE deliveryId = :deliveryId AND lineId != '' ORDER BY position ASC")
    suspend fun productLineIds(deliveryId: DeliveryId): List<ProductLineId>

    @Query("SELECT lineId FROM products WHERE deliveryId = :deliveryId AND isToPay = 1 ORDER BY position ASC")
    suspend fun paymentProductLineIds(deliveryId: DeliveryId): List<ProductLineId>

    @Query("SELECT COALESCE(MAX(position), 0) FROM products WHERE deliveryId = :deliveryId")
    suspend fun maxPosition(deliveryId: DeliveryId): Int

    @Query("SELECT EXISTS(SELECT 1 FROM products WHERE deliveryId = :deliveryId AND status != :soldStatus AND isToPay = 1)")
    suspend fun isPaymentExist(deliveryId: DeliveryId, soldStatus: String = ProductResponse.STATUS_SOLD): Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM products)")
    suspend fun isProductsExist(): Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM products WHERE status = :returnStatus)")
    suspend fun isReturnsExist(returnStatus: String = ProductResponse.STATUS_RETURN): Boolean

    @Upsert
    suspend fun upsert(entities: List<ProductEntity>)

    @Update
    suspend fun update(entities: List<ProductEntity>)

    @Query("DELETE FROM products WHERE deliveryId = :deliveryId")
    suspend fun remove(deliveryId: DeliveryId)
}
