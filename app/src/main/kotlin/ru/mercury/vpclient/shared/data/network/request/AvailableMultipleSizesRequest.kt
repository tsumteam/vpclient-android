package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AvailableMultipleSizesRequest(
    @SerialName("items") val items: List<AvailableMultipleSizesRequestItem>
)
