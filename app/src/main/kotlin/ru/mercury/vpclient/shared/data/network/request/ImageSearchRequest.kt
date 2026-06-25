package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImageSearchRequest(
    @SerialName("imageUrl") val imageUrl: String? = null
)
