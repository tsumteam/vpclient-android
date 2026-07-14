package ru.mercury.vpclient.shared.data.persistence.database.entity

import androidx.room.Entity
import kotlinx.serialization.json.JsonObject
import ru.mercury.vpclient.shared.data.entity.HomeSectionType
import ru.mercury.vpclient.shared.data.network.type.MainScreenCategoryType

@Entity(
    tableName = "MainScreenSection",
    primaryKeys = ["id"]
)
data class MainScreenSectionEntity(
    val id: String,
    val category: MainScreenCategoryType,
    val type: HomeSectionType,
    val order: Int,
    val title: String,
    val titleCatalogLink: JsonObject?,
    val imageUrl: String,
    val giftCardPhotoUrl: String
)
