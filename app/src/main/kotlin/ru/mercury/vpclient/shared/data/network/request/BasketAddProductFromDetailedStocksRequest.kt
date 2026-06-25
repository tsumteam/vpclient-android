package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BasketAddProductFromDetailedStocksRequest(
    @SerialName("clientId") val clientId: String? = null,
    @SerialName("itemId") val itemId: String? = null,
    @SerialName("colorId") val colorId: String? = null,
    @SerialName("sizeId") val sizeId: String? = null,
    @SerialName("locationId") val locationId: String? = null
)
