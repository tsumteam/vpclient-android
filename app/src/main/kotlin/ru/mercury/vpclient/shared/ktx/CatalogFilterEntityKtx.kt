package ru.mercury.vpclient.shared.ktx

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive
import ru.mercury.vpclient.shared.entity.FilterChip
import ru.mercury.vpclient.shared.entity.FilterRibbonData
import ru.mercury.vpclient.shared.network.response.FilterRibbonResponse
import ru.mercury.vpclient.shared.network.response.FilterRibbonValueResponse
import ru.mercury.vpclient.shared.persistence.database.entity.CatalogFilterEntity
import ru.mercury.vpclient.shared.persistence.database.entity.FilterValuesEntity

// fixme

private val json = Json { ignoreUnknownKeys = true; explicitNulls = false }

fun CatalogFilterEntity.toFilterRibbonData(): FilterRibbonData {
    val filterDtoList = filterRibbonResponseList() ?: return FilterRibbonData.Empty

    val filterChipDataList = filterDtoList.mapNotNull(::toFilterRibbonChipNetworkData)
    val filterValueChipDataList = filterDtoList.flatMap(::toFilterRibbonValueChipNetworkDataList)
    val topChipDataList = filterChipDataList
        .filter { chipData -> chipData.row == 0 || chipData.row == 1 }
        .sortedBy(FilterRibbonChipNetworkData::order)
        .map(FilterRibbonChipNetworkData::toFilterChipData)
    val topValueChipDataList = filterValueChipDataList
        .filter { chipData -> chipData.row == 0 || chipData.row == 1 }
        .sortedBy(FilterRibbonChipNetworkData::order)
        .map(FilterRibbonChipNetworkData::toFilterChipData)
    val bottomChipDataList = filterChipDataList
        .filter { chipData -> chipData.row == 2 }
        .sortedBy(FilterRibbonChipNetworkData::order)
        .map(FilterRibbonChipNetworkData::toFilterChipData)

    return FilterRibbonData(
        topFilterChips = topChipDataList,
        topFilterValueChips = topValueChipDataList,
        bottomFilterChips = bottomChipDataList
    )
}

fun CatalogFilterEntity.toFilterValuesPickers(): List<FilterValuesEntity> {
    val filterDtoList = filterRibbonResponseList() ?: return emptyList()

    return filterDtoList.mapNotNull { dto ->
        val chipId = dto.chipId ?: return@mapNotNull null
        val title = dto.label.orEmpty().ifBlank { return@mapNotNull null }
        val values = dto.values.orEmpty().toFilterValueChips(chipId)

        when {
            values.isEmpty() -> null
            else -> FilterValuesEntity(
                chipId = chipId,
                title = title,
                valueIds = values.map { it.id },
                valueLabels = values.map { it.label }
            )
        }
    }
}

fun CatalogFilterEntity.filterValuesRequestFilters(): List<JsonElement> {
    if (filtersJson.isBlank()) return emptyList()

    return runCatching {
        Json.parseToJsonElement(filtersJson).jsonArray.toList()
    }.getOrElse { emptyList() }
}

fun List<JsonElement>.toFilterValueChipsFromJsonElements(chipId: String): List<FilterChip> {
    return mapNotNull { element ->
        runCatching { json.decodeFromJsonElement<FilterRibbonValueResponse>(element) }
            .getOrNull()
            ?.toFilterValueChip(chipId)
    }
}

fun List<FilterRibbonValueResponse>.toFilterValueChips(chipId: String): List<FilterChip> {
    return mapNotNull { valueResponse -> valueResponse.toFilterValueChip(chipId) }
}

private fun CatalogFilterEntity.filterRibbonResponseList(): List<FilterRibbonResponse>? {
    return runCatching {
        Json.parseToJsonElement(filtersJson).jsonArray
            .mapNotNull { element -> runCatching { json.decodeFromJsonElement<FilterRibbonResponse>(element) }.getOrNull() }
    }.getOrNull()
}

private val FilterRibbonResponse.chipId: String?
    get() {
        val filterType = filterType.orEmpty()
        if (filterType.isBlank()) return null
        return when {
            filterSubtype.isNullOrBlank() -> filterType
            else -> "${filterType}_${filterSubtype}"
        }
    }

private fun toFilterRibbonChipNetworkData(dto: FilterRibbonResponse): FilterRibbonChipNetworkData? {
    val ribbonSettings = dto.ribbonSettings ?: return null
    val isVisible = ribbonSettings.isVisible ?: false
    val row = ribbonSettings.row ?: return null
    val order = dto.order ?: Int.MAX_VALUE
    val filterType = dto.filterType.orEmpty()
    val filterSubtype = dto.filterSubtype
    val label = dto.label.orEmpty()

    return when {
        isVisible.not() -> null
        label.isBlank() -> null
        filterType.isBlank() -> null
        else -> {
            val id = when {
                filterSubtype.isNullOrBlank() -> filterType
                else -> "${filterType}_$filterSubtype"
            }
            FilterRibbonChipNetworkData(
                id = id,
                label = label,
                row = row,
                order = order
            )
        }
    }
}

private fun toFilterRibbonValueChipNetworkDataList(dto: FilterRibbonResponse): List<FilterRibbonChipNetworkData> {
    return when (val filterType = dto.filterType.orEmpty()) {
        "category", "brand" -> dto.values.orEmpty().mapNotNull { valueDto ->
            val ribbonSettings = valueDto.ribbonSettings ?: return@mapNotNull null
            val isVisible = ribbonSettings.isVisible ?: false
            val row = ribbonSettings.row ?: return@mapNotNull null
            val order = ribbonSettings.order ?: Int.MAX_VALUE
            val label = valueDto.label.orEmpty()
            val value = valueDto.value?.jsonPrimitive?.contentOrNull

            when {
                isVisible.not() -> null
                label.isBlank() -> null
                value.isNullOrBlank() -> null
                else -> FilterRibbonChipNetworkData(
                    id = "${filterType}_$value",
                    label = label,
                    row = row,
                    order = order
                )
            }
        }
        else -> emptyList()
    }
}

private fun FilterRibbonValueResponse.toFilterValueChip(chipId: String): FilterChip? {
    val label = label.orEmpty()
    val valueId = value.filterValueId ?: return null

    return when {
        label.isBlank() -> null
        else -> FilterChip(
            id = "${chipId}_$valueId",
            label = label
        )
    }
}

private val JsonElement?.filterValueId: String?
    get() = this?.jsonPrimitive?.contentOrNull

private data class FilterRibbonChipNetworkData(
    val id: String,
    val label: String,
    val row: Int,
    val order: Int
) {
    fun toFilterChipData(): FilterChip {
        return FilterChip(
            id = id,
            label = label
        )
    }
}
