package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetDeliveryIntervalsForExistingOrderResponse(
    @SerialName("deliveryTimes") val deliveryTimes: List<DeliveryTimeResponse>? = null
)
