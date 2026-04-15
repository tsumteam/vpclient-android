package ru.mercury.vpclient.shared.domain.mapper

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonPrimitive
import ru.mercury.vpclient.shared.data.network.response.FilterValuesValueResponse
import ru.mercury.vpclient.shared.data.persistence.database.entity.FilterValuesEntity

// fixme

private val filterValuesJson = Json { ignoreUnknownKeys = true; explicitNulls = false }

fun List<JsonElement>.toFilterValuesEntity(
    chipId: String,
    title: String
): FilterValuesEntity {
    val values = mapNotNull { element ->
        runCatching { filterValuesJson.decodeFromJsonElement<FilterValuesValueResponse>(element) }
            .getOrNull()
            ?.toFilterValue(chipId)
    }.sortedBy(FilterValueData::order)

    return FilterValuesEntity(
        chipId = chipId,
        title = title,
        valueIds = values.map(FilterValueData::id),
        valueLabels = values.map(FilterValueData::label),
        valueLabelPhotoUrls = values.map(FilterValueData::labelPhotoUrl),
        valueLabelsItalian = values.map(FilterValueData::labelItalian),
        valueLabelsFrench = values.map(FilterValueData::labelFrench),
        valueLabelsInternational = values.map(FilterValueData::labelInternational),
        valueIsFavorites = values.map { if (it.isFavorite) "true" else "false" },
        valueIsTopBrands = values.map { if (it.isTopBrand) "true" else "false" }
    )
}

private fun FilterValuesValueResponse.toFilterValue(chipId: String): FilterValueData? {
    val labelValue = label.orEmpty()
    val filterValueId = value.filterValueId ?: return null

    return when {
        labelValue.isBlank() -> null
        else -> FilterValueData(
            id = "${chipId}_$filterValueId",
            label = labelValue,
            labelPhotoUrl = labelPhotoUrl.orEmpty(),
            labelItalian = labelItalian.orEmpty(),
            labelFrench = labelFrench.orEmpty(),
            labelInternational = labelInternational.orEmpty(),
            order = order ?: Int.MAX_VALUE,
            isFavorite = isFavorite ?: false,
            isTopBrand = isTopBrand ?: false
        )
    }
}

private val JsonElement?.filterValueId: String?
    get() {
        return this?.jsonPrimitive?.contentOrNull
    }

private data class FilterValueData(
    val id: String,
    val label: String,
    val labelPhotoUrl: String,
    val labelItalian: String,
    val labelFrench: String,
    val labelInternational: String,
    val order: Int,
    val isFavorite: Boolean = false,
    val isTopBrand: Boolean = false
)
