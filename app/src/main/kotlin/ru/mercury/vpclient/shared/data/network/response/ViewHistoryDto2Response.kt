package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ViewHistoryDto2Response(
    @SerialName("error") val error: ErrorResponse? = null,
    @SerialName("data") val data: ViewHistoryDtoResponse? = null
)
