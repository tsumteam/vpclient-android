package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BasketLineResponse(
    @SerialName("lineId") val lineId: String? = null,
    @SerialName("lookId") val lookId: String? = null,
    @SerialName("order") val order: Int? = null,
    @SerialName("paySwitch") val paySwitch: Boolean? = null,
    @SerialName("products") val products: List<BasketProductResponse>? = null,
    @SerialName("quantity") val quantity: Int? = null,
    @SerialName("barcode") val barcode: String? = null,
    @SerialName("locationId") val locationId: String? = null,
    @SerialName("locationAsString") val locationAsString: String? = null,
    @SerialName("controls") val controls: BasketLineControlsResponse? = null,
    @SerialName("alternatives") val alternatives: List<BasketAlternativeResponse>? = null
)
