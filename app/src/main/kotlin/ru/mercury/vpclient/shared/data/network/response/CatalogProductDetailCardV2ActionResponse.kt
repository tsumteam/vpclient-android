package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.Serializable

@Serializable
data class CatalogProductDetailCardV2ActionResponse(
    val name: String? = null,
    val isCashDesk: Boolean? = null
)
