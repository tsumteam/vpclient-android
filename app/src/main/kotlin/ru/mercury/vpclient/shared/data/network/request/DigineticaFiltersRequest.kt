package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DigineticaFiltersRequest(
    @SerialName("searchText") val searchText: String? = null,
    @SerialName("filtersRequest") val filtersRequest: FiltersRequest? = null
)
