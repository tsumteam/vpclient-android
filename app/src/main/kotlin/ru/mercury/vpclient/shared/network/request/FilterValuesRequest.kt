@file:OptIn(ExperimentalSerializationApi::class)

package ru.mercury.vpclient.shared.network.request

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FilterValuesRequest(
    @SerialName("filterType") val filterType: String,
    @SerialName("filterSubtype") val filterSubtype: String?,
    @SerialName("filterTreeValuesLevel") val filterTreeValuesLevel: Int?,
    @SerialName("viewType") val viewType: String,
    @SerialName("hasUserInteractedWithStandartSizesFilter") val hasUserInteractedWithStandartSizesFilter: Boolean,
    @SerialName("filters") @EncodeDefault val filters: List<CatalogFilterRequest>
)
