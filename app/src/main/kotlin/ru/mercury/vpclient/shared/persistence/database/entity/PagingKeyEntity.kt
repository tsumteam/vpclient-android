package ru.mercury.vpclient.shared.persistence.database.entity

import androidx.room.Entity

@Entity(
    tableName = "PagingKey",
    primaryKeys = ["categoryId", "titleCategoryId"]
)
data class PagingKeyEntity(
    val categoryId: Int,
    val titleCategoryId: Int,
    val offset: Int?,
    val limit: Int?
)
