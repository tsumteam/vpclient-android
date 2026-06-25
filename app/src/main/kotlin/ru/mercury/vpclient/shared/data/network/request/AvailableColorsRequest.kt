package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AvailableColorsRequest(
    @SerialName("itemId") val itemId: String? = null,
    @SerialName("sizeId") val sizeId: String? = null
)
