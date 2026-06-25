package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserProfileUploadPhotoPostResponse(
    @SerialName("file") val file: ByteArray? = null
)
