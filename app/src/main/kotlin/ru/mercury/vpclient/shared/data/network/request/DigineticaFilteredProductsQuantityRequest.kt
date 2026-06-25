package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DigineticaFilteredProductsQuantityRequest(
    @SerialName("searchText") val searchText: String? = null,
    @SerialName("request") val request: FilteredProductsQuantityRequest? = null
)
