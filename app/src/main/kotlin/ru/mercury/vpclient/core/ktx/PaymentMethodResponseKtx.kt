package ru.mercury.vpclient.core.ktx

import ru.mercury.vpclient.core.network.response.PaymentMethodResponse
import ru.mercury.vpclient.core.persistence.database.entity.PaymentMethodEntity
import ru.mercury.vpclient.core.DeliveryId
import ru.mercury.vpclient.core.RouteId

fun PaymentMethodResponse.entity(
    routeId: RouteId,
    deliveryId: DeliveryId
): PaymentMethodEntity {
    return PaymentMethodEntity(
        routeId = routeId,
        deliveryId = deliveryId,
        paymentMethodId = paymentId.orEmpty(),
        paymentName = paymentName.orEmpty(),
        paymentType = paymentType.orEmpty()
    )
}
