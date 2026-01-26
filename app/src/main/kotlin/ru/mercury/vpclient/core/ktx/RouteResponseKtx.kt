package ru.mercury.vpclient.core.ktx

import ru.mercury.vpclient.core.DeliveryId
import ru.mercury.vpclient.core.network.response.DeliveryResponse
import ru.mercury.vpclient.core.network.response.RouteResponse
import ru.mercury.vpclient.core.persistence.database.entity.BoutiqueEntity
import ru.mercury.vpclient.core.persistence.database.entity.DeliveryEntity
import ru.mercury.vpclient.core.persistence.database.entity.RouteEntity
import ru.mercury.vpclient.core.persistence.database.pojo.DeliveryPojo

val RouteResponse.entity: RouteEntity
    get() = RouteEntity(
        routeId = routeId.orEmpty(),
        routeStatus = routeStatus.orEmpty(),
        driverId = driverId.orEmpty(),
        driverName = driverName.orEmpty(),
        driverBarcode = driverBarcode.orEmpty(),
        cashOnHand = cashOnHand.orEmpty
    )

val RouteResponse.deliveryResponseList: List<DeliveryResponse>
    get() = deliveries.orEmpty()

val RouteResponse.apiDeliveryIds: Set<DeliveryId>
    get() = deliveryResponseList.mapNotNull(DeliveryResponse::deliveryId).toSet()

fun RouteResponse.boutiqueEntities(
    currentBoutiqueEntities: List<BoutiqueEntity>,
    boutiqueMaxPosition: Int
): List<BoutiqueEntity> {
    val routeEntity = entity
    return deliveryResponseList
        .distinctBy(DeliveryResponse::inventLocationId)
        .mapIndexed { index, deliveryResponse ->
            val currentBoutiqueEntity = currentBoutiqueEntities.find { it.boutiqueId == deliveryResponse.inventLocationId }
            when {
                currentBoutiqueEntity != null -> currentBoutiqueEntity.copy(routeStatus = routeEntity.routeStatus)
                else -> {
                    deliveryResponse.boutiqueEntity(
                        routeId = routeEntity.routeId,
                        routeStatus = routeEntity.routeStatus,
                        position = boutiqueMaxPosition.plus(index).plus(1)
                    )
                }
            }
        }
}

fun RouteResponse.deliveryEntities(
    currentDeliveryPojos: List<DeliveryPojo>,
    deliveryMaxPosition: Int
): List<DeliveryEntity> {
    val routeEntity = entity
    return deliveryResponseList.mapIndexed { index, deliveryResponse ->
        val currentDeliveryPojo = currentDeliveryPojos.find { it.deliveryEntity.deliveryId == deliveryResponse.deliveryId }
        when {
            currentDeliveryPojo != null -> deliveryResponse.deliveryEntity(currentDeliveryPojo.deliveryEntity)
            else -> deliveryResponse.deliveryEntity(
                routeId = routeEntity.routeId,
                routeStatus = routeEntity.routeStatus,
                position = deliveryMaxPosition.plus(index).plus(1)
            )
        }
    }
}
