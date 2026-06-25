package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FittingDeliveryTimeResponse(
    @SerialName("deliveryTimes") val deliveryTimes: List<DeliveryTimeResponse>? = null,
    @SerialName("deliveries") val deliveries: List<FittingDeliveryTimeDeliveryResponse>? = null
)
