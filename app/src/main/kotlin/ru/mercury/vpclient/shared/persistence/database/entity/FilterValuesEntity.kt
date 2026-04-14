package ru.mercury.vpclient.shared.persistence.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "FilterValues")
data class FilterValuesEntity(
    @PrimaryKey val chipId: String,
    val title: String,
    val valueIds: List<String>,
    val valueLabels: List<String>,
    val valueLabelPhotoUrls: List<String> = emptyList(),
    val valueLabelsItalian: List<String> = emptyList(),
    val valueLabelsFrench: List<String> = emptyList(),
    val valueLabelsInternational: List<String> = emptyList(),
    val valueIsFavorites: List<String> = emptyList(),
    val valueIsTopBrands: List<String> = emptyList()
) {
    companion object {
        val Empty = FilterValuesEntity(
            chipId = "",
            title = "",
            valueIds = emptyList(),
            valueLabels = emptyList(),
            valueLabelPhotoUrls = emptyList(),
            valueLabelsItalian = emptyList(),
            valueLabelsFrench = emptyList(),
            valueLabelsInternational = emptyList(),
            valueIsFavorites = emptyList(),
            valueIsTopBrands = emptyList()
        )
    }
}
