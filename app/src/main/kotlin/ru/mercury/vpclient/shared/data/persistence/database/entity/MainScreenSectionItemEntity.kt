package ru.mercury.vpclient.shared.data.persistence.database.entity

import androidx.room.Entity
import kotlinx.serialization.json.JsonObject
import ru.mercury.vpclient.shared.data.network.type.MainScreenLinkType

@Entity(
    tableName = "MainScreenSectionItem",
    primaryKeys = ["sectionId", "position"]
)
data class MainScreenSectionItemEntity(
    val sectionId: String,
    val position: Int,
    val title: String,
    val subtitle: String,
    val imageUrl: String,
    val videoUrl: String,
    val brand: String,
    val brandLogoUrl: String?,
    val productId: String?,
    val productItemId: String?,
    val productColorId: String?,
    val catalogLink: JsonObject?,
    val linkType: MainScreenLinkType?,
    val bannerLinkUrl: String?,
    val fashionImageId: Int?,
    val fashionImageTitle: String?
)
