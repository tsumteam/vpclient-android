package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetDeliveryIntervalsForExistingFittingResponse(
    @SerialName("deliveryTimes") val deliveryTimes: List<DeliveryTimeResponse>? = null
)
