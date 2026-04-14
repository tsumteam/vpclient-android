package ru.mercury.vpclient.shared.persistence.database.entity

import androidx.room.Entity

@Entity(
    tableName = "CatalogCategory",
    primaryKeys = ["id"]
)
data class CatalogCategoryEntity(
    val id: Int,
    val parentId: Int?,
    val rootId: Int,
    val level: Int,
    val name: String,
    val photoUrl: String,
    val categoryType: String,
    val sortSettingId: Int,
    val position: Int
) {
    companion object {
        const val LEVEL_BASIC = 0
        const val LEVEL_TOP = 1
        const val LEVEL_BOTTOM = 2

        val Empty = CatalogCategoryEntity(
            id = 0,
            parentId = null,
            rootId = 0,
            level = LEVEL_BASIC,
            name = "",
            photoUrl = "",
            categoryType = "",
            sortSettingId = 0,
            position = 0
        )
    }
}
