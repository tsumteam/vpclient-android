package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class CatalogProductDetailCardV2ButtonResponse(
    @SerialName("title") val title: String? = null,
    @SerialName("catalogLink") val catalogLink: JsonObject? = null
)
