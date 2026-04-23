package ru.mercury.vpclient.shared.domain.mapper

import ru.mercury.vpclient.features.main.tabs.catalog.stack.filter.model.FilterModel
import ru.mercury.vpclient.features.main.tabs.catalog.stack.filter.navigation.FilterRoute
import ru.mercury.vpclient.shared.data.entity.FilterChip
import ru.mercury.vpclient.shared.data.network.request.CatalogFilterRequest

fun String.onlyDigits(): String {
    return filter(Char::isDigit)
}

fun FilterRoute.includeDefaultCategory(): Boolean {
    return viewTypeOverride != "brand"
}

fun FilterRoute.requestFilterValueChipIds(selectedFilterValueChipIds: Set<String>): Set<String> {
    return selectedFilterValueChipIds + hiddenFilterValueChipIds.toSet()
}

fun FilterRoute.topBarBrandId(): Int? {
    return topBarBrandChipId()
        ?.substringAfter("brand_")
        ?.toIntOrNull()
}

fun FilterRoute.topBarBrandChipId(): String? {
    return hiddenFilterValueChipIds.firstOrNull { it.startsWith("brand_") }
}

fun FilterModel.priceFilterChip(): FilterChip? {
    val priceSelectionIds = priceSelectionIds(filterPriceFrom, filterPriceTo)
    val chipId = priceSelectionIds.firstOrNull() ?: return null
    val label = buildPriceChipLabel(filterPriceFrom, filterPriceTo)

    return FilterChip(
        id = chipId,
        label = label
    )
}

fun priceSelectionIds(
    priceFrom: String,
    priceTo: String
): Set<String> {
    val (normalizedPriceFrom, normalizedPriceTo) = normalizePriceRange(
        priceFrom = priceFrom.onlyDigits(),
        priceTo = priceTo.onlyDigits()
    )

    if (normalizedPriceFrom.isEmpty() && normalizedPriceTo.isEmpty()) {
        return emptySet()
    }
    return setOf(
        buildPriceChipId(
            priceFrom = normalizedPriceFrom,
            priceTo = normalizedPriceTo
        )
    )
}

private fun buildPriceChipId(
    priceFrom: String,
    priceTo: String
): String {
    val fromToken = priceFrom.ifBlank { "-" }
    val toToken = priceTo.ifBlank { "-" }
    return "${CatalogFilterRequest.PRICE}_range_${fromToken}_$toToken"
}

private fun buildPriceChipLabel(
    priceFrom: String,
    priceTo: String
): String {
    val (normalizedPriceFrom, normalizedPriceTo) = normalizePriceRange(priceFrom, priceTo)
    return when {
        normalizedPriceFrom.isNotEmpty() && normalizedPriceTo.isNotEmpty() -> "$normalizedPriceFrom - $normalizedPriceTo"
        normalizedPriceFrom.isNotEmpty() -> "$normalizedPriceFrom+"
        normalizedPriceTo.isNotEmpty() -> "<= $normalizedPriceTo"
        else -> ""
    }
}

private fun normalizePriceRange(
    priceFrom: String,
    priceTo: String
): Pair<String, String> {
    val fromValue = priceFrom.toIntOrNull()
    val toValue = priceTo.toIntOrNull()

    return when {
        fromValue != null && toValue != null && fromValue > toValue -> Pair(
            first = toValue.toString(),
            second = fromValue.toString()
        )
        else -> Pair(
            first = priceFrom,
            second = priceTo
        )
    }
}
