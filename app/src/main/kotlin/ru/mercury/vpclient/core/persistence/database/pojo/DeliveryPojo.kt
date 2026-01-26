package ru.mercury.vpclient.core.persistence.database.pojo

import androidx.room.Embedded
import androidx.room.Relation
import ru.mercury.vpclient.core.persistence.database.entity.CargoEntity
import ru.mercury.vpclient.core.persistence.database.entity.DeliveryEntity
import ru.mercury.vpclient.core.persistence.database.entity.ProductEntity

data class DeliveryPojo(
    @Embedded val deliveryEntity: DeliveryEntity,
    @Relation(
        parentColumn = "deliveryId",
        entityColumn = "parentDeliveryId",
        entity = DeliveryEntity::class
    ) val childDeliveryEntities: List<DeliveryEntity>,
    @Relation(
        parentColumn = "deliveryId",
        entityColumn = "deliveryId",
        entity = ProductEntity::class
    ) val productEntities: List<ProductEntity>,
    @Relation(
        parentColumn = "deliveryId",
        entityColumn = "deliveryId",
        entity = CargoEntity::class
    ) val cargoEntities: List<CargoEntity>
) {
    companion object {
        val Empty = DeliveryPojo(
            deliveryEntity = DeliveryEntity.Empty,
            childDeliveryEntities = emptyList(),
            productEntities = emptyList(),
            cargoEntities = emptyList()
        )
    }
}
