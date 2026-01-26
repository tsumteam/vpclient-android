package ru.mercury.vpclient.core.ktx

import ru.mercury.vpclient.core.network.response.PaymentMethodResponse
import ru.mercury.vpclient.core.persistence.database.entity.PaymentEntity
import ru.mercury.vpclient.core.persistence.database.entity.PaymentMethodEntity
import ru.mercury.vpclient.core.DeliveryId
import ru.mercury.vpclient.core.RouteId

val PaymentMethodEntity.isBonusMercury: Boolean
    get() = paymentMethodId == PaymentMethodResponse.PAYMENT_ID_BONUS_MERCURY

val PaymentMethodEntity.isManualPayments: Boolean
    get() = paymentMethodId in listOf(PaymentMethodResponse.PAYMENT_ID_SBP, PaymentMethodResponse.PAYMENT_ID_CASH, PaymentMethodResponse.PAYMENT_ID_CREDIT_MAESTRO, PaymentMethodResponse.PAYMENT_ID_CARD)

fun PaymentMethodEntity.paymentEntity(
    routeId: RouteId,
    deliveryId: DeliveryId
): PaymentEntity {
    return PaymentEntity.Empty.copy(
        routeId = routeId,
        deliveryId = deliveryId,
        paymentMethodId = paymentMethodId,
        paymentName = paymentName,
        paymentType = paymentType
    )
}
