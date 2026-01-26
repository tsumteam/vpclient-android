package ru.mercury.vpclient.core.ktx

import ru.mercury.vpclient.core.network.response.CargoPackageResponse
import ru.mercury.vpclient.core.persistence.database.entity.CargoEntity
import ru.mercury.vpclient.core.BoutiqueId
import ru.mercury.vpclient.core.DeliveryId
import ru.mercury.vpclient.core.RouteId

fun CargoPackageResponse.entity(
    routeId: RouteId,
    boutiqueId: BoutiqueId,
    deliveryId: DeliveryId,
    scannedInBoutique: Boolean
): CargoEntity {
    return CargoEntity(
        routeId = routeId,
        boutiqueId = boutiqueId,
        deliveryId = deliveryId,
        barcode = barcode.orEmpty(),
        lineIds = lineIds.orEmpty(),
        scannedInBoutique = scannedInBoutique,
        scannedAtDelivery = false
    )
}
