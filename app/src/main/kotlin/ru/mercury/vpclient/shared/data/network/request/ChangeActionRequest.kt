package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChangeActionRequest(
    @SerialName("actionId") val actionId: Int? = null,
    @SerialName("orderBy") val orderBy: Int? = null,
    @SerialName("type") val type: Int? = null,
    @SerialName("imageUrl") val imageUrl: String? = null,
    @SerialName("isHidden") val isHidden: Boolean? = null,
    @SerialName("title") val title: String? = null
)
