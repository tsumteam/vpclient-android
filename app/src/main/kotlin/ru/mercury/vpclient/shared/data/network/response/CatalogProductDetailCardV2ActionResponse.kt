package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CatalogProductDetailCardV2ActionResponse(
    @SerialName("name") val name: String? = null,
    @SerialName("isCashDesk") val isCashDesk: Boolean? = null
)
