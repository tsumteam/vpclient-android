package ru.mercury.vpclient.shared.data.entity

import ru.mercury.vpclient.shared.data.network.type.OrderPaymentStatus

data class FittingCheckoutPaymentResultData(
    val paymentStatus: OrderPaymentStatus,
    val deliveryInterval: String,
    val address: String
)
