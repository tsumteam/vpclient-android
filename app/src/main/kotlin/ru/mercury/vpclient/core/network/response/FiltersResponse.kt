package ru.mercury.vpclient.core.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class FiltersResponse(
    @SerialName("filters") val filters: List<JsonElement>? = null
) {
    companion object {
        const val VIEW_TYPE_CATALOG_LEVEL_4 = "catalogLevel4"
        const val VIEW_TYPE_BRAND = "brand"
        const val VIEW_TYPE_ALL_FILTERS = "allFilters"
        const val VIEW_TYPE_CATALOG_LEVEL_5 = "catalogLevel5"
        const val VIEW_TYPE_CATALOG_LEVEL_3 = "catalogLevel3"
        const val VIEW_TYPE_TEXT_SEARCH = "textSearch"
    }
}
