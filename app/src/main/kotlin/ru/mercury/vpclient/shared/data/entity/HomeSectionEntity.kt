package ru.mercury.vpclient.shared.data.entity

import kotlinx.serialization.json.JsonObject

data class HomeSectionEntity(
    val type: HomeSectionType,
    val order: Int,
    val title: String = "",
    val items: List<HomeSectionItemEntity> = emptyList(),
    val titleCatalogLink: JsonObject? = null,
    val imageUrl: String = "",
    val giftCardPhotoUrl: String = ""
)
