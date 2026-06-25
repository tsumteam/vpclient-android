package ru.mercury.vpclient.shared.data.network.type

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ImageCategory {
    @SerialName("looks") LOOKS,
    @SerialName("imagesearch") IMAGESEARCH,
    @SerialName("messenger") MESSENGER,
    @SerialName("compilations") COMPILATIONS
}
