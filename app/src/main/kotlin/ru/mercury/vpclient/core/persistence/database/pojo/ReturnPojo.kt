package ru.mercury.vpclient.core.persistence.database.pojo

import androidx.room.Embedded
import androidx.room.Relation
import ru.mercury.vpclient.core.persistence.database.entity.BoutiqueEntity
import ru.mercury.vpclient.core.persistence.database.entity.DeliveryEntity
import ru.mercury.vpclient.core.persistence.database.entity.ProductEntity

data class ReturnPojo(
    @Embedded val boutiqueEntity: BoutiqueEntity,
    @Relation(
        parentColumn = "boutiqueId",
        entityColumn = "boutiqueId",
        entity = DeliveryEntity::class
    ) val deliveryEntities: List<DeliveryEntity>,
    @Relation(
        parentColumn = "boutiqueId",
        entityColumn = "boutiqueId",
        entity = ProductEntity::class
    ) val productEntities: List<ProductEntity>
) {
    companion object {
        val Empty = ReturnPojo(
            boutiqueEntity = BoutiqueEntity.Empty,
            deliveryEntities = emptyList(),
            productEntities = emptyList()
        )
    }
}
