package ru.mercury.vpclient.core.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeliveryRequest(
    @SerialName("deliveryId") val deliveryId: String
)
