package ru.mercury.vpclient.features.checkout_payment_result.model

import ru.mercury.vpclient.shared.mvi.Model

data class CheckoutPaymentResultModel(
    val status: CheckoutPaymentResultStatus = CheckoutPaymentResultStatus.Success,
    val deliveryInterval: String = "",
    val address: String = "",
    val itemsCount: Int = 0
): Model
