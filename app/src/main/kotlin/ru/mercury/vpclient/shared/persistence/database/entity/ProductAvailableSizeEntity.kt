package ru.mercury.vpclient.shared.persistence.database.entity

import kotlinx.serialization.Serializable

// fixme

@Serializable
data class ProductAvailableSizeEntity(
    val sizeId: String,
    val russianSize: String?,
    val sizeFullName: String?,
    val inStock: Boolean
)
