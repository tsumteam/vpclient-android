package ru.mercury.vpclient.shared.domain.mapper

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import ru.mercury.vpclient.shared.data.entity.CatalogLinkData
import ru.mercury.vpclient.shared.data.entity.FilterChip
import ru.mercury.vpclient.shared.data.network.request.CatalogFilterRequest
import ru.mercury.vpclient.shared.data.network.request.CatalogFilterValueRequest

private val catalogLinkJson = Json {
    ignoreUnknownKeys = true
}

fun JsonObject.toCatalogLinkData(): CatalogLinkData? {
    val response = runCatching {
        catalogLinkJson.decodeFromJsonElement<CatalogLinkResponse>(this)
    }.getOrNull() ?: return null
    val categoryFilter = response.filters.firstOrNull { filter ->
        filter.filterType == CatalogFilterRequest.CATEGORY
    }
    val categoryId = categoryFilter
        ?.values
        ?.firstNotNullOfOrNull { value ->
            value.value?.jsonPrimitive?.contentOrNull?.toIntOrNull()
        }
    val selectedFilterValueChipIds = response.filters.flatMap { filter ->
        filter.toFilterChipIds()
    }
    val selectedFilterValueChips = response.filters.flatMap { filter ->
        filter.toFilterChips()
    }

    return CatalogLinkData(
        viewType = response.viewType,
        rootCategoryId = response.rootCategoryId,
        categoryId = categoryId,
        selectedFilterValueChipIds = selectedFilterValueChipIds,
        selectedFilterValueChips = selectedFilterValueChips
    )
}

@Serializable
private data class CatalogLinkResponse(
    val viewType: String? = null,
    val rootCategoryId: Int? = null,
    val filters: List<CatalogLinkFilterResponse> = emptyList()
)

@Serializable
private data class CatalogLinkFilterResponse(
    val filterType: String? = null,
    val filterSubtype: String? = null,
    val values: List<CatalogLinkFilterValueResponse> = emptyList()
)

@Serializable
private data class CatalogLinkFilterValueResponse(
    val valueType: String? = null,
    val value: JsonElement? = null,
    val label: String? = null
)

private fun CatalogLinkFilterResponse.toFilterChips(): List<FilterChip> {
    val resolvedFilterType = filterType.orEmpty()
    if (resolvedFilterType.isBlank()) {
        return emptyList()
    }
    return values.mapNotNull { value ->
        value.toFilterChip(
            filterType = resolvedFilterType,
            filterSubtype = filterSubtype
        )
    }
}

private fun CatalogLinkFilterResponse.toFilterChipIds(): List<String> {
    val resolvedFilterType = filterType.orEmpty()
    if (resolvedFilterType.isBlank()) {
        return emptyList()
    }
    return values.mapNotNull { value ->
        value.toFilterChipId(
            filterType = resolvedFilterType,
            filterSubtype = filterSubtype
        )
    }
}

private fun CatalogLinkFilterValueResponse.toFilterChip(
    filterType: String,
    filterSubtype: String?
): FilterChip? {
    val chipId = toFilterChipId(
        filterType = filterType,
        filterSubtype = filterSubtype
    ) ?: return null
    val chipLabel = label.orEmpty().trim()
    if (chipLabel.isEmpty()) {
        return null
    }

    return FilterChip(
        id = chipId,
        label = chipLabel
    )
}

private fun CatalogLinkFilterValueResponse.toFilterChipId(
    filterType: String,
    filterSubtype: String?
): String? {
    return when (filterType) {
        CatalogFilterRequest.ACTION,
        CatalogFilterRequest.BRAND,
        CatalogFilterRequest.COLOR,
        CatalogFilterRequest.SIZE -> {
            val rawValue = value?.jsonPrimitive?.contentOrNull?.toIntOrNull() ?: return null
            "${filterType}_$rawValue"
        }
        CatalogFilterRequest.CATEGORY -> {
            val rawValue = value?.jsonPrimitive?.contentOrNull?.toIntOrNull() ?: return null
            when (valueType) {
                CatalogFilterValueRequest.ID_TREE -> "${filterType}_tree_$rawValue"
                else -> "${filterType}_$rawValue"
            }
        }
        CatalogFilterRequest.ATTRIBUTE -> {
            val subtype = filterSubtype.orEmpty().ifBlank { return null }
            val rawValue = value?.jsonPrimitive?.contentOrNull?.toIntOrNull() ?: return null
            when (valueType) {
                CatalogFilterValueRequest.ID_TREE -> "${filterType}_${subtype}_tree_$rawValue"
                else -> "${filterType}_${subtype}_$rawValue"
            }
        }
        CatalogFilterRequest.PRICE -> {
            val valueObject = value?.jsonObject ?: return null
            val fromValue = valueObject["from"]?.jsonPrimitive?.contentOrNull?.toDoubleOrNull()?.toInt()
            val toValue = valueObject["to"]?.jsonPrimitive?.contentOrNull?.toDoubleOrNull()?.toInt()
            if (fromValue == null && toValue == null) {
                return null
            }
            val fromToken = fromValue?.toString() ?: "-"
            val toToken = toValue?.toString() ?: "-"
            "${filterType}_range_${fromToken}_$toToken"
        }
        else -> return null
    }
}
