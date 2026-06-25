package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LookByProductsResponseItemResponse(
    @SerialName("searchCard") val searchCard: CatalogProductSearchCardResponse? = null,
    @SerialName("statistics") val statistics: StatisticsResponseBaseResponse? = null
)
