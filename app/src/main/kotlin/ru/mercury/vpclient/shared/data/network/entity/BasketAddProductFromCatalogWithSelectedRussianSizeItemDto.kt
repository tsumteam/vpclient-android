package ru.mercury.vpclient.shared.data.network.entity

import kotlinx.serialization.Serializable

@Serializable
data class BasketAddProductFromCatalogWithSelectedRussianSizeItemDto(
    val itemId: String? = null,
    val colorId: String? = null,
    val russianSizeCode: String? = null
)
