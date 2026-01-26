package ru.mercury.vpclient.core.persistence.database.pojo

import androidx.room.Embedded
import androidx.room.Relation
import ru.mercury.vpclient.core.persistence.database.entity.DeliveryEntity
import ru.mercury.vpclient.core.persistence.database.entity.LoyaltyEntity
import ru.mercury.vpclient.core.persistence.database.entity.PaymentEntity
import ru.mercury.vpclient.core.persistence.database.entity.PaymentMethodEntity
import ru.mercury.vpclient.core.persistence.database.entity.ProductEntity

data class PaymentPojo(
    @Embedded val deliveryEntity: DeliveryEntity,
    @Relation(
        parentColumn = "deliveryId",
        entityColumn = "deliveryId",
        entity = ProductEntity::class
    ) val productEntities: List<ProductEntity>,
    @Relation(
        parentColumn = "deliveryId",
        entityColumn = "deliveryId",
        entity = LoyaltyEntity::class
    ) val loyaltyEntity: LoyaltyEntity?,
    @Relation(
        parentColumn = "deliveryId",
        entityColumn = "deliveryId",
        entity = PaymentMethodEntity::class
    ) val paymentMethodEntities: List<PaymentMethodEntity>,
    @Relation(
        parentColumn = "deliveryId",
        entityColumn = "deliveryId",
        entity = PaymentEntity::class
    ) val paymentEntities: List<PaymentEntity>
)
