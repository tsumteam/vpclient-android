package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CartCountResponse(
    @SerialName("lines") val lines: List<CartCountLineResponse> = emptyList()
)
