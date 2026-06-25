package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DigineticaFilterValuesRequest(
    @SerialName("searchText") val searchText: String? = null,
    @SerialName("request") val request: FilterValuesRequest? = null
)
