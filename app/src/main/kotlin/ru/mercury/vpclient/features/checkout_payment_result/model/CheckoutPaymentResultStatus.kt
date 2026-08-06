package ru.mercury.vpclient.features.checkout_payment_result.model

import kotlinx.serialization.Serializable

@Serializable
enum class CheckoutPaymentResultStatus {
    Success,
    Ordered,
    Unpaid,
    Error
}
