package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LookProductsResponse(
    @SerialName("lookInfo") val lookInfo: LooksResponseItemDtoResponse? = null,
    @SerialName("products") val products: List<CatalogProductSearchCardResponse>? = null
)
