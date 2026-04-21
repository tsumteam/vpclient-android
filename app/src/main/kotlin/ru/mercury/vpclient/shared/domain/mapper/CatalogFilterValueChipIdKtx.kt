package ru.mercury.vpclient.shared.domain.mapper

import ru.mercury.vpclient.shared.data.network.request.CatalogFilterRequest

data class PriceRangeChipData(
    val from: Int?,
    val to: Int?
)

fun String.toPriceRangeChipData(): PriceRangeChipData? {
    if (!startsWith("${CatalogFilterRequest.PRICE}_range_")) {
        return null
    }
    val rawValue = substringAfter("${CatalogFilterRequest.PRICE}_range_")
    val fromValue = rawValue.substringBefore("_", "").toChipRangeValue()
    val toValue = rawValue.substringAfter("_", "").toChipRangeValue()

    if (fromValue == null && toValue == null) {
        return null
    }
    return PriceRangeChipData(
        from = fromValue,
        to = toValue
    )
}

fun String.isTreeFilterValueChipId(): Boolean {
    return contains("_tree_")
}

private fun String.toChipRangeValue(): Int? {
    return when (this) {
        "-", "" -> null
        else -> toIntOrNull()
    }
}
