package ru.mercury.vpclient.shared.data.persistence.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "FilterValues")
data class FilterValuesEntity(
    @PrimaryKey val chipId: String,
    val title: String,
    val valueType: String? = null,
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
