package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.type.ImageCategory

@Serializable
data class ImagesUploadPostResponse(
    @SerialName("Files")
val files: List<ByteArray>? = null,
    @SerialName("Category")
val category: ImageCategory? = null,
    @SerialName("GeneratePreview")
val generatePreview: Boolean? = null
)
