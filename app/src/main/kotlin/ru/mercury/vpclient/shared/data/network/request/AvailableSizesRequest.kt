package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AvailableSizesRequest(
    @SerialName("colorId") val colorId: String? = null,
    @SerialName("itemId") val itemId: String? = null
)
