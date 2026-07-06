package ru.mercury.vpclient.shared.data.persistence.database.entity

import androidx.room.Entity
import ru.mercury.vpclient.shared.data.network.type.CatalogFilterValueType

@Entity(
    tableName = "FilterValues",
    primaryKeys = ["chipId"]
)
data class FilterValuesEntity(
    val chipId: String,
    val title: String,
    val valueType: CatalogFilterValueType? = null,
    val showSearchBar: Boolean = false,
    val showSidePanelWithLetters: Boolean = false,
    val items: List<FilterValueItemEntity> = emptyList()
) {
    companion object {
        val Empty = FilterValuesEntity(
            chipId = "",
            title = "",
            items = emptyList()
        )
    }
}
