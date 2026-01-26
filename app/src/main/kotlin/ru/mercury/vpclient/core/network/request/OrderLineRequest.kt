package ru.mercury.vpclient.core.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OrderLineRequest(
    @SerialName("lineId") val lineId: String,
    @SerialName("actionType") val actionType: String?
)
