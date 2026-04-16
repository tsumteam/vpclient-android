package ru.mercury.vpclient.shared.data.persistence.database.entity

import kotlinx.serialization.Serializable

// fixme

@Serializable
data class ProductOtherColorEntity(
    val imageUrls: List<String>,
    val urlItemVideo: String? = null
)
