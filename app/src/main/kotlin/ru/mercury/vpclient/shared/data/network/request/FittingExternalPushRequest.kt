package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.response.FittingExternalPushRequestItemResponse

@Serializable
data class FittingExternalPushRequest(
    @SerialName("items") val items: List<FittingExternalPushRequestItemResponse>? = null
)
