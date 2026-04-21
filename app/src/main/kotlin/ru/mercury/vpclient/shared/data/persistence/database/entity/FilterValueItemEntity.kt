package ru.mercury.vpclient.shared.data.persistence.database.entity

import kotlinx.serialization.Serializable

@Serializable
data class FilterValueItemEntity(
    val id: String,
    val label: String,
    val labelPhotoUrl: String = "",
    val labelItalian: String = "",
    val labelFrench: String = "",
    val labelInternational: String = "",
    val order: Int = Int.MAX_VALUE,
    val isFavorite: Boolean = false,
    val isTopBrand: Boolean = false,
    val requestValueType: String? = null,
    val requestValue: String? = null,
    val parentId: String? = null,
    val childIds: List<String> = emptyList()
) {

    val hasChildren: Boolean
        get() = childIds.isNotEmpty()
}
