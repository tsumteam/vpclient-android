package ru.mercury.vpclient.core.persistence.database.pojo

import androidx.room.Embedded
import androidx.room.Relation
import ru.mercury.vpclient.core.persistence.database.entity.BoutiqueEntity
import ru.mercury.vpclient.core.persistence.database.entity.DeliveryEntity
import ru.mercury.vpclient.core.persistence.database.entity.RouteEntity

data class RoutePojo(
    @Embedded val routeEntity: RouteEntity,
    @Relation(
        parentColumn = "routeId",
        entityColumn = "routeId",
        entity = BoutiqueEntity::class
    ) val boutiquePojos: List<BoutiquePojo>,
    @Relation(
        parentColumn = "routeId",
        entityColumn = "routeId",
        entity = DeliveryEntity::class
    ) val deliveryPojos: List<DeliveryPojo>,
    @Relation(
        parentColumn = "routeId",
        entityColumn = "routeId",
        entity = BoutiqueEntity::class
    ) val returnPojos: List<ReturnPojo>
) {
    companion object {
        val Empty = RoutePojo(
            routeEntity = RouteEntity.Empty,
            boutiquePojos = emptyList(),
            deliveryPojos = emptyList(),
            returnPojos = emptyList()
        )
    }
}
