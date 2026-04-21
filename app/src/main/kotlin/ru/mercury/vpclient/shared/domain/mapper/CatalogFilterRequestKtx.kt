package ru.mercury.vpclient.shared.domain.mapper

import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import ru.mercury.vpclient.shared.data.network.request.CatalogFilterRequest
import ru.mercury.vpclient.shared.data.network.request.CatalogFilterValueRequest

// fixme

fun String.isFilterValuesDialogChipId(): Boolean {
    return when (substringBefore("_")) {
        CatalogFilterRequest.ACTION,
        CatalogFilterRequest.ATTRIBUTE,
        CatalogFilterRequest.BRAND,
        CatalogFilterRequest.CATEGORY,
        CatalogFilterRequest.COLOR,
        CatalogFilterRequest.PRICE,
        CatalogFilterRequest.SIZE -> true
        else -> false
    }
}

fun String.isRequestAffectingCatalogFilterValueChipId(): Boolean {
    return startsWith("${CatalogFilterRequest.CATEGORY}_") ||
        startsWith("${CatalogFilterRequest.BRAND}_") ||
        startsWith("${CatalogFilterRequest.COLOR}_") ||
        startsWith("${CatalogFilterRequest.PRICE}_") ||
        startsWith("${CatalogFilterRequest.SIZE}_") ||
        startsWith("${CatalogFilterRequest.ACTION}_") ||
        startsWith("${CatalogFilterRequest.ATTRIBUTE}_")
}

fun Set<String>.requests(categoryId: Int): List<CatalogFilterRequest> {
    val valuesByRequestKey = mutableMapOf<RequestKey, MutableList<CatalogFilterValueRequest>>()

    for (chipId in sorted()) {
        val requestEntry = chipId.toCatalogFilterRequestEntry() ?: continue
        valuesByRequestKey.getOrPut(requestEntry.key) { mutableListOf() }
            .add(requestEntry.value)
    }
    val categoryRequestKey = RequestKey(filterType = CatalogFilterRequest.CATEGORY, filterSubtype = null)
    if (valuesByRequestKey[categoryRequestKey].isNullOrEmpty()) {
        valuesByRequestKey.getOrPut(categoryRequestKey) { mutableListOf() }
            .add(CatalogFilterValueRequest(CatalogFilterValueRequest.ID_TREE, JsonPrimitive(categoryId)))
    }
    return buildList {
        valuesByRequestKey.toSortedMap(compareBy<RequestKey> { it.filterType }.thenBy { it.filterSubtype.orEmpty() })
            .forEach { (requestKey, values) ->
            val catalogFilterRequest = CatalogFilterRequest(
                filterType = requestKey.filterType,
                filterSubtype = requestKey.filterSubtype,
                values = values.distinct()
            )
            add(catalogFilterRequest)
            }
    }
}

private fun String.toCatalogFilterRequestEntry(): RequestEntry? {
    return when {
        startsWith("${CatalogFilterRequest.ACTION}_") -> {
            simpleRequestEntry(
                chipId = this,
                filterType = CatalogFilterRequest.ACTION,
                valueType = CatalogFilterValueRequest.ID
            )
        }
        startsWith("${CatalogFilterRequest.CATEGORY}_") -> {
            categoryRequestEntry(chipId = this)
        }
        startsWith("${CatalogFilterRequest.BRAND}_") -> {
            simpleRequestEntry(
                chipId = this,
                filterType = CatalogFilterRequest.BRAND,
                valueType = CatalogFilterValueRequest.BRAND
            )
        }
        startsWith("${CatalogFilterRequest.COLOR}_") -> {
            simpleRequestEntry(
                chipId = this,
                filterType = CatalogFilterRequest.COLOR,
                valueType = CatalogFilterValueRequest.ID
            )
        }
        startsWith("${CatalogFilterRequest.PRICE}_range_") -> {
            priceRequestEntry(chipId = this)
        }
        startsWith("${CatalogFilterRequest.SIZE}_") -> {
            simpleRequestEntry(
                chipId = this,
                filterType = CatalogFilterRequest.SIZE,
                valueType = CatalogFilterValueRequest.CATALOG_PRODUCT_SIZE
            )
        }
        startsWith("${CatalogFilterRequest.ATTRIBUTE}_") -> {
            attributeRequestEntry(chipId = this)
        }
        else -> null
    }
}

private fun categoryRequestEntry(chipId: String): RequestEntry? {
    val rawValue = chipId.substringAfter("${CatalogFilterRequest.CATEGORY}_")
    val valueString = when {
        rawValue.startsWith("tree_") -> rawValue.substringAfter("tree_")
        else -> rawValue
    }
    val value = valueString.toIntOrNull() ?: return null
    return RequestEntry(
        key = RequestKey(filterType = CatalogFilterRequest.CATEGORY, filterSubtype = null),
        value = CatalogFilterValueRequest(
            valueType = CatalogFilterValueRequest.ID_TREE,
            value = JsonPrimitive(value)
        )
    )
}

private fun simpleRequestEntry(
    chipId: String,
    filterType: String,
    valueType: String
): RequestEntry? {
    val value = chipId.substringAfter("${filterType}_").toIntOrNull() ?: return null
    return RequestEntry(
        key = RequestKey(filterType = filterType, filterSubtype = null),
        value = CatalogFilterValueRequest(
            valueType = valueType,
            value = JsonPrimitive(value)
        )
    )
}

private fun priceRequestEntry(chipId: String): RequestEntry? {
    val value = chipId.substringAfter("${CatalogFilterRequest.PRICE}_range_")
    val fromToken = value.substringBefore("_", "").ifBlank { "-" }
    val toToken = value.substringAfter("_", "").ifBlank { "-" }
    val fromValue = fromToken.takeIf { it != "-" }?.toDoubleOrNull()
    val toValue = toToken.takeIf { it != "-" }?.toDoubleOrNull()

    if (fromValue == null && toValue == null) {
        return null
    }
    return RequestEntry(
        key = RequestKey(filterType = CatalogFilterRequest.PRICE, filterSubtype = null),
        value = CatalogFilterValueRequest(
            valueType = CatalogFilterValueRequest.DECIMAL_RANGE,
            value = buildJsonObject {
                if (fromValue != null) {
                    put("from", fromValue)
                }
                if (toValue != null) {
                    put("to", toValue)
                }
            }
        )
    )
}

private fun attributeRequestEntry(chipId: String): RequestEntry? {
    val rawValue = chipId.substringAfter("${CatalogFilterRequest.ATTRIBUTE}_")
    val treeSeparator = "_tree_"
    val filterSubtype = when {
        rawValue.contains(treeSeparator) -> rawValue.substringBefore(treeSeparator)
        else -> rawValue.substringBeforeLast("_", "")
    }
    if (filterSubtype.isBlank()) {
        return null
    }
    val valueString = when {
        rawValue.contains(treeSeparator) -> rawValue.substringAfter(treeSeparator)
        else -> rawValue.substringAfterLast("_")
    }
    val value = valueString.toIntOrNull() ?: return null
    val valueType = when {
        rawValue.contains(treeSeparator) -> CatalogFilterValueRequest.ID_TREE
        else -> CatalogFilterValueRequest.ID
    }
    return RequestEntry(
        key = RequestKey(filterType = CatalogFilterRequest.ATTRIBUTE, filterSubtype = filterSubtype),
        value = CatalogFilterValueRequest(
            valueType = valueType,
            value = JsonPrimitive(value)
        )
    )
}

private data class RequestEntry(
    val key: RequestKey,
    val value: CatalogFilterValueRequest
)

private data class RequestKey(
    val filterType: String,
    val filterSubtype: String?
)
