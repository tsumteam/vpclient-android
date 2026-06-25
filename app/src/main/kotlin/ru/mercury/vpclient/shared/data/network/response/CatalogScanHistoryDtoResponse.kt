package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CatalogScanHistoryDtoResponse(
    @SerialName("items") val items: List<CatalogProductScanHistoryCardResponse>? = null
)
