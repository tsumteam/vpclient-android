package ru.mercury.vpclient.shared.data.network.entity

import kotlinx.serialization.Serializable

@Serializable
data class BasketAddProductFromCatalogWithSelectedRussianSizeRequestDto(
    val clientId: String? = null,
    val items: List<BasketAddProductFromCatalogWithSelectedRussianSizeItemDto>? = null
)
