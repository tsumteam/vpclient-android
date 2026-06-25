package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BasketAddProductByBarcodeRequest(
    @SerialName("clientId") val clientId: String? = null,
    @SerialName("barcode") val barcode: String? = null
)
