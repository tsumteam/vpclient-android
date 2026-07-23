package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class DigineticaFilteredProductsResponse(
    @SerialName("products") val products: FilteredProductsResponse? = null,
    @SerialName("correction") val correction: String? = null,
    @SerialName("catalogLink") val catalogLink: JsonObject? = null
)
