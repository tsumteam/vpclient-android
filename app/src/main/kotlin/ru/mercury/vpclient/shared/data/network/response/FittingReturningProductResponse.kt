package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FittingReturningProductResponse(
    @SerialName("order") val order: Int? = null,
    @SerialName("product") val product: CatalogProductSearchCardResponse? = null,
    @SerialName("productId") val productId: String? = null,
    @SerialName("quantity") val quantity: Int? = null
)
