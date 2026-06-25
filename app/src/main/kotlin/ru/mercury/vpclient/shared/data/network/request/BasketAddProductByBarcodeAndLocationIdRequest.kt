package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BasketAddProductByBarcodeAndLocationIdRequest(
    @SerialName("clientId") val clientId: String? = null,
    @SerialName("barcode") val barcode: String? = null,
    @SerialName("locationId") val locationId: String? = null
)
