package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OrderResponseWithBadgeDtoResponse(
    @SerialName("badge") val badge: Int? = null,
    @SerialName("order") val order: OrderResponse? = null
)
