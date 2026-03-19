@file:OptIn(ExperimentalSerializationApi::class)

package ru.mercury.vpclient.core.network.request

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FilteredProductsQuantityRequest(
    @SerialName("viewType") val viewType: String,
    @SerialName("hasUserInteractedWithStandartSizesFilter") val hasUserInteractedWithStandartSizesFilter: Boolean,
    @SerialName("filters") @EncodeDefault val filters: List<CatalogFilterRequest>
)
