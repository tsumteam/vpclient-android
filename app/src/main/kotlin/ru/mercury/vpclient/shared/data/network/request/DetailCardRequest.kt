package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DetailCardRequest(
    @SerialName("itemId") val itemId: String? = null,
    @SerialName("colorId") val colorId: String? = null,
    @SerialName("compilationLookProductId") val compilationLookProductId: Int? = null,
    @SerialName("fashionImageId") val fashionImageId: Int? = null
)
