package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class CatalogProductDetailCardV2ButtonResponse(
    val title: String? = null,
    val catalogLink: JsonObject? = null
)
