package ru.mercury.vpclient.core.ktx

import ru.mercury.vpclient.core.RouteId
import ru.mercury.vpclient.core.network.response.DeliveryResponse
import ru.mercury.vpclient.core.persistence.database.entity.BoutiqueEntity
import ru.mercury.vpclient.core.persistence.database.entity.DeliveryEntity

val readOnlyStatuses: List<String>
    get() = listOf(
        DeliveryResponse.DETAILED_STATUS_NONE,
        DeliveryResponse.DETAILED_STATUS_PICKED_UP_GOODS,
        DeliveryResponse.DETAILED_STATUS_ON_ROUTE_TO_CLIENT,
        DeliveryResponse.DETAILED_STATUS_ARRIVED_AT_CLIENT,
        DeliveryResponse.DETAILED_STATUS_PAYMENT_COMPLETED,
        DeliveryResponse.DETAILED_STATUS_LEFT_CLIENT,
        DeliveryResponse.DETAILED_STATUS_COMPLETED
    )

val returnTypeReadOnlyStatuses: List<String>
    get() = listOf(
        DeliveryResponse.DETAILED_STATUS_NONE,
        DeliveryResponse.DETAILED_STATUS_PICKED_UP_GOODS,
        DeliveryResponse.DETAILED_STATUS_ON_ROUTE_TO_CLIENT,
        DeliveryResponse.DETAILED_STATUS_PAYMENT_COMPLETED,
        DeliveryResponse.DETAILED_STATUS_LEFT_CLIENT,
        DeliveryResponse.DETAILED_STATUS_COMPLETED
    )

val shippedStatuses: List<String>
    get() = listOf(
        DeliveryResponse.DETAILED_STATUS_PICKED_UP_GOODS,
        DeliveryResponse.DETAILED_STATUS_ON_ROUTE_TO_CLIENT,
        DeliveryResponse.DETAILED_STATUS_ARRIVED_AT_CLIENT
    )

val paymentStatuses: List<String>
    get() = listOf(
        DeliveryResponse.DETAILED_STATUS_HANDED_OVER_GOODS,
        DeliveryResponse.DETAILED_STATUS_FITTING_COMPLETED,
        DeliveryResponse.DETAILED_STATUS_PAYMENT_COMPLETED,
        DeliveryResponse.DETAILED_STATUS_LEFT_CLIENT,
        DeliveryResponse.DETAILED_STATUS_COMPLETED
    )

fun DeliveryResponse.boutiqueEntity(
    routeId: RouteId,
    routeStatus: String,
    position: Int
): BoutiqueEntity {
    return BoutiqueEntity(
        routeId = routeId,
        routeStatus = routeStatus,
        boutiqueId = inventLocationId.orEmpty(),
        boutiqueName = inventLocationName.orEmpty(),
        position = position
    )
}

fun DeliveryResponse.deliveryEntity(
    routeId: RouteId,
    routeStatus: String,
    position: Int
): DeliveryEntity {
    return DeliveryEntity(
        routeId = routeId,
        routeStatus = routeStatus,
        deliveryId = deliveryId.orEmpty(),
        deliveryStatus = deliveryStatus.orEmpty(),
        detailedStatus = detailedStatus.orEmpty(),
        codeGoal = codeGoal.orEmpty(),
        codeTask = codeTask.orEmpty(),
        isPaid = isPaid.orEmpty,
        numberOfItems = numberOfItems.orEmpty,
        sourceDocId = sourceDocId.orEmpty(),
        sourceDocType = sourceDocType.orEmpty(),
        startTime = startTime.orEmpty(),
        arrivalFrom = arrivalFrom.orEmpty(),
        arrivalTo = arrivalTo.orEmpty(),
        sellerName = sellerName.orEmpty(),
        sellerPhone = sellerPhone.orEmpty(),
        clientName = clientName.orEmpty(),
        clientPhone = clientPhone.orEmpty(),
        clientAddress = clientAddress.orEmpty(),
        comment = comment.orEmpty(),
        clientCanCall = clientCanCall.orEmpty,
        boutiqueId = inventLocationId.orEmpty(),
        boutiqueName = inventLocationName.orEmpty(),
        isFitting = isFitting.orEmpty,
        clientClass = clientClass.orEmpty(),
        extOrderId = extOrderId.orEmpty(),
        orderId = "",
        cargoPackagesLineIds = cargoPackages.orEmpty().flatMap { it.lineIds.orEmpty() },
        position = position,
        fittingTimer = 0L,
        isParent = false,
        isOffline = false,
        parentDeliveryId = "",
        detailedStatusTrackingStatus = detailedStatus.orEmpty(),
        detailedStatusTrackingTimestampMs = System.currentTimeMillis()
    )
}

fun DeliveryResponse.deliveryEntity(
    currentDeliveryEntity: DeliveryEntity
): DeliveryEntity {
    return DeliveryEntity(
        routeId = currentDeliveryEntity.routeId,
        routeStatus = currentDeliveryEntity.routeStatus,
        deliveryId = deliveryId.orEmpty(),
        deliveryStatus = deliveryStatus.orEmpty(),
        detailedStatus = detailedStatus.orEmpty(),
        codeGoal = codeGoal.orEmpty(),
        codeTask = codeTask.orEmpty(),
        isPaid = isPaid.orEmpty,
        numberOfItems = numberOfItems.orEmpty,
        sourceDocId = sourceDocId.orEmpty(),
        sourceDocType = sourceDocType.orEmpty(),
        startTime = startTime.orEmpty(),
        arrivalFrom = arrivalFrom.orEmpty(),
        arrivalTo = arrivalTo.orEmpty(),
        sellerName = sellerName.orEmpty(),
        sellerPhone = sellerPhone.orEmpty(),
        clientName = clientName.orEmpty(),
        clientPhone = clientPhone.orEmpty(),
        clientAddress = clientAddress.orEmpty(),
        comment = comment.orEmpty(),
        clientCanCall = clientCanCall.orEmpty,
        boutiqueId = inventLocationId.orEmpty(),
        boutiqueName = inventLocationName.orEmpty(),
        isFitting = isFitting.orEmpty,
        clientClass = clientClass.orEmpty(),
        extOrderId = extOrderId.orEmpty(),
        orderId = currentDeliveryEntity.orderId,
        cargoPackagesLineIds = currentDeliveryEntity.cargoPackagesLineIds,
        position = currentDeliveryEntity.position,
        fittingTimer = currentDeliveryEntity.fittingTimer,
        isParent = currentDeliveryEntity.isParent,
        isOffline = currentDeliveryEntity.isOffline,
        parentDeliveryId = currentDeliveryEntity.parentDeliveryId,
        detailedStatusTrackingStatus = currentDeliveryEntity.detailedStatusTrackingStatus,
        detailedStatusTrackingTimestampMs = currentDeliveryEntity.detailedStatusTrackingTimestampMs
    )
}
