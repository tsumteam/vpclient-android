@file:OptIn(ExperimentalSerializationApi::class)

package ru.mercury.vpclient.core.network.request

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FilteredProductsRequest(
    @SerialName("viewType") val viewType: String,
    @SerialName("sortType") val sortType: String,
    @SerialName("hasUserInteractedWithStandartSizesFilter") val hasUserInteractedWithStandartSizesFilter: Boolean,
    @SerialName("filters") @EncodeDefault val filters: List<CatalogFilterRequest>
) {
    companion object {
        const val PRICE_ASCENDING = "priceAscending"
        const val PRICE_DESCENDING = "priceDescending"
        const val ARRIVAL_DATE_DESCENDING = "arrivalDateDescending"
        const val OUR_CHOICE = "ourChoice"
    }
}
