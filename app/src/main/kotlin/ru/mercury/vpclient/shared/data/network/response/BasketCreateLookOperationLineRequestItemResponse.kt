package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BasketCreateLookOperationLineRequestItemResponse(
    @SerialName("lineId") val lineId: String? = null
)
