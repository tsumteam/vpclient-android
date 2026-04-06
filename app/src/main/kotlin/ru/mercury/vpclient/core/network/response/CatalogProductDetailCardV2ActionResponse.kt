package ru.mercury.vpclient.core.network.response

import kotlinx.serialization.Serializable

@Serializable
data class CatalogProductDetailCardV2ActionResponse(
    val name: String? = null,
    val isCashDesk: Boolean? = null
)
