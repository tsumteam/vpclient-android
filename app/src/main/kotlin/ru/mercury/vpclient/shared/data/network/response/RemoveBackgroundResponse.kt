package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.type.RemoveBackgroundStatus

@Serializable
data class RemoveBackgroundResponse(
    @SerialName("status") val status: RemoveBackgroundStatus? = null,
    @SerialName("previewImageUrl") val previewImageUrl: String? = null,
    @SerialName("resultImageUrl") val resultImageUrl: String? = null
)
