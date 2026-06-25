package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeliveryTimeResponse(
    @SerialName("from")
val fromValue: String? = null,
    @SerialName("to") val to: String? = null
)
