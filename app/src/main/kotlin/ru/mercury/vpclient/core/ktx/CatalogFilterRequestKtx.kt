package ru.mercury.vpclient.core.ktx

import ru.mercury.vpclient.core.network.request.CatalogFilterRequest
import ru.mercury.vpclient.core.network.request.CatalogFilterValueRequest

// fixme

fun String.isFilterValuesDialogChipId(): Boolean {
    return when (substringBefore("_")) {
        CatalogFilterRequest.ACTION, CatalogFilterRequest.ATTRIBUTE, CatalogFilterRequest.BRAND, CatalogFilterRequest.COLOR, CatalogFilterRequest.SIZE -> true
        else -> false
    }
}

fun String.isRequestAffectingCatalogFilterValueChipId(): Boolean {
    return startsWith("${CatalogFilterRequest.CATEGORY}_") ||
        startsWith("${CatalogFilterRequest.BRAND}_") ||
        startsWith("${CatalogFilterRequest.COLOR}_") ||
        startsWith("${CatalogFilterRequest.SIZE}_") ||
        startsWith("${CatalogFilterRequest.ACTION}_") ||
        startsWith("${CatalogFilterRequest.ATTRIBUTE}_")
}

fun Set<String>.requests(categoryId: Int): List<CatalogFilterRequest> {
    val actionValues = mutableListOf<CatalogFilterValueRequest>()
    val categoryValues = mutableListOf<CatalogFilterValueRequest>()
    val brandValues = mutableListOf<CatalogFilterValueRequest>()
    val colorValues = mutableListOf<CatalogFilterValueRequest>()
    val sizeValues = mutableListOf<CatalogFilterValueRequest>()
    val attributeValuesBySubtype = mutableMapOf<String, MutableList<CatalogFilterValueRequest>>()

    for (chipId in sorted()) {
        when {
            chipId.startsWith("${CatalogFilterRequest.ACTION}_") -> {
                val value = chipId.substringAfter("${CatalogFilterRequest.ACTION}_").toIntOrNull()
                if (value != null) {
                    actionValues.add(CatalogFilterValueRequest(CatalogFilterValueRequest.ID, value))
                }
            }
            chipId.startsWith("${CatalogFilterRequest.CATEGORY}_") -> {
                val value = chipId.substringAfter("${CatalogFilterRequest.CATEGORY}_").toIntOrNull()
                if (value != null) {
                    categoryValues.add(CatalogFilterValueRequest(CatalogFilterValueRequest.ID_TREE, value))
                }
            }
            chipId.startsWith("${CatalogFilterRequest.BRAND}_") -> {
                val value = chipId.substringAfter("${CatalogFilterRequest.BRAND}_").toIntOrNull()
                if (value != null) {
                    brandValues.add(CatalogFilterValueRequest(CatalogFilterValueRequest.BRAND, value))
                }
            }
            chipId.startsWith("${CatalogFilterRequest.COLOR}_") -> {
                val value = chipId.substringAfter("${CatalogFilterRequest.COLOR}_").toIntOrNull()
                if (value != null) {
                    colorValues.add(CatalogFilterValueRequest(CatalogFilterValueRequest.ID, value))
                }
            }
            chipId.startsWith("${CatalogFilterRequest.SIZE}_") -> {
                val value = chipId.substringAfter("${CatalogFilterRequest.SIZE}_").toIntOrNull()
                if (value != null) {
                    sizeValues.add(CatalogFilterValueRequest(CatalogFilterValueRequest.CATALOG_PRODUCT_SIZE, value))
                }
            }
            chipId.startsWith("${CatalogFilterRequest.ATTRIBUTE}_") -> {
                val rawValue = chipId.substringAfter("${CatalogFilterRequest.ATTRIBUTE}_")
                val filterSubtype = rawValue.substringBeforeLast("_", "")
                val value = rawValue.substringAfterLast("_").toIntOrNull()

                if (filterSubtype.isNotBlank() && value != null) {
                    attributeValuesBySubtype.getOrPut(filterSubtype) { mutableListOf() }
                        .add(CatalogFilterValueRequest(CatalogFilterValueRequest.ID, value))
                }
            }
        }
    }
    if (categoryValues.isEmpty()) {
        categoryValues.add(CatalogFilterValueRequest(CatalogFilterValueRequest.ID_TREE, categoryId))
    }
    return buildList {
        val categoryCatalogFilterRequest = CatalogFilterRequest(
            filterType = CatalogFilterRequest.CATEGORY,
            filterSubtype = null,
            values = categoryValues.distinct()
        )
        add(categoryCatalogFilterRequest)
        if (actionValues.isNotEmpty()) {
            val actionCatalogFilterRequest = CatalogFilterRequest(
                filterType = CatalogFilterRequest.ACTION,
                filterSubtype = null,
                values = actionValues.distinct()
            )
            add(actionCatalogFilterRequest)
        }
        if (brandValues.isNotEmpty()) {
            val brandCatalogFilterRequest = CatalogFilterRequest(
                filterType = CatalogFilterRequest.BRAND,
                filterSubtype = null,
                values = brandValues.distinct()
            )
            add(brandCatalogFilterRequest)
        }
        if (colorValues.isNotEmpty()) {
            val colorCatalogFilterRequest = CatalogFilterRequest(
                filterType = CatalogFilterRequest.COLOR,
                filterSubtype = null,
                values = colorValues.distinct()
            )
            add(colorCatalogFilterRequest)
        }
        if (sizeValues.isNotEmpty()) {
            val sizeCatalogFilterRequest = CatalogFilterRequest(
                filterType = CatalogFilterRequest.SIZE,
                filterSubtype = null,
                values = sizeValues.distinct()
            )
            add(sizeCatalogFilterRequest)
        }
        attributeValuesBySubtype.toSortedMap().forEach { (filterSubtype, values) ->
            val catalogFilterRequest = CatalogFilterRequest(
                filterType = CatalogFilterRequest.ATTRIBUTE,
                filterSubtype = filterSubtype,
                values = values.distinct()
            )
            add(catalogFilterRequest)
        }
    }
}
