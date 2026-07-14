package ru.mercury.vpclient.shared.data.entity

import kotlinx.serialization.json.JsonObject
import ru.mercury.vpclient.shared.data.network.type.MainScreenLinkType

data class HomeSectionItemEntity(
    val title: String = "",
    val subtitle: String = "",
    val imageUrl: String = "",
    val videoUrl: String = "",
    val brand: String = "",
    val brandLogoUrl: String? = null,
    val productId: String? = null,
    val productItemId: String? = null,
    val productColorId: String? = null,
    val catalogLink: JsonObject? = null,
    val linkType: MainScreenLinkType? = null,
    val bannerLinkUrl: String? = null,
    val fashionImageId: Int? = null,
    val fashionImageTitle: String? = null
)
