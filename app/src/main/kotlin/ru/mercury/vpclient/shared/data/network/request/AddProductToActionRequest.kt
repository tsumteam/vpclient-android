package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddProductToActionRequest(
    @SerialName("actionId") val actionId: Int? = null,
    @SerialName("productId") val productId: Int? = null
)
