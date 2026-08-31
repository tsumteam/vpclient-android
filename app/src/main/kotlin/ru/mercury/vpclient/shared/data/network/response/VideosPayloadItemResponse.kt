package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VideosPayloadItemResponse(
    @SerialName("videoUrl") val videoUrl: String? = null,
    @SerialName("previewUrl") val previewUrl: String? = null
)
