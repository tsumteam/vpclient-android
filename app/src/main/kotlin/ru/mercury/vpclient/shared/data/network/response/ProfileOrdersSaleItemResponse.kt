package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileOrdersSaleItemResponse(
    @SerialName("badge") val badge: Int? = null,
    @SerialName("sale") val sale: ProfileOrdersSaleResponse? = null
)
