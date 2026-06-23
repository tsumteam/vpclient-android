package ru.mercury.vpclient.shared.data.network.type

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class FilteredProductsSortType {
    @SerialName("arrivalDateDescending") ARRIVAL_DATE_DESCENDING,
    @SerialName("ourChoice") OUR_CHOICE,
    @SerialName("priceAscending") PRICE_ASCENDING,
    @SerialName("priceDescending") PRICE_DESCENDING
}
