package ru.mercury.vpclient.core.persistence.database.entity

import androidx.room.Entity

@Entity(
    tableName = "CatalogFilter",
    primaryKeys = ["categoryId", "titleCategoryId"]
)
data class CatalogFilterEntity(
    val categoryId: Int,
    val titleCategoryId: Int,
    val filtersJson: String
) {
    companion object {
        val Empty = CatalogFilterEntity(
            categoryId = 0,
            titleCategoryId = 0,
            filtersJson = ""
        )
    }
}
