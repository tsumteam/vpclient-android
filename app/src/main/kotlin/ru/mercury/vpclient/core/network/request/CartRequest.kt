package ru.mercury.vpclient.core.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CartRequest(
    @SerialName("orderId") val orderId: String
)
