package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.response.BasketAddProductFromCatalogWithSelectedRussianSizeItemResponse

@Serializable
data class BasketAddProductFromCatalogWithSelectedRussianSizeRequest(
    @SerialName("clientId") val clientId: String,
    @SerialName("items") val items: List<BasketAddProductFromCatalogWithSelectedRussianSizeItemResponse>
)
