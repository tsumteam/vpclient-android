package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.type.PaymentType

@Serializable
data class OrderCreationFromFittingRequest(
    @SerialName("pairedUserId") val pairedUserId: String? = null,
    @SerialName("deliveryIds") val deliveryIds: List<String> = emptyList(),
    @SerialName("paymentType") val paymentType: PaymentType? = null
)
