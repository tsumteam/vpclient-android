package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CartCountLineResponse(
    @SerialName("quantity") val quantity: Int? = null,
    @SerialName("products") val products: List<CartCountLineProductResponse> = emptyList()
)
