package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateNoPhotoResultResponse(
    @SerialName("insertedProductsWithPhotos") val insertedProductsWithPhotos: Int? = null,
    @SerialName("updatedDboProducts") val updatedDboProducts: Int? = null,
    @SerialName("updatedCatalogProducts") val updatedCatalogProducts: Int? = null
)
