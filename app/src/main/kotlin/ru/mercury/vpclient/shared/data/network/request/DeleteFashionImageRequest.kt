package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeleteFashionImageRequest(
    @SerialName("fashionImageCollectionId") val fashionImageCollectionId: Int? = null,
    @SerialName("fashionImageNo") val fashionImageNo: Int? = null
)
