package ru.mercury.vpclient.core.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OrderLineBarcodeRequest(
    @SerialName("lineId") val lineId: String,
    @SerialName("barcode") val barcode: String
)
