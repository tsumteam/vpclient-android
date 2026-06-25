package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FittingHistoryResponseItemResponse(
    @SerialName("operationInfo") val operationInfo: FittingHistoryOperationInfoResponse? = null,
    @SerialName("product") val product: CatalogProductSearchCardResponse? = null
)
