package ru.mercury.vpclient.core.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeliveryStatusRequest(
    @SerialName("deliveryId") val deliveryId: String,
    @SerialName("detailedStatus") val detailedStatus: String
)
