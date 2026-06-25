package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BasketLookResponse(
    @SerialName("imageUrl") val imageUrl: String? = null,
    @SerialName("lookId") val lookId: String? = null,
    @SerialName("name") val name: String? = null
)
