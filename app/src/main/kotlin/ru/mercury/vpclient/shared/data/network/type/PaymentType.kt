package ru.mercury.vpclient.shared.data.network.type

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class PaymentType {
    @SerialName("none") NONE,
    @SerialName("cashlessOnline") CASHLESS_ONLINE,
    @SerialName("cashlessCourier") CASHLESS_COURIER,
    @SerialName("cashCourier") CASH_COURIER,
    @SerialName("other") OTHER,
    @SerialName("cashlessOnlineUrl") CASHLESS_ONLINE_URL,
    @SerialName("onlinePaymentClient") ONLINE_PAYMENT_CLIENT
}
