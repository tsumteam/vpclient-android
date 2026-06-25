package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImagesPayloadItemResponse(
    @SerialName("imageUrl") val imageUrl: String? = null,
    @SerialName("previewUrl") val previewUrl: String? = null
)
