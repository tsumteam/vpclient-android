package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AvailableMultipleSizesRequestItem(
    @SerialName("itemId") val itemId: String,
    @SerialName("colorId") val colorId: String
)
