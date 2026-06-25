package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SalesByBrandResponse(
    @SerialName("items") val items: List<SalesByBrandItemResponse>? = null
)
