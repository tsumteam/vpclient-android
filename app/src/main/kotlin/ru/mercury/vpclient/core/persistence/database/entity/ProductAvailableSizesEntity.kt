package ru.mercury.vpclient.core.persistence.database.entity

import kotlinx.serialization.Serializable

// fixme

@Serializable
data class ProductAvailableSizesEntity(
    val items: List<ProductAvailableSizeEntity>,
    val countryCode: String?,
    val sizeTableTitle: String?,
    val sizeTableUrl: String?
)
