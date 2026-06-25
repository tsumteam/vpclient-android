package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BasketCheckoutOrderItemResponse(
    @SerialName("product") val product: CatalogProductSearchCardResponse? = null,
    @SerialName("productId") val productId: String? = null
)
