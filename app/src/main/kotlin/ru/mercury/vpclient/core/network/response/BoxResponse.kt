package ru.mercury.vpclient.core.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BoxResponse(
    @SerialName("barcode") val barcode: String?,
    @SerialName("lineIds") val lineIds: List<String>?
)
