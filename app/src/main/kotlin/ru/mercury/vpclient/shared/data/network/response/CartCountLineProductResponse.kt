package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CartCountLineProductResponse(
    @SerialName("product") val product: CartCountProductResponse? = null
)
