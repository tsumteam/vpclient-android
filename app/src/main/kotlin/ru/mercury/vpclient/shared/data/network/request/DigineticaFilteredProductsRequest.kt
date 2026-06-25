package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DigineticaFilteredProductsRequest(
    @SerialName("searchText") val searchText: String? = null,
    @SerialName("filteredProductsRequest") val filteredProductsRequest: FilteredProductsRequest? = null
)
