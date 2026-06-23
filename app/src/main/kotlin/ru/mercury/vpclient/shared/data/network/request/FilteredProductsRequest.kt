@file:OptIn(ExperimentalSerializationApi::class)

package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.type.CatalogViewType
import ru.mercury.vpclient.shared.data.network.type.FilteredProductsSortType

@Serializable
data class FilteredProductsRequest(
    @SerialName("viewType") val viewType: CatalogViewType,
    @SerialName("sortType") val sortType: FilteredProductsSortType,
    @SerialName("hasUserInteractedWithStandartSizesFilter") val hasUserInteractedWithStandartSizesFilter: Boolean,
    @SerialName("filters") @EncodeDefault val filters: List<CatalogFilterRequest>
)
