package ru.mercury.vpclient.shared.data.network.type

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class CatalogViewType {
    @SerialName("allFilters") ALL_FILTERS,
    @SerialName("brand") BRAND,
    @SerialName("catalogLevel3") CATALOG_LEVEL_3,
    @SerialName("catalogLevel4") CATALOG_LEVEL_4,
    @SerialName("catalogLevel5") CATALOG_LEVEL_5,
    @SerialName("textSearch") TEXT_SEARCH
}
