package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.response.CreateLookFromFashionImageRequestItemResponse

@Serializable
data class CreateLookFromFashionImageRequest(
    @SerialName("items") val items: List<CreateLookFromFashionImageRequestItemResponse>? = null
)
