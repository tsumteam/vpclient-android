package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddProductToFashionImageRequest(
    @SerialName("fashionImageNo") val fashionImageNo: Int? = null,
    @SerialName("fashionImageCollectionId") val fashionImageCollectionId: Int? = null,
    @SerialName("productId") val productId: String? = null,
    @SerialName("orderBy") val orderBy: Int? = null
)
