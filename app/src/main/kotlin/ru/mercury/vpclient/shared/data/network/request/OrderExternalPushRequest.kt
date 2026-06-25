package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.response.OrderExternalPushRequestItemResponse

@Serializable
data class OrderExternalPushRequest(
    @SerialName("items") val items: List<OrderExternalPushRequestItemResponse>? = null
)
