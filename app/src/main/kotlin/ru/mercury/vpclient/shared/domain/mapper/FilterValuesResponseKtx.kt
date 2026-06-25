package ru.mercury.vpclient.shared.domain.mapper

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import ru.mercury.vpclient.shared.data.network.response.FilterDecimalRangeValueResponse
import ru.mercury.vpclient.shared.data.network.response.FilterIdTreeValueResponse
import ru.mercury.vpclient.shared.data.network.response.FilterValuesValueResponse
import ru.mercury.vpclient.shared.data.network.type.CatalogFilterValueType
import ru.mercury.vpclient.shared.data.persistence.database.entity.FilterValueItemEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.FilterValuesEntity

// fixme

private val filterValuesJson = Json { ignoreUnknownKeys = true; explicitNulls = false }

fun List<JsonElement>.toFilterValuesEntity(
    chipId: String,
    title: String,
    valueType: CatalogFilterValueType? = null,
    showSearchBar: Boolean = false,
    showSidePanelWithLetters: Boolean = false
): FilterValuesEntity {
    val resolvedValueType = valueType ?: firstNotNullOfOrNull { element -> element.filterValueType }
    val values = when (resolvedValueType) {
        CatalogFilterValueType.DECIMAL_RANGE -> {
            mapNotNull { element -> element.toDecimalRangeFilterValue(chipId) }
        }
        CatalogFilterValueType.ID_TREE -> {
            flatMap { element -> element.toTreeFilterValues(chipId, parentId = null) }
        }
        else -> {
            mapNotNull { element -> element.toRegularFilterValue(chipId) }
        }
    }.sortedBy(FilterValueItemEntity::order)

    return FilterValuesEntity(
        chipId = chipId,
        title = title,
        valueType = resolvedValueType,
        showSearchBar = showSearchBar,
        showSidePanelWithLetters = showSidePanelWithLetters,
        items = values
    )
}

private fun JsonElement.toRegularFilterValue(chipId: String): FilterValueItemEntity? {
    val response = runCatching { filterValuesJson.decodeFromJsonElement<FilterValuesValueResponse>(this) }
        .getOrNull()
        ?: return null
    val labelValue = response.label.orEmpty()
    val filterValueId = response.value.filterValueId ?: return null

    if (labelValue.isBlank()) {
        return null
    }
    return FilterValueItemEntity(
        id = "${chipId}_$filterValueId",
        label = labelValue,
        labelPhotoUrl = response.labelPhotoUrl.orEmpty(),
        labelItalian = response.labelItalian.orEmpty(),
        labelFrench = response.labelFrench.orEmpty(),
        labelInternational = response.labelInternational.orEmpty(),
        order = response.order ?: Int.MAX_VALUE,
        isFavorite = response.isFavorite ?: false,
        isTopBrand = response.isTopBrand ?: false,
        requestValue = filterValueId
    )
}

private fun JsonElement.toDecimalRangeFilterValue(chipId: String): FilterValueItemEntity? {
    val response = runCatching { filterValuesJson.decodeFromJsonElement<FilterDecimalRangeValueResponse>(this) }
        .getOrNull()
        ?: return null
    val labelValue = response.label.orEmpty()
    val fromValue = response.fromValue
    val toValue = response.to

    if (labelValue.isBlank()) {
        return null
    }
    if (fromValue == null && toValue == null) {
        return null
    }
    return FilterValueItemEntity(
        id = chipId.priceRangeChipId(fromValue, toValue),
        label = labelValue,
        order = response.order ?: Int.MAX_VALUE
    )
}

private fun JsonElement.toTreeFilterValues(
    chipId: String,
    parentId: String?
): List<FilterValueItemEntity> {
    val response = runCatching { filterValuesJson.decodeFromJsonElement<FilterIdTreeValueResponse>(this) }
        .getOrNull()
        ?: return emptyList()
    return response.toTreeFilterValues(chipId, parentId)
}

private fun FilterIdTreeValueResponse.toTreeFilterValues(
    chipId: String,
    parentId: String?
): List<FilterValueItemEntity> {
    val labelValue = label.orEmpty()
    val valueId = value ?: return emptyList()

    if (labelValue.isBlank()) {
        return emptyList()
    }
    val id = chipId.treeValueChipId(valueId)
    val childValues = children.orEmpty().flatMap { child ->
        child.toTreeFilterValues(chipId = chipId, parentId = id)
    }
    val childIds = childValues
        .filter { item -> item.parentId == id }
        .map(FilterValueItemEntity::id)

    return buildList {
        add(
            FilterValueItemEntity(
                id = id,
                label = labelValue,
                labelPhotoUrl = labelPhotoUrl.orEmpty(),
                order = order ?: Int.MAX_VALUE,
                requestValue = valueId.toString(),
                parentId = parentId,
                childIds = childIds
            )
        )
        addAll(childValues)
    }
}

private val JsonElement.filterValueType: CatalogFilterValueType?
    get() {
        val valueTypeElement = jsonObject["valueType"] ?: return null
        return runCatching {
            filterValuesJson.decodeFromJsonElement<CatalogFilterValueType>(valueTypeElement)
        }.getOrNull()
    }

private val JsonElement?.filterValueId: String?
    get() = this?.jsonPrimitive?.contentOrNull

private fun String.priceRangeChipId(
    fromValue: Double?,
    toValue: Double?
): String {
    val fromToken = fromValue?.toInt()?.toString() ?: "-"
    val toToken = toValue?.toInt()?.toString() ?: "-"
    return "${this}_range_${fromToken}_$toToken"
}

private fun String.treeValueChipId(
    valueId: Int
): String {
    return when (this) {
        "category" -> "${this}_tree_$valueId"
        else -> "${this}_tree_$valueId"
    }
}
