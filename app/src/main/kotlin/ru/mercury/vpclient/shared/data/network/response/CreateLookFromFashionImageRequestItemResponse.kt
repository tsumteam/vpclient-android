package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateLookFromFashionImageRequestItemResponse(
    @SerialName("fashionImageId") val fashionImageId: Int? = null,
    @SerialName("fashionImageCollectionId") val fashionImageCollectionId: Int? = null,
    @SerialName("compilationId") val compilationId: Int? = null,
    @SerialName("name") val name: String? = null
)
