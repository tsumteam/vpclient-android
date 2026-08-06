package ru.mercury.vpclient.features.checkout_payment_result.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.features.checkout_payment_result.model.CheckoutPaymentResultStatus

@Serializable
data class CheckoutPaymentResultRoute(
    val status: CheckoutPaymentResultStatus,
    val deliveryInterval: String = "",
    val address: String = "",
    val itemsCount: Int = 0
): NavKey
